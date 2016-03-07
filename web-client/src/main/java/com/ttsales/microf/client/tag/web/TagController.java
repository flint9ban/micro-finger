package com.ttsales.microf.client.tag.web;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 2016/3/7.
 */
@Controller
@RequestMapping("tag/tagController")
public class TagController {

    @RequestMapping(value = "init")
    public String init(){
        return "tag";
    }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET, value = "/names")
    @ResponseBody
    public Collection<String> getReservationNames() {
        return restTemplate
                .exchange("http://tag-service/tagController/tags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Tag>>() {
                })
                .getBody()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/name")
    @ResponseBody
    public String getReservationName1() {
        String cout = restTemplate
                .getForObject("http://tag-service/tags",String.class);

        return cout;
    }




}
class  Tag{
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public Tag(){

    }

    public Tag(Long id,String name){
        setId(id);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}