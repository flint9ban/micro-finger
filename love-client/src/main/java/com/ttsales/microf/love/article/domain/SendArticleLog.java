package com.ttsales.microf.love.article.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/12.
 */
@Data
@Entity
@Table(name="dat_article_send_log")
public class SendArticleLog extends SuperEntity{


    @Column(name="media_id")
    private String mediaId;

    @Column(name="open_id")
    private String openId;

}
