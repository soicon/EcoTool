package com.topica.checking.service;

import com.topica.checking.service.dto.DataPrefixDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DataPrefix.
 */
public interface DataPrefixService {

    /**
     * Save a dataPrefix.
     *
     * @param dataPrefixDTO the entity to save
     * @return the persisted entity
     */
    DataPrefixDTO save(DataPrefixDTO dataPrefixDTO);

    /**
     * Get all the dataPrefixes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DataPrefixDTO> findAll(Pageable pageable);

    Optional<DataPrefixDTO> findByName(String name);


    /**
     * Get the "id" dataPrefix.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DataPrefixDTO> findOne(Long id);

    /**
     * Delete the "id" dataPrefix.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
