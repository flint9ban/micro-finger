package com.ttsales.microf.love.fans.service;


import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.domain.FansInfoTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class FansServiceImpl implements FansService{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void createFansTag(String openId, List<Long> tagIds) {
        FansInfo fansInfo = getFansInfoByOpenId(openId);
        if (fansInfo != null) {
            tagIds.forEach(tagId->addFansInfoTag(tagId,fansInfo.getId()));
        }
    }

    private void addFansInfoTag(Long tagId,Long fansId){
        FansInfoTag fansInfoTag = new FansInfoTag();
        fansInfoTag.setFansId(fansId);
        fansInfoTag.setTagId(tagId);
        restTemplate.postForObject("http://love-service/fansInfoTags",fansInfoTag,FansInfoTag.class);
    }


    @Override
    public List<String> getOpenIdsByTags(List<Long> tags){
        return tags.stream().map(this::getFansIdByTag)
                .flatMap(fansIds->fansIds.stream())
                .collect(Collectors.groupingBy(FansInfoTag::getFansId,Collectors.summingInt(p->1)))
                .entrySet().stream().filter(entry->entry.getValue().equals(tags.size()))
                .map(entry->entry.getKey())
                .map(this::getFansInfoByFansId)
                .map(FansInfo::getOpenId)
                .collect(Collectors.toList());
    }


    private FansInfo getFansInfoByFansId(Long fansId){
        return restTemplate.getForObject("http://love-service/fansInfos/"+fansId,FansInfo.class);
    }

    private List<FansInfoTag> getFansIdByTag(Long tagId){
        return restTemplate.exchange("http://love-service/fansInfoTags/search/find-tagId?tagId="+tagId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Resources<FansInfoTag>>(){})
                .getBody()
                .getContent()
                .stream()
                .collect(Collectors.toList());
    }

    private FansInfo getFansInfoByOpenId(String openId){
        return restTemplate.getForObject("http://love-service/fansInfos/search/find-openId?openId="+openId,FansInfo.class);
    }
}
