package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.RunnerLogService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.RunnerLogDTO;
import com.topica.checking.service.dto.RunnerLogCriteria;
import com.topica.checking.service.RunnerLogQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RunnerLog.
 */
@RestController
@RequestMapping("/api")
public class RunnerLogResource {

    private final Logger log = LoggerFactory.getLogger(RunnerLogResource.class);

    private static final String ENTITY_NAME = "runnerLog";

    private final RunnerLogService runnerLogService;

    private final RunnerLogQueryService runnerLogQueryService;

    public RunnerLogResource(RunnerLogService runnerLogService, RunnerLogQueryService runnerLogQueryService) {
        this.runnerLogService = runnerLogService;
        this.runnerLogQueryService = runnerLogQueryService;
    }

    /**
     * POST  /runner-logs : Create a new runnerLog.
     *
     * @param runnerLogDTO the runnerLogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new runnerLogDTO, or with status 400 (Bad Request) if the runnerLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/runner-logs")
    @Timed
    public ResponseEntity<RunnerLogDTO> createRunnerLog(@RequestBody RunnerLogDTO runnerLogDTO) throws URISyntaxException {
        log.debug("REST request to save RunnerLog : {}", runnerLogDTO);
        if (runnerLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new runnerLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RunnerLogDTO result = runnerLogService.save(runnerLogDTO);
        return ResponseEntity.created(new URI("/api/runner-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /runner-logs : Updates an existing runnerLog.
     *
     * @param runnerLogDTO the runnerLogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated runnerLogDTO,
     * or with status 400 (Bad Request) if the runnerLogDTO is not valid,
     * or with status 500 (Internal Server Error) if the runnerLogDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/runner-logs")
    @Timed
    public ResponseEntity<RunnerLogDTO> updateRunnerLog(@RequestBody RunnerLogDTO runnerLogDTO) throws URISyntaxException {
        log.debug("REST request to update RunnerLog : {}", runnerLogDTO);
        if (runnerLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RunnerLogDTO result = runnerLogService.save(runnerLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, runnerLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /runner-logs : get all the runnerLogs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of runnerLogs in body
     */
    @GetMapping("/runner-logs")
    @Timed
    public ResponseEntity<List<RunnerLogDTO>> getAllRunnerLogs(RunnerLogCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RunnerLogs by criteria: {}", criteria);
        Page<RunnerLogDTO> page = runnerLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/runner-logs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /runner-logs/count : count all the runnerLogs.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/runner-logs/count")
    @Timed
    public ResponseEntity<Long> countRunnerLogs(RunnerLogCriteria criteria) {
        log.debug("REST request to count RunnerLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(runnerLogQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /runner-logs/:id : get the "id" runnerLog.
     *
     * @param id the id of the runnerLogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the runnerLogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/runner-logs/{id}")
    @Timed
    public ResponseEntity<RunnerLogDTO> getRunnerLog(@PathVariable Long id) {
        log.debug("REST request to get RunnerLog : {}", id);
        Optional<RunnerLogDTO> runnerLogDTO = runnerLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(runnerLogDTO);
    }

    /**
     * DELETE  /runner-logs/:id : delete the "id" runnerLog.
     *
     * @param id the id of the runnerLogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/runner-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRunnerLog(@PathVariable Long id) {
        log.debug("REST request to delete RunnerLog : {}", id);
        runnerLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
