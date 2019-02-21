package com.topica.checking.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.topica.checking.domain.FileStatus;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.FileStatusRepository;
import com.topica.checking.service.dto.FileStatusCriteria;
import com.topica.checking.service.dto.FileStatusDTO;
import com.topica.checking.service.mapper.FileStatusMapper;

/**
 * Service for executing complex queries for FileStatus entities in the database.
 * The main input is a {@link FileStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileStatusDTO} or a {@link Page} of {@link FileStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileStatusQueryService extends QueryService<FileStatus> {

    private final Logger log = LoggerFactory.getLogger(FileStatusQueryService.class);

    private final FileStatusRepository fileStatusRepository;

    private final FileStatusMapper fileStatusMapper;

    public FileStatusQueryService(FileStatusRepository fileStatusRepository, FileStatusMapper fileStatusMapper) {
        this.fileStatusRepository = fileStatusRepository;
        this.fileStatusMapper = fileStatusMapper;
    }

    /**
     * Return a {@link List} of {@link FileStatusDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileStatusDTO> findByCriteria(FileStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileStatus> specification = createSpecification(criteria);
        return fileStatusMapper.toDto(fileStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileStatusDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileStatusDTO> findByCriteria(FileStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileStatus> specification = createSpecification(criteria);
        return fileStatusRepository.findAll(specification, page)
            .map(fileStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileStatus> specification = createSpecification(criteria);
        return fileStatusRepository.count(specification);
    }

    /**
     * Function to convert FileStatusCriteria to a {@link Specification}
     */
    private Specification<FileStatus> createSpecification(FileStatusCriteria criteria) {
        Specification<FileStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FileStatus_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FileStatus_.name));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), FileStatus_.url));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResult(), FileStatus_.result));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), FileStatus_.status));
            }
            if (criteria.getDownload_result_url() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDownload_result_url(), FileStatus_.download_result_url));
            }
            if (criteria.getFileType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileType(), FileStatus_.fileType));
            }
            if (criteria.getVersionInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersionInfo(), FileStatus_.versionInfo));
            }
        }
        return specification;
    }
}
