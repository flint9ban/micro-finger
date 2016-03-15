package com.ttsales.microf.love.weixin.web.support;

import org.springframework.core.Ordered;

public interface WXCallbackHandler extends Ordered {

	@Override
	default public int getOrder() {
		return 0;
	}

	/**
	 * 判断是否要处理该回调
	 * 
	 * @param context
	 * @return
	 */
	default public boolean accept(WXCallbackContext context) {
		return true;
	}

	/**
	 * 处理微信回调
	 * 
	 * @param context
	 */
	public void handle(WXCallbackContext context);
}
