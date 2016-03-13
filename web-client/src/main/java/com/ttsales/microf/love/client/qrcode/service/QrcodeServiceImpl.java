package com.ttsales.microf.love.client.qrcode.service;

import com.ttsales.microf.love.client.qrcode.Qrcode;
import com.ttsales.microf.love.client.weixin.MPApi;
import com.ttsales.microf.love.client.weixin.QrCodeActionType;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Created by liyi on 16/3/13.
 */
public class QrcodeServiceImpl implements QrcodeService {

    @Autowired
    private MPApi mpApi;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getQrCodeTicket(String qrcodeId) {
        Qrcode qrcode = restTemplate.getForObject("http://love-service/qrCodes/"+qrcodeId, Qrcode.class);
        return qrcode.getTicket();
    }

    @Override
    public Qrcode createQrCode(QrCodeActionType qrCodeActionType,Integer refType) throws WXApiException, HttpException {
        String sceneId = UUID.randomUUID().toString();
        String ticketId = null;
        if(QrCodeActionType.QR_LIMIT_STR_SCENE.equals(qrCodeActionType)){
            ticketId = mpApi.getQrCodeTicket(null,qrCodeActionType,null,sceneId);
        }else{
            ticketId = mpApi.getQrCodeTicket(2592000l,qrCodeActionType,null,sceneId);
        }
        Qrcode qrcode = new Qrcode();
        qrcode.setSceneId(sceneId);
        qrcode.setTicket(ticketId);
        qrcode.setQrcodeType(qrCodeActionType);
        qrcode.setRefType(refType);
        qrcode = restTemplate.postForObject("http://love-service/qrCodes",qrcode,Qrcode.class);
        return qrcode;
    }

    @Override
    public Qrcode getQrcode(String sceneId) {
        return restTemplate.getForObject("http://love-service/qrCodes/search/find-sceneId?sceneId="+sceneId,Qrcode.class);
    }


}
