package com.ttsales.microf.love.weixin.web;


import com.ttsales.microf.love.util.SHA1Encoder;
import com.ttsales.microf.love.weixin.MPApiConfig;
import com.ttsales.microf.love.weixin.web.support.WXCallbackContext;
import com.ttsales.microf.love.weixin.web.support.WXCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class ReceiveWXCallbackController {

	@Autowired(required = false)
	private List<WXCallbackHandler> handlers = new ArrayList<WXCallbackHandler>();

	@Autowired
	private MPApiConfig mpApiConfig;

	@RequestMapping(value = "/weixin/receive", params = { "signature",
			"timestamp", "nonce", "echostr" }, method = RequestMethod.GET)
	@ResponseBody
	public String access(String signature, String timestamp, String nonce,
			String echostr) {
		if (validateSignature(signature, timestamp, nonce)) {
			return echostr;
		} else {
			return "invalid signature";
		}
	}

	@RequestMapping(value = "/weixin/receive", params = { "signature",
			"timestamp", "nonce" }, method = RequestMethod.POST)
	public void receive(HttpServletRequest request,
			HttpServletResponse response, String signature, String timestamp,
			String nonce) throws IOException {
		PrintWriter writer = response.getWriter();
		if (!validateSignature(signature, timestamp, nonce)) {
			writer.write("");
		} else {
			WXCallbackContext context = new WXCallbackContext(request, response);
			handlers.forEach((WXCallbackHandler handler) -> {
				if (handler.accept(context)) {
					handler.handle(context);
				}
			});
			if (!context.isResponded()) {
				writer.write("");
			}
		}
		writer.flush();
		writer.close();
	}

	private boolean validateSignature(String signature, String timestamp,
			String nonce) {
		return signature != null
				&& signature.equals(generateSignature(mpApiConfig.getToken(),
						timestamp, nonce));
	}

	private String generateSignature(String token, String timestamp,
			String nonce) {
		if (StringUtils.hasLength(token) && StringUtils.hasLength(timestamp)
				&& StringUtils.hasLength(nonce)) {
			List<String> params = Arrays.asList(token, timestamp, nonce);
			params.sort((String str1, String str2) -> str1.compareTo(str2));
			return SHA1Encoder.encode(StringUtils.collectionToDelimitedString(
					params, ""));
		}
		return null;
	}
}
