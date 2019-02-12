package com.topica.checking.repository;

import com.topica.checking.domain.InputVersion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InputVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InputVersionRepository extends JpaRepository<InputVersion, Long>, JpaSpecificationExecutor<InputVersion> {

}
