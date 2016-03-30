package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.CXCity;
import com.ttsales.microf.love.quote.domain.CXCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface CXCityRepository extends JpaRepository<CXCity,Long>{


    List<CXCity> findAllByProvinceAndCityAndSeriesIdInOrderByPriConDesc(String province,String city, List<String> series);

    List<CXCity> findTop5ByProvinceAndCityAndBrandIdInOrderByPriConDesc(String province,String city,List<String> brands);
}
