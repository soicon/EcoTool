package com.topica.checking.repository;

import com.topica.checking.domain.ApiVersion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ApiVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiVersionRepository extends JpaRepository<ApiVersion, Long>, JpaSpecificationExecutor<ApiVersion> {

}
