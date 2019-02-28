package com.topica.checking.service;

import com.topica.checking.service.dto.ServerStatusDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ServerStatus.
 */
public interface ServerStatusService {

    /**
     * Save a serverStatus.
     *
     * @param serverStatusDTO the entity to save
     * @return the persisted entity
     */
    ServerStatusDTO save(ServerStatusDTO serverStatusDTO);

    /**
     * Get all the serverStatuses.
     *
     * @return the list of entities
     */
    List<ServerStatusDTO> findAll();


    /**
     * Get the "id" serverStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServerStatusDTO> findOne(Long id);

    /**
     * Delete the "id" serverStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
