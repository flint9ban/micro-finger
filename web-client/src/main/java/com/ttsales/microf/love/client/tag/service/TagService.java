package com.ttsales.microf.love.client.tag.service;

import com.ttsales.microf.love.client.tag.domain.Container;
import com.ttsales.microf.love.client.tag.domain.Tag;
import com.ttsales.microf.love.client.tag.domain.TagContainer;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
public interface TagService {

    String createQrcodeTicket(Long containerId) throws WXApiException, HttpException;

    List<TagContainer> getTagContainer(String qrcodeTicket);

    void createContainer(Container container);

    boolean isContainerExist(Container container);

    void createTag(Tag tag, List<Container> containers);

    void updateTag(Tag tag,List<Container> containers);

    void removeTag(Tag tag);

    List<Tag> queryTags(String tagName,List<Long> containerIds);

}
