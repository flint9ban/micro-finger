package com.ttsales.microf.love.weixin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Created by liyi on 2016/3/9.
 */

@Component
@RefreshScope
public class MPApi {


    @Value("${weixinApiPre}")
    private String weixinApiPre;

    private String sendCustomerMessageApi=weixinApiPre+"/api/material-batchget-material";

    public String sendCustomerMessage(String broker, Object sendMsg) {
        String accessToken = this.getAccessTokenByBroker(broker);
        String customerMsgUrl = BundleUtil.getProperty(SASConstants.RESOURCE_WEIXIN, "wx.mp.url.customerMessage", accessToken);
        log.info("sendMsgUrl:" + customerMsgUrl);
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(customerMsgUrl);
            post.setHeader("Content-Type", "application/json");
            HttpEntity msgEntity = new StringEntity(JSONObject.fromObject(sendMsg).toString(), "UTF-8");
            post.setEntity(msgEntity);
            CloseableHttpResponse response = client.execute(post);

            String back = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject result = JSONObject.fromObject(back);
                System.err.println(result);
                return result.getString("errcode");
            } else {
                log.error("sendCustomerMessage: " + back);
                return String.valueOf(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return "failed";
        }
    }
}
