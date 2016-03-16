package com.ttsales.microf.love.tag.repository;

import com.ttsales.microf.love.tag.domain.TagContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by liyi on 2016/3/4.
 */

@RepositoryRestResource
@Repository
public interface TagContainerRepository extends JpaRepository<TagContainer,Long>{

    @RestResource(path = "find-containerId")
    Collection<TagContainer> findAllByContainerId(@Param("containerId") Long containerId);

    List<TagContainer> findAllByTagId(Long tagId);

    public void removeByTagId(Long tagId);

}

