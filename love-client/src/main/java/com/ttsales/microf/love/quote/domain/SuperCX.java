package com.ttsales.microf.love.quote.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by liyi on 2016/3/25.
 */

@Data
@MappedSuperclass
public class SuperCX {

    @Column
    @Id
    private String id;

    @Column(name="brand_id")
    private String brandId;

    @Column
    private String brand;

    //车系
    @Column(name="series_id")
    private String seriesId;

    @Column
    private String series;

    @Column
    private String model;

    //最低价
    @Column(name = "price_store")
    private String priceStore;
    //厂商报价
    @Column(name="price_vendor")
    private String priceVendor;

    //降幅
    @Column(name="pri_con")
    private Double priCon;

    //最低价门店数量
    @Column(name="store_num")
    private String storeNum;
}
