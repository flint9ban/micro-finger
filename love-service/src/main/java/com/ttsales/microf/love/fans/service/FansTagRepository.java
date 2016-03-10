package com.ttsales.microf.love.fans.service;

import com.ttsales.microf.love.fans.domain.FansInfoTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
//@RepositoryRestResource
public interface FansTagRepository extends JpaRepository<FansInfoTag,Long>{

    List<FansInfoTag> findAllByFansId(Long fansId);
}
