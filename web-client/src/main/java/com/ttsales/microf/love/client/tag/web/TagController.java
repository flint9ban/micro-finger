package com.ttsales.microf.love.client.tag.web;

import com.ttsales.microf.love.client.tag.domain.Container;
import com.ttsales.microf.love.client.tag.domain.Tag;
import com.ttsales.microf.love.client.tag.service.TagService;
import lombok.Data;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 2016/3/7.
 */
@Controller
@RequestMapping("tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/init")
    public String init(){
        return "tag/tag";
    }

    @RequestMapping(value="/abc")
    @ResponseBody
    public String getAbc(){
        return "abc";
    }

    @RequestMapping(value = "/createType",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createType(Container container){
        JSONObject json = new JSONObject();
        if(tagService.isContainerExist(container)){
            json.put("error","类型已存在！");
        }else{
            tagService.createContainer(container);
        }
        return json;
    }


    @RequestMapping(value = "/createTag",method = RequestMethod.POST)
    public JSONObject createTag(Tag tag){
//        tagService.createTag(tag);
        return null;
    }


//    @Autowired
//    private RestTemplate restTemplate;
//
//    @RequestMapping(method = RequestMethod.GET, value = "/names")
//    @ResponseBody
//    public Collection<String> getReservationNames() {
//        return restTemplate
//                .exchange("http://tag-domain/tagController/tags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Tag>>() {
//                })
//                .getBody()
//                .stream()
//                .map(Tag::getName)
//                .collect(Collectors.toList());
//
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/name")
//    @ResponseBody
//    public List<Tag> getReservationName1() {
//        List<Tag> tags= restTemplate
//                .exchange("http://tag-domain/tags", HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Tag>>() {
//                }).getBody().getContent().stream().collect(Collectors.toList());
//        return tags;
//    }




}

