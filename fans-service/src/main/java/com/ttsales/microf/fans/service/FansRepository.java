package com.ttsales.microf.fans.service;

import com.ttsales.microf.fans.domain.FansInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Created by liyi on 16/3/6.
 */
@RepositoryRestResource
@Repository
public interface FansRepository extends JpaRepository<FansInfo,Long> {
}
