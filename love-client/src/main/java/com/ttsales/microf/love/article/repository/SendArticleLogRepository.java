package com.ttsales.microf.love.article.repository;

import com.ttsales.microf.love.article.domain.SendArticleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by liyi on 16/3/12.
 */
@RepositoryRestResource
public interface SendArticleLogRepository extends JpaRepository<SendArticleLog,Long>{

    @RestResource(path="find-mediaId-OpenId")
    SendArticleLog findByMediaIdAndOpenId(@Param("mId") String mediaId, @Param("openId") String openId);
}
