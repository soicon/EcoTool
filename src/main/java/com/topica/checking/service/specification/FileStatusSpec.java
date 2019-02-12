package com.topica.checking.service.specification;

import com.topica.checking.domain.FileStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class FileStatusSpec implements Specification<FileStatus> {

    private SearchCriteria criteria;

    public FileStatusSpec(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(javax.persistence.criteria.Root<FileStatus> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return  PredicateSpec.toPredicate(root,criteriaQuery,criteriaBuilder,criteria);

    }
}
