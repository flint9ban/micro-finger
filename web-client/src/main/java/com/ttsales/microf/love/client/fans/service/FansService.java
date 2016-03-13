package com.ttsales.microf.love.client.fans.service;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
public interface FansService {

    void createFansTag(String openId,List<Long> tagIds);

    List<String> getOpenIdsByTags(List<Long> tagIds);
}
