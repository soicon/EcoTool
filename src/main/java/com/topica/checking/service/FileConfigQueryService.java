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

import com.topica.checking.domain.FileConfig;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.FileConfigRepository;
import com.topica.checking.service.dto.FileConfigCriteria;
import com.topica.checking.service.dto.FileConfigDTO;
import com.topica.checking.service.mapper.FileConfigMapper;

/**
 * Service for executing complex queries for FileConfig entities in the database.
 * The main input is a {@link FileConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileConfigDTO} or a {@link Page} of {@link FileConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileConfigQueryService extends QueryService<FileConfig> {

    private final Logger log = LoggerFactory.getLogger(FileConfigQueryService.class);

    private final FileConfigRepository fileConfigRepository;

    private final FileConfigMapper fileConfigMapper;

    public FileConfigQueryService(FileConfigRepository fileConfigRepository, FileConfigMapper fileConfigMapper) {
        this.fileConfigRepository = fileConfigRepository;
        this.fileConfigMapper = fileConfigMapper;
    }

    /**
     * Return a {@link List} of {@link FileConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileConfigDTO> findByCriteria(FileConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileConfig> specification = createSpecification(criteria);
        return fileConfigMapper.toDto(fileConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileConfigDTO> findByCriteria(FileConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileConfig> specification = createSpecification(criteria);
        return fileConfigRepository.findAll(specification, page)
            .map(fileConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileConfig> specification = createSpecification(criteria);
        return fileConfigRepository.count(specification);
    }

    /**
     * Function to convert FileConfigCriteria to a {@link Specification}
     */
    private Specification<FileConfig> createSpecification(FileConfigCriteria criteria) {
        Specification<FileConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FileConfig_.id));
            }
            if (criteria.getFileStatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getFileStatusId(),
                    root -> root.join(FileConfig_.fileStatus, JoinType.LEFT).get(FileStatus_.id)));
            }
        }
        return specification;
    }
}
