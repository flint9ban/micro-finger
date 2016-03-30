package com.ttsales.microf.love.quote.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/25.
 */
@Data
@Entity
@Table(name = "dat_store_cars")
public class StoreCars {

    @Id
    @Column
    private String id;

    @Column(name = "store_id")
    private String storeId;

    @Column(name="brand_id")
    private String brandId;

}
