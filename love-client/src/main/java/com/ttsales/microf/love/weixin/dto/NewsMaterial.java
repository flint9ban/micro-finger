package com.ttsales.microf.love.weixin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
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

    private Long updateTime;

    private List<MaterialItem> items;

    public static List<NewsMaterial> convert(JSONObject json){
        JSONObject jsonValue = JSONObject.fromObject(json);
        JSONArray items = jsonValue.getJSONArray("item");
        return Stream.of(items.toArray()).map(NewsMaterial::newMaterial).collect(Collectors.toList());
    }

    private static NewsMaterial newMaterial(Object item){
        NewsMaterial newsMaterial = null;
        JSONObject json = JSONObject.fromObject(item);
        String mediaId = json.getString("media_id");
        Long updateTime = json.getLong("update_time");
        JSONObject content = json.getJSONObject("content");
        JSONArray newItemes=  content.getJSONArray("news_item");
        List<MaterialItem> materialItems = getItems(newItemes);

        JSONObject newItem = newItemes.getJSONObject(0);
        if (newItem != null) {
            String firstTitle = newItem.getString("title");
            String firstContent = newItem.getString("digest");
            if(firstContent.length()>20){
                firstContent = firstContent.substring(0,20);
            }
            newsMaterial = new NewsMaterial(mediaId,firstTitle,firstContent,updateTime,materialItems);
        }
        return newsMaterial;
    }

    private static List<MaterialItem> getItems(JSONArray items){
        List<MaterialItem> materialItems = new ArrayList<MaterialItem>();
        for(int i=0;i<items.size();i++){
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            String content = item.getString("digest");
            String contentUrl = item.getString("url");
            if(content.length()>20){
                content = content.substring(0,20);
            }
            MaterialItem materialItem= new MaterialItem(title,content,contentUrl,i);
            materialItems.add(materialItem);
        }
        return materialItems;
    }
}
