package com.ttsales.microf.love.client.weixin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liyi on 2016/3/10.
 */
@Configuration
@RefreshScope
public class MPApiConfig {

    @Value("${wx.apiUrl}")
    private String apiUrl;

    @Value("${wx.token}")
    private String token;

    public String getSendCustomerMessageApi(){
        return apiUrl+"/api/message-custom-send";
    }

    public String getMaterialApi(){
        return apiUrl+"/api/material-batchget-material";
    }

    public String getToken(){
        return  token;
    }

}
