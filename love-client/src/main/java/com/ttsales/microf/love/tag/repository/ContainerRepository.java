package com.ttsales.microf.love.tag.repository;

import com.ttsales.microf.love.tag.domain.Container;
import com.ttsales.microf.love.tag.domain.ContainerType;
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
public interface ContainerRepository extends JpaRepository<Container,Long>{

    Container findByQrcodeTicket(String qrcodeTicket);

    @RestResource(path = "find-name")
    Container findByName(String name);

    @RestResource(path = "find-name-like")
    List<Container> findByNameContainingAndContainerType(String name,ContainerType containerType);

    @Query(value = "select c.* from dat_tag_container c join  dat_tag_container_ref t on c.id=t.container_id where t.tag_id=?1",nativeQuery = true)
    List<Container> findAllByTagId(Long tagId);

    @Query(value = "select c.* from dat_tag_container c join  dat_tag_container_ref t on c.id=t.container_id where t.tag_id=?1 and c.container_type=?2",nativeQuery = true)
    List<Container> findAllByTagIdAndContainerType(Long tagId,Integer containerType);

    List<Container> findAllByContainerType(ContainerType containerType);

    List<Container> findTop5ByNameContaining(String name);
}
