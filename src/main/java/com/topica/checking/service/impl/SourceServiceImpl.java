package com.topica.checking.service.impl;

import com.topica.checking.service.SourceService;
import com.topica.checking.domain.Source;
import com.topica.checking.repository.SourceRepository;
import com.topica.checking.service.dto.SourceDTO;
import com.topica.checking.service.mapper.SourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Viewtool.
 */
@Service
@Transactional
public class SourceServiceImpl implements SourceService {

    private final Logger log = LoggerFactory.getLogger(SourceServiceImpl.class);

    private final SourceRepository sourceRepository;

    private final SourceMapper sourceMapper;

    public SourceServiceImpl(SourceRepository sourceRepository, SourceMapper sourceMapper) {
        this.sourceRepository = sourceRepository;
        this.sourceMapper = sourceMapper;
    }

    /**
     * Save a source.
     *
     * @param sourceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SourceDTO save(SourceDTO sourceDTO) {
        log.debug("Request to save source : {}", sourceDTO);

        Source source = sourceMapper.toEntity(sourceDTO);
        source = sourceRepository.save(source);
        return sourceMapper.toDto(source);
    }

    @Override
    public List<Object> findQA(long sourceDTO) {
        return sourceRepository.selectSource(sourceDTO);
    }

    /**
     * Get all the sources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sources");
        return sourceRepository.findAll(pageable)
            .map(sourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourceDTO> filter(Pageable pageable) {
        log.debug("Request to get all Sources");
        return sourceRepository.filter(pageable)
            .map(sourceMapper::toDto);
    }


    /**
     * Get one source by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SourceDTO> findOne(Long id) {
        log.debug("Request to get Viewtool : {}", id);
        return sourceRepository.findById(id)
            .map(sourceMapper::toDto);
    }

    /**
     * Delete the source by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Viewtool : {}", id);
        sourceRepository.deleteById(id);
    }
}
