package com.ttsales.microf.love.common.repository;

import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.fans.domain.FansInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
//
@RepositoryRestResource
public interface RegionRepository extends JpaRepository<OrgRegion,String> {

    List<OrgRegion> findByParentRegionCode(@Param("parentRegionCode") String parentRegionCode);

}
