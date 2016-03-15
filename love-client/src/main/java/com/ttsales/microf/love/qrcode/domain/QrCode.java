package com.ttsales.microf.love.qrcode.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 16/3/12.
 */
@Data
@Entity
@Table(name = "dat_qrcode")
public class QrCode extends SuperEntity{

    public static final Integer REF_TYPE_ARTICLE = 1;

    public static final Integer REF_TYPE_TAG_CONTAINER = 2;

    private String sceneId;

    private String ticket;

    private QrCodeType qrCodeType;

    @Column(name = "ref_type")
    private Integer refType;
}
