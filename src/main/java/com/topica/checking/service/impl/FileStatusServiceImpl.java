package com.topica.checking.service.impl;

import com.topica.checking.service.FileStatusService;
import com.topica.checking.domain.FileStatus;
import com.topica.checking.repository.FileStatusRepository;
import com.topica.checking.service.dto.FileStatusDTO;
import com.topica.checking.service.mapper.FileStatusMapper;
import com.topica.checking.service.specification.FileStatusSpec;
import com.topica.checking.service.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing FileStatus.
 */
@Service
@Transactional
public class FileStatusServiceImpl implements FileStatusService {

    private final Logger log = LoggerFactory.getLogger(FileStatusServiceImpl.class);

    private final FileStatusRepository fileStatusRepository;

    private final FileStatusMapper fileStatusMapper;

    public FileStatusServiceImpl(FileStatusRepository fileStatusRepository, FileStatusMapper fileStatusMapper) {
        this.fileStatusRepository = fileStatusRepository;
        this.fileStatusMapper = fileStatusMapper;
    }

    /**
     * Save a fileStatus.
     *
     * @param fileStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FileStatusDTO save(FileStatusDTO fileStatusDTO) {
        log.debug("Request to save FileStatus : {}", fileStatusDTO);

        FileStatus fileStatus = fileStatusMapper.toEntity(fileStatusDTO);
        fileStatus = fileStatusRepository.save(fileStatus);
        return fileStatusMapper.toDto(fileStatus);
    }

    /**
     * Get all the fileStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FileStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileStatuses");
        return fileStatusRepository.findAll(pageable)
            .map(fileStatusMapper::toDto);
    }


    /**
     * Get one fileStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FileStatusDTO> findOne(Long id) {
        log.debug("Request to get FileStatus : {}", id);
        return fileStatusRepository.findById(id)
            .map(fileStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileStatusDTO> findByName(String ver) {
        log.debug("Request to get  DataVersions");
        FileStatusSpec dataVersionSpec = new FileStatusSpec(new SearchCriteria("name",":",ver));
        return fileStatusRepository.findOne(dataVersionSpec).map(fileStatusMapper::toDto);
    }

    /**
     * Delete the fileStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileStatus : {}", id);
        fileStatusRepository.deleteById(id);
    }
}
