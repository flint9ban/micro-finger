package com.ttsales.microf.love.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResponseUtil {
	private static  String CONTENT_TYPE = "text/html;charset=utf-8";

	public static void toClient(HttpServletResponse response,Object object) throws IOException{
		PrintWriter out = response.getWriter();
		out.print(object);
		out.flush();
		out.close();
	}
	
	public static  void toClient(HttpServletResponse response, String content)
			throws IOException {
		response.setContentType(CONTENT_TYPE);
		response.getWriter().print(content);
		response.getWriter().flush();
		response.getWriter().close();
	}

	public static void toClient(HttpServletResponse response,
			HashMap<String, String> map) throws IOException {
		if (null != map)
			toClient(response, jsonObject(map));
	}
    private static  String jsonObject(HashMap<String, String> map) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.putAll(map);
        return jsonObj.toString();
    }
    
    public static void toClient(HttpServletResponse response,byte[] image) throws IOException{
		BufferedImage imag = null;
		imag = ImageIO.read(new ByteArrayInputStream(image));
		OutputStream out = response.getOutputStream();
		ImageIO.write(imag, "png", out);
    }
    
    @SuppressWarnings("rawtypes")
	public static void toClient(HttpServletResponse response,String code,String info,List<String> primaryKeyNames,List records,JsonConfig jsonConfig) throws IOException{
    	toClient(response,getReturnJSONObject(code,info,primaryKeyNames,records,jsonConfig));
	}
    
    @SuppressWarnings("rawtypes")
	public static void toClient(HttpServletResponse response,List<String> primaryKeyNames,List records,JsonConfig jsonConfig) throws IOException{
    	toClient(response,"1","",primaryKeyNames,records,jsonConfig);
    }
    
    public static void toClient(HttpServletResponse response,String code,String info) throws IOException{
    	toClient(response,code,info,null,null,null);
    }
    
    @SuppressWarnings("rawtypes")
	private static JSONObject getReturnJSONObject(String code,String info,List<String> primaryKeyNames,List records,JsonConfig jsonConfig){
    	JSONObject returnObject = new JSONObject();
    	returnObject.put("code", code);
    	returnObject.put("info", info);
    	if(jsonConfig==null){
    		jsonConfig = new JsonConfig();
    	}
    	if( !ListUtil.isEmpty(records) && !ListUtil.isEmpty(primaryKeyNames)){
    		List<PidDTO> pidDTOs = new ArrayList<PidDTO>();
        	for(Object obj:records){
        		PidDTO pidDTO = new PidDTO();
    			List<String> pidNameList = new ArrayList<String>();
    			List<String> pidValueList = new ArrayList<String>();
        		for(String pKeyName:primaryKeyNames){
        			for (Field field : obj.getClass().getDeclaredFields()) {
        		 		if(field.getName().equals((pKeyName))){
        		 			try {
        		 				field.setAccessible(true);
        		 				pidNameList.add(pKeyName);
        		 				pidValueList.add((String) field.get(obj));
        					} catch (Exception e) {
        						 e.printStackTrace();
        					}
        		 		}
        			}
        		}
        		pidDTO.setField(pidNameList);
        		pidDTO.setValue(pidValueList);
        		pidDTOs.add(pidDTO);
        	}
        	JSONArray pidArray = JsonUtil.fromList(pidDTOs,new JsonConfig());
        	returnObject.put("pids", pidArray);
    	}
    	if(ListUtil.isEmpty(primaryKeyNames)){
    		returnObject.put("pids", "[]");
    	}
    	if(!ListUtil.isEmpty(records)){
    		@SuppressWarnings("unchecked")
    		JSONArray recordArray = JsonUtil.fromList(records, jsonConfig);
        	returnObject.put("data", recordArray);
    	}else{
    		returnObject.put("data", "[]");
    	}
    	return returnObject;
    }

}

class PidDTO{
	List<String> field;
	List<String> value;
	public List<String> getField() {
		return field;
	}
	public void setField(List<String> field) {
		this.field = field;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
}
