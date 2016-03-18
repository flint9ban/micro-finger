package com.ttsales.microf.love.fans.web;

import com.ttsales.microf.love.article.domain.Article;
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
import net.sf.json.JsonConfig;
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




    @RequestMapping(value = "/init")
    public String initFans() {
        return "fans/fans";
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
        JSONArray jsonArray = new JSONArray();
        if ("store".equals(type)) {
            List<OrgStore> orgStores = orgService.findByCity(id);
            jsonArray = JSONArray.fromObject(orgStores);
        }
        if ("brand".equals(type)) {
            return  JSONArray.fromObject(orgService.getAllBrands());
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
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"sendTime","creatAt","lastUpdateAt","reloadTime"});
        JSONArray array = JSONArray.fromObject(articleService.getAllArricles(),jsonConfig);
        return array;
    }


    @RequestMapping(value = "/sendArticles", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendArticles(FansInfo fansInfo, String tagIds, String mediaId, Long articleId) {
        List<FansInfo> fansInfos = fansService.queryFans(fansInfo,  getTypeIds(tagIds));
        List<String> openIds = new ArrayList<String>();
        for (FansInfo fanInfo : fansInfos) {
            openIds.add(fanInfo.getOpenId());
        }
        JSONObject json=new JSONObject();
        if(openIds.size()==0){
            json.put("errMsg","没有可发送的用户");
            return json;
        }
        articleService.sendArticle(articleId, mediaId, openIds);
        return json;
    }







}
