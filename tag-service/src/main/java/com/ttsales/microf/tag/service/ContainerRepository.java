package com.ttsales.microf.tag.service;

import com.ttsales.microf.tag.domain.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by liyi on 2016/3/4.
 */

@RepositoryRestResource
public interface ContainerRepository extends JpaRepository<Container,Long>{
}
