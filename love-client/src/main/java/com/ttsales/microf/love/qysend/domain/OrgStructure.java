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
@Table(name = "org_structure")
public class OrgStructure {

    @Id
    @Column(name = "org_struct_id")
    private String orgStructId;
    @Column(name = "org_struct_name")
    private String orgStructName;
    @Column(name = "org_type")
    private String orgType;
    @Column(name = "org_id")
    private String orgId;
    @Column(name = "wx_org_id")
    private String wxOrgId;
    @Column(name = "state")
    private String state;
    @Column(name="store_name")
    private String storeName;



}
