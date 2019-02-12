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

import com.topica.checking.domain.InputVersion;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.InputVersionRepository;
import com.topica.checking.service.dto.InputVersionCriteria;
import com.topica.checking.service.dto.InputVersionDTO;
import com.topica.checking.service.mapper.InputVersionMapper;

/**
 * Service for executing complex queries for InputVersion entities in the database.
 * The main input is a {@link InputVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InputVersionDTO} or a {@link Page} of {@link InputVersionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InputVersionQueryService extends QueryService<InputVersion> {

    private final Logger log = LoggerFactory.getLogger(InputVersionQueryService.class);

    private final InputVersionRepository inputVersionRepository;

    private final InputVersionMapper inputVersionMapper;

    public InputVersionQueryService(InputVersionRepository inputVersionRepository, InputVersionMapper inputVersionMapper) {
        this.inputVersionRepository = inputVersionRepository;
        this.inputVersionMapper = inputVersionMapper;
    }

    /**
     * Return a {@link List} of {@link InputVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InputVersionDTO> findByCriteria(InputVersionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InputVersion> specification = createSpecification(criteria);
        return inputVersionMapper.toDto(inputVersionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InputVersionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InputVersionDTO> findByCriteria(InputVersionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InputVersion> specification = createSpecification(criteria);
        return inputVersionRepository.findAll(specification, page)
            .map(inputVersionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InputVersionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InputVersion> specification = createSpecification(criteria);
        return inputVersionRepository.count(specification);
    }

    /**
     * Function to convert InputVersionCriteria to a {@link Specification}
     */
    private Specification<InputVersion> createSpecification(InputVersionCriteria criteria) {
        Specification<InputVersion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InputVersion_.id));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), InputVersion_.version));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), InputVersion_.description));
            }
        }
        return specification;
    }
}
