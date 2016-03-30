package com.ttsales.microf.love.article.service;


import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleItem;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.article.domain.SendArticleLog;
import com.ttsales.microf.love.article.repository.ArticleItemRepository;
import com.ttsales.microf.love.article.repository.ArticleRepository;
import com.ttsales.microf.love.article.repository.ArticleTagRepository;
import com.ttsales.microf.love.article.repository.SendArticleLogRepository;
import com.ttsales.microf.love.domainUtil.LocalDateTimeUtil;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.domain.QrCodeType;
import com.ttsales.microf.love.qrcode.service.QrcodeService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import com.ttsales.microf.love.weixin.MPApiConfig;
import com.ttsales.microf.love.weixin.dto.NewsMaterial;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by liyi on 2016/3/11.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    Logger logger =  Logger.getLogger(ArticleServiceImpl.class);

    @Autowired
    private MPApi mpApi;

    @Autowired
    private MPApiConfig mpApiConfig;

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private FansService fansService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleTagRepository articleTagRepository;

    @Autowired
    private SendArticleLogRepository sendArticleLogRepository;

    @Autowired
    private ArticleItemRepository articleItemRepository;

    @Override
    @Transactional
    public void sychnoizeArticle() throws WXApiException, HttpException {
        List<NewsMaterial> materials = mpApi.getNewsMaterials();
        materials.forEach(material -> mergeArticle2DB(material));
    }

    @Override
    public Page<Article> queryArticle(PageRequest pageRequest, String title, Date startDate, Date endTime) {
        Specification<Article> specification = (Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder builder)->{
            Predicate predicate = null;
            if (title != null) {
                predicate = builder.like(root.get("title"),"%"+title+"%");
            }
            if (startDate!=null){
                LocalDateTime startDateTime = LocalDateTimeUtil.convertToDateTime(startDate.getTime());
                startDateTime = LocalDateTimeUtil.clearTime(startDateTime);
                Predicate predicate1 = builder.greaterThanOrEqualTo(root.<LocalDateTime>get("sendTime"),startDateTime);
                if (predicate != null) {
                    predicate = builder.and(predicate,predicate1);
                }else{
                    predicate = predicate1;
                }
            }
            if (endTime != null) {
                LocalDateTime endDateTime = LocalDateTimeUtil.convertToDateTime(endTime.getTime());
                endDateTime = endDateTime.plusDays(1);
                endDateTime = LocalDateTimeUtil.clearTime(endDateTime);
                Predicate predicate1 = builder.lessThan(root.<LocalDateTime>get("sendTime"), endDateTime);
                if (predicate != null) {
                    predicate = builder.and(predicate,predicate1);
                }else{
                    predicate = predicate1;
                }
            }
            return predicate;
        };
        return articleRepository.findAll(specification,pageRequest);
    }


    private void mergeArticle2DB(NewsMaterial material) {
        String mediaId = material.getMediaId();
        Collection<Article> articles =
                articleRepository.findAllByMediaId(mediaId);
        if (articles.isEmpty()) {
            addArticle(material);
        } else {
            articles.stream().forEach(article -> updateArticle(material, article));
        }
    }

    private void updateArticle(NewsMaterial material, Article article) {
        Long lastReloadTime = article.getReloadTime().atZone(ZoneId.systemDefault()).toEpochSecond();
        if (material.getUpdateTime() > lastReloadTime) {
            article.setTitle(material.getFirstTitle());
            article.setContent(material.getFirstContent());
            article.setReloadTime(LocalDateTimeUtil.convertToDateTime(material.getUpdateTime()*1000));
            putArticle(article);
            articleItemRepository.deleteByArticleId(article.getId());
            addArticleItems(material,article.getId());
        }
    }

    private void addArticle(NewsMaterial material) {
        Article article = new Article();
        article.setTitle(material.getFirstTitle());
        article.setContent(material.getFirstContent());
        article.setMediaId(material.getMediaId());
        article.setReloadTime(LocalDateTimeUtil.convertToDateTime(material.getUpdateTime()*1000));

        article = articleRepository.save(article);
        Long articleId = article.getId();
        addArticleItems(material,articleId);
    }

    private void addArticleItems(NewsMaterial material,Long articleId){
        material.getItems().stream().forEach(materialItem -> {
            ArticleItem item = new ArticleItem();
            item.setArticleId(articleId);
            item.setContent(materialItem.getContent());
            item.setUrl(materialItem.getContentSourceUrl());
            item.setItemIndex(materialItem.getItemIndex());
            item.setTitle(materialItem.getTitle());
            articleItemRepository.save(item);
        });
    }


    @Override
    public void sendArticleByTags(Long articleId,String mediaId,List<Long> tags){
        List<String> openIds = fansService.getOpenIdsByTags(tags);
        sendArticle(articleId,mediaId,openIds);
    }

    @Override
    @Transactional
    public void sendArticle(Long articleId,String mediaId,List<String> openIds){
        LocalDate localDate = LocalDate.now();
        String currentDate = localDate.getYear()+"-"+localDate.getMonthValue()+"-"+localDate.getDayOfMonth();
        Article article = getArticle(articleId);
        openIds.stream()
                .filter(openId->!isArticleSended(mediaId,openId))
                .forEach(openId->sendArticle(article,openId,currentDate));

        article.setSendTime(LocalDateTime.now());
        putArticle(article);
    }

    @Override
    public List<ArticleTag> getArticleTags(Long articleId) {
        return articleTagRepository.findByArticleId(articleId);
    }


    @Override
    public String createQrcodeTicket(Long articleId) throws WXApiException, HttpException {
        QrCode qrcode = qrcodeService.createQrCode(QrCodeType.QR_LIMIT_STR_SCENE, QrCode.REF_TYPE_ARTICLE);
        Article article = getArticle(articleId);
        article.setQrcodeTicket(qrcode.getTicket());
        putArticle(article);
        return qrcode.getTicket();
    }

    @Override
    public Article getArticleByTicket(String qrcodeTicket) {
        return articleRepository.findByQrcodeTicket(qrcodeTicket);
    }

    @Override
    @Transactional
    public void updateArticleTags(Article article,List<Long> tagIds) {
        articleRepository.save(article);
        articleTagRepository.removeByArticleId(article.getId());
        if (tagIds != null) {
            tagIds.forEach(tagId->{
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagId);
                articleTagRepository.save(articleTag);
            });
        }
    }

    @Override
    public Article getArticle(Long articleId){
        return articleRepository.findOne(articleId);
    }

    private void putArticle(Article article){
        articleRepository.save(article);
    }

    private boolean isArticleSended(String mediaId,String openId){
        SendArticleLog log = sendArticleLogRepository.findByMediaIdAndOpenId(mediaId,openId);
        return log!=null;
    }

    private void sendArticle(Article article,String openId,String currentDate){
        String mediaId = article.getMediaId();
        try{
            mpApi.sendMpnewsMessage(openId,mediaId);
        }catch (Exception e){
            logger.warn("send customer article fail:"+e.getMessage());
            sendArticleByTmp(openId,currentDate,article);
        }
        SendArticleLog log
                = new SendArticleLog();
        log.setMediaId(mediaId);
        log.setOpenId(openId);
        sendArticleLogRepository.save(log);
    }

    private void sendArticleByTmp(String openId,String currentDate,Article article){
        List<ArticleItem> items = articleItemRepository.findAllByArticleIdOrderByItemIndex(article.getId());
        List<Tag> tags = fansService.getTagsByOpenId(openId);

        String tagNames = tags.stream().map(Tag::getName).reduce("",(preName,name)->preName+=name+"、");
        if(tagNames.length()>0){
            tagNames = tagNames.substring(0,tagNames.length()-1);
        }
        String dataType = tags.stream().filter(tag->{
            List<Container> containers = tagService.getTagContainerByTagId(tag.getId());
            return containers.stream().anyMatch(container -> container.getId().equals(Container.SUBSCRIBE_CONTAINER_DATA_ID));
        }).map(container->container.getName()).findFirst().orElse("");
        String contentName = tagNames;
        items.stream().forEach(articleItem -> {
            JSONObject data = getTmpData(articleItem,contentName,dataType,currentDate);
            try {
                mpApi.sendTemplateMessage(openId,mpApiConfig.getDefaultTmpMsg(),articleItem.getUrl(),data.toString());
            } catch (Exception e) {
                logger.error("send customer article fail:"+e.getMessage());
            }
        });

    }

    private JSONObject getTmpData(ArticleItem articleItem,String tagNames,String dataType,String currentDate){

        JSONObject first = setTmpItem(articleItem.getTitle());
        JSONObject keyword1 = setTmpItem(dataType);
        JSONObject keyword2 = setTmpItem(tagNames);
        JSONObject keyword3 = setTmpItem(currentDate);
        JSONObject remark = setTmpItem("查看具体数据内容！");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first",first);
        jsonObject.put("keyword1",keyword1);
        jsonObject.put("keyword2",keyword2);
        jsonObject.put("keyword3",keyword3);
        jsonObject.put("remark",remark);
        return jsonObject;
    }

    private JSONObject setTmpItem(String value){
        JSONObject item = new JSONObject();
        item.put("value",value);
        item.put("color","#173177");
        return item;
    }

    public List<Article> getAllArricles(){
        return articleRepository.findAll();
    }



}
