package com.ttsales.microf.love.fans.service;

import com.ttsales.microf.love.common.service.OrgService;
import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.domain.FansInfoTag;
import com.ttsales.microf.love.fans.repository.FansRepository;
import com.ttsales.microf.love.fans.repository.FansTagRepository;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class FansServiceImpl implements FansService {

    @Autowired
    private RestTemplate restTemplate;
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
            return null;
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

    public List<FansInfo> queryFans(FansInfo fansInfo,String[] tagIds){
        if (StringUtils.isEmpty(fansInfo.getName()) &&
                StringUtils.isEmpty(fansInfo.getOrgType()) &&
                StringUtils.isEmpty(fansInfo.getOrgBrand()) &&
                StringUtils.isEmpty(fansInfo.getOrgPosition()) &&
                StringUtils.isEmpty(fansInfo.getOrgProvince()) &&
                StringUtils.isEmpty(fansInfo.getOrgCity()) &&
                StringUtils.isEmpty(fansInfo.getOrgStore()) &&
                tagIds == null) {
            return fansRepository.findAll();
        }
        return fansRepository.findAll();
//        if(tagIds!=null){
//
//        }
//        if(fansInfo!=null){
//            return fansRepository.findAll();
//        }


        //TODO 模糊查询
    //return null;
    }

    public List<Tag> findbyContainerId(Long containerId){
        return tagRepository.findByContainerId(containerId);
    }

    public List<Tag> findCityTags(String parentRegionCode){
        return tagRepository.findCityTags(parentRegionCode);
    }

    public void initTestData(){
        fansTagRepository.deleteAll();
        tagContainerRepository.deleteAll();;
        tagRepository.deleteAll();;
        fansRepository.deleteAll();
        containerRepository.deleteAll();;

        FansInfo currFansInfo=new FansInfo();
        currFansInfo.setOpenId("123");
        currFansInfo.setName("张三");
        currFansInfo.setMobile("18357194946");
        currFansInfo.setOrgType("4S店");
        currFansInfo.setOrgBrand("东风日产");
        currFansInfo.setOrgArea("浙江，杭州");
        currFansInfo.setOrgProvince("1001");
        currFansInfo.setOrgCity("100121");
        currFansInfo.setOrgPosition("店长");
        currFansInfo.setOrgStore("杭州奥迪4S店");
        currFansInfo=fansRepository.save(currFansInfo);

        Tag tag =new  Tag();
        tag.setName("市场分析");
        tag=tagRepository.save(tag);
        Tag tag2 =new  Tag();
        tag2.setName("公众号影响力");
        tag2= tagRepository.save(tag2);
        Tag tag3=new  Tag();
        tag3.setName("东风日产");
        tag3= tagRepository.save(tag3);
        Tag tag4=new  Tag();
        tag4.setName("广汽三菱");
        tag4= tagRepository.save(tag4);
        Tag tag5=new  Tag();
        tag5.setName("杭州市");
        tag5= tagRepository.save(tag5);
        Tag tag6=new  Tag();
        tag6.setName("合肥市");
        tag6= tagRepository.save(tag6);


        Container   container=new Container();
        container.setName("数据");
        container= containerRepository.save(container);
        Container   container2=new Container();
        container2.setName("品牌");
        container2=  containerRepository.save(container2);
        Container   container3=new Container();
        container3.setName("地区");
        container3= containerRepository.save(container3);

        TagContainer tagContainer=new TagContainer();
        tagContainer.setContainerId(container.getId());
        tagContainer.setTagId(tag.getId());
        tagContainerRepository.save(tagContainer);
        TagContainer tagContainer2=new TagContainer();
        tagContainer2.setContainerId(container.getId());
        tagContainer2.setTagId(tag2.getId());
        tagContainerRepository.save(tagContainer2);
        TagContainer tagContainer3=new TagContainer();
        tagContainer3.setContainerId(container2.getId());
        tagContainer3.setTagId(tag3.getId());
        tagContainerRepository.save(tagContainer3);
        TagContainer tagContainer4=new TagContainer();
        tagContainer4.setContainerId(container2.getId());
        tagContainer4.setTagId(tag4.getId());
        tagContainerRepository.save(tagContainer4);
        TagContainer tagContainer5=new TagContainer();
        tagContainer5.setContainerId(container3.getId());
        tagContainer5.setTagId(tag5.getId());
        tagContainerRepository.save(tagContainer5);
        TagContainer tagContainer6=new TagContainer();
        tagContainer6.setContainerId(container3.getId());
        tagContainer6.setTagId(tag6.getId());
        tagContainerRepository.save(tagContainer6);


        FansInfoTag fansInfoTag=new FansInfoTag();
        fansInfoTag.setTagId(tag.getId());
        fansInfoTag.setFansId(currFansInfo.getId());
        fansTagRepository.save(fansInfoTag);
        FansInfoTag fansInfoTag2=new FansInfoTag();
        fansInfoTag2.setTagId(tag2.getId());
        fansInfoTag2.setFansId(currFansInfo.getId());
        fansTagRepository.save(fansInfoTag2);
        FansInfoTag fansInfoTag3=new FansInfoTag();
        fansInfoTag3.setTagId(tag3.getId());
        fansInfoTag3.setFansId(currFansInfo.getId());
        fansTagRepository.save(fansInfoTag3);
    }
}
