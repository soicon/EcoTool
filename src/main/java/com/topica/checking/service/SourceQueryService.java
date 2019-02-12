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

import com.topica.checking.domain.Source;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.SourceRepository;
import com.topica.checking.service.dto.SourceCriteria;
import com.topica.checking.service.dto.SourceDTO;
import com.topica.checking.service.mapper.SourceMapper;

/**
 * Service for executing complex queries for Viewtool entities in the database.
 * The main input is a {@link SourceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SourceDTO} or a {@link Page} of {@link SourceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SourceQueryService extends QueryService<Source> {

    private final Logger log = LoggerFactory.getLogger(SourceQueryService.class);

    private final SourceRepository sourceRepository;

    private final SourceMapper sourceMapper;

    public SourceQueryService(SourceRepository sourceRepository, SourceMapper sourceMapper) {
        this.sourceRepository = sourceRepository;
        this.sourceMapper = sourceMapper;
    }

    /**
     * Return a {@link List} of {@link SourceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SourceDTO> findByCriteria(SourceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Source> specification = createSpecification(criteria);
        return sourceMapper.toDto(sourceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SourceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SourceDTO> findByCriteria(SourceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Source> specification = createSpecification(criteria);
        return sourceRepository.findAll(specification, page)
            .map(sourceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SourceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Source> specification = createSpecification(criteria);
        return sourceRepository.count(specification);
    }

    /**
     * Function to convert SourceCriteria to a {@link Specification}
     */
    private Specification<Source> createSpecification(SourceCriteria criteria) {
        Specification<Source> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Source_.id));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), Source_.path));
            }
            if (criteria.getDevice_id() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDevice_id(), Source_.device_id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), Source_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Source_.status));
            }
            if (criteria.getNeed_re_answer() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNeed_re_answer(), Source_.need_re_answer));
            }
            if (criteria.getQuestion_id() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuestion_id(), Source_.question_id));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(Source_.questions, JoinType.LEFT).get(Question_.id)));
            }
        }
        return specification;
    }
}
