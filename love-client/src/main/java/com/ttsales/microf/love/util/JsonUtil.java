/**
 * Copyright (c) 2012 SDFSHOP.All rights reserved.
 * @filename JsonUtil.java
 * @package sdf.core.util
 * @author dandyzheng
 * @date 2013-7-13
 */
package com.ttsales.microf.love.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonUtil {
	public static JSONObject fromStr(String json){
		return JSONObject.fromObject(json);
	}
	
	public static<T> JSONObject fromObject(T object,JsonConfig jsonConfig){
		if(jsonConfig == null){
			jsonConfig = new JsonConfig();
		}
		return JSONObject.fromObject(object, jsonConfig);
	}
	
	public static<T> JSONArray fromList(List<T> list,JsonConfig jsonConfig){
		if(jsonConfig == null){
			jsonConfig = new JsonConfig();
		}
		return JSONArray.fromObject(list, jsonConfig);
	}
	
	public static JsonConfig changeDateConfig(String format){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonValueProcessor() {

					@Override
					public Object processObjectValue(String arg0, Object value,
							JsonConfig arg2) {
						if (value instanceof Date) {
							 String str = new SimpleDateFormat(format).format((Date) value); 
							 return str; 
						}
						return value;
					}

					@Override
					public Object processArrayValue(Object value,
							JsonConfig arg1) {
						return null;
					}
				});
		return jsonConfig;
	}

}

