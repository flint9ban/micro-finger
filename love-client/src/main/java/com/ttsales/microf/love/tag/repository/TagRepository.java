package com.ttsales.microf.love.tag.repository;

import com.ttsales.microf.love.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 2016/3/4.
 */

@RepositoryRestResource
@Repository
public interface TagRepository extends JpaRepository<Tag,Long>{
    @RestResource(path = "find-name")
    Tag findByName(@Param("name") String name);

    @Query(value="select t from dat_tag t,dat_tag_container_ref r where t.id=r.tag_id and r.container_id=:containerId and t.name like :name",nativeQuery = true)
    @RestResource(path = "find-container-name")
    List<Tag> findByContainerIdAndName(@Param("containerId") Long containerId, @Param("name") String name);

    @RestResource(path = "find-name-like")
    List<Tag> findByNameContaining(@Param("name") String name);

}
