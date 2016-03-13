package com.ttsales.microf.love.client.article.service;

import com.ttsales.microf.love.client.article.domain.Article;
import com.ttsales.microf.love.client.article.domain.ArticleTag;
import com.ttsales.microf.love.client.article.domain.SendArticleLog;
import com.ttsales.microf.love.client.fans.service.FansService;
import com.ttsales.microf.love.client.qrcode.Qrcode;
import com.ttsales.microf.love.client.qrcode.service.QrcodeService;
import com.ttsales.microf.love.client.weixin.MPApi;
import com.ttsales.microf.love.client.weixin.QrCodeActionType;
import com.ttsales.microf.love.client.weixin.dto.NewsMaterial;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by liyi on 2016/3/11.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    Logger logger =  Logger.getLogger(ArticleServiceImpl.class);

    @Autowired
    private MPApi mpApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private FansService fansService;

    @Override
    public void sychnoizeArticle() throws WXApiException, HttpException {
        List<NewsMaterial> materials = mpApi.getNewsMaterials();
        materials.forEach(material -> mergeArticle2DB(material));
    }

    private void mergeArticle2DB(NewsMaterial material) {
        String mediaId = material.getMediaId();
        Collection<Article> articles =
                restTemplate.
                        exchange("http://love-service/articles/search/find-by-meidaId?mediaId"+mediaId,
                                HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Article>>() {
        })
                .getBody().getContent();
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
        restTemplate.postForObject("http://love-service/articles",article,Article.class);
    }

    public static void main(String[] args) {
        LocalDateTime date = LocalDateTime.now();
        Long time = date.atZone(ZoneId.systemDefault()).toEpochSecond();
        Long current = System.currentTimeMillis() / 1000;
        System.out.println(time + ":" + current);


    }

    public void sendArticleByTags(String mediaId,List<Long> tags){
        List<String> openIds = fansService.getOpenIdsByTags(tags);
        sendArticle(mediaId,openIds);
    }

    public void sendArticle(String mediaId,List<String> openIds){
        openIds.stream()
                .filter(openId->!isArticleSended(mediaId,openId))
                .forEach(openId->sendArticle(mediaId,openId));
    }

    @Override
    public List<ArticleTag> getArticleTags(Long articleId) {
        return restTemplate.exchange("http://love-service/articleTags/search/find-articleId?articleId="+articleId
                ,HttpMethod.GET,null,new ParameterizedTypeReference<Resources<ArticleTag>>(){})
                .getBody().getContent().stream().collect(Collectors.toList());
    }


    @Override
    public String createQrcodeTicket(Long articleId) throws WXApiException, HttpException {
        Qrcode qrcode = qrcodeService.createQrCode(QrCodeActionType.QR_LIMIT_STR_SCENE, Qrcode.REF_TYPE_ARTICLE);
        Article article = getArticle(articleId);
        article.setQrcodeTicket(qrcode.getTicket());
        putArticle(article);
        return qrcode.getTicket();
    }

    @Override
    public Article getArticleByTicket(String qrcodeTicket) {
        Article article = restTemplate.getForObject("http://love-service/articles/search/find-qrcodeTicket?qrcodeTicket="+qrcodeTicket,Article.class);
        return article;
    }

    private Article getArticle(Long articleId){
        Article article = restTemplate.getForObject("http://love-service/articles/"+articleId,Article.class);
        return article;
    }

    private void putArticle(Article article){
        restTemplate.put("http://love-service/articles/"+article.getId(),article);
    }

    private boolean isArticleSended(String mediaId,String openId){
        SendArticleLog log = restTemplate.getForObject("http://love-service/sendArticleLogs/find-mediaId-openId?mId="+mediaId+"&openId="+openId, SendArticleLog.class);
        return log!=null;
    }

    private void sendArticle(String mediaId,String openId){
        try{
            mpApi.sendMpnewsMessage(openId,mediaId);
            SendArticleLog log
                    = new SendArticleLog();
            log.setMediaId(mediaId);
            log.setOpenId(openId);
            restTemplate.postForObject("http://love-service/sendArticleLogs",log,SendArticleLog.class);
        }catch (Exception e){
            logger.error("send article fail:"+e.getMessage());
        }

    }
}
