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

import com.topica.checking.domain.RunnerLog;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.RunnerLogRepository;
import com.topica.checking.service.dto.RunnerLogCriteria;
import com.topica.checking.service.dto.RunnerLogDTO;
import com.topica.checking.service.mapper.RunnerLogMapper;

/**
 * Service for executing complex queries for RunnerLog entities in the database.
 * The main input is a {@link RunnerLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RunnerLogDTO} or a {@link Page} of {@link RunnerLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RunnerLogQueryService extends QueryService<RunnerLog> {

    private final Logger log = LoggerFactory.getLogger(RunnerLogQueryService.class);

    private final RunnerLogRepository runnerLogRepository;

    private final RunnerLogMapper runnerLogMapper;

    public RunnerLogQueryService(RunnerLogRepository runnerLogRepository, RunnerLogMapper runnerLogMapper) {
        this.runnerLogRepository = runnerLogRepository;
        this.runnerLogMapper = runnerLogMapper;
    }

    /**
     * Return a {@link List} of {@link RunnerLogDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RunnerLogDTO> findByCriteria(RunnerLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RunnerLog> specification = createSpecification(criteria);
        return runnerLogMapper.toDto(runnerLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RunnerLogDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RunnerLogDTO> findByCriteria(RunnerLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RunnerLog> specification = createSpecification(criteria);
        return runnerLogRepository.findAll(specification, page)
            .map(runnerLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RunnerLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RunnerLog> specification = createSpecification(criteria);
        return runnerLogRepository.count(specification);
    }

    /**
     * Function to convert RunnerLogCriteria to a {@link Specification}
     */
    private Specification<RunnerLog> createSpecification(RunnerLogCriteria criteria) {
        Specification<RunnerLog> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RunnerLog_.id));
            }
            if (criteria.getApiversionId() != null) {
                specification = specification.and(buildSpecification(criteria.getApiversionId(),
                    root -> root.join(RunnerLog_.apiversion, JoinType.LEFT).get(ApiVersion_.id)));
            }
            if (criteria.getDataversionId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataversionId(),
                    root -> root.join(RunnerLog_.dataversion, JoinType.LEFT).get(DataVersion_.id)));
            }
            if (criteria.getInputversionId() != null) {
                specification = specification.and(buildSpecification(criteria.getInputversionId(),
                    root -> root.join(RunnerLog_.inputversion, JoinType.LEFT).get(InputVersion_.id)));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildSpecification(criteria.getSourceId(),
                    root -> root.join(RunnerLog_.source, JoinType.LEFT).get(Source_.id)));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(RunnerLog_.question, JoinType.LEFT).get(Question_.id)));
            }
            if (criteria.getAnswerId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnswerId(),
                    root -> root.join(RunnerLog_.answer, JoinType.LEFT).get(Answer_.id)));
            }
        }
        return specification;
    }
}
