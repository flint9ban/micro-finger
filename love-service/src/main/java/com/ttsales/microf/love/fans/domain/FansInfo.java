package com.ttsales.microf.love.fans.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
public class FansInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String mobile;
}
