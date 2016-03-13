package com.ttsales.microf.love.tag.web;

import com.ttsales.microf.love.tag.MessageConfig;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liyi on 2016/3/7.
 */

@RefreshScope
@RestController
@RequestMapping("tagController/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MessageConfig config;

    @Value("${messages}")
    private String message;

    private String newMessage = message+"abc";

    @RequestMapping("efg")
    public String get2(){
        return config.getRefreshMessage();
    }

    @RequestMapping("/abc")
    public String get(){
        return  config.getNewMessage();
    }

    @RequestMapping("/bcd")
    public String get1(){
        return  config.getMessage();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> getAll(){
        System.out.println("call in tag controller");
        return tagRepository.findAll();
    }
}
