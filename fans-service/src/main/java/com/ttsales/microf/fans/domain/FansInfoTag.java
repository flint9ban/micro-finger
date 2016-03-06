package com.ttsales.microf.fans.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name = "fans_tag")
public class FansInfoTag {

    private Long id;

    private Long fansId;

    private Long tagId;

}
