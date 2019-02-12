package com.topica.checking.repository;

import com.topica.checking.domain.AppVersion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AppVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long>, JpaSpecificationExecutor<AppVersion> {

}
