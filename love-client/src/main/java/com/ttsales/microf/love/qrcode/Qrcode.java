package com.ttsales.microf.love.qrcode;


import com.ttsales.microf.love.weixin.QrCodeActionType;
import lombok.Data;

/**
 * Created by liyi on 16/3/12.
 */
@Data
public class Qrcode {

    public static final Integer REF_TYPE_ARTICLE=1;

    public static final Integer REF_TYPE_TAG_CONTAINER = 2;

    private Long id;

    private String ticket;

    private QrCodeActionType qrcodeType;

    private String sceneId;

    private Integer refType;

}
