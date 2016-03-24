package com.ttsales.microf.love.quote.repository;

import com.ttsales.microf.love.quote.domain.QueryLog;
import com.ttsales.microf.love.quote.domain.QueryLogCompete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by liyi on 2016/3/24.
 */
@Repository
public interface QueryLogCompeteRepository extends JpaRepository<QueryLogCompete,Long>{
}
