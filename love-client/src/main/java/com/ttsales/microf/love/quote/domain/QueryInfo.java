package com.ttsales.microf.love.quote.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liyi on 2016/3/24.
 */
@Data
@Entity
@Table(name = "dat_price_query_info")
public class QueryInfo extends SuperEntity {

    public static final Integer REGION_TYPE_COUNTRY = 1;

    public static final Integer REGION_TYPE_PROVINCE = 2;

    public static final Integer REGION_TYPE_CITY = 3;

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
    @Column(name = "compete_region_type")
    private Integer competeRegionType;
    @Column(name = "compete_region_name")
    private String competeRegionName;
    @Column(name = "compete_ids")
    private String competeIds;
    @Column(name = "compete_names")
    private String competeNames;
    @Column(name = "region")
    private String region;
    @Column(name = "parent_region")
    private String parentRegion;
    @Column(name = "region_type")
    private Integer regionType;
    @Column(name = "region_name")
    private String regionName;

    public List<String> getCompleteList(){
        if(competeIds.length()==0){
            return new ArrayList<String>();
        }else{
            return Arrays.asList(competeIds.split(","));
        }
    }

}
