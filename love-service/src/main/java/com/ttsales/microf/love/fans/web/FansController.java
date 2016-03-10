package com.ttsales.microf.love.fans.web;

import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.domain.FansInfoTag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/6.
 */
@RestController
@RequestMapping(value = "fans")
public class FansController {

//    @Autowired
//    private FansRepository fansRepository;
//
//    @Autowired
//    private FansTagRepository fansTagRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TagClient tagClient;

//    @RequestMapping(method = RequestMethod.GET)
//    public List<Fans> getAll(){
//        List<FansInfo> fanses = fansRepository.findAll();
//        return fanses.stream().map(fans->getFans(fans)).collect(Collectors.toList());
//    }

    @RequestMapping(method = RequestMethod.GET,name="tag")
    public List<Tag> getTag(){
        List<Tag> tags= restTemplate
                .exchange("http://tag-domain/tags", HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Tag>>() {
                }).getBody().getContent().stream().collect(Collectors.toList());
        return tags;
    }

//    private Fans getFans(FansInfo info){
//        Fans fans = new Fans();
//        fans.setId(info.getId());
//        fans.setMobile(info.getMobile());
//        fans.setName(info.getName());
//        List<FansInfoTag> fk = fansTagRepository.findAllByFansId(info.getId());
//        List<String> tags = fk.stream().map(fansInfoTag -> getTag(fansInfoTag)).collect(Collectors.toList());
//        fans.setTagNames(tags);
//        return fans;
//    }

    private String getTag(FansInfoTag fansTag){
        RestTemplate restTemplate = new RestTemplate();
        return null;
    }

    public static void main(String[] args) {
        Fans fans = new Fans();
        List<String> tagNames= Arrays.asList("a","b");
        fans.setTagNames(tagNames);
        System.out.println("args = [" + fans.toString()+ "]");
    }


}


@Data
class Fans extends FansInfo {
    private List<String> tagNames;

    @Override
    public String toString() {
        String toString = super.toString();
        if(tagNames!=null){
            return tagNames.stream().reduce(toString,(preTagName, tagName) -> preTagName += "," + tagName);
        }
        return toString;
    }
}

@Data
class  Tag{

    private Long id;
    private String name;


}