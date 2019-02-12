package com.topica.checking.repository;

import com.topica.checking.domain.RunnerLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RunnerLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RunnerLogRepository extends JpaRepository<RunnerLog, Long>, JpaSpecificationExecutor<RunnerLog> {

}
