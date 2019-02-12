package com.topica.checking.service;

import com.topica.checking.service.dto.QuestionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Question.
 */
public interface QuestionService {

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuestionDTO> findAll(Pageable pageable);
    /**
     * Get all the QuestionDTO where Answer is null.
     *
     * @return the list of entities
     */
    List<QuestionDTO> findAllWhereAnswerIsNull();


    /**
     * Get the "id" question.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<QuestionDTO> findOne(Long id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
