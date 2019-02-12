package com.topica.checking.service.specification;

import com.topica.checking.domain.InputVersion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class InputVersionSpec implements Specification<InputVersion> {

    private SearchCriteria criteria;

    public InputVersionSpec(SearchCriteria criteria) {
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(javax.persistence.criteria.Root<InputVersion> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        return  PredicateSpec.toPredicate(root,criteriaQuery,builder,criteria);
    }
}
