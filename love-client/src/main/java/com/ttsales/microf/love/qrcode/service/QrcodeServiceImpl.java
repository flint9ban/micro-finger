package com.ttsales.microf.love.qrcode.service;


import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.domain.QrCodeType;
import com.ttsales.microf.love.qrcode.repository.QrCodeRepository;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class QrcodeServiceImpl implements QrcodeService {

    @Autowired
    private MPApi mpApi;

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Override
    public String getQrCodeTicket(Long qrcodeId) {
        QrCode qrcode = qrCodeRepository.findOne(qrcodeId);
        return qrcode.getTicket();
    }

    @Override
    public QrCode createQrCode(QrCodeType qrCodeType, Integer refType) throws WXApiException, HttpException {
        // limit qrcode use uuid be sceneid
        if(QrCodeType.QR_LIMIT_STR_SCENE.equals(qrCodeType)){
            String sceneId = UUID.randomUUID().toString();
            String ticketId = mpApi.getQrCodeTicket(null,qrCodeType,null,sceneId);
            QrCode qrcode = new QrCode();
            qrcode.setSceneId(sceneId);
            qrcode.setTicket(ticketId);
            qrcode.setQrCodeType(qrCodeType);
            qrcode.setRefType(refType);
            qrcode = qrCodeRepository.save(qrcode);
            return qrcode;
            //unlimit qrcode use qrcode id be sceneid;
        }else if(QrCodeType.QR_SCENE.equals(qrCodeType)){
            QrCode qrcode = new QrCode();
            qrcode.setQrCodeType(qrCodeType);
            qrcode.setRefType(refType);
            qrcode = qrCodeRepository.save(qrcode);
            String ticketId = mpApi.getQrCodeTicket(2592000l,qrCodeType,qrcode.getId(),null);
            qrcode.setTicket(ticketId);
            qrcode.setSceneId(qrcode.getId().toString());
            qrCodeRepository.save(qrcode);
            return qrcode;
        }
        return null;
    }

    @Override
    public QrCode getQrcode(String sceneId) {
        return  qrCodeRepository.findBySceneId(sceneId);
    }


}
