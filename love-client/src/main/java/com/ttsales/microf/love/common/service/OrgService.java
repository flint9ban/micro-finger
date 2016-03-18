package com.ttsales.microf.love.common.service;

import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
public interface OrgService {

    List<OrgRegion> findByParentRegionCode(String parentRegionCode);

    List<OrgStore> findByCity(String city);

    OrgRegion findRegionById(String regionId);

    void  creatTestData();
}
