package com.topica.checking.service.impl;

import com.topica.checking.service.DataPrefixService;
import com.topica.checking.domain.DataPrefix;
import com.topica.checking.repository.DataPrefixRepository;
import com.topica.checking.service.dto.DataPrefixDTO;
import com.topica.checking.service.mapper.DataPrefixMapper;
import com.topica.checking.service.specification.DataPrefixSpec;
import com.topica.checking.service.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DataPrefix.
 */
@Service
@Transactional
public class DataPrefixServiceImpl implements DataPrefixService {

    private final Logger log = LoggerFactory.getLogger(DataPrefixServiceImpl.class);

    private final DataPrefixRepository dataPrefixRepository;

    private final DataPrefixMapper dataPrefixMapper;

    public DataPrefixServiceImpl(DataPrefixRepository dataPrefixRepository, DataPrefixMapper dataPrefixMapper) {
        this.dataPrefixRepository = dataPrefixRepository;
        this.dataPrefixMapper = dataPrefixMapper;
    }

    /**
     * Save a dataPrefix.
     *
     * @param dataPrefixDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DataPrefixDTO save(DataPrefixDTO dataPrefixDTO) {
        log.debug("Request to save DataPrefix : {}", dataPrefixDTO);

        DataPrefix dataPrefix = dataPrefixMapper.toEntity(dataPrefixDTO);
        dataPrefix = dataPrefixRepository.save(dataPrefix);
        return dataPrefixMapper.toDto(dataPrefix);
    }

    /**
     * Get all the dataPrefixes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DataPrefixDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataPrefixes");
        return dataPrefixRepository.findAll(pageable)
            .map(dataPrefixMapper::toDto);
    }



    @Override
    @Transactional(readOnly = true)
    public Optional<DataPrefixDTO> findByName(String ver) {
        log.debug("InputVersionDTO to get  DataVersions");
        DataPrefixSpec dataVersionSpec = new DataPrefixSpec(new SearchCriteria("name",":",ver));
        return dataPrefixRepository.findOne(dataVersionSpec).map(dataPrefixMapper::toDto);
    }
    /**
     * Get one dataPrefix by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DataPrefixDTO> findOne(Long id) {
        log.debug("Request to get DataPrefix : {}", id);
        return dataPrefixRepository.findById(id)
            .map(dataPrefixMapper::toDto);
    }

    /**
     * Delete the dataPrefix by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataPrefix : {}", id);
        dataPrefixRepository.deleteById(id);
    }
}
