package com.topica.checking.service;

import com.topica.checking.service.dto.ApiVersionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ApiVersion.
 */
public interface ApiVersionService {

    /**
     * Save a apiVersion.
     *
     * @param apiVersionDTO the entity to save
     * @return the persisted entity
     */
    ApiVersionDTO save(ApiVersionDTO apiVersionDTO);

    /**
     * Get all the apiVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ApiVersionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" apiVersion.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ApiVersionDTO> findOne(Long id);


    Optional<ApiVersionDTO> findByVersion(String ver);

    /**
     * Delete the "id" apiVersion.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
