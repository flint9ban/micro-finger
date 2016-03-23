package com.ttsales.microf.love.article.repository;

import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by liyi on 2016/3/9.
 */
@RepositoryRestResource
public interface ArticleItemRepository extends JpaRepository<ArticleItem,Long>{

    List<ArticleItem> findAllByArticleIdOrderByItemIndex(Long articleId);

    void deleteByArticleId(Long articleId);

}
