package com.topica.checking.repository;

import com.topica.checking.domain.DataPrefix;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataPrefix entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataPrefixRepository extends JpaRepository<DataPrefix, Long>, JpaSpecificationExecutor<DataPrefix> {

}
