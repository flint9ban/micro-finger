package com.ttsales.microf.love.quote;

import com.ttsales.microf.love.quote.domain.CXCountry;
import com.ttsales.microf.love.quote.domain.CXProvince;
import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.domain.StoreCars;
import com.ttsales.microf.love.quote.repository.CXCountryRepository;
import com.ttsales.microf.love.quote.repository.CXProvinceRepository;
import com.ttsales.microf.love.quote.repository.QueryInfoRepository;
import com.ttsales.microf.love.quote.repository.StoreCarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Created by liyi on 2016/3/28.
 */
//@SpringBootApplication
public class QuoteApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(QuoteApplication.class, args);
//    }
}
@Component
class DummyAR implements ApplicationRunner {

    @Autowired
    QueryInfoRepository queryInfoRepository;

    @Autowired
    StoreCarsRepository storeBrandRepository;

    @Autowired
    CXCountryRepository cxCountryRepository;

    @Autowired
    CXProvinceRepository cxProvinceRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setOpenId("A1");
        queryInfo.setStoreId("store1");
        queryInfo.setCompeteRegion("region1");
        queryInfo.setCompeteRegionType(QueryInfo.REGION_TYPE_COUNTRY);
        queryInfo.setCompeteIds("1,2,3,4,5");
        queryInfo.setRegion("region2");
        queryInfo.setRegionType(QueryInfo.REGION_TYPE_PROVINCE);
        queryInfoRepository.save(queryInfo);

        StoreCars storeBrand = new StoreCars();
        storeBrand.setId("1");
        storeBrand.setStoreId("store1");
        storeBrand.setBrandId("brand1");
        StoreCars storeBrand1 = new StoreCars();
        storeBrand1.setId("2");
        storeBrand1.setBrandId("brand2");
        storeBrand1.setStoreId("store1");
        storeBrandRepository.save(storeBrand);
        storeBrandRepository.save(storeBrand1);

        CXCountry country = new CXCountry();
        country.setId("1");
        country.setBrandId("compet1");
        country.setBrand("competeBrand1");
        country.setSeriesId("1");
        country.setSeries("competeCX1");
        country.setModel("competeModel1");
        country.setPriCon(0.5);
        country.setStoreNum("3");
        country.setPriceStore("12.1");
        country.setPriceVendor("12.5");
        cxCountryRepository.save(country);

        CXCountry country1 = new CXCountry();
        country1.setId("2");
        country1.setBrandId("compet1");
        country1.setBrand("competeBrand1");
        country1.setSeriesId("2");
        country1.setSeries("competeCX2");
        country1.setModel("competeModel2");
        country1.setPriCon(0.5);
        country1.setStoreNum("4");
        country1.setPriceStore("11.1");
        country1.setPriceVendor("10.5");
        cxCountryRepository.save(country1);

        CXCountry country2 = new CXCountry();
        country2.setId("3");
        country2.setBrandId("compet2");
        country2.setBrand("competeBrand2");
        country2.setSeriesId("3");
        country2.setSeries("competeCX3");
        country2.setModel("competeModel3");
        country2.setPriCon(0.5);
        country2.setStoreNum("4");
        country2.setPriceStore("11.1");
        country2.setPriceVendor("12.5");
        cxCountryRepository.save(country2);

        CXCountry country3 = new CXCountry();
        country3.setId("4");
        country3.setBrandId("compet2");
        country3.setBrand("competeBrand2");
        country3.setSeriesId("4");
        country3.setSeries("competeCX4");
        country3.setModel("competeModel4");
        country3.setPriCon(0.3);
        country3.setStoreNum("3");
        country3.setPriceStore("10.1");
        country3.setPriceVendor("12.5");
        cxCountryRepository.save(country3);

        CXCountry country4 = new CXCountry();
        country4.setId("5");
        country4.setBrandId("compet3");
        country4.setBrand("competeBrand3");
        country4.setSeriesId("5");
        country4.setSeries("competeCX5");
        country4.setModel("competeModel5");
        country4.setPriCon(0.3);
        country4.setStoreNum("2");
        country4.setPriceStore("9.1");
        country4.setPriceVendor("12.5");
        cxCountryRepository.save(country4);

        CXProvince province = new CXProvince();
        province.setProvince("region2");
        province.setId("1");
        province.setBrandId("brand1");
        province.setBrand("brand1");
        province.setSeriesId("cx1");
        province.setSeries("cx1");
        province.setModel("model1");
        province.setStoreNum("2");
        province.setPriceStore("10.3");
        province.setPriceVendor("12.3");
        province.setPriCon(0.35);
        cxProvinceRepository.save(province);

        CXProvince province1 = new CXProvince();
        province1.setProvince("region2");
        province1.setId("2");
        province1.setBrandId("brand1");
        province1.setBrand("brand1");
        province1.setSeriesId("cx2");
        province1.setSeries("cx2");
        province1.setModel("model2");
        province1.setStoreNum("3");
        province1.setPriceStore("9.13");
        province1.setPriceVendor("11.3");
        province1.setPriCon(0.35);
        cxProvinceRepository.save(province1);

        CXProvince province2 = new CXProvince();
        province2.setProvince("region2");
        province2.setId("3");
        province2.setBrandId("brand1");
        province2.setBrand("brand1");
        province2.setSeriesId("cx3");
        province2.setSeries("cx3");
        province2.setModel("model3");
        province2.setStoreNum("2");
        province2.setPriceStore("7.13");
        province2.setPriceVendor("10.3");
        province2.setPriCon(0.15);
        cxProvinceRepository.save(province2);

        CXProvince province3 = new CXProvince();
        province3.setProvince("region2");
        province3.setId("4");
        province3.setBrandId("brand2");
        province3.setBrand("brand2");
        province3.setSeriesId("cx4");
        province3.setSeries("cx4");
        province3.setModel("model4");
        province3.setStoreNum("6");
        province3.setPriceStore("10.13");
        province3.setPriceVendor("12.3");
        province3.setPriCon(0.35);
        cxProvinceRepository.save(province3);

        CXProvince province4 = new CXProvince();
        province4.setProvince("region2");
        province4.setId("5");
        province4.setBrandId("brand2");
        province4.setBrand("brand2");
        province4.setSeriesId("cx5");
        province4.setSeries("cx5");
        province4.setModel("model5");
        province4.setStoreNum("4");
        province4.setPriceStore("10.13");
        province4.setPriceVendor("12.3");
        province4.setPriCon(0.35);
        cxProvinceRepository.save(province4);
    }
}