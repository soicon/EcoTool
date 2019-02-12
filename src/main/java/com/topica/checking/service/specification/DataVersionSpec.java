package com.topica.checking.service.specification;

import com.topica.checking.domain.DataVersion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;


public class DataVersionSpec implements  Specification<DataVersion>{

    private SearchCriteria criteria;

    public DataVersionSpec(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(javax.persistence.criteria.Root<DataVersion> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return  PredicateSpec.toPredicate(root,criteriaQuery,criteriaBuilder,criteria);

    }


}
