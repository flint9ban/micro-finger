package com.ttsales.microf.love.fans.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name = "dat_fans")
public class FansInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "open_id")
    private String openId;

    private String name;

    private String mobile;
}
