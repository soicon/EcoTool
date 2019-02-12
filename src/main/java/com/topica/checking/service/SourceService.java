package com.topica.checking.service;

import com.topica.checking.service.dto.SourceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Viewtool.
 */
public interface SourceService {

    /**
     * Save a source.
     *
     * @param sourceDTO the entity to save
     * @return the persisted entity
     */
    SourceDTO save(SourceDTO sourceDTO);


    List<Object> findQA(long sourceDTO);


    /**
     * Get all the sources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SourceDTO> findAll(Pageable pageable);


    Page<SourceDTO> filter(Pageable pageable);


    /**
     * Get the "id" source.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SourceDTO> findOne(Long id);

    /**
     * Delete the "id" source.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
