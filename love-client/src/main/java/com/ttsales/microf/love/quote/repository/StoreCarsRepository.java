package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.StoreCars;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by liyi on 2016/3/25.
 */
public interface StoreCarsRepository extends JpaRepository<StoreCars,String>{

    List<StoreCars> findAllByStoreId(String storeId);

}
