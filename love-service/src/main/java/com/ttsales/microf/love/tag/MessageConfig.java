package com.ttsales.microf.love.tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liyi on 2016/3/10.
 */
@RefreshScope
@Configuration
public class MessageConfig {

    @Value("${messages}")
    private String message;

    private String newMessage = message+"abc";

    public String getMessage(){
        return message;
    }

    public String getNewMessage(){
        return newMessage;
    }

    public String getRefreshMessage(){
        return message+"bcd";
    }

}

