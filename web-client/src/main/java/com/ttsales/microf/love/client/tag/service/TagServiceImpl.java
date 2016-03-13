package com.ttsales.microf.love.client.tag.service;

import com.ttsales.microf.love.client.qrcode.Qrcode;
import com.ttsales.microf.love.client.qrcode.service.QrcodeService;
import com.ttsales.microf.love.client.tag.domain.Container;
import com.ttsales.microf.love.client.tag.domain.TagContainer;
import com.ttsales.microf.love.client.weixin.QrCodeActionType;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QrcodeService qrcodeService;

    @Override
    public String createQrcodeTicket(Long containerId) throws WXApiException, HttpException {
        Qrcode qrcode = qrcodeService.createQrCode(QrCodeActionType.QR_LIMIT_STR_SCENE, Qrcode.REF_TYPE_TAG_CONTAINER);
        String ticket = qrcode.getTicket();
        Container container = getContainer(containerId);
        container.setQrcodeTicket(ticket);
        putContainer(container);
        return ticket;
    }

    @Override
    public List<TagContainer> getTagContainer(String qrcodeTicket) {
        Container container = restTemplate.getForObject("http://love-service/containers/search/find-qrcodeTicket?qrcodeTicket="+qrcodeTicket,Container.class);
        if (container != null) {
            return restTemplate.exchange("http://love-service/tagContainers/search/find-containerId?containerId="+container.getId(),
                    HttpMethod.GET,null, new ParameterizedTypeReference< Resources<TagContainer>>(){})
                    .getBody().getContent().stream().collect(Collectors.toList());
        }
        return null;
    }

    private void putContainer(Container container){
        restTemplate.put("http://love-service/containers/"+container.getId(),container);
    }

    private Container getContainer(Long containerId){
        return restTemplate.getForObject("http://love-service/containers/"+containerId,Container.class);
    }
}
