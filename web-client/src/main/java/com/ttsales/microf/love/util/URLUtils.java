package com.ttsales.microf.love.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * URL帮助�?
 * 
 * @author JinYiyuan
 * 
 */
public class URLUtils {
	/**
	 * URLEncode
	 * 
	 * @param str
	 *            待编码字符串
	 * @return
	 * @author JinYiyuan
	 * @date 2014�?9�?23�?
	 * @see
	 */
	public static String encode(String str) {
		if (str == null) {
			return null;
		}
		try {
			return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * URLDecode
	 * 
	 * @param str
	 *            待解码字符串
	 * @return
	 * @author JinYiyuan
	 * @date 2014�?9�?23�?
	 * @see
	 */
	public static String decode(String str) {
		if (str == null) {
			return null;
		}
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


	


	public static String getParameter(String uri, String name) {
		Map<String, List<String>> map = getParameterMap(uri);
		List<String> values = map.get(name);
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		} else {
			return null;
		}
	}

	public static List<String> getParameterValues(String uri, String name) {
		Map<String, List<String>> map = getParameterMap(uri);
		return map.get(name);
	}

	public static Map<String, List<String>> getParameterMap(String uri) {
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		if (uri == null || uri.isEmpty()) {
			return map;
		}
		String search = sliceSearchHash(uri)[1];
		if (search == null || search.isEmpty()) {
			return map;
		}
		for (String searchSection : search.split("&")) {
			String[] sectionParts = searchSection.split("=");
			String name = decode(sectionParts[0]);
			String value = sectionParts.length > 1 ? decode(sectionParts[1])
					: "";
			List<String> values = map.get(name);
			if (values == null) {
				values = new ArrayList<String>();
				map.put(name, values);
			}
			values.add(value);
		}
		return map;
	}

	public static String addParameter(String uri, String name, String value) {
		if (uri == null || name == null) {
			return uri;
		}
		String[] parts = sliceSearchHash(uri);
		String uriNoSearch = parts[0];
		String search = parts[1];
		String hash = parts[2];
		StringBuilder newUri = new StringBuilder(uriNoSearch);
		newUri.append('?');
		if (search != null && !search.isEmpty()) {
			newUri.append(search).append('&');
		}
		newUri.append(encode(name));
		if (value != null) {
			newUri.append('=').append(encode(value));
		}
		if (hash != null) {
			newUri.append('#').append(hash);
		}
		return newUri.toString();
	}

	public static String addParameters(String uri,
			Map<String, List<String>> params) {
		if (uri == null || params == null || params.isEmpty()) {
			return uri;
		}
		String[] parts = sliceSearchHash(uri);
		String uriNoSearch = parts[0];
		String search = parts[1];
		String hash = parts[2];
		StringBuilder newUri = new StringBuilder(uriNoSearch);
		newUri.append('?');
		if (search != null) {
			newUri.append(search).append('&');
		}
		params.forEach((String name, List<String> values) -> {
			if (name != null) {
				name = encode(name);
				if (values == null || values.isEmpty()) {
					newUri.append(name).append('&');
				} else {
					for (String value : values) {
						newUri.append(name);
						if (value != null) {
							newUri.append('=').append(encode(value));
						}
						newUri.append('&');
					}
				}
			}
		});
		newUri.deleteCharAt(newUri.length() - 1);
		if (hash != null) {
			newUri.append('#').append(hash);
		}
		return newUri.toString();
	}

	private static String[] sliceSearchHash(String uri) {
		String[] parts = uri.split("#", 2);
		String uriNoHash = parts[0];
		String hash = parts.length > 1 ? parts[1] : null;
		parts = uriNoHash.split("\\?", 2);
		return new String[] { parts[0], parts.length > 1 ? parts[1] : null,
				hash };
	}
}
