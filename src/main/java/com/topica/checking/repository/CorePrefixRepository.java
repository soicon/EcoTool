package com.topica.checking.repository;

import com.topica.checking.domain.CorePrefix;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CorePrefix entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorePrefixRepository extends JpaRepository<CorePrefix, Long>, JpaSpecificationExecutor<CorePrefix> {

}
