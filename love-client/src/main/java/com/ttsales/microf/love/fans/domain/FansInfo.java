package com.ttsales.microf.love.fans.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 16/3/6.
 */
@Data
@Entity
@Table(name = "dat_fans")
public class FansInfo extends SuperEntity {


    @Column(name = "open_id")
    private String openId;

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

    @Column(name = "org_area")
    private String orgArea;

    @Column(name = "org_province")
    private String orgProvince;

    @Column(name = "org_city")
    private String orgCity;
}
