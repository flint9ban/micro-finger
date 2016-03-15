package com.ttsales.microf.love.tag.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/4.
 */
@Data
@Entity
@Table(name="dat_tag_container")
public class Container extends SuperEntity{

    @Column
    private String name;

    @Column(name = "qrcode_ticket")
    private String qrcodeTicket;

}
