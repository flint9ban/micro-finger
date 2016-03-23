package com.ttsales.microf.love.article.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;
import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/22.
 */
@Data
@Entity
@Table(name="dat_article_item")
public class ArticleItem extends SuperEntity{

    @Column(name="article_id")
    private Long articleId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String url;

    @Column(name="item_index")
    private Integer itemIndex;

}
