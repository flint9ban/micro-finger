package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.CXCountry;
import com.ttsales.microf.love.quote.domain.CXProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface CXCountryRepository extends JpaRepository<CXCountry,Long>{


    List<CXCountry> findAllBySeriesIdInOrderByPriConDesc(List<String> cx);

    List<CXCountry> findTop5ByBrandIdInOrderByPriConDesc(List<String> brands);
}
