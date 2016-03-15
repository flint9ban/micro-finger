package com.ttsales.microf.love.util;

import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie操作工具�?
 * 
 * @author JinYiyuan
 * 
 */
public class CookieUtils {

	/**
	 * 获取Cookie
	 * 
	 * @param request
	 * @param name
	 *            名称
	 * @return �?
	 * @author JinYiyuan
	 * @date 2014�?9�?23�?
	 * @see
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(name, "Cookie Name must not be null");
		Cookie cookies[] = request.getCookies();
		if (cookies == null) {
			return null;
		}
		String cookie = null;
		for (int i = 0; i < cookies.length; i++) {
			if (name.equals(unescape(cookies[i].getName()))) {
				cookie = unescape(cookies[i].getValue());
				break;
			}
		}
		return cookie;
	}

	/**
	 * 设置Cookie
	 * 
	 * @param response
	 * @param name
	 *            名称
	 * @param value
	 *            �?
	 * @author JinYiyuan
	 * @date 2014�?9�?23�?
	 * @see
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value) {
		setCookie(response, name, value, -1, null, null);
	}

	/**
	 * 设置Cookie
	 * 
	 * @param response
	 * @param name
	 *            名称
	 * @param value
	 *            �?
	 * @param expiry
	 *            有效时长（秒为单位）
	 * @author JinYiyuan
	 * @date 2014�?9�?23�?
	 * @see
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value, int expiry) {
		setCookie(response, name, value, expiry, null, null);
	}

	public static void setCookie(HttpServletResponse response, String name,
			String value, int expiry, String domain, String path) {
		Cookie cookie = new Cookie(escape(name), escape(value));
		cookie.setMaxAge(expiry);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		if (path != null) {
			cookie.setPath(path);
		}
		response.addCookie(cookie);
	}

	/**
	 * 删除Cookie
	 * 
	 * @param response
	 * @param name
	 *            名称
	 * @author JinYiyuan
	 * @date 2014�?9�?23�?
	 * @see
	 */
	public static void removeCookie(HttpServletResponse response, String name) {
		setCookie(response, name, null, 0);
	}

	private static String escape(String str) {
		return URLUtils.encode(str);
	}

	private static String unescape(String str) {
		return URLUtils.decode(str);
	}
}
