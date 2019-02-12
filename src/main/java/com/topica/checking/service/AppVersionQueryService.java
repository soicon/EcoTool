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

import com.topica.checking.domain.AppVersion;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.AppVersionRepository;
import com.topica.checking.service.dto.AppVersionCriteria;
import com.topica.checking.service.dto.AppVersionDTO;
import com.topica.checking.service.mapper.AppVersionMapper;

/**
 * Service for executing complex queries for AppVersion entities in the database.
 * The main input is a {@link AppVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppVersionDTO} or a {@link Page} of {@link AppVersionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppVersionQueryService extends QueryService<AppVersion> {

    private final Logger log = LoggerFactory.getLogger(AppVersionQueryService.class);

    private final AppVersionRepository appVersionRepository;

    private final AppVersionMapper appVersionMapper;

    public AppVersionQueryService(AppVersionRepository appVersionRepository, AppVersionMapper appVersionMapper) {
        this.appVersionRepository = appVersionRepository;
        this.appVersionMapper = appVersionMapper;
    }

    /**
     * Return a {@link List} of {@link AppVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppVersionDTO> findByCriteria(AppVersionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppVersion> specification = createSpecification(criteria);
        return appVersionMapper.toDto(appVersionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppVersionDTO> findByCriteria(AppVersionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppVersion> specification = createSpecification(criteria);
        return appVersionRepository.findAll(specification, page)
            .map(appVersionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppVersionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppVersion> specification = createSpecification(criteria);
        return appVersionRepository.count(specification);
    }

    /**
     * Function to convert AppVersionCriteria to a {@link Specification}
     */
    private Specification<AppVersion> createSpecification(AppVersionCriteria criteria) {
        Specification<AppVersion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AppVersion_.id));
            }
            if (criteria.getApiVer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApiVer(), AppVersion_.apiVer));
            }
            if (criteria.getDataVer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataVer(), AppVersion_.dataVer));
            }
            if (criteria.getInputVer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInputVer(), AppVersion_.inputVer));
            }
        }
        return specification;
    }
}
