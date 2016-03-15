package com.ttsales.microf.love.tag.service;


import com.ttsales.microf.love.qrcode.Qrcode;
import com.ttsales.microf.love.qrcode.service.QrcodeService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.tag.repository.ContainerRepository;
import com.ttsales.microf.love.tag.repository.TagContainerRepository;
import com.ttsales.microf.love.tag.repository.TagRepository;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.QrCodeActionType;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 16/3/13.
 */
@Service
public class TagServiceImpl implements TagService {


    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagContainerRepository tagContainerRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private QrcodeService qrcodeService;


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
        Container container = containerRepository.findByQrcodeTicket(qrcodeTicket);
        if (container != null) {
            return tagContainerRepository.findAllByContainerId(container.getId()).stream().collect(Collectors.toList());
        }
        return new ArrayList<TagContainer>();
    }

    @Override
    public List<Container> getTagContainerByTagId(Long tagId) {
        return containerRepository.findAllByTagId(tagId);
    }

    @Override
    public List<Container> findContainerByName(String name) {
        return containerRepository.findByNameContaining(name);
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
    public Tag findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Long createTag(Tag tag, List<Long> containers) {
        Tag tag1 =getTagByName(tag.getName());
        if (tag1 != null) {
            // TODO: 2016/3/14
        }
        tag = postTag(tag);
        Long tagId=tag.getId();
        containers.forEach(container->postTagContainer(tagId,container));
        return tagId;
    }

    @Override
    public void updateTag(Tag tag, List<Long> containers) {

    }

    @Override
    public void removeTag(Long tagId) {
        deleteTag(tagId);
    }

    @Override
    public List<Tag> queryTags(String tagName, List<Long> containerIds) {
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
        return tagRepository.findByNameContaining("%"+tagName+"%").stream().collect(Collectors.toList());

    }

    private List<Tag> queryTags(String tagName,Long containerId){
        return tagRepository.findByContainerIdAndName(containerId,"%"+tagName+"%").stream().collect(Collectors.toList());
    }


    private void putContainer(Container container){
       containerRepository.save(container);
    }

    private void postContainer(Container container){
        containerRepository.save(container);
    }

    private Container getContainerByName(String name){
        return  containerRepository.findByName(name);
    }

    private Container getContainer(Long containerId){
        return containerRepository.findOne(containerId);
    }

    private Tag getTagByName(String name){
        return tagRepository.findByName(name);
    }

    private Tag postTag(Tag tag){
       return tagRepository.save(tag);
    }

    private void deleteTag(Long tagId){
        tagRepository.delete(tagId);
    }

    private TagContainer postTagContainer(Long tagId,Long containerId){
        TagContainer tagContainer = new TagContainer();
        tagContainer.setTagId(tagId);
        tagContainer.setContainerId(containerId);
        return tagContainerRepository.save(tagContainer);
    }

    private void deleteTagContainer(Long tagId){

    }
}
