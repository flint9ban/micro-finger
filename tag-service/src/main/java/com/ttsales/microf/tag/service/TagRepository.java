package com.ttsales.microf.tag.service;

import com.ttsales.microf.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

/**
 * Created by liyi on 2016/3/4.
 */

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag,Long>{
    @RestResource(path = "by-name")
    Collection<Tag> findByName(@Param("rn") String rn);
}
