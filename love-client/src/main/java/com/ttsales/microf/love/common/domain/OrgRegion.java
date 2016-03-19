package com.ttsales.microf.love.common.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by lenovo on 2016/3/16.
 */
@Data
@Entity
@Table(name="org_region")
public class OrgRegion {

    @Id
    private String id;

    @Column(name="region_code")
    private String regionCode;

    @Column(name="parent_region_code")
    private String parentRegionCode;

    private String name;

    private String fullname;

    private int level;

    private  String pinyin;
}
