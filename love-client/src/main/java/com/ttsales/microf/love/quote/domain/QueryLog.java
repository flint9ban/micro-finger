package com.ttsales.microf.love.quote.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/24.
 */
@Data
@Entity
@Table(name = "dat_price_query_log")
public class QueryLog extends SuperEntity{

    @Column(name="open_id")
    private String openId;
    @Column(name = "store_id")
    private String storeId;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "compete_region")
    private String competeRegion;
    @Column(name = "compete_parent_region")
    private String competeParentRegion;
    @Column(name = "compete_region_name")
    private String competeRegionName;
    @Column(name = "region")
    private String region;
    @Column(name = "parent_region")
    private String parentRegion;
    @Column(name = "region_name")
    private String regionName;

}
