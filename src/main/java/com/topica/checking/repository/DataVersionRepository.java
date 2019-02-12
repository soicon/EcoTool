package com.topica.checking.repository;

import com.topica.checking.domain.DataVersion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataVersionRepository extends JpaRepository<DataVersion, Long>, JpaSpecificationExecutor<DataVersion> {
    //DataVersion findByVersion(String ver);
}
