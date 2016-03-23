package com.ttsales.microf.love.keyword.service;

import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liyi on 2016/3/23.
 */
public interface KeywordRepository extends JpaRepository<Keyword,Long> {
}
