package com.ttsales.microf.love.common.service;

import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
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

   public  List<OrgRegion> findByParentRegionCode(String parentRegionCode){
      return   regionRepository.findByParentRegionCode(parentRegionCode);
   }

    public  List<OrgStore> findByCity(String city){
        return storeRepository.findByCity(city);
    }

    public void creatTestData(){
        regionRepository.deleteAll();
        OrgRegion orgRegion=new OrgRegion();
          orgRegion.setId("110011");
        orgRegion.setName("浙江省");
        orgRegion.setParentRegionCode("00");
        orgRegion.setRegionCode("1001");
        regionRepository.save(orgRegion);
        OrgRegion orgRegion2=new OrgRegion();
          orgRegion2.setId("10021");
        orgRegion2.setName("安徽省");
        orgRegion2.setParentRegionCode("00");
        orgRegion2.setRegionCode("1002");
        regionRepository.save(orgRegion2);
        OrgRegion orgRegion3=new OrgRegion();
      orgRegion3.setId("100121");
        orgRegion3.setName("杭州市");
        orgRegion3.setParentRegionCode("1001");
        orgRegion3.setRegionCode("10011");
        regionRepository.save(orgRegion3);
        OrgRegion orgRegion4=new OrgRegion();
       orgRegion4.setId("100111");
        orgRegion4.setName("合肥市");
        orgRegion4.setParentRegionCode("1002");
        orgRegion4.setRegionCode("10022");
        regionRepository.save(orgRegion4);

        OrgStore orgStore=new OrgStore();
        orgStore.setCity("100121");
        orgStore.setStoreId("10011");
        orgStore.setStoreName("杭州奥迪4S店");
        storeRepository.save(orgStore);
        List<OrgRegion> regions=regionRepository.findAll();
    }
   public  OrgRegion findRegionById(String regionId){
       return regionRepository.findOne(regionId);
   }
}
