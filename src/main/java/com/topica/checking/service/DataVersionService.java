package com.topica.checking.service;

import com.topica.checking.service.dto.DataVersionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DataVersion.
 */
public interface DataVersionService {

    /**
     * Save a dataVersion.
     *
     * @param dataVersionDTO the entity to save
     * @return the persisted entity
     */
    DataVersionDTO save(DataVersionDTO dataVersionDTO);

    /**
     * Get all the dataVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DataVersionDTO> findAll(Pageable pageable);

    Optional<DataVersionDTO> findByVersion(String ver);


    /**
     * Get the "id" dataVersion.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DataVersionDTO> findOne(Long id);

    /**
     * Delete the "id" dataVersion.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
