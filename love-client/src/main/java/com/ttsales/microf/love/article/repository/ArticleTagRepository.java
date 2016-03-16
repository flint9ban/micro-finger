package com.ttsales.microf.love.article.repository;

import com.ttsales.microf.love.article.domain.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
@RepositoryRestResource
public interface ArticleTagRepository extends JpaRepository<ArticleTag,Long>{

    @RestResource(path = "find-articleId")
    List<ArticleTag> findByArticleId(@Param("articleId") Long articleId);

    void removeByArticleId(Long articleId);

}
