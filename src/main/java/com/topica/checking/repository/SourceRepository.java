package com.topica.checking.repository;

import com.topica.checking.domain.Source;
import com.topica.checking.service.dto.SourceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Viewtool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceRepository extends JpaRepository<Source, Long>, JpaSpecificationExecutor<Source> {

    @Query("SELECT s.id, s.path, q.question_text, a.answer_text FROM Source s " +
        "inner join Question q on s.id = q.source and s.id = :id " +
        "inner join Answer a on a.question = q.id")
    List<Object> selectSource(@Param("id") long id);


    @Query(value = "SELECT distinct  s.* " +
        "FROM source  s " +
        "left join question q on s.id = q.source_id " +
        "inner join answer a on a.question_id = q.id",
        countQuery = "SELECT  count(distinct s.id)" +
            "FROM source  s " +
            "left join question q on s.id = q.source_id " +
            "inner join answer a on a.question_id = q.id",
    nativeQuery = true)
    Page<Source> filter(Pageable pageable);
}
