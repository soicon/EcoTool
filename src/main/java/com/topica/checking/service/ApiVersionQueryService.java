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

import com.topica.checking.domain.ApiVersion;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.ApiVersionRepository;
import com.topica.checking.service.dto.ApiVersionCriteria;
import com.topica.checking.service.dto.ApiVersionDTO;
import com.topica.checking.service.mapper.ApiVersionMapper;

/**
 * Service for executing complex queries for ApiVersion entities in the database.
 * The main input is a {@link ApiVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApiVersionDTO} or a {@link Page} of {@link ApiVersionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApiVersionQueryService extends QueryService<ApiVersion> {

    private final Logger log = LoggerFactory.getLogger(ApiVersionQueryService.class);

    private final ApiVersionRepository apiVersionRepository;

    private final ApiVersionMapper apiVersionMapper;

    public ApiVersionQueryService(ApiVersionRepository apiVersionRepository, ApiVersionMapper apiVersionMapper) {
        this.apiVersionRepository = apiVersionRepository;
        this.apiVersionMapper = apiVersionMapper;
    }

    /**
     * Return a {@link List} of {@link ApiVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApiVersionDTO> findByCriteria(ApiVersionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ApiVersion> specification = createSpecification(criteria);
        return apiVersionMapper.toDto(apiVersionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ApiVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApiVersionDTO> findByCriteria(ApiVersionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApiVersion> specification = createSpecification(criteria);
        return apiVersionRepository.findAll(specification, page)
            .map(apiVersionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApiVersionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ApiVersion> specification = createSpecification(criteria);
        return apiVersionRepository.count(specification);
    }

    /**
     * Function to convert ApiVersionCriteria to a {@link Specification}
     */
    private Specification<ApiVersion> createSpecification(ApiVersionCriteria criteria) {
        Specification<ApiVersion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ApiVersion_.id));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), ApiVersion_.version));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ApiVersion_.description));
            }
        }
        return specification;
    }
}
