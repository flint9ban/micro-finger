package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.CXCity;
import com.ttsales.microf.love.quote.domain.QuoteCity;
import com.ttsales.microf.love.quote.domain.QuoteCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface QuoteCityRepository extends JpaRepository<QuoteCity,String>{
    List<QuoteCity> findByStoreId(String storeId);
}
