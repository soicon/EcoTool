package com.topica.checking.service.impl;

import com.topica.checking.service.CorePrefixService;
import com.topica.checking.domain.CorePrefix;
import com.topica.checking.repository.CorePrefixRepository;
import com.topica.checking.service.dto.CorePrefixDTO;
import com.topica.checking.service.mapper.CorePrefixMapper;
import com.topica.checking.service.specification.CoreSpec;
import com.topica.checking.service.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CorePrefix.
 */
@Service
@Transactional
public class CorePrefixServiceImpl implements CorePrefixService {

    private final Logger log = LoggerFactory.getLogger(CorePrefixServiceImpl.class);

    private final CorePrefixRepository corePrefixRepository;

    private final CorePrefixMapper corePrefixMapper;

    public CorePrefixServiceImpl(CorePrefixRepository corePrefixRepository, CorePrefixMapper corePrefixMapper) {
        this.corePrefixRepository = corePrefixRepository;
        this.corePrefixMapper = corePrefixMapper;
    }

    /**
     * Save a corePrefix.
     *
     * @param corePrefixDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CorePrefixDTO save(CorePrefixDTO corePrefixDTO) {
        log.debug("Request to save CorePrefix : {}", corePrefixDTO);

        CorePrefix corePrefix = corePrefixMapper.toEntity(corePrefixDTO);
        corePrefix = corePrefixRepository.save(corePrefix);
        return corePrefixMapper.toDto(corePrefix);
    }

    /**
     * Get all the corePrefixes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorePrefixDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CorePrefixes");
        return corePrefixRepository.findAll(pageable)
            .map(corePrefixMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CorePrefixDTO> findByName(String ver) {
        log.debug("InputVersionDTO to get  DataVersions");
        CoreSpec dataVersionSpec = new CoreSpec(new SearchCriteria("name",":",ver));
        return corePrefixRepository.findOne(dataVersionSpec).map(corePrefixMapper::toDto);
    }
    /**
     * Get one corePrefix by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CorePrefixDTO> findOne(Long id) {
        log.debug("Request to get CorePrefix : {}", id);
        return corePrefixRepository.findById(id)
            .map(corePrefixMapper::toDto);
    }

    /**
     * Delete the corePrefix by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CorePrefix : {}", id);
        corePrefixRepository.deleteById(id);
    }
}
