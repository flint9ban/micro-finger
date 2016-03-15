package com.ttsales.microf.love.qrcode.service;

import com.ttsales.microf.love.qrcode.Qrcode;
import com.ttsales.microf.love.weixin.QrCodeActionType;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

/**
 * Created by liyi on 16/3/13.
 */
public interface QrcodeService {

    String getQrCodeTicket(String qrcodeId);

    Qrcode createQrCode(QrCodeActionType qrCodeActionType, Integer refType) throws WXApiException, HttpException;

    Qrcode getQrcode(String sceneId);
}
