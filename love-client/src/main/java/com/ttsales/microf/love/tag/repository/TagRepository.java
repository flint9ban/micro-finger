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

    @Query(value="select t.* from dat_tag t,dat_tag_container_ref r where t.id=r.tag_id and r.container_id=?1 and t.name like ?2",nativeQuery = true)
    List<Tag> findByContainerIdAndName(Long containerId,String name);


    @Query(value="select t.* from dat_tag t,dat_tag_container_ref r where t.id=r.tag_id and r.container_id=?1",nativeQuery = true)
    List<Tag> findByContainerId(Long containerId);

    List<Tag> findByNameContaining(String name);

    List<Tag> findTop5ByNameContainingOrderByName(String name);

    @Query(value="select t.* from dat_tag t,dat_fans_tag r where t.id=r.tag_id and r.fans_id=?1",nativeQuery = true)
    List<Tag> findByFansId(Long fansId);

    @Query(value="select t.* from dat_tag t,org_region r where t.name=r.name and r.parent_region_code =?1",nativeQuery = true)
    List<Tag> findCityTags(String parentRegionCode);
}
