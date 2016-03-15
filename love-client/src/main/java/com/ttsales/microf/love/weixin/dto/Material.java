package com.ttsales.microf.love.weixin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by liyi on 2016/3/10.
 */
@Data
@AllArgsConstructor
public class Material {

    private String mediaId;

    private String name;

    private String updateTime;

    private String url;

    public static List<Material> convert(JSONObject json){
        JSONObject jsonValue = JSONObject.fromObject(json);
        JSONArray items = jsonValue.getJSONArray("item");
        return Stream.of(items.toArray()).map(Material::newMaterial).collect(Collectors.toList());
    }

    private static Material newMaterial(Object item){
        JSONObject json = JSONObject.fromObject(item);
        String mediaId = json.getString("media_id");
        String name = json.optString("name",null);
        String updateTime = json.getString("update_time");
        String url = json.optString("url",null);
        Material material = new Material(mediaId,name,updateTime,url);
        return material;
    }


}
