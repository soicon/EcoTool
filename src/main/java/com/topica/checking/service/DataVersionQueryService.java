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

import com.topica.checking.domain.DataVersion;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.DataVersionRepository;
import com.topica.checking.service.dto.DataVersionCriteria;
import com.topica.checking.service.dto.DataVersionDTO;
import com.topica.checking.service.mapper.DataVersionMapper;

/**
 * Service for executing complex queries for DataVersion entities in the database.
 * The main input is a {@link DataVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataVersionDTO} or a {@link Page} of {@link DataVersionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataVersionQueryService extends QueryService<DataVersion> {

    private final Logger log = LoggerFactory.getLogger(DataVersionQueryService.class);

    private final DataVersionRepository dataVersionRepository;

    private final DataVersionMapper dataVersionMapper;

    public DataVersionQueryService(DataVersionRepository dataVersionRepository, DataVersionMapper dataVersionMapper) {
        this.dataVersionRepository = dataVersionRepository;
        this.dataVersionMapper = dataVersionMapper;
    }

    /**
     * Return a {@link List} of {@link DataVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataVersionDTO> findByCriteria(DataVersionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataVersion> specification = createSpecification(criteria);
        return dataVersionMapper.toDto(dataVersionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DataVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataVersionDTO> findByCriteria(DataVersionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataVersion> specification = createSpecification(criteria);
        return dataVersionRepository.findAll(specification, page)
            .map(dataVersionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataVersionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataVersion> specification = createSpecification(criteria);
        return dataVersionRepository.count(specification);
    }

    /**
     * Function to convert DataVersionCriteria to a {@link Specification}
     */
    private Specification<DataVersion> createSpecification(DataVersionCriteria criteria) {
        Specification<DataVersion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataVersion_.id));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), DataVersion_.version));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DataVersion_.description));
            }
            if (criteria.getVersionInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersionInfo(), DataVersion_.versionInfo));
            }
        }
        return specification;
    }
}
