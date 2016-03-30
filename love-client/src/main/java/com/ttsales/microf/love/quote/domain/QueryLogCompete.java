package com.ttsales.microf.love.quote.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/24.
 */
@Data
@Entity
@Table(name = "dat_price_query_log_compete")
public class QueryLogCompete extends SuperEntity{

    @Column(name="log_id")
    private Long logId;

    @Column(name="compete_id")
    private String competeId;

}
