package com.ttsales.microf.love.quote.service;


import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.domain.QueryLog;
import com.ttsales.microf.love.quote.domain.QueryLogCompete;
import com.ttsales.microf.love.quote.repository.QueryInfoRepository;
import com.ttsales.microf.love.quote.repository.QueryLogCompeteRepository;
import com.ttsales.microf.love.quote.repository.QueryLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private void saveQueryInfo(QueryInfo queryInfo){
        queryInfo  = queryInfoRepository.save(queryInfo);
        QueryLog log = new QueryLog();
        log.setRegion(queryInfo.getRegion());
        log.setCompeteRegion(queryInfo.getCompeteRegion());
        log.setCompeteRegionName(queryInfo.getCompeteRegionName());
        log.setStoreId(queryInfo.getStoreId());
        log.setStoreName(queryInfo.getStoreName());
        log.setOpenId(queryInfo.getOpenId());
        log.setRegionName(queryInfo.getRegionName());
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
        return queryInfoRepository.findByOpenId(openId);
    }

}
