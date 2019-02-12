package com.topica.checking.service.impl;

import com.topica.checking.service.AppVersionService;
import com.topica.checking.domain.AppVersion;
import com.topica.checking.repository.AppVersionRepository;
import com.topica.checking.service.dto.AppVersionDTO;
import com.topica.checking.service.mapper.AppVersionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing AppVersion.
 */
@Service
@Transactional
public class AppVersionServiceImpl implements AppVersionService {

    private final Logger log = LoggerFactory.getLogger(AppVersionServiceImpl.class);

    private final AppVersionRepository appVersionRepository;

    private final AppVersionMapper appVersionMapper;

    public AppVersionServiceImpl(AppVersionRepository appVersionRepository, AppVersionMapper appVersionMapper) {
        this.appVersionRepository = appVersionRepository;
        this.appVersionMapper = appVersionMapper;
    }

    /**
     * Save a appVersion.
     *
     * @param appVersionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AppVersionDTO save(AppVersionDTO appVersionDTO) {
        log.debug("Request to save AppVersion : {}", appVersionDTO);

        AppVersion appVersion = appVersionMapper.toEntity(appVersionDTO);
        appVersion = appVersionRepository.save(appVersion);
        return appVersionMapper.toDto(appVersion);
    }

    /**
     * Get all the appVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppVersionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppVersions");
        return appVersionRepository.findAll(pageable)
            .map(appVersionMapper::toDto);
    }


    /**
     * Get one appVersion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AppVersionDTO> findOne(Long id) {
        log.debug("Request to get AppVersion : {}", id);
        return appVersionRepository.findById(id)
            .map(appVersionMapper::toDto);
    }

    /**
     * Delete the appVersion by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppVersion : {}", id);
        appVersionRepository.deleteById(id);
    }
}
