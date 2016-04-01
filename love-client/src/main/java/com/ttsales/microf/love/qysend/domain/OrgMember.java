package com.ttsales.microf.love.qysend.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/31.
 */

@Data
@Entity
@Table(name = "org_member")
public class OrgMember {

    @Id
    @Column
    private String id;

    @Column(name = "store_id")
    private String storeId;

    @Column(name="store_name")
    private String storeName;

    @Column(name="member_id")
    private String memberId;


}
