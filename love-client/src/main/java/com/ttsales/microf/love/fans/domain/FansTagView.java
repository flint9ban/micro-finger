package com.ttsales.microf.love.fans.domain;

/**
 * Created by lenovo on 2016/3/18.
 */

import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name = "dat_fans_tag_view")
public class FansTagView {
    @Id
    private String id;

    @Column(name = "fans_id")
    private Long fansId;

    private String name;

    private String mobile;

    @Column(name = "org_type")
    private String orgType;

    @Column(name = "org_position")
    private String orgPosition;

    @Column(name = "org_store")
    private String orgStore;

    @Column(name = "org_brand")
    private String orgBrand;

    @Column(name = "org_province")
    private String orgProvince;

    @Column(name = "org_city")
    private String orgCity;

    @Column(name = "tag_id")
    private Long tagId;
}
