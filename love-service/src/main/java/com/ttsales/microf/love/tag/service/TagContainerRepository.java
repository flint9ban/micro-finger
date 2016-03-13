package com.ttsales.microf.love.tag.service;

import com.ttsales.microf.love.tag.domain.TagContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

/**
 * Created by liyi on 2016/3/4.
 */

@RepositoryRestResource
public interface TagContainerRepository extends JpaRepository<TagContainer,Long>{

    @RestResource(path = "find-containerId")
    Collection<TagContainer> findAllByContainerId(@Param("containerId") String containerId);
}
