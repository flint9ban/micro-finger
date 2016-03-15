package com.ttsales.microf.love.article.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/13.
 */
@Data
@Entity
@Table(name="dat_article_tag")
public class ArticleTag extends SuperEntity{

    @Column(name="article_id")
    private Long articleId;

    @Column(name="tag_id")
    private Long tagId;

}
