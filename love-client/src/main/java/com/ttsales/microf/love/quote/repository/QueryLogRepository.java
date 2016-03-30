package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.domain.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface QueryLogRepository extends JpaRepository<QueryLog,Long>{

    Long countByOpenIdAndCreatAtGreaterThan(String openId, LocalDateTime now);
}
