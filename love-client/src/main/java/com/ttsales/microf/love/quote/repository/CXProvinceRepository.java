package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.CXProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface CXProvinceRepository extends JpaRepository<CXProvince,Long>{


    List<CXProvince> findAllByProvinceAndSeriesIdInOrderByPriConDesc(String province, List<String> cx);

    List<CXProvince> findTop5ByProvinceAndBrandIdInOrderByPriConDesc(String province, List<String> brands);
}
