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

import com.topica.checking.domain.CorePrefix;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.CorePrefixRepository;
import com.topica.checking.service.dto.CorePrefixCriteria;
import com.topica.checking.service.dto.CorePrefixDTO;
import com.topica.checking.service.mapper.CorePrefixMapper;

/**
 * Service for executing complex queries for CorePrefix entities in the database.
 * The main input is a {@link CorePrefixCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorePrefixDTO} or a {@link Page} of {@link CorePrefixDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorePrefixQueryService extends QueryService<CorePrefix> {

    private final Logger log = LoggerFactory.getLogger(CorePrefixQueryService.class);

    private final CorePrefixRepository corePrefixRepository;

    private final CorePrefixMapper corePrefixMapper;

    public CorePrefixQueryService(CorePrefixRepository corePrefixRepository, CorePrefixMapper corePrefixMapper) {
        this.corePrefixRepository = corePrefixRepository;
        this.corePrefixMapper = corePrefixMapper;
    }

    /**
     * Return a {@link List} of {@link CorePrefixDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorePrefixDTO> findByCriteria(CorePrefixCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CorePrefix> specification = createSpecification(criteria);
        return corePrefixMapper.toDto(corePrefixRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorePrefixDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorePrefixDTO> findByCriteria(CorePrefixCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CorePrefix> specification = createSpecification(criteria);
        return corePrefixRepository.findAll(specification, page)
            .map(corePrefixMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorePrefixCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CorePrefix> specification = createSpecification(criteria);
        return corePrefixRepository.count(specification);
    }

    /**
     * Function to convert CorePrefixCriteria to a {@link Specification}
     */
    private Specification<CorePrefix> createSpecification(CorePrefixCriteria criteria) {
        Specification<CorePrefix> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CorePrefix_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CorePrefix_.name));
            }
        }
        return specification;
    }
}
