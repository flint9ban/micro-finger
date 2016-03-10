package com.ttsales.microf.love.fans.domain;

import lombok.Data;

import javax.persistence.Entity;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
public class FansInfoTag {

    private Long id;

    private Long fansId;

    private Long tagId;

}
