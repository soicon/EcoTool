package com.topica.checking.service.impl;

import com.topica.checking.service.InputVersionService;
import com.topica.checking.domain.InputVersion;
import com.topica.checking.repository.InputVersionRepository;
import com.topica.checking.service.dto.InputVersionDTO;
import com.topica.checking.service.mapper.InputVersionMapper;
import com.topica.checking.service.specification.InputVersionSpec;
import com.topica.checking.service.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing InputVersion.
 */
@Service
@Transactional
public class InputVersionServiceImpl implements InputVersionService {

    private final Logger log = LoggerFactory.getLogger(InputVersionServiceImpl.class);

    private final InputVersionRepository inputVersionRepository;

    private final InputVersionMapper inputVersionMapper;

    public InputVersionServiceImpl(InputVersionRepository inputVersionRepository, InputVersionMapper inputVersionMapper) {
        this.inputVersionRepository = inputVersionRepository;
        this.inputVersionMapper = inputVersionMapper;
    }

    /**
     * Save a inputVersion.
     *
     * @param inputVersionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InputVersionDTO save(InputVersionDTO inputVersionDTO) {
        log.debug("Request to save InputVersion : {}", inputVersionDTO);

        InputVersion inputVersion = inputVersionMapper.toEntity(inputVersionDTO);
        inputVersion = inputVersionRepository.save(inputVersion);
        return inputVersionMapper.toDto(inputVersion);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InputVersionDTO> findByVersion(String ver) {
        log.debug("InputVersionDTO to get  DataVersions");
        InputVersionSpec dataVersionSpec = new InputVersionSpec(new SearchCriteria("version",":",ver));
        return inputVersionRepository.findOne(dataVersionSpec).map(inputVersionMapper::toDto);
    }
    /**
     * Get all the inputVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InputVersionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InputVersions");
        return inputVersionRepository.findAll(pageable)
            .map(inputVersionMapper::toDto);
    }


    /**
     * Get one inputVersion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InputVersionDTO> findOne(Long id) {
        log.debug("Request to get InputVersion : {}", id);
        return inputVersionRepository.findById(id)
            .map(inputVersionMapper::toDto);
    }

    /**
     * Delete the inputVersion by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InputVersion : {}", id);
        inputVersionRepository.deleteById(id);
    }
}
