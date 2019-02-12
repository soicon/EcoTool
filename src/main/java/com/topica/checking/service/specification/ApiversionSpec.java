package com.topica.checking.service.specification;

import com.topica.checking.domain.ApiVersion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class ApiversionSpec implements Specification<ApiVersion> {

    private SearchCriteria criteria;

    public ApiversionSpec(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(javax.persistence.criteria.Root<ApiVersion> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return  PredicateSpec.toPredicate(root,criteriaQuery,criteriaBuilder,criteria);

    }
}
