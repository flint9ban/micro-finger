package com.ttsales.microf.love.common.service;

import com.ttsales.microf.love.common.domain.*;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.util.WXApiException;
import net.sf.json.JSONArray;
import org.apache.http.HttpException;

import java.util.List;

/**
 * Created by liyi on 16/3/13.
 */
public interface OrgService {

    List<OrgRegion> findByParentRegionCode(String parentRegionCode);

    List<OrgStore> findByCity(String city);

    OrgRegion findRegionById(String regionId);

   List<OrgBrand>   getAllBrands();

    List<OrgRegion> findByLevel(Integer level);

    List<Tag> findAllBrandTags();

    JSONArray getGroupBrands(String  ids,Integer type);

    List<OrgCarType> queryCarTypesByBrandId(String brandId);

    OrgBrand getBrandById(String brandId);

   JSONArray covertJson(String competeIds);

    OrgStore findStoreByMemberId(String memberId);
}
