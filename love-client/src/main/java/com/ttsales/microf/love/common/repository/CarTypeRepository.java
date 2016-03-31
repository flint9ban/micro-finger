package com.ttsales.microf.love.common.repository;

import com.ttsales.microf.love.common.domain.OrgBrand;
import com.ttsales.microf.love.common.domain.OrgCarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
//
@RepositoryRestResource
public interface CarTypeRepository extends JpaRepository<OrgCarType,String> {
        List<OrgCarType> findAllByBrandId(String brandId);
}
