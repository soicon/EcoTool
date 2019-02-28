package com.topica.checking.repository;

import com.topica.checking.domain.ServerStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServerStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServerStatusRepository extends JpaRepository<ServerStatus, Long> {

}
