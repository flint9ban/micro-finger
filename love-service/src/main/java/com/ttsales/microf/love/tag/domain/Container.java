package com.ttsales.microf.love.tag.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 2016/3/4.
 */
@Data
@Entity
@Table(name="cf_tag_container")
public class Container {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;
}
