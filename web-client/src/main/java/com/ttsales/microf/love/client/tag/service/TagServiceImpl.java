package com.ttsales.microf.love.client.tag.service;

import com.ttsales.microf.love.client.fans.domain.FansInfo;
import com.ttsales.microf.love.client.fans.domain.FansInfoTag;
import com.ttsales.microf.love.client.qrcode.Qrcode;
import com.ttsales.microf.love.client.qrcode.service.QrcodeService;
import com.ttsales.microf.love.client.tag.domain.Container;
import com.ttsales.microf.love.client.tag.domain.Tag;
import com.ttsales.microf.love.client.tag.domain.TagContainer;
import com.ttsales.microf.love.client.weixin.QrCodeActionType;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QrcodeService qrcodeService;

    private String loveServiceUrl = "http://love-service";

    private String tagUrl = loveServiceUrl+"/tags";

    private String containerUrl=loveServiceUrl+"/containers";

    private String tagContainerUrl = loveServiceUrl + "tagContainers";

    @Override
    public String createQrcodeTicket(Long containerId) throws WXApiException, HttpException {
        Qrcode qrcode = qrcodeService.createQrCode(QrCodeActionType.QR_LIMIT_STR_SCENE, Qrcode.REF_TYPE_TAG_CONTAINER);
        String ticket = qrcode.getTicket();
        Container container = getContainer(containerId);
        container.setQrcodeTicket(ticket);
        putContainer(container);
        return ticket;
    }

    @Override
    public List<TagContainer> getTagContainer(String qrcodeTicket) {
        Container container = restTemplate.getForObject(containerUrl+"/search/find-qrcodeTicket?qrcodeTicket="+qrcodeTicket,Container.class);
        if (container != null) {
            return restTemplate.exchange(tagContainerUrl+"/search/find-containerId?containerId="+container.getId(),
                    HttpMethod.GET,null, new ParameterizedTypeReference< Resources<TagContainer>>(){})
                    .getBody().getContent().stream().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void createContainer(Container container) {
        postContainer(container);
    }

    @Override
    public boolean isContainerExist(Container container) {
        return getContainerByName(container.getName())!=null;
    }

    @Override
    public void createTag(Tag tag, List<Container> containers) {
        Tag tag1 =getTagByName(tag.getName());
        if (tag1 != null) {
            // TODO: 2016/3/14
        }
        tag = postTag(tag);
        Long tagId=tag.getId();
        containers.forEach(container->postTagContainer(tagId,container.getId()));
    }

    @Override
    public void updateTag(Tag tag, List<Container> containers) {

    }

    @Override
    public void removeTag(Tag tag) {
        deleteTag(tag.getId());
    }

    @Override
    public List<Tag> queryTags(String tagName,List<Long> containerIds) {
        if (containerIds != null&&containerIds.size()>0) {
            return containerIds.stream().map(containerId->queryTags(tagName,containerId))
                    .flatMap(ids->ids.stream())
                    .collect(Collectors.groupingBy(Tag::getId))
                    .entrySet().stream().filter(entry->entry.getValue().size()==containerIds.size())
                    .map(entry->entry.getValue().get(0))
                    .collect(Collectors.toList());
        }else{
            return queryTagByNameLike(tagName);
        }
    }

    private List<Tag> queryTagByNameLike(String tagName){
        return restTemplate.exchange(tagUrl+"/search/find-name-like?name=%"+tagName+"%",HttpMethod.GET,null,new ParameterizedTypeReference<Resources<Tag>>(){})
                .getBody().getContent().stream().collect(Collectors.toList());

    }

    private List<Tag> queryTags(String tagName,Long containerId){
        return restTemplate.exchange(tagUrl + "/search/find-container-name?containerId=" + containerId + "&tagName=%" + tagName + "%",
                HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Tag>>() {
                }).getBody().getContent().stream().collect(Collectors.toList());
    }


    private void putContainer(Container container){
        restTemplate.put(containerUrl+"/"+container.getId(),container);
    }

    private void postContainer(Container container){
        restTemplate.postForObject(containerUrl,container,Container.class);
    }

    private Container getContainerByName(String name){
        return  restTemplate.getForObject(containerUrl+"/search/find-name?name="+name,Container.class);
    }

    private Container getContainer(Long containerId){
        return restTemplate.getForObject(containerUrl+"/"+containerId,Container.class);
    }

    private Tag getTagByName(String name){
        return restTemplate.getForObject(tagUrl+"/search/find-name?name="+name,Tag.class);
    }

    private Tag postTag(Tag tag){
       return restTemplate.postForObject(tagUrl,tag,Tag.class);
    }

    private void deleteTag(Long tagId){
        restTemplate.delete(tagUrl+"/"+tagId);
    }

    private TagContainer postTagContainer(Long tagId,Long containerId){
        TagContainer tagContainer = new TagContainer();
        tagContainer.setTagId(tagId);
        tagContainer.setContainerId(containerId);
        return restTemplate.postForObject(tagContainerUrl,tagContainer,TagContainer.class);
    }

    private void deleteTagContainer(Long tagId){

    }
}
