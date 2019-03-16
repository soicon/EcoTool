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

import com.topica.checking.domain.DataPrefix;
import com.topica.checking.domain.*; // for static metamodels
import com.topica.checking.repository.DataPrefixRepository;
import com.topica.checking.service.dto.DataPrefixCriteria;
import com.topica.checking.service.dto.DataPrefixDTO;
import com.topica.checking.service.mapper.DataPrefixMapper;

/**
 * Service for executing complex queries for DataPrefix entities in the database.
 * The main input is a {@link DataPrefixCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataPrefixDTO} or a {@link Page} of {@link DataPrefixDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataPrefixQueryService extends QueryService<DataPrefix> {

    private final Logger log = LoggerFactory.getLogger(DataPrefixQueryService.class);

    private final DataPrefixRepository dataPrefixRepository;

    private final DataPrefixMapper dataPrefixMapper;

    public DataPrefixQueryService(DataPrefixRepository dataPrefixRepository, DataPrefixMapper dataPrefixMapper) {
        this.dataPrefixRepository = dataPrefixRepository;
        this.dataPrefixMapper = dataPrefixMapper;
    }

    /**
     * Return a {@link List} of {@link DataPrefixDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataPrefixDTO> findByCriteria(DataPrefixCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataPrefix> specification = createSpecification(criteria);
        return dataPrefixMapper.toDto(dataPrefixRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DataPrefixDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataPrefixDTO> findByCriteria(DataPrefixCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataPrefix> specification = createSpecification(criteria);
        return dataPrefixRepository.findAll(specification, page)
            .map(dataPrefixMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataPrefixCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataPrefix> specification = createSpecification(criteria);
        return dataPrefixRepository.count(specification);
    }

    /**
     * Function to convert DataPrefixCriteria to a {@link Specification}
     */
    private Specification<DataPrefix> createSpecification(DataPrefixCriteria criteria) {
        Specification<DataPrefix> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataPrefix_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DataPrefix_.name));
            }
        }
        return specification;
    }
}
