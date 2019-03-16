package com.topica.checking.service.specification;

import com.topica.checking.domain.ApiVersion;
import com.topica.checking.domain.CorePrefix;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class CoreSpec implements Specification<CorePrefix> {

    private SearchCriteria criteria;

    public CoreSpec(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(javax.persistence.criteria.Root<CorePrefix> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return  PredicateSpec.toPredicate(root,criteriaQuery,criteriaBuilder,criteria);

    }
}
