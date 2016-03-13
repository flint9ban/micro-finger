package com.ttsales.microf.love.fans.service;

import com.ttsales.microf.love.fans.domain.FansInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by liyi on 16/3/6.
 */
//
@RepositoryRestResource
public interface FansRepository extends JpaRepository<FansInfo,Long> {

    @RestResource(path = "find-openId")
    FansInfo findByOpenId(@Param("openId") String openId);

}
