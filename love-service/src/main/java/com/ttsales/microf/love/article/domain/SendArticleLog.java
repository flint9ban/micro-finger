package com.ttsales.microf.love.article.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by liyi on 16/3/12.
 */
@Data
@Entity
public class SendArticleLog extends SuperEntity{


    @Column(name="media_id")
    private String mediaId;

    @Column(name="open_id")
    private String openId;

}
