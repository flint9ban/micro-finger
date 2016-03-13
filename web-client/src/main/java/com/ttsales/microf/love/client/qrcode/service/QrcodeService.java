package com.ttsales.microf.love.client.qrcode.service;

import com.ttsales.microf.love.client.qrcode.Qrcode;
import com.ttsales.microf.love.client.weixin.QrCodeActionType;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

/**
 * Created by liyi on 16/3/13.
 */
public interface QrcodeService {

    String getQrCodeTicket(String qrcodeId);

    Qrcode createQrCode(QrCodeActionType qrCodeActionType,Integer refType) throws WXApiException, HttpException;

    Qrcode getQrcode(String sceneId);
}
