package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.InputVersionService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.InputVersionDTO;
import com.topica.checking.service.dto.InputVersionCriteria;
import com.topica.checking.service.InputVersionQueryService;
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
 * REST controller for managing InputVersion.
 */
@RestController
@RequestMapping("/api")
public class InputVersionResource {

    private final Logger log = LoggerFactory.getLogger(InputVersionResource.class);

    private static final String ENTITY_NAME = "inputVersion";

    private final InputVersionService inputVersionService;

    private final InputVersionQueryService inputVersionQueryService;

    public InputVersionResource(InputVersionService inputVersionService, InputVersionQueryService inputVersionQueryService) {
        this.inputVersionService = inputVersionService;
        this.inputVersionQueryService = inputVersionQueryService;
    }

    /**
     * POST  /input-versions : Create a new inputVersion.
     *
     * @param inputVersionDTO the inputVersionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inputVersionDTO, or with status 400 (Bad Request) if the inputVersion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/input-versions")
    @Timed
    public ResponseEntity<InputVersionDTO> createInputVersion(@RequestBody InputVersionDTO inputVersionDTO) throws URISyntaxException {
        log.debug("REST request to save InputVersion : {}", inputVersionDTO);
        if (inputVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new inputVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InputVersionDTO result = inputVersionService.save(inputVersionDTO);
        return ResponseEntity.created(new URI("/api/input-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /input-versions : Updates an existing inputVersion.
     *
     * @param inputVersionDTO the inputVersionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inputVersionDTO,
     * or with status 400 (Bad Request) if the inputVersionDTO is not valid,
     * or with status 500 (Internal Server Error) if the inputVersionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/input-versions")
    @Timed
    public ResponseEntity<InputVersionDTO> updateInputVersion(@RequestBody InputVersionDTO inputVersionDTO) throws URISyntaxException {
        log.debug("REST request to update InputVersion : {}", inputVersionDTO);
        if (inputVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InputVersionDTO result = inputVersionService.save(inputVersionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inputVersionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /input-versions : get all the inputVersions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of inputVersions in body
     */
    @GetMapping("/input-versions")
    @Timed
    public ResponseEntity<List<InputVersionDTO>> getAllInputVersions(InputVersionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InputVersions by criteria: {}", criteria);
        Page<InputVersionDTO> page = inputVersionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/input-versions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /input-versions/count : count all the inputVersions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/input-versions/count")
    @Timed
    public ResponseEntity<Long> countInputVersions(InputVersionCriteria criteria) {
        log.debug("REST request to count InputVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(inputVersionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /input-versions/:id : get the "id" inputVersion.
     *
     * @param id the id of the inputVersionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inputVersionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/input-versions/{id}")
    @Timed
    public ResponseEntity<InputVersionDTO> getInputVersion(@PathVariable Long id) {
        log.debug("REST request to get InputVersion : {}", id);
        Optional<InputVersionDTO> inputVersionDTO = inputVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inputVersionDTO);
    }

    /**
     * DELETE  /input-versions/:id : delete the "id" inputVersion.
     *
     * @param id the id of the inputVersionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/input-versions/{id}")
    @Timed
    public ResponseEntity<Void> deleteInputVersion(@PathVariable Long id) {
        log.debug("REST request to delete InputVersion : {}", id);
        inputVersionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
