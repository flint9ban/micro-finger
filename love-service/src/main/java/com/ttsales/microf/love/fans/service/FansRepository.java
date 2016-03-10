package com.ttsales.microf.love.fans.service;

import com.ttsales.microf.love.fans.domain.FansInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liyi on 16/3/6.
 */
//@RepositoryRestResource
public interface FansRepository extends JpaRepository<FansInfo,Long> {
}
