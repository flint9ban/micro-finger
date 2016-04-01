package com.ttsales.microf.love.qysend.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/31.
 */
@Data
@Entity
@Table(name = "dat_qy_send_log")
public class Log extends SuperEntity{

    public static final Integer STATE_Y=1;

    public static final Integer STATE_N=0;

    @Column(name="member_id")
    private String memberId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "store_name")
    private String storeName;

    private Integer state;

}
