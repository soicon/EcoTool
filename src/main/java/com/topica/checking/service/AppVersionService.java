package com.topica.checking.service;

import com.topica.checking.service.dto.AppVersionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing AppVersion.
 */
public interface AppVersionService {

    /**
     * Save a appVersion.
     *
     * @param appVersionDTO the entity to save
     * @return the persisted entity
     */
    AppVersionDTO save(AppVersionDTO appVersionDTO);

    /**
     * Get all the appVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AppVersionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" appVersion.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AppVersionDTO> findOne(Long id);

    /**
     * Delete the "id" appVersion.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
