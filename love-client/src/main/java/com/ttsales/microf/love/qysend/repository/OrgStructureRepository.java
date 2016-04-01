package com.ttsales.microf.love.qysend.repository;

import com.ttsales.microf.love.qysend.domain.Log;
import com.ttsales.microf.love.qysend.domain.OrgStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by liyi on 2016/3/31.
 */
public interface OrgStructureRepository extends JpaRepository<OrgStructure,Long> {

    @Query(value="select structure.*,store.store_name from org_structure structure,org_store store where structure.org_id=store.store_id",nativeQuery = true)
    List<OrgStructure> getAllStoreStructure();

}
