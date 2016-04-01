package com.ttsales.microf.love.qysend.repository;

import com.ttsales.microf.love.qysend.domain.OrgMember;
import com.ttsales.microf.love.qysend.domain.OrgStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by liyi on 2016/3/31.
 */
public interface OrgMemberRepository extends JpaRepository<OrgMember,Long> {

    @Query(value="select a.*,store.store_name,store.store_id from org_member_ass a,org_store store where a.org_id=store.store_id",nativeQuery = true)
    List<OrgMember> getAllStoreMember();

}
