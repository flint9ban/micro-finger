package com.ttsales.microf.love.tag.web;

import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
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

    @RequestMapping(value = "/find-type-name",method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> findTypeByName(String name){
        return tagService.findContainerByName("%"+name+"%").stream()
                .map(this::put2Json)
                .collect(Collectors.toList());
    }

    private JSONObject put2Json(Container container){
        JSONObject json= new JSONObject();
        json.put("text",container.getName());
        json.put("value",container.getId());
        return json;
    }

    
    //// TODO: 2016/3/15 fenye
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public List<JSONObject> query(String tagName,String typeIds){
        List<Long> ids = new ArrayList<Long>();
        if(typeIds!=null){
            ids = Arrays.asList(typeIds.split(",")).stream()
                    .map(Long::parseLong).collect(Collectors.toList());
        }
        return tagService.queryTags(tagName,ids).stream().map(this::getTagWithType)
                .collect(Collectors.toList());
    }

    private JSONObject getTagWithType(Tag tag){
        String types = tagService.getTagContainerByTagId(tag.getId()).stream().map(Container::getName)
                .reduce(String::concat).get();
        JSONObject json = new JSONObject();
        json.put("id",tag.getId());
        json.put("name",tag.getName());
        json.put("types",types);
        return json;
    }

    @RequestMapping(value = "/createTag",method = RequestMethod.POST)
    public JSONObject createTag(Tag tag,String typeIds){
        JSONObject json = new JSONObject();
        if(tagService.findTagByName(tag.getName())!=null){
            json.put("error","兴趣点已存在！");
        }else{
            List<Long> ids = getTypeIds(typeIds);
            Long id = tagService.createTag(tag,ids);
            json.put("id",id);
        }
       return json;
    }

    @RequestMapping(value = "/updateTag",method = RequestMethod.POST)
    public JSONObject updateTag(Tag tag,String typeIds){
        JSONObject json = new JSONObject();
        Tag oldTag = tagService.findTagByName(tag.getName());
        if(oldTag==null||oldTag.getId().equals(tag.getId())){
            List<Long> ids = getTypeIds(typeIds);
            oldTag.setName(tag.getName());
            tagService.updateTag(oldTag,ids);
        }else {
            json.put("error","兴趣点已存在！");
        }
        return json;
    }

    private List<Long> getTypeIds(String typeIds){
        List<Long> ids = new ArrayList<Long>();
        if(typeIds!=null){
            ids = Arrays.asList(typeIds.split(",")).stream()
                    .map(Long::parseLong).collect(Collectors.toList());
        }
        return ids;
    }

    @RequestMapping(value = "/deleteTag",method = RequestMethod.POST)
    public void updateTag(Long id){
       tagService.removeTag(id);
    }




}

