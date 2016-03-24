package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.QueryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface QueryInfoRepository extends JpaRepository<QueryInfo,Long>{

    QueryInfo findByOpenId(String openId);
}
