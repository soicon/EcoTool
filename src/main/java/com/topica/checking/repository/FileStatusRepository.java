package com.topica.checking.repository;

import com.topica.checking.domain.FileStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FileStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileStatusRepository extends JpaRepository<FileStatus, Long>, JpaSpecificationExecutor<FileStatus> {

}
