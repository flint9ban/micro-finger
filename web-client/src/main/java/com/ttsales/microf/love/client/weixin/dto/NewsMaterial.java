package com.ttsales.microf.love.client.weixin.dto;

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
public class NewsMaterial{

    private String mediaId;

    private String firstTitle;

    private String firstContent;

    private String updateTime;

    public static List<NewsMaterial> convert(JSONObject json){
        JSONObject jsonValue = JSONObject.fromObject(json);
        JSONArray items = jsonValue.getJSONArray("item");
        return Stream.of(items.toArray()).map(NewsMaterial::newMaterial).collect(Collectors.toList());
    }

    private static NewsMaterial newMaterial(Object item){
        JSONObject json = JSONObject.fromObject(item);
        String mediaId = json.getString("media_id");
        String updateTime = json.getString("update_time");
        JSONArray newItemes=  json.getJSONArray("news_item");
        JSONObject newItem = newItemes.getJSONObject(0);
        if (newItem != null) {
            String firstTitle = newItem.getString("title");
            String firstContent = newItem.getString("content");
            if(firstContent.length()>20){
                firstContent = firstContent.substring(0,20);
            }
            NewsMaterial newsMaterial = new NewsMaterial(mediaId,firstTitle,firstContent,updateTime);
            return newsMaterial;
        }
        return null;
    }
}
