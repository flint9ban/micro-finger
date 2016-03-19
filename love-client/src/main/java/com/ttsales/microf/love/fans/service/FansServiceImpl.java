package com.ttsales.microf.love.fans.service;

import com.ttsales.microf.love.common.repository.SpecificationBuilder;
import com.ttsales.microf.love.common.service.OrgService;
import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.domain.FansInfoTag;
import com.ttsales.microf.love.fans.domain.FansTagView;
import com.ttsales.microf.love.fans.repository.FansRepository;
import com.ttsales.microf.love.fans.repository.FansTagRepository;
import com.ttsales.microf.love.fans.repository.FansTagViewRepository;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.tag.repository.ContainerRepository;
import com.ttsales.microf.love.tag.repository.TagContainerRepository;
import com.ttsales.microf.love.tag.repository.TagRepository;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class FansServiceImpl implements FansService {


    @Autowired
    private FansRepository fansRepository;
    @Autowired
    private FansTagRepository  fansTagRepository;
    @Autowired
    private TagRepository   tagRepository;
    @Autowired
    private ContainerRepository containerRepository;
    @Autowired
    private TagContainerRepository tagContainerRepository;
    @Autowired
    private FansTagViewRepository   fansTagViewRepository;

    @Override
    public void createFansTag(String openId, List<Long> tagIds) {
        FansInfo fansInfo = getFansInfoByOpenId(openId);
        if (fansInfo == null) {
            fansInfo = new FansInfo();
            fansInfo.setOpenId(openId);
            fansInfo = fansRepository.save(fansInfo);
        }
        if (fansInfo != null) {
            Long fansId = fansInfo.getId();
            tagIds.forEach(tagId->addFansInfoTag(tagId,fansId));
        }
    }

    private void addFansInfoTag(Long tagId,Long fansId){
        FansInfoTag fansInfoTag = new FansInfoTag();
        fansInfoTag.setFansId(fansId);
        fansInfoTag.setTagId(tagId);
        fansTagRepository.save(fansInfoTag);
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
        return fansRepository.findOne(fansId);
    }

    private List<FansInfoTag> getFansIdByTag(Long tagId){
        return  fansTagRepository.findAllByTagId(tagId);
    }

    public FansInfo getFansInfoByOpenId(String openId){
        return fansRepository.findByOpenId(openId);
    }


    public void editFansInfo(FansInfo fansInfo){
        FansInfo currFansInfo=   getFansInfoByOpenId(fansInfo.getOpenId());
        if(currFansInfo==null){
            createFansInfo(fansInfo);
        }else{
            currFansInfo.setMobile(fansInfo.getMobile());
            currFansInfo.setName(fansInfo.getName());
            currFansInfo.setOpenId(fansInfo.getOpenId());
            currFansInfo.setOrgPosition(fansInfo.getOrgPosition());
            currFansInfo.setOrgBrand(fansInfo.getOrgBrand());
            currFansInfo.setOrgType(fansInfo.getOrgType());
            if(!fansInfo.getOrgType().equals("4S店")){
                currFansInfo.setOrgArea("");
                currFansInfo.setOrgCity("");
                currFansInfo.setOrgProvince("");
                currFansInfo.setOrgStore("");
                fansRepository.save(currFansInfo);
                return ;
            }
            currFansInfo.setOrgArea(fansInfo.getOrgArea());
            currFansInfo.setOrgCity(fansInfo.getOrgCity());
            currFansInfo.setOrgProvince(fansInfo.getOrgProvince());
            currFansInfo.setOrgStore(fansInfo.getOrgStore());
            fansRepository.save(currFansInfo);
        }

    }

    private void createFansInfo(FansInfo fansInfo){
        FansInfo newFansInfo=new FansInfo();
        fansInfo.setMobile(fansInfo.getMobile());
        fansInfo.setName(fansInfo.getName());
        fansInfo.setOpenId(fansInfo.getOpenId());
        fansInfo.setOrgPosition(fansInfo.getOrgPosition());
        fansInfo.setOrgBrand(fansInfo.getOrgBrand());
        fansInfo.setOrgType(fansInfo.getOrgType());
        if(fansInfo.getOrgType().equals("4S店")){
            fansInfo.setOrgCity(fansInfo.getOrgCity());
            fansInfo.setOrgProvince(fansInfo.getOrgProvince());
            fansInfo.setOrgArea(fansInfo.getOrgArea());
            fansInfo.setOrgStore(fansInfo.getOrgStore());
        }
        fansRepository.save(fansInfo);
    }

    public List<Tag> getTagsByOpenId(String openId){
        FansInfo fansInfo = getFansInfoByOpenId(openId);
        if(fansInfo==null){
            fansInfo=new FansInfo();
            fansInfo.setOpenId(openId);
            fansInfo=fansRepository.save(fansInfo);
        }
        return tagRepository.findByFansId(fansInfo.getId());
    }

    public void  editFansTags(String openId,List<Long> tagIds){
        FansInfo fansInfo = getFansInfoByOpenId(openId);
        if(fansInfo==null){
            return ;
        }
        List<FansInfoTag> fansInfoTag=fansTagRepository.findAllByFansId(fansInfo.getId());
        fansTagRepository.delete(fansInfoTag);
        tagIds.forEach(tagId->addFansInfoTag(tagId,fansInfo.getId()));
    }

    public List<FansInfo> queryFans(FansInfo fansInfo, List<Long> tagIdList){
        if (tagIdList.size()==0) {
            return fansRepository.findAll(SpecificationBuilder.build(fansInfo));
        }
        List<FansTagView> fansTagViews=createParamViews(fansInfo ,tagIdList);
        return fansTagViews.stream().map(this::queryFansByView)
                .flatMap(fansIds->fansIds.stream())
                .collect(Collectors.groupingBy(FansTagView::getFansId,Collectors.summingInt(p->1)))
                .entrySet().stream().filter(entry->entry.getValue().equals(tagIdList.size()))
                .map(entry->entry.getKey())
                .map(this::getFansInfoByFansId)
                .collect(Collectors.toList());
    }


    private List<FansTagView> createParamViews(FansInfo fansInfo ,List<Long> tagIds){
        List<FansTagView> fansTagViews=new ArrayList<FansTagView>();
        if(tagIds.size()==0){
            FansTagView fansTagView=new FansTagView();
            fansTagView.setName(fansInfo.getName());
            fansTagView.setOrgType(fansInfo.getOrgType());
            fansTagView.setOrgBrand(fansInfo.getOrgBrand());
            fansTagView.setMobile(fansInfo.getMobile());
            fansTagView.setOrgPosition(fansInfo.getOrgPosition());
            fansTagView.setOrgProvince(fansInfo.getOrgProvince());
            fansTagView.setOrgCity(fansInfo.getOrgCity());
            fansTagView.setOrgStore(fansInfo.getOrgStore());
            fansTagViews.add(fansTagView);
            return fansTagViews;
        }
        for (Long tagId:tagIds){
            FansTagView fansTagView=new FansTagView();
            fansTagView.setName(fansInfo.getName());
            fansTagView.setOrgType(fansInfo.getOrgType());
            fansTagView.setOrgBrand(fansInfo.getOrgBrand());
            fansTagView.setMobile(fansInfo.getMobile());
            fansTagView.setOrgPosition(fansInfo.getOrgPosition());
            fansTagView.setOrgProvince(fansInfo.getOrgProvince());
            fansTagView.setOrgCity(fansInfo.getOrgCity());
            fansTagView.setOrgStore(fansInfo.getOrgStore());
            fansTagView.setTagId(tagId);
            fansTagViews.add(fansTagView);
        }
        return fansTagViews;
    }
    private List<FansTagView> queryFansByView(FansTagView fansTagView){
        List<FansTagView>  fansTagViews=fansTagViewRepository.findAll(SpecificationBuilder.build(fansTagView));
        return fansTagViews;
    }

    public List<Tag> findbyContainerId(Long containerId){
        return tagRepository.findByContainerId(containerId);
    }

    public List<Tag> findCityTags(String parentRegionCode){
        return tagRepository.findCityTags(parentRegionCode);
    }
}
