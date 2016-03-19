package com.ttsales.microf.love.weixin;


import com.ttsales.microf.love.qrcode.domain.QrCodeType;
import com.ttsales.microf.love.util.HttpUtil;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.dto.Material;
import com.ttsales.microf.love.weixin.dto.NewsMaterial;
import net.sf.json.JSONObject;
import org.apache.catalina.util.URLEncoder;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liyi on 2016/3/9.
 */

@Component
public class MPApi {


    @Autowired
    private MPApiConfig mpApiConfig;

    public void sendCustomerMessage(String requestBody) throws HttpException,WXApiException {
        HttpUtil util = new HttpUtil();
        String result = util.post(mpApiConfig.getSendCustomerMessageApi(),requestBody);
        validateWeixinResult(JSONObject.fromObject(result));
    }

    public void sendMpnewsMessage(String openId,String mediaId) throws HttpException,WXApiException{
        String requestBody = "{\"touser\":\"%s\",\"msgtype\":\"mpnews\",\"mpnews\":{ \"media_id\":\"%s\"}}";
        String requestBodyWithParam = String.format(requestBody, openId,mediaId);
        sendCustomerMessage(requestBodyWithParam);
    }

    public List<NewsMaterial> getNewsMaterials() throws HttpException,WXApiException{
        return NewsMaterial.convert(getMaterialsOrginal(MaterialType.news,0,20));
    }

    private JSONObject getMaterialsOrginal(MaterialType type,Integer offSet,Integer count) throws HttpException,WXApiException{
        String requestBody = "{\"type\":\"%s\",\"offset\":\"%d\",\"count\":\"%d\"}";
        String requestBodyWithParam = String.format(requestBody, type,offSet,count);
        HttpUtil util = new HttpUtil();
        String result = util.post(mpApiConfig.getMaterialApi(),requestBodyWithParam);
        JSONObject returnValue = JSONObject.fromObject(result);
        validateWeixinResult(returnValue);
        return returnValue;
    }

    public List<Material> getMaterials(MaterialType type, Integer offSet, Integer count) throws HttpException,WXApiException{
        return Material.convert(getMaterialsOrginal(type,offSet,count));
    }


    public String getQrCodeTicket(Long expireSeconds, QrCodeType actionType, Long sceneId, String sceneStr) throws WXApiException, HttpException {
        return createQrCodeTicket(expireSeconds,actionType,sceneId,sceneStr);
    }

    public String createQrCodeTicket(Long expireSeconds,QrCodeType actionType,Long sceneId,String sceneStr) throws HttpException, WXApiException {
        String requestWithParam = null;
        if (QrCodeType.QR_SCENE.equals(actionType)){
            String request = "{\"expire_seconds\": %d, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
            requestWithParam = String.format(request, expireSeconds,sceneId);
        }else if(QrCodeType.QR_LIMIT_STR_SCENE.equals(actionType)){
            String request = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"%s\"}}}";
            requestWithParam = String.format(request,sceneStr);
        }else{
            String request = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
            requestWithParam = String.format(request,sceneId);
        }
        HttpUtil util = new HttpUtil();
        String result = util.post(mpApiConfig.getCreateQrCodeTicketApi(),requestWithParam);
        JSONObject returnValue = JSONObject.fromObject(result);
        validateWeixinResult(returnValue);
        String ticket = returnValue.getString("ticket");
        return URLEncoder.DEFAULT.encode(ticket);
    }

    public String getOpenIdByAccessToken(String code) throws HttpException, WXApiException {
        HttpUtil util = new HttpUtil();
        String result = util.get(mpApiConfig.getOathAccessToken()+"?code="+code);
        JSONObject returnValue = JSONObject.fromObject(result);
        validateWeixinResult(returnValue);
        return returnValue.optString("openId",null);
    }


    private void validateWeixinResult(JSONObject jsonObject) throws WXApiException {
        if (jsonObject.containsKey("errcode")&&!"0".equals(jsonObject.getString("errcode"))){
            throw new WXApiException(jsonObject.toString());
        }
    }

}
