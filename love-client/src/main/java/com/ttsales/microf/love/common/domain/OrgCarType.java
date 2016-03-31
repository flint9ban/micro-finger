package com.ttsales.microf.love.common.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lenovo on 2016/3/16.
 */
@Data
@Entity
@Table(name="dat_price_car_type")
public class OrgCarType {

    @Id
    @Column(name="car_type_id")
    private String id;

    @Column(name="brand_id")
    private String brandId;

    private String type;


    private String name;

    private String level;

    @Column(name="last_update_at")
    private Long lastUpdateAt;
}
