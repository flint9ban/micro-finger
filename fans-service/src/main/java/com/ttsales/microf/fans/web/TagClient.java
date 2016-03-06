package com.ttsales.microf.fans.web;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
@FeignClient("tag-service")
public interface TagClient {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    List<String> getTags();
}
