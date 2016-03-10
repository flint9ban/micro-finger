package com.ttsales.microf.love.article.service;

import com.ttsales.microf.love.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by liyi on 2016/3/9.
 */
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article,Long>{
}
