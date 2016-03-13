package com.ttsales.microf.love.article.service;

import com.ttsales.microf.love.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by liyi on 2016/3/9.
 */
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article,Long>{

    @RestResource(path = "/find-by-meidaId")
    List<Article> findAllByMediaId(@Param("mediaId") String mediaId);

    @RestResource(path = "/find-qrcodeTicket")
    Article findByQrcodeTicket(@Param("qrcodeTicket") String qrcodeTicket);
}
