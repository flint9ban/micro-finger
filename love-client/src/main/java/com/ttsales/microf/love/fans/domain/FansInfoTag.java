package com.ttsales.microf.love.fans.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name = "dat_fans_tag")
public class FansInfoTag extends SuperEntity{


    @Column(name = "fans_id")
    private Long fansId;

    @Column(name = "tag_id")
    private Long tagId;

}
