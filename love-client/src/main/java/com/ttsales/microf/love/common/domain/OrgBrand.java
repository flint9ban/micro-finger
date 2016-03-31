package com.ttsales.microf.love.common.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by lenovo on 2016/3/16.
 */
@Data
@Entity
@Table(name="dat_brand")
public class OrgBrand {

    @Id
    @Column(name="id")
    private String id;

    private String name;

    private String pinyin;

    @Column(name="tag_id")
    private Long tagId;
}
