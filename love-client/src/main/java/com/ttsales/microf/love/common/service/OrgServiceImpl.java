package com.ttsales.microf.love.common.service;

import com.ttsales.microf.love.common.domain.OrgBrand;
import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.common.repository.BrandRepository;
import com.ttsales.microf.love.common.repository.RegionRepository;
import com.ttsales.microf.love.common.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lenovo on 2016/3/16.
 */
@Service
public class OrgServiceImpl implements OrgService  {

    @Autowired
    private RegionRepository   regionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BrandRepository brandRepository;

   public  List<OrgRegion> findByParentRegionCode(String parentRegionCode){
      return   regionRepository.findByParentRegionCodeOrderByPinyin(parentRegionCode);
   }

    public  List<OrgStore> findByCity(String city){
        return storeRepository.findByCity(city);
    }

    public List<OrgBrand>   getAllBrands(){
        return brandRepository.findAll();
    }

   public  OrgRegion findRegionById(String regionId){
       return regionRepository.findOne(regionId);
   }

    public List<OrgRegion> findByLevel(Integer level){
        return regionRepository.findByLevel(level);
    }
}
