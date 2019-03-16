package com.topica.checking.service;

import com.topica.checking.service.dto.FileConfigDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FileConfig.
 */
public interface FileConfigService {

    /**
     * Save a fileConfig.
     *
     * @param fileConfigDTO the entity to save
     * @return the persisted entity
     */
    FileConfigDTO save(FileConfigDTO fileConfigDTO);

    /**
     * Get all the fileConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FileConfigDTO> findAll(Pageable pageable);




    /**
     * Get the "id" fileConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FileConfigDTO> findOne(Long id);

    /**
     * Delete the "id" fileConfig.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
