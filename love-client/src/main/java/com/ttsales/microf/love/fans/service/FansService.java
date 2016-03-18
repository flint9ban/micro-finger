package com.ttsales.microf.love.fans.service;

import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.tag.domain.Tag;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
public interface FansService {

    void createFansTag(String openId,List<Long> tagIds);

    List<String> getOpenIdsByTags(List<Long> tagIds);


    FansInfo getFansInfoByOpenId(String openId);

    void editFansInfo(FansInfo fansInfo);

    List<Tag> getTagsByOpenId(String openId);

    void  editFansTags(String openId,List<Long> tagIds);

    List<FansInfo> queryFans(FansInfo fansInfo,String[] tagIds);

    List<Tag> findbyContainerId(Long containerId);

    void initTestData();

    List<Tag> findCityTags(String parentRegionCode);
}
