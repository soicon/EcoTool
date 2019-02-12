package com.topica.checking.service;

import com.topica.checking.service.dto.RunnerLogDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RunnerLog.
 */
public interface RunnerLogService {

    /**
     * Save a runnerLog.
     *
     * @param runnerLogDTO the entity to save
     * @return the persisted entity
     */
    RunnerLogDTO save(RunnerLogDTO runnerLogDTO);

    void saveAll (List<RunnerLogDTO> runnerLogDTO);

    /**
     * Get all the runnerLogs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RunnerLogDTO> findAll(Pageable pageable);


    /**
     * Get the "id" runnerLog.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RunnerLogDTO> findOne(Long id);

    /**
     * Delete the "id" runnerLog.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
