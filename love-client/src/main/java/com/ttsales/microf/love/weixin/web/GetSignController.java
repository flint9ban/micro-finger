package com.ttsales.microf.love.weixin.web;

import com.ttsales.microf.love.util.ResponseUtil;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import com.ttsales.microf.love.weixin.MPApiConfig;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping(value="weixin/jsSDK/")
public class GetSignController {
	

    @Autowired
    private MPApi mpApi;

    @Autowired
    private MPApiConfig mpApiConfig;
	
	@RequestMapping(value="getSign.do",method = RequestMethod.POST)
	protected ModelAndView getSign(HttpServletRequest request, HttpServletResponse response, String url) throws WXApiException, HttpException, IOException {
		if(url.indexOf("#")>0){
			url = url.substring(0,url.indexOf("#"));
		}
		String jsapi_ticket =  mpApi.getJsApiTicket();
        Map<String, String> ret = sign(jsapi_ticket, url);
        JSONObject jsonObject =  JSONObject.fromObject(ret, new JsonConfig());
        response.setHeader("Access-Control-Allow-Origin", "*");
        ResponseUtil.toClient(response, jsonObject);
		return null;
	}
	
	
	
	
    public  Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有�?
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
//        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        ret.put("appId", mpApiConfig.getAppid());
        ret.put("timestamp", timestamp);
        ret.put("nonceStr", nonce_str);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
