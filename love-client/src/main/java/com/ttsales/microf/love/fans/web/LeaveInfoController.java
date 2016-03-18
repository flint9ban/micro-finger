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
@RequestMapping("leaveInfo")
public class LeaveInfoController {

    @Autowired
    private FansService fansService;
    @Autowired
    private TagService tagService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/init")
    public String initLeaveInfo(HttpServletRequest request) {

        return "fans/leaveInfo";
    }
    @RequestMapping(value = "/linkPage")
    public String linkPage(HttpServletRequest request,
                           HttpServletResponse response, String type, String url, String tagIds) {

        if ("province".equals(type)) {
            request.setAttribute("regions", getRegion("00"));
        }
        if ("city".equals(type)) {
            request.setAttribute("regions", getRegion(tagIds));
        }
        return url;
    }

    private JSONArray getRegion(String paraentCod) {
        List<OrgRegion> regions = orgService.findByParentRegionCode(paraentCod);
        return JSONArray.fromObject(regions);
    }

    private JSONArray getRegionTags(String tagIds) {
        return JSONArray.fromObject(fansService.findCityTags(tagIds));
    }


    @RequestMapping(value = "/getFanInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getFanInfo(String openId) {
        FansInfo fansInfo = fansService.getFansInfoByOpenId(openId);
        JSONObject json = new JSONObject();
        json.put("fansInfo", fansInfo);
        json.put("brands", orgService.getAllBrands());
        if (!StringUtils.isEmpty(fansInfo.getOrgCity())) {
            json.put("stores", orgService.findByCity(fansInfo.getOrgCity()));
        }
        return json;

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

}
