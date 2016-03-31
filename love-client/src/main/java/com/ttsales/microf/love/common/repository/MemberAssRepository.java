package com.ttsales.microf.love.common.repository;

import com.ttsales.microf.love.common.domain.OrgMemberAss;
import com.ttsales.microf.love.common.domain.OrgStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
//
@RepositoryRestResource
public interface MemberAssRepository extends JpaRepository<OrgMemberAss,String> {

    List<OrgMemberAss> findByMemberId(String memberId);


}
