package com.topica.checking.service;

import com.topica.checking.service.dto.CorePrefixDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CorePrefix.
 */
public interface CorePrefixService {

    /**
     * Save a corePrefix.
     *
     * @param corePrefixDTO the entity to save
     * @return the persisted entity
     */
    CorePrefixDTO save(CorePrefixDTO corePrefixDTO);

    /**
     * Get all the corePrefixes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CorePrefixDTO> findAll(Pageable pageable);

    Optional<CorePrefixDTO> findByName(String name);


    /**
     * Get the "id" corePrefix.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CorePrefixDTO> findOne(Long id);

    /**
     * Delete the "id" corePrefix.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
