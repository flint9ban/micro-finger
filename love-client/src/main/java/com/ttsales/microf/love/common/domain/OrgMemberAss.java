package com.ttsales.microf.love.common.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by lenovo on 2016/3/16.
 */
@Data
@Entity
@Table(name="org_member_ass")
public class OrgMemberAss {

    @Id
    private String id;

    @Column(name="org_id")
    private String orgId;

    @Column(name="member_id")
    private String memberId;

    @Column(name="org_type")
    private Integer orgType;

}
