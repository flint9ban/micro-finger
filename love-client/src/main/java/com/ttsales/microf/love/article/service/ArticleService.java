package com.ttsales.microf.love.article.service;

import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by liyi on 2016/3/11.
 */
public interface ArticleService {

    void sychnoizeArticle() throws WXApiException, HttpException;

    Page<Article> queryArticle(PageRequest pageRequest, String title, Date startDate, Date endTime);

    void sendArticleByTags(Long articleId,String mediaId, List<Long> tags);

    void sendArticle(Long articleId,String mediaId, List<String> openIds);

    List<ArticleTag> getArticleTags(Long articleId);

    Article getArticle(Long articleId);

    String createQrcodeTicket(Long articleId) throws WXApiException, HttpException;

    Article getArticleByTicket(String qrcodeTicket);

    void updateArticleTags(Long articleId, List<Long> tagIds);

    List<Article> getAllArricles();
}
