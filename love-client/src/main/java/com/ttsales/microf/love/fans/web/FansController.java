package com.ttsales.microf.love.fans.web;

import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.common.service.OrgService;
import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.catalina.Session;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.Region;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List
 * Created by zwt on 2016/3/15.
 */
@Controller
@RequestMapping("fans")
public class FansController {

    @Autowired
    private FansService fansService;
    @Autowired
    private TagService tagService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/initLeaveInfo")
    public String initLeaveInfo(HttpServletRequest request) {

        return "fans/leaveInfo";
    }

    @RequestMapping(value = "/init")
    public String init() {
        return "abc";
    }

    @RequestMapping(value = "/initSubTags")
    public String initSubTags() {
        return "fans/subTags";
    }

    @RequestMapping(value = "/initFans")
    public String initFans() {
        return "fans/fans";
    }


    @RequestMapping(value = "/linkPage")
    public String linkPage(HttpServletRequest request,
                           HttpServletResponse response, String type, String url, String tagIds) {
        if ("dataType".equals(type)) {
            request.setAttribute("tags", getTypeTags(tagIds, Long.parseLong("5")));
        }
        if ("brand".equals(type)) {
            request.setAttribute("tags", getTypeTags(tagIds, Long.parseLong("6")));
        }
        if ("province".equals(type)) {
            request.setAttribute("regions", getRegion("00"));
        }
        if ("tag-city".equals(type)) {
            request.setAttribute("tags", getRegionTags(tagIds));
        }
        if ("city".equals(type)) {
            request.setAttribute("regions", getRegion(tagIds));
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


    @RequestMapping(value = "/getFanInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getFanInfo(String openId) {
        FansInfo fansInfo = fansService.getFansInfoByOpenId(openId);
//        if (fansInfo == null) {
//            fansService.initTestData();
//            orgService.creatTestData();
//            fansInfo = fansService.getFansInfoByOpenId(openId);
//        }///TODO
        //   FansInfo fansInfo= fansService.getFansInfoByOpenId(openId);
        JSONObject json = new JSONObject();
        json.put("fansInfo", fansInfo);
        json.put("brands", cloneBrandData());
        if (!StringUtils.isEmpty(fansInfo.getOrgCity())) {
            json.put("stores", orgService.findByCity(fansInfo.getOrgCity()));
        }
        return json;

    }

    private JSONArray cloneBrandData() {
        JSONArray array = new JSONArray();
        JSONObject brand = new JSONObject();
        brand.put("id", "100");
        brand.put("name", "东风日产");
        JSONObject brand2 = new JSONObject();
        brand2.put("id", "101");
        brand2.put("name", "广汽三菱");
        array.add(brand);
        array.add(brand2);
        return array;
    }


    @RequestMapping(value = "/getRegions", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getRegions(String parentCode) {
        return getRegion(parentCode);
    }

    private JSONArray getRegion(String paraentCod) {
        List<OrgRegion> regions = orgService.findByParentRegionCode(paraentCod);

        return JSONArray.fromObject(regions);
    }


    @RequestMapping(value = "/getStores", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getStores(String city) {
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isEmpty(city)) {
            return jsonArray;
        }
        List<OrgStore> orgStores = orgService.findByCity(city);
        jsonArray = JSONArray.fromObject(orgStores);
        return jsonArray;
    }


    @RequestMapping(value = "/getFansQueryParms", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getFansQueryParms(String type, String id) {
        //  orgService.creatTestData();
        JSONArray jsonArray = new JSONArray();
        if ("store".equals(type)) {
            List<OrgStore> orgStores = orgService.findByCity(id);
            jsonArray = JSONArray.fromObject(orgStores);
        }
        if ("brand".equals(type)) {
            return cloneBrandData();
        }
        if ("province".equals(type)) {
            List<OrgRegion> orgRegions = orgService.findByParentRegionCode("00");
            jsonArray = JSONArray.fromObject(orgRegions);
        }
        if ("city".equals(type)) {
            OrgRegion proRegion = orgService.findRegionById(id);
            List<OrgRegion> orgRegions = orgService.findByParentRegionCode(proRegion.getRegionCode());
            jsonArray = JSONArray.fromObject(orgRegions);
        }
        return jsonArray;
    }


    @RequestMapping(value = "/queryFans", method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> queryFans(FansInfo fansInfo, String tagIds) {
     List<Long> tagIdList=getTypeIds(tagIds);
        List<FansInfo> fansInfos = fansService.queryFans(fansInfo, tagIdList);
        return fansInfos.stream().map(this::getFanTags)
                .collect(Collectors.toList());
    }

    private List<Long> getTypeIds(String typeIds){
        List<Long> ids = new ArrayList<Long>();
        if(typeIds!=null&&typeIds.length()>0){
            ids = Arrays.asList(typeIds.split(",")).stream()
                    .map(Long::parseLong).collect(Collectors.toList());
        }
        return ids;
    }



    private JSONObject getFanTags(FansInfo fansInfo) {
        List<Tag> tags = fansService.getTagsByOpenId(fansInfo.getOpenId());
        String tagName = "";
        for (Tag tag : tags) {
            tagName += tag.getName() + ",";
        }
        JSONObject json = new JSONObject();
        json.put("id", fansInfo.getId());
        json.put("mobile", fansInfo.getMobile());
        json.put("name", fansInfo.getName());
        json.put("orgType", fansInfo.getOrgType());
        json.put("orgPosition", fansInfo.getOrgPosition());
        json.put("orgStore", fansInfo.getOrgStore());
        json.put("orgBrand", fansInfo.getOrgBrand());
        json.put("orgArea", fansInfo.getOrgArea());
        json.put("orgProvince", fansInfo.getOrgProvince());
        json.put("orgCity", fansInfo.getOrgCity());
        json.put("tags", tagName.length() > 0 ? tagName.substring(0, tagName.length() - 1) : "");
        return json;


    }

    @RequestMapping(value = "/getArticles", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getArticles() {
        JSONArray array = JSONArray.fromObject(articleService.getAllArricles());
        return array;
    }

    @RequestMapping(value = "/sendArticles", method = RequestMethod.POST)
    @ResponseBody
    public void sendArticles(FansInfo fansInfo, String tagIds, String mediaId, Long articleId) {
        List<FansInfo> fansInfos = fansService.queryFans(fansInfo,  getTypeIds(tagIds));
        List<String> openIds = new ArrayList<String>();
        for (FansInfo fanInfo : fansInfos) {
            openIds.add(fanInfo.getOpenId());
        }
        articleService.sendArticle(articleId, mediaId, openIds);
    }


    @RequestMapping(value = "/editFanInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editFanInfo(FansInfo fansInfo) {
        JSONObject json = new JSONObject();
        if (fansInfo == null) {
            json.put("errMsg", "参数错误");
        }
        fansService.editFansInfo(fansInfo);
        return json;
    }

    @RequestMapping(value = "/queryFanTags", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray queryFanTags(String openId) {
        FansInfo fansInfo = fansService.getFansInfoByOpenId(openId);
//        if (fansInfo == null) {
//            fansService.initTestData();
//            orgService.creatTestData();
//            fansInfo = fansService.getFansInfoByOpenId(openId);
//        }
        List<Tag> tags = fansService.getTagsByOpenId(openId);
        JSONArray array = new JSONArray();
        for (Tag tag : tags) {
            JSONObject json = new JSONObject();
            json.put("id", tag.getId());
            json.put("name", tag.getName());
            List<Container> containers = tagService.getTagContainerByTagId(tag.getId());
            if (containers != null) {
                json.put("categoryId", containers.get(0).getId());
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
