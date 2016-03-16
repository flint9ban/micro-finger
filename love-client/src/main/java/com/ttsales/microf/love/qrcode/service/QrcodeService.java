package com.ttsales.microf.love.qrcode.service;

import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.domain.QrCodeType;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

/**
 * Created by liyi on 16/3/13.
 */
public interface QrcodeService {

    String getQrCodeTicket(Long qrcodeId);

    QrCode createQrCode(QrCodeType qrCodeType, Integer refType) throws WXApiException, HttpException;

    QrCode getQrcode(String sceneId);
}
