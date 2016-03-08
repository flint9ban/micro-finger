package com.ttsales.microf.client.tag.web;

import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.stream.Collector;
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
    public List<Tag> getReservationName1() {
        List<Tag> tags= restTemplate
                .exchange("http://tag-service/tags", HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Tag>>() {
                }).getBody().getContent().stream().collect(Collectors.toList());
        return tags;
    }




}

@Data
class  Tag{

    private Long id;
    private String name;


}