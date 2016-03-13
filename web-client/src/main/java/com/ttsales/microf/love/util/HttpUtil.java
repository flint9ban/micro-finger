package com.ttsales.microf.love.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Created by liyi on 2016/3/10.
 */
public class HttpUtil {

    private Logger log = Logger.getLogger(HttpUtil.class);

    public String post(String url,String param) throws HttpException{
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            HttpEntity msgEntity = new StringEntity(param, "UTF-8");
            post.setEntity(msgEntity);
            CloseableHttpResponse response = client.execute(post);

            String back = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return  back;
            } else {
                log.error(url+":" + back);
                throw new HttpException(url+response.getStatusLine().getStatusCode());
//                return String.valueOf(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return "failed";
        }
    }

    public String post(String url, JSONObject json) throws HttpException{
        return post(url,json.toString());
    }

    public String get(String url) throws HttpException{
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);

            CloseableHttpResponse response = client.execute(get);

            String back = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return  back;
            } else {
                log.error(url+":" + back);
                throw new HttpException(url+response.getStatusLine().getStatusCode());
//                return String.valueOf(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return "failed";
        }
    }

}
