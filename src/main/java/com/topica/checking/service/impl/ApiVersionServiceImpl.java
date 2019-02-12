package com.topica.checking.service.impl;

import com.topica.checking.service.ApiVersionService;
import com.topica.checking.domain.ApiVersion;
import com.topica.checking.repository.ApiVersionRepository;
import com.topica.checking.service.dto.ApiVersionDTO;
import com.topica.checking.service.mapper.ApiVersionMapper;
import com.topica.checking.service.specification.ApiversionSpec;
import com.topica.checking.service.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ApiVersion.
 */
@Service
@Transactional
public class ApiVersionServiceImpl implements ApiVersionService {

    private final Logger log = LoggerFactory.getLogger(ApiVersionServiceImpl.class);

    private final ApiVersionRepository apiVersionRepository;

    private final ApiVersionMapper apiVersionMapper;

    public ApiVersionServiceImpl(ApiVersionRepository apiVersionRepository, ApiVersionMapper apiVersionMapper) {
        this.apiVersionRepository = apiVersionRepository;
        this.apiVersionMapper = apiVersionMapper;
    }

    /**
     * Save a apiVersion.
     *
     * @param apiVersionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApiVersionDTO save(ApiVersionDTO apiVersionDTO) {
        log.debug("Request to save ApiVersion : {}", apiVersionDTO);

        ApiVersion apiVersion = apiVersionMapper.toEntity(apiVersionDTO);
        apiVersion = apiVersionRepository.save(apiVersion);
        return apiVersionMapper.toDto(apiVersion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApiVersionDTO> findByVersion(String ver) {
        log.debug("InputVersionDTO to get  DataVersions");
        ApiversionSpec dataVersionSpec = new ApiversionSpec(new SearchCriteria("version",":",ver));
        return apiVersionRepository.findOne(dataVersionSpec).map(apiVersionMapper::toDto);
    }

    /**
     * Get all the apiVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApiVersionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApiVersions");
        return apiVersionRepository.findAll(pageable)
            .map(apiVersionMapper::toDto);
    }


    /**
     * Get one apiVersion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ApiVersionDTO> findOne(Long id) {
        log.debug("Request to get ApiVersion : {}", id);
        return apiVersionRepository.findById(id)
            .map(apiVersionMapper::toDto);
    }

    /**
     * Delete the apiVersion by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApiVersion : {}", id);
        apiVersionRepository.deleteById(id);
    }
}
