package com.ttsales.microf.love.fans.web;

import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.common.service.OrgService;
import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List
 * Created by zwt on 2016/3/15.
 */
@Controller
@RequestMapping("subTag")
public class SubTagController {

    @Autowired
    private FansService fansService;
    @Autowired
    private TagService tagService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/init")
    public String init() {
        return "fans/subTags";
    }

    @RequestMapping(value = "/linkPage")
    public String linkPage(HttpServletRequest request,
                           HttpServletResponse response, String type, String url, String tagIds) {
        if ("dataType".equals(type)) {
            request.setAttribute("tags", getTypeTags(tagIds, Long.parseLong("5")));
        }
        if ("brand".equals(type)) {
            request.setAttribute("tags", getGroupBrands( tagIds));
            request.setAttribute("selectTags",getSelectTags( tagIds));
        }
        if ("province".equals(type)) {
            request.setAttribute("regions", getRegion("00"));
        }
        if ("tag-city".equals(type)) {
            request.setAttribute("tags", getRegionTags(tagIds));
        }
        return url;
    }

    private JSONArray getRegionTags(String tagIds) {
        return JSONArray.fromObject(fansService.findCityTags(tagIds));
    }

    private JSONArray getTypeTags(String tagIds, Long containerId) {
        List<Tag> tags = fansService.findbyContainerId(containerId);
        if (StringUtils.isEmpty(tagIds)) {
            return getTags(tags, null);
        }
        String[] tagId = tagIds.split(",");
        return getTags(tags, tagId);
    }

    private List<Tag> getSelectTags(String tagIds){
       return  Arrays.asList(tagIds.split(",")).stream()
                .map(Long::parseLong).map(this::getTag).collect(Collectors.toList());

    }


    private  Tag getTag(Long id){
        return tagService.getTag(id);
    }

    private JSONArray getGroupBrands(String tagIds){
       List<Long> ids=Arrays.asList(tagIds.split(",")).stream()
                .map(Long::parseLong).collect(Collectors.toList());
        return  orgService.getGroupBrands(ids);
    }


    private JSONArray getTags(List<Tag> tags, String[] tagIds) {
        JSONArray array = new JSONArray();
        for (Tag tag : tags) {
            JSONObject json = new JSONObject();
            json.put("name", tag.getName());
            json.put("id", tag.getId());
            String state = "unselect";
            if (tagIds == null) {
                json.put("state", state);
                array.add(json);
                continue;
            }
            for (String id : tagIds) {
                if (Long.parseLong(id) == tag.getId()) {
                    state = "select";
                    break;
                }
            }
            json.put("state", state);
            array.add(json);
        }
        return array;

    }


    private JSONArray getRegion(String parentCod) {
        List<OrgRegion> regions = orgService.findByParentRegionCode(parentCod);
        return JSONArray.fromObject(regions);
    }



    @RequestMapping(value = "/queryFanTags", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray queryFanTags(String openId) {
        JSONArray array = new JSONArray();
      //  FansInfo fansInfo = fansService.getFansInfoByOpenId(openId);
        List<Tag> tags = fansService.getTagsByOpenId(openId);
        for (Tag tag : tags) {
            JSONObject json = new JSONObject();
            json.put("id", tag.getId());
            json.put("name", tag.getName());
            List<Container> containers = tagService.getTagContainerByTagId(tag.getId());
            for(Container ct:containers){
                if(ContainerType.SUBSCRIBE.equals(ct.getContainerType())){
                    json.put("categoryId", ct.getId());
                    break;
                }
            }

            array.add(json);
        }
        return array;
    }

    @RequestMapping(value = "/editFanTags", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editFanTags(String openId, String tagIds) {
        List<Long> idList = Arrays.asList(tagIds.split(",")).stream()
                .map(Long::parseLong).collect(Collectors.toList());
        JSONObject json = new JSONObject();
        fansService.editFansTags(openId, idList);
        return json;
    }


}
