package com.ttsales.microf.love.keyword.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/23.
 */
@Data
@Entity
@Table(name = "dat_keyword")
public class Keyword extends SuperEntity {

    private String name;

    private String declare;

    private  String keyword;

    @Column(name="sub_send")
    private Integer subSend;

    @Column(name="media_id")
    private String mediaId;
}
