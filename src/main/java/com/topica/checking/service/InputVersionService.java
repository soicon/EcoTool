package com.topica.checking.service;

import com.topica.checking.service.dto.InputVersionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing InputVersion.
 */
public interface InputVersionService {

    /**
     * Save a inputVersion.
     *
     * @param inputVersionDTO the entity to save
     * @return the persisted entity
     */
    InputVersionDTO save(InputVersionDTO inputVersionDTO);

    /**
     * Get all the inputVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InputVersionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" inputVersion.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InputVersionDTO> findOne(Long id);


    Optional<InputVersionDTO> findByVersion(String ver);


    /**
     * Delete the "id" inputVersion.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
