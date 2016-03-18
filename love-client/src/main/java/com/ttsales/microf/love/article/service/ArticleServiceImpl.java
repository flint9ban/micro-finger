package com.ttsales.microf.love.article.service;


import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.article.domain.SendArticleLog;
import com.ttsales.microf.love.article.repository.ArticleRepository;
import com.ttsales.microf.love.article.repository.ArticleTagRepository;
import com.ttsales.microf.love.article.repository.SendArticleLogRepository;
import com.ttsales.microf.love.domainUtil.LocalDateTimeUtil;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.domain.QrCodeType;
import com.ttsales.microf.love.qrcode.service.QrcodeService;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import com.ttsales.microf.love.weixin.dto.NewsMaterial;
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
    private QrcodeService qrcodeService;

    @Autowired
    private FansService fansService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleTagRepository articleTagRepository;

    @Autowired
    private SendArticleLogRepository sendArticleLogRepository;

    @Override
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
                Predicate predicate1 = builder.greaterThanOrEqualTo(root.<LocalDateTime>get("sendTime"), LocalDateTimeUtil.convertToDateTime(startDate.getTime()));
                if (predicate != null) {
                    predicate = builder.and(predicate,predicate1);
                }else{
                    predicate = predicate1;
                }
            }
            if (endTime != null) {
                Predicate predicate1 = builder.lessThanOrEqualTo(root.<LocalDateTime>get("sendTime"), LocalDateTimeUtil.convertToDateTime(endTime.getTime()));
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
            article.setReloadTime(LocalDateTime.now());
            //for test other properties will be clear
            putArticle(article);
        }
    }

    private void addArticle(NewsMaterial material) {
        Article article = new Article();
        article.setTitle(material.getFirstTitle());
        article.setContent(material.getFirstContent());
        article.setMediaId(material.getMediaId());
        article.setReloadTime(LocalDateTime.now());
        articleRepository.save(article);
    }

    public static void main(String[] args) {
        LocalDateTime date = LocalDateTime.now();
        Long time = date.atZone(ZoneId.systemDefault()).toEpochSecond();
        Long current = System.currentTimeMillis() / 1000;
        System.out.println(time + ":" + current);


    }

    @Override
    public void sendArticleByTags(Long articleId,String mediaId,List<Long> tags){
        List<String> openIds = fansService.getOpenIdsByTags(tags);
        sendArticle(articleId,mediaId,openIds);
    }

    @Override
    @Transactional
    public void sendArticle(Long articleId,String mediaId,List<String> openIds){
        openIds.stream()
                .filter(openId->!isArticleSended(mediaId,openId))
                .forEach(openId->sendArticle(mediaId,openId));
        Article article = getArticle(articleId);
        article.setSendTime(LocalDateTime.now());
        putArticle(article);
    }

    @Override
    public List<ArticleTag> getArticleTags(Long articleId) {
        return articleTagRepository.findByArticleId(articleId);
    }


    @Override
    public String createQrcodeTicket(Long articleId) throws WXApiException, HttpException {
        QrCode qrcode = qrcodeService.createQrCode(QrCodeType.QR_SCENE, QrCode.REF_TYPE_ARTICLE);
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
    public void updateArticleTags(Long articleId,List<Long> tagIds) {
        articleTagRepository.removeByArticleId(articleId);
        if (tagIds != null) {
            tagIds.forEach(tagId->{
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
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

    private void sendArticle(String mediaId,String openId){
        try{
            mpApi.sendMpnewsMessage(openId,mediaId);
            SendArticleLog log
                    = new SendArticleLog();
            log.setMediaId(mediaId);
            log.setOpenId(openId);
            sendArticleLogRepository.save(log);
        }catch (Exception e){
            logger.error("send article fail:"+e.getMessage());
        }

    }
    public List<Article> getAllArricles(){
        return articleRepository.findAll();
    }
}
