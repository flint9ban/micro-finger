package com.ttsales.microf.love.keyword.service;

import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by liyi on 2016/3/23.
 */
public interface KeywordRepository extends JpaRepository<Keyword,Long> {

    List<Keyword> findAllBySubSend(Integer subSend);

    Keyword findOneByKeyword(String keyword);

    List<Keyword> findAllByKeywordLike(String keyword);
}
