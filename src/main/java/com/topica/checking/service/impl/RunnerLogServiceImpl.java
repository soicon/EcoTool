package com.topica.checking.service.impl;

import com.topica.checking.service.RunnerLogService;
import com.topica.checking.domain.RunnerLog;
import com.topica.checking.repository.RunnerLogRepository;
import com.topica.checking.service.dto.RunnerLogDTO;
import com.topica.checking.service.mapper.RunnerLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing RunnerLog.
 */
@Service
@Transactional
public class RunnerLogServiceImpl implements RunnerLogService {

    private final Logger log = LoggerFactory.getLogger(RunnerLogServiceImpl.class);

    private final RunnerLogRepository runnerLogRepository;

    private final RunnerLogMapper runnerLogMapper;

    public RunnerLogServiceImpl(RunnerLogRepository runnerLogRepository, RunnerLogMapper runnerLogMapper) {
        this.runnerLogRepository = runnerLogRepository;
        this.runnerLogMapper = runnerLogMapper;
    }

    /**
     * Save a runnerLog.
     *
     * @param runnerLogDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RunnerLogDTO save(RunnerLogDTO runnerLogDTO) {
        log.debug("Request to save RunnerLog : {}", runnerLogDTO);

        RunnerLog runnerLog = runnerLogMapper.toEntity(runnerLogDTO);
        runnerLog = runnerLogRepository.save(runnerLog);
        return runnerLogMapper.toDto(runnerLog);
    }

    @Override
    public void saveAll(List<RunnerLogDTO> runnerLogDTO) {
        log.debug("Request to save RunnerLog : {}", runnerLogDTO);

        List<RunnerLog> runnerLog = runnerLogMapper.toEntity(runnerLogDTO);
         runnerLogRepository.saveAll(runnerLog);
    }

    /**
     * Get all the runnerLogs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RunnerLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RunnerLogs");
        return runnerLogRepository.findAll(pageable)
            .map(runnerLogMapper::toDto);
    }


    /**
     * Get one runnerLog by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RunnerLogDTO> findOne(Long id) {
        log.debug("Request to get RunnerLog : {}", id);
        return runnerLogRepository.findById(id)
            .map(runnerLogMapper::toDto);
    }

    /**
     * Delete the runnerLog by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RunnerLog : {}", id);
        runnerLogRepository.deleteById(id);
    }
}
