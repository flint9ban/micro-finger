package com.ttsales.microf.love.quote.service;


import com.ttsales.microf.love.common.repository.BrandRepository;
import com.ttsales.microf.love.domainUtil.LocalDateTimeUtil;
import com.ttsales.microf.love.quote.domain.*;
import com.ttsales.microf.love.quote.repository.*;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 2016/3/11.
 */
@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QueryInfoRepository queryInfoRepository;

    @Autowired
    private QueryLogRepository queryLogRepository;

    @Autowired
    private QueryLogCompeteRepository queryLogCompeteRepository;

    @Autowired
    private CXCityRepository cxCityRepository;

    @Autowired
    private CXCountryRepository cxCountryRepository;

    @Autowired
    private CXProvinceRepository cxProvinceRepository;

    @Autowired
    private StoreCarsRepository storeBrandRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private TagService tagService;
    @Autowired
    private QuoteCityRepository quoteCityRepository;

    @Autowired
    private QuoteProvinceRepository quoteProvinceRepository;

    @Autowired
    private QuoteCountryRepository quoteCountryRepository;

    public static final Integer QUERY_LIMIT_COUNT=2;
    
    public JSONObject query(QueryInfo queryInfo){
        saveQueryInfo(queryInfo);
        List<JSONObject> selfBrand = querySelfBrand(queryInfo);
        List<JSONObject> competeBrand = queryCompetBrand(queryInfo);
        JSONObject json = new JSONObject();
        json.put("selfBrand",selfBrand);
        json.put("competeBrand",competeBrand);
        return json;
    }

    private void saveQueryInfo(QueryInfo queryInfo){
        QueryInfo persitenctQueryInfo =  queryInfoRepository.findByOpenId(queryInfo.getOpenId());
        if(persitenctQueryInfo!=null){
            queryInfo.setId(persitenctQueryInfo.getId());
        }
        queryInfo  = queryInfoRepository.save(queryInfo);
        QueryLog log = new QueryLog();
        log.setRegion(queryInfo.getRegion());
        log.setCompeteRegion(queryInfo.getCompeteRegion());
        log.setCompeteRegionName(queryInfo.getCompeteRegionName());
        log.setStoreId(queryInfo.getStoreId());
        log.setStoreName(queryInfo.getStoreName());
        log.setOpenId(queryInfo.getOpenId());
        log.setRegionName(queryInfo.getRegionName());
        log.setParentRegion(queryInfo.getParentRegion());
        log.setCompeteParentRegion(queryInfo.getCompeteParentRegion());
        log = queryLogRepository.save(log);
        Long logId = log.getId();
        List<String> competeIds = queryInfo.getCompleteList();
        competeIds.stream().forEach(competeId->{
            QueryLogCompete compete = new QueryLogCompete();
            compete.setLogId(logId);
            compete.setCompeteId(competeId);
            queryLogCompeteRepository.save(compete);
        });
    }

    @Override
    public QueryInfo queryQueryInfo(String openId) {
        QueryInfo queryInfo =  queryInfoRepository.findByOpenId(openId);
        if (queryInfo == null) {
            queryInfo = getQueryInfoFromQy(openId);
        }
        return queryInfo;
    }

    @Override
    public boolean isQueryLimit(String openId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = LocalDateTimeUtil.clearTime(localDateTime);
        Long count = queryLogRepository.countByOpenIdAndCreatAtGreaterThan(openId,localDateTime);
        return count<QUERY_LIMIT_COUNT;
    }

    public Long countQueryTims(String openId ){
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = LocalDateTimeUtil.clearTime(localDateTime);
        return  queryLogRepository.countByOpenIdAndCreatAtGreaterThan(openId,localDateTime);
    }


    @Override
    public JSONObject queryQuote(String storeId) {
        return null;
    }


    @Override
    public List<JSONObject> querySelfBrand(QueryInfo queryInfo) {
        String storeId = queryInfo.getStoreId();
        List<StoreCars> storeBrands = storeBrandRepository.findAllByStoreId(storeId);
        List<String> brands = storeBrands.stream().map(StoreCars::getBrandId).collect(Collectors.toList());
        String region = queryInfo.getRegion();
        String parentRegion = queryInfo.getParentRegion();
        Integer regionType = queryInfo.getRegionType();
        if(QueryInfo.REGION_TYPE_COUNTRY.equals(regionType)){
            return convertCX2JSON(cxCountryRepository.findTop5ByBrandIdInOrderByPriConDesc(brands));
        }else if(QueryInfo.REGION_TYPE_PROVINCE.equals(regionType)){
            return convertCX2JSON(cxProvinceRepository.findTop5ByProvinceAndBrandIdInOrderByPriConDesc(region,brands));
        }else if(QueryInfo.REGION_TYPE_CITY.equals(regionType)){
            return convertCX2JSON(cxCityRepository.findAllByProvinceAndCityAndSeriesIdInOrderByPriConDesc(parentRegion,region,brands));
        }
        return null;
    }

    @Override
    public List<JSONObject> queryCompetBrand(QueryInfo queryInfo) {
        List<String> competeList = queryInfo.getCompleteList();
        String region = queryInfo.getCompeteRegion();
        String parentRegion = queryInfo.getCompeteParentRegion();
        Integer regionType = queryInfo.getCompeteRegionType();
        if(QueryInfo.REGION_TYPE_COUNTRY.equals(regionType)){
            return convertCX2JSON(cxCountryRepository.findAllBySeriesIdInOrderByPriConDesc(competeList));
        }else if(QueryInfo.REGION_TYPE_PROVINCE.equals(regionType)){
            return convertCX2JSON(cxProvinceRepository.findAllByProvinceAndSeriesIdInOrderByPriConDesc(region,competeList));
        }else if(QueryInfo.REGION_TYPE_CITY.equals(regionType)){
            return convertCX2JSON(cxCityRepository.findAllByProvinceAndCityAndSeriesIdInOrderByPriConDesc(parentRegion,region,competeList));
        }
        return null;
    }

    @Override
    public List<Tag> getQuoteTag(String openId) {
        List<Tag> tags = new ArrayList<Tag>();
        QueryInfo queryInfo = queryQueryInfo(openId);
        if(queryInfo!=null){
            String region = queryInfo.getRegionName();
            String competeRegion = queryInfo.getCompeteRegionName();
            String storeId = queryInfo.getStoreId();
            List<StoreCars> storeBrands = storeBrandRepository.findAllByStoreId(storeId);
            Tag tag  = tagService.findTagByName("4S店报价情报");
            Tag regionTag = tagService.findTagByName(region);
            Tag competeRegionTag = tagService.findTagByName(competeRegion);
            List<Tag> brandTag = storeBrands.stream()
                                    .map(storeCar ->brandRepository.findOne(storeCar.getBrandId()))
                                    .map(brand->tagService.findTagByName(brand.getName()))
                                    .collect(Collectors.toList());
            List<Tag> competeTag = queryInfo.getCompleteList().stream().map(tagService::findTagByName).collect(Collectors.toList());
            tags.addAll(brandTag);
            tags.addAll(competeTag);
            if (tag != null) {
                tags.add(tag);
            }
            if (regionTag != null) {
                tags.add(regionTag);
            }
            if (competeRegionTag != null) {
                tags.add(competeRegionTag);
            }
        }
        return tags;
    }

    private List<JSONObject> convertCX2JSON(List<? extends SuperCX> cxs){
        return cxs.stream().map(superCX->{
            JSONObject json = new JSONObject();
            json.put("brand",superCX.getBrand());
            json.put("series",superCX.getSeries());
            json.put("model",superCX.getModel());
            json.put("storeNum",superCX.getStoreNum());
            json.put("priceStore",superCX.getPriceStore());
            json.put("priceVendor",superCX.getPriceVendor());
            json.put("priCon",(superCX.getPriCon()*100)+"%");
            return json;
        }).collect(Collectors.toList());
    }

    //get queryinfo from 企业号
    private QueryInfo getQueryInfoFromQy(String openId){
        // TODO: 2016/3/24
        return null;
    }

    public JSONObject queryStorePrice(String storeId){
     JSONObject json=new JSONObject();
        List<QuoteCountry> quoteCountrys=quoteCountryRepository.findByStoreId(storeId);
        List<QuoteProvince> quoteProvinces=quoteProvinceRepository.findByStoreId(storeId);
        List<QuoteCity> quoteCitys=quoteCityRepository.findByStoreId(storeId);
        json.put("countryMinNum", CollectionUtils.isEmpty(quoteCountrys)?"":quoteCountrys.get(0).getMinModelNum());
        json.put("countryMaxNum", CollectionUtils.isEmpty(quoteCountrys)?"":quoteCountrys.get(0).getMaxModelNum());
        json.put("provinceMinNum", CollectionUtils.isEmpty(quoteProvinces)?"":quoteProvinces.get(0).getMinModelNum());
        json.put("provinceMaxNum", CollectionUtils.isEmpty(quoteProvinces)?"":quoteProvinces.get(0).getMaxModelNum());
        json.put("cityMinNum", CollectionUtils.isEmpty(quoteCitys)?"":quoteCitys.get(0).getMinModelNum());
        json.put("cityMaxNum", CollectionUtils.isEmpty(quoteCitys)?"":quoteCitys.get(0).getMaxModelNum());
        return json;
    }
}
