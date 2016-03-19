package com.ttsales.microf.love.weixin.web;



import com.ttsales.microf.love.util.CookieUtils;
import com.ttsales.microf.love.util.URLUtils;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.MPApi;
import com.ttsales.microf.love.weixin.MPApiConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth/")
public class AuthController {
	public static final String COOKIE_OPENID = "_openId";
	public static final int COOKIE_EXPIRY = 30 * 24 * 60 * 60;

	public static  final String SNSAPI_BASE ="snsapi_base";

	private final static Logger logger = Logger.getLogger(AuthController.class);

	@Autowired
	private MPApiConfig mpApiConfig;

	@Autowired
	private MPApi mpApi;



	@RequestMapping(value = "check", params = { "scope=snsapi_base",
			"target_uri" }, method = RequestMethod.GET)
	public String checkBaseAuth(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "target_uri") String targetUri,
			@RequestParam(value = "param_name", defaultValue = "userId") String paramName,
			@RequestParam(value = "redirect", required = false) String redirect)
			 {
		if (StringUtils.isEmpty(targetUri)) {
			return targetUri;
		}
				 System.out.println("check========================");
		return buildBaseAuthURL(request, targetUri, paramName,
				redirect != null);
	}
	
	@RequestMapping(value = "reset", params = { "scope=snsapi_base", "code",
			"target_uri", "param_name" }, method = RequestMethod.GET)
	public String resetBaseAuth(HttpServletRequest request,
								HttpServletResponse response, String code,
								@RequestParam(value = "target_uri") String targetUri,
								@RequestParam(value = "param_name") String paramName,
								@RequestParam(value = "redirect", required = false) String redirect)
			throws  IOException {
		try{
			System.out.println(code);
			String openId = mpApi.getOpenIdByAccessToken(code);
			if(StringUtils.isEmpty(openId)){
				throw new WXApiException("授权失败");
			}
			CookieUtils.setCookie(response, COOKIE_OPENID, openId, COOKIE_EXPIRY);
			return buildTargetURI(targetUri, paramName, openId, redirect != null);
		}catch (Exception e){
			logger.error(e);
		}
		return buildTargetURI(targetUri, paramName, null, redirect != null);
	}


	private String buildBaseAuthURL(HttpServletRequest request,
			String targetUri, String paramName, boolean redirect)
			{
		StringBuilder url = new StringBuilder(mpApiConfig.getAppUrl());
		url.append("auth/reset?scope=snsapi_base&target_uri=");
		url.append(URLUtils.encode(targetUri));
		url.append("&param_name=");
		url.append(paramName);
		if (redirect) {
			url.append("&redirect");
		}
		return "redirect:"+mpApiConfig.getOauthCodeApi()+"?redirect_uri="+URLUtils.encode(url.toString())+"&scope="+SNSAPI_BASE;
	}

	private String buildTargetURI(String targetUri, String paramName,
			String value, boolean redirect) {
		String[] uriParts = targetUri.split("\\?", 2);
		String noSearch = uriParts[0];
		List<String> params = new ArrayList<String>();
		if (uriParts.length == 2) {
			String exceptPrefix = paramName + "=";
			for (String param : uriParts[1].split("&")) {
				if (!StringUtils.isEmpty(param)
						&& !param.startsWith(exceptPrefix)) {
					params.add(param);
				}
			}
		}
		params.add(paramName + "=" + value);
		StringBuilder uri = new StringBuilder();
		uri.append(redirect ? "redirect:" : "forward:");
		if (!noSearch.matches("^[a-zA-Z]+\\:(\\/\\/)?.*")) {
			uri.append('/');
		}
		uri.append(noSearch).append('?');
		uri.append(StringUtils.collectionToDelimitedString(params, "&"));
		return uri.toString();
	}
}
