package com.ttsales.microf.love.tag.web;

import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
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
import java.util.stream.IntStream;

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
            container.setContainerType(ContainerType.DEFINED);
            tagService.createContainer(container);
        }
        return json;
    }

    @RequestMapping(value = "/find-type-name",method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> findTypeByName(String name){
        List<Container> containers = tagService.findContainerLimit5ByName("%"+name+"%", ContainerType.DEFINED);
        return containers.stream()
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
    @RequestMapping(value = "/query")
    @ResponseBody
    public JSONObject query(Integer page,Integer rows,String tagName,String typeIds){

        int startRow = 0;
        int endRow = 0;
        if(page==null||rows==null){
            startRow = 0;
            endRow = 10;
        }else {
            startRow = (page-1)*rows;
            endRow = startRow+rows;
        }
        List<Long> ids = getTypeIds(typeIds);
        List<Tag> tags = tagService.queryTags(tagName,ids);
        int count = tags.size();
        if(endRow>count){
            endRow = count;
        }
        List<JSONObject> content = tags.subList(startRow,endRow).stream().map(this::getTagWithType).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",count);
        jsonObject.put("rows",content);
        return jsonObject;
    }

    private JSONObject getTagWithType(Tag tag){
        String[] types = tagService.getTagContainerByTagId(tag.getId(),ContainerType.DEFINED).stream().map(container -> {
                                            return new String[]{container.getId().toString(),container.getName()};})
                .reduce(
                        new String[]{"",""},(prePair,pair) -> {
                            prePair[0]+=pair[0]+",";
                            prePair[1]+=pair[1]+",";
                            return  prePair;
                        }
                );
        JSONObject json = new JSONObject();
        json.put("id",tag.getId());
        json.put("name",tag.getName());
        String typeIds = "";
        String typeNames = "";
        if(types!=null&&types.length==2){
            String typeIds1 = types[0];
            String typeNames1= types[1];
            if (typeIds1 != null&&typeIds1.endsWith(",")) {
                typeIds = typeIds1.substring(0,typeIds1.length()-1);
                typeNames = typeNames1.substring(0,typeNames1.length()-1);
            }else if(typeIds!=null){
                typeIds = typeIds1;
                typeNames =typeNames1;
            }
        }
        json.put("typeIds",typeIds);
        json.put("typeNames",typeNames);
        return json;
    }

    @RequestMapping(value = "/createTag",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createTag(String tagName,String typeIds){
        JSONObject json = new JSONObject();
        if(tagService.findTagByName(tagName)!=null){
            json.put("error","兴趣点已存在！");
        }else{
            List<Long> ids = getTypeIds(typeIds);
            Tag tag = new Tag();
            tag.setName(tagName);
            Long id = tagService.createTag(tag,ids);
            json.put("id",id);
        }
       return json;
    }

    @RequestMapping(value = "/updateTag",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateTag(String tagName,Long tagId,String typeIds){
        JSONObject json = new JSONObject();
        Tag oldTag = tagService.findTagByName(tagName);
        if(oldTag==null||oldTag.getId().equals(tagId)){
            List<Long> ids = getTypeIds(typeIds);
            Tag tag =  new Tag();
            tag.setName(tagName);
            tag.setId(tagId);
            tagService.updateTag(tag,ids);
        }else {
            json.put("error","兴趣点已存在！");
        }
        return json;
    }

    private List<Long> getTypeIds(String typeIds){
        List<Long> ids = new ArrayList<Long>();
        if(typeIds!=null&&typeIds.length()>0){
            ids = Arrays.asList(typeIds.split(",")).stream()
                    .map(Long::parseLong).collect(Collectors.toList());
        }
        return ids;
    }

    @RequestMapping(value = "/deleteTag",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateTag(Long id){
       tagService.removeTag(id);
        return new JSONObject();
    }


}

