package com.topica.checking.service;

import com.topica.checking.service.dto.FileStatusDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FileStatus.
 */
public interface FileStatusService {

    /**
     * Save a fileStatus.
     *
     * @param fileStatusDTO the entity to save
     * @return the persisted entity
     */
    FileStatusDTO save(FileStatusDTO fileStatusDTO);

    /**
     * Get all the fileStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FileStatusDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fileStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FileStatusDTO> findOne(Long id);

    Optional<FileStatusDTO> findByName(String id);

    /**
     * Delete the "id" fileStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
