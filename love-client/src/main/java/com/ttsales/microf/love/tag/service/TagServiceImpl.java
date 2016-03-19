package com.ttsales.microf.love.tag.service;


import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.domain.QrCodeType;
import com.ttsales.microf.love.qrcode.service.QrcodeService;
import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.tag.repository.ContainerRepository;
import com.ttsales.microf.love.tag.repository.TagContainerRepository;
import com.ttsales.microf.love.tag.repository.TagRepository;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public String createQrcodeTicket(Long containerId) throws WXApiException, HttpException {
        QrCode qrcode = qrcodeService.createQrCode(QrCodeType.QR_LIMIT_STR_SCENE, QrCode.REF_TYPE_TAG_CONTAINER);
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
    public List<Container> getTagContainerByTagId(Long tagId,ContainerType containerType) {
        return containerRepository.findAllByTagIdAndContainerType(tagId,containerType!=null?containerType.ordinal():null);
    }

    @Override
    public List<Container> findContainerByName(String name,ContainerType containerType) {
        return containerRepository.findByNameContainingAndContainerType(name,containerType);
    }

    @Override
    public List<Container> findContainerLimit5ByName(String name,ContainerType containerType) {
        return containerRepository.findTop5ByNameContaining("%"+name+"%");
    }

    @Override
    public List<Container> findCommonContainer() {
        return containerRepository.findAllByContainerType(ContainerType.COMMON);
    }

    @Override
    public List<Tag> findTagByContainerId(Container container) {
        return tagRepository.findByContainerId(container.getId());
    }

    @Override
    @Transactional
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
    @Transactional
    public Long createTag(Tag tag, List<Long> containers) {
        tag = postTag(tag);
        Long tagId=tag.getId();
        containers.forEach(container->postTagContainer(tagId,container));
        return tagId;
    }

    @Override
    @Transactional
    public void updateTag(Tag tag, List<Long> containers) {
        Tag updateTag = tagRepository.findOne(tag.getId());
        updateTag.setName(tag.getName());
        tagRepository.save(updateTag);
        tagContainerRepository.deleteByTagId(tag.getId());
        containers.stream().forEach(containerId->postTagContainer(tag.getId(),containerId));
    }

    @Override
    @Transactional
    public void removeTag(Long tagId) {
        deleteTag(tagId);
        tagContainerRepository.deleteByTagId(tagId);
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

    @Override
    public Long createCotnainerWithTags(Container container, List<Long> tagIds) {
        Container newContainer = postContainer(container);
        if (tagIds != null) {
            tagIds.forEach(tagId->{postTagContainer(tagId,newContainer.getId());});
        }
        return newContainer.getId();
    }

    @Override
    @Transactional
    public void removeContainer(Long containerId) {
        containerRepository.delete(containerId);
        tagContainerRepository.deleteByContainerId(containerId);
    }

    @Override
    public Tag getTag(Long tagId) {
        return tagRepository.findOne(tagId);
    }


    private List<Tag> queryTagByNameLike(String tagName){
        return tagRepository.findByNameContaining("%"+tagName+"%");
    }

    @Override
    public List<Tag> queryTagLimit5ByNameLike(String tagName){
        return tagRepository.findTop5ByNameContainingOrderByName("%"+tagName+"%");
    }

    private List<Tag> queryTags(String tagName,Long containerId){
        return tagRepository.findByContainerIdAndName(containerId,"%"+tagName+"%").stream().collect(Collectors.toList());
    }


    private void putContainer(Container container){
       containerRepository.save(container);
    }

    private Container postContainer(Container container){
        return containerRepository.save(container);
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
