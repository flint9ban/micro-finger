package com.ttsales.microf.love.common.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by lenovo on 2016/3/16.
 */
@Data
@Entity
@Table(name="org_store")
public class OrgStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="store_id")
    private String storeId;

    @Column(name="store_name")
    private String storeName;

    private String province;

    private String city;

    private String address;
}
