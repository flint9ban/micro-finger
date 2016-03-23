package com.ttsales.microf.love.tag.service;


import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
public interface TagService {

    String createQrcodeTicket(Long containerId) throws WXApiException, HttpException;

    List<TagContainer> getTagContainer(String qrcodeTicket);

    List<Container> getTagContainerByTagId(Long tagId);

    List<Container> getTagContainerByTagId(Long tagId,ContainerType containerType);

    List<Container> findContainerByName(String name, ContainerType containerType);

    List<Container> findContainerLimit5ByName(String name, ContainerType containerType);

    List<Container> findCommonContainer();

    List<Tag> findTagByContainerId(Container container);

    void createContainer(Container container);

    boolean isContainerExist(Container container);

    Tag findTagByName(String name);

    Long createTag(Tag tag, List<Long> containers);

    void updateTag(Tag tag, List<Long> containers);

    void removeTag(Long tagId);

    List<Tag> queryTags(String tagName, List<Long> containerIds);

    Long createCotnainerWithTags(Container container,List<Long> tagIds);

    void removeContainer(Long containerId);

    Tag getTag(Long tagId);

    List<Tag> queryTagLimit5ByNameLike(String name);

}
