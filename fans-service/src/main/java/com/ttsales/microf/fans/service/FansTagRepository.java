package com.ttsales.microf.fans.service;

import com.ttsales.microf.fans.domain.FansInfoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
@RepositoryRestResource
@Repository
public interface FansTagRepository extends JpaRepository<FansInfoTag,Long>{

    public List<FansInfoTag> findAllByFansId(Long fansId);
}
