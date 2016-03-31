package com.ttsales.microf.love.common.service;

import com.ttsales.microf.love.common.domain.OrgBrand;
import com.ttsales.microf.love.common.domain.OrgCarType;
import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.common.repository.BrandRepository;
import com.ttsales.microf.love.common.repository.CarTypeRepository;
import com.ttsales.microf.love.common.repository.RegionRepository;
import com.ttsales.microf.love.common.repository.StoreRepository;
import com.ttsales.microf.love.fans.domain.FansInfoTag;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.repository.TagRepository;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lenovo on 2016/3/16.
 */
@Service
public class OrgServiceImpl implements OrgService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CarTypeRepository carTypeRepository;

    public static final Integer BRAND_TYPE_TAG = 1;
    public static final Integer BRAND_TYPE_PRICE = 2;

    public List<OrgRegion> findByParentRegionCode(String parentRegionCode) {
        return regionRepository.findByParentRegionCodeOrderByPinyin(parentRegionCode);
    }

    public List<OrgStore> findByCity(String city) {
        return storeRepository.findByCity(city);
    }

    public List<OrgBrand> getAllBrands() {

        return brandRepository.findAllByOrderByPinyin();
    }

    public OrgRegion findRegionById(String regionId) {
        return regionRepository.findOne(regionId);
    }

    public List<OrgRegion> findByLevel(Integer level) {
        return regionRepository.findByLevel(level);
    }

    public List<Tag> findAllBrandTags() {
        return tagRepository.findAllBrandTags();
    }


    public JSONArray getGroupBrands(String ids, Integer type) {
        List<OrgBrand> brands = brandRepository.findAllByOrderByPinyin();
        String pinyins[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        JSONArray typeArrays = new JSONArray();
        for (String py : pinyins) {
            JSONArray eachArray = new JSONArray();
            for (OrgBrand b : brands) {
                String firstZM = b.getPinyin().substring(0, 1).toUpperCase();
                if (py.equals(firstZM)) {
                    JSONObject eachObject = getBrandObj(b, ids, type);
                    eachArray.add(eachObject);
                }
            }
            if (eachArray.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("index", py);
                json.put("data", eachArray);
                typeArrays.add(json);
            }
        }
        return typeArrays;
    }

    private JSONObject getBrandObj(OrgBrand b, String ids, Integer type) {
        List<Long> idList = null;
        String[] idArr = null;
        if (StringUtils.isEmpty(ids)) {
            if (type == BRAND_TYPE_TAG) {
                return getTagSelectState(b, null);
            }
            if (type == BRAND_TYPE_PRICE) {
                return getPriceSelectState(b, null);
            }
        }
        if (type == BRAND_TYPE_TAG) {
            idList = Arrays.asList(ids.split(",")).stream()
                    .map(Long::parseLong).collect(Collectors.toList());
            return getTagSelectState(b, idList);
        }
        if (type == BRAND_TYPE_PRICE) {
            idArr = ids.split(",");
            return getPriceSelectState(b, idArr);
        }
        return null;
    }

    private JSONObject getTagSelectState(OrgBrand tag, List<Long> ids) {
        JSONObject json = new JSONObject();
        json.put("name", tag.getName());
        json.put("id", tag.getId());
        json.put("tagId", tag.getTagId());
        String state = "unselect";
        if (ids == null || ids.size() == 0) {
            json.put("state", state);
            return json;
        }
        for (Long id : ids) {
            if (id == tag.getTagId()) {
                state = "select";
                break;
            }
        }
        json.put("state", state);
        return json;
    }

    private JSONObject getPriceSelectState(OrgBrand tag, String[] ids) {
        JSONObject json = new JSONObject();
        json.put("name", tag.getName());
        json.put("id", tag.getId());
        String state = "unselect";
        if (ids == null || ids.length == 0) {
            json.put("state", state);
            return json;
        }
        for (String id : ids) {
            if (id.equals(tag.getId())) {
                state = "select";
                break;
            }
        }
        json.put("state", state);
        return json;
    }


    public List<OrgCarType> queryCarTypesByBrandId(String brandId) {
        return carTypeRepository.findAllByBrandId(brandId);
    }

    public OrgBrand getBrandById(String brandId) {
        return brandRepository.findOne(brandId);
    }

    public JSONArray covertJson(String competeIds) {
        String competeId[] = competeIds.split(",");
        JSONArray array = new JSONArray();
        for (String id : competeId) {
            OrgCarType orgCarType = carTypeRepository.findOne(id);
            OrgBrand orgBrand = brandRepository.findOne(orgCarType.getBrandId());
            boolean exist = false;
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (orgBrand.getId().equals(obj.optString("brandId"))) {
                    exist = true;
                    String carTypeIds = obj.getString("carTypeIds");
                    String carTypeNames = obj.getString("carTypeNames");
                    carTypeIds += "," + orgCarType.getId();
                    carTypeNames += "," + orgCarType.getName();
                    obj.put("carTypeIds", carTypeIds);
                    obj.put("carTypeNames", carTypeNames);
                    break;
                }
            }
            if (!exist) {
                JSONObject json = new JSONObject();
                json.put("brandId", orgBrand.getId());
                json.put("carTypeIds", orgCarType.getId());
                json.put("carTypeNames", orgCarType.getName());
                array.add(json);
            }
        }
        return array;
    }
}
