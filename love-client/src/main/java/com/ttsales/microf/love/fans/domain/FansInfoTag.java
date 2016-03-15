package com.ttsales.microf.love.fans.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name = "dat_fans_tag")
public class FansInfoTag extends SuperEntity{

    private Long fansId;

    private Long tagId;

}
