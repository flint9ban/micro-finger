package com.ttsales.microf.love.client.weixin;

import com.ttsales.microf.love.client.weixin.dto.Material;
import com.ttsales.microf.love.client.weixin.dto.NewsMaterial;
import com.ttsales.microf.love.util.HttpUtil;
import com.ttsales.microf.love.util.WXApiException;

import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liyi on 2016/3/9.
 */

@Component
@RefreshScope
public class MPApi {


    @Autowired
    private MPApiConfig mpApiConfig;

    public void sendCustomerMessage(String requestBody) throws HttpException,WXApiException{
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


    private void validateWeixinResult(JSONObject jsonObject) throws WXApiException {
        if (jsonObject.containsKey("errcode")){
            throw new WXApiException(jsonObject.toString());
        }
    }

}
