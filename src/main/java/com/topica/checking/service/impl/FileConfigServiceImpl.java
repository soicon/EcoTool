package com.topica.checking.service.impl;

import com.topica.checking.service.FileConfigService;
import com.topica.checking.domain.FileConfig;
import com.topica.checking.repository.FileConfigRepository;
import com.topica.checking.service.dto.FileConfigDTO;
import com.topica.checking.service.mapper.FileConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing FileConfig.
 */
@Service
@Transactional
public class FileConfigServiceImpl implements FileConfigService {

    private final Logger log = LoggerFactory.getLogger(FileConfigServiceImpl.class);

    private final FileConfigRepository fileConfigRepository;

    private final FileConfigMapper fileConfigMapper;

    public FileConfigServiceImpl(FileConfigRepository fileConfigRepository, FileConfigMapper fileConfigMapper) {
        this.fileConfigRepository = fileConfigRepository;
        this.fileConfigMapper = fileConfigMapper;
    }

    /**
     * Save a fileConfig.
     *
     * @param fileConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FileConfigDTO save(FileConfigDTO fileConfigDTO) {
        log.debug("Request to save FileConfig : {}", fileConfigDTO);

        FileConfig fileConfig = fileConfigMapper.toEntity(fileConfigDTO);
        fileConfig = fileConfigRepository.save(fileConfig);
        return fileConfigMapper.toDto(fileConfig);
    }

    /**
     * Get all the fileConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FileConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileConfigs");
        return fileConfigRepository.findAll(pageable)
            .map(fileConfigMapper::toDto);
    }


    /**
     * Get one fileConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FileConfigDTO> findOne(Long id) {
        log.debug("Request to get FileConfig : {}", id);
        return fileConfigRepository.findById(id)
            .map(fileConfigMapper::toDto);
    }

    /**
     * Delete the fileConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileConfig : {}", id);
        fileConfigRepository.deleteById(id);
    }
}
