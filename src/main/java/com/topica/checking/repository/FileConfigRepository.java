package com.topica.checking.repository;

import com.topica.checking.domain.FileConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FileConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileConfigRepository extends JpaRepository<FileConfig, Long>, JpaSpecificationExecutor<FileConfig> {

}
