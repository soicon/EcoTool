package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.CorePrefixService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.CorePrefixDTO;
import com.topica.checking.service.dto.CorePrefixCriteria;
import com.topica.checking.service.CorePrefixQueryService;
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
 * REST controller for managing CorePrefix.
 */
@RestController
@RequestMapping("/api")
public class CorePrefixResource {

    private final Logger log = LoggerFactory.getLogger(CorePrefixResource.class);

    private static final String ENTITY_NAME = "corePrefix";

    private final CorePrefixService corePrefixService;

    private final CorePrefixQueryService corePrefixQueryService;

    public CorePrefixResource(CorePrefixService corePrefixService, CorePrefixQueryService corePrefixQueryService) {
        this.corePrefixService = corePrefixService;
        this.corePrefixQueryService = corePrefixQueryService;
    }

    /**
     * POST  /core-prefixes : Create a new corePrefix.
     *
     * @param corePrefixDTO the corePrefixDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corePrefixDTO, or with status 400 (Bad Request) if the corePrefix has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/core-prefixes")
    @Timed
    public ResponseEntity<CorePrefixDTO> createCorePrefix(@RequestBody CorePrefixDTO corePrefixDTO) throws URISyntaxException {
        log.debug("REST request to save CorePrefix : {}", corePrefixDTO);
        if (corePrefixDTO.getId() != null) {
            throw new BadRequestAlertException("A new corePrefix cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorePrefixDTO result = corePrefixService.save(corePrefixDTO);
        return ResponseEntity.created(new URI("/api/core-prefixes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /core-prefixes : Updates an existing corePrefix.
     *
     * @param corePrefixDTO the corePrefixDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corePrefixDTO,
     * or with status 400 (Bad Request) if the corePrefixDTO is not valid,
     * or with status 500 (Internal Server Error) if the corePrefixDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/core-prefixes")
    @Timed
    public ResponseEntity<CorePrefixDTO> updateCorePrefix(@RequestBody CorePrefixDTO corePrefixDTO) throws URISyntaxException {
        log.debug("REST request to update CorePrefix : {}", corePrefixDTO);
        if (corePrefixDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CorePrefixDTO result = corePrefixService.save(corePrefixDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, corePrefixDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /core-prefixes : get all the corePrefixes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of corePrefixes in body
     */
    @GetMapping("/core-prefixes")
    @Timed
    public ResponseEntity<List<CorePrefixDTO>> getAllCorePrefixes(CorePrefixCriteria criteria) {
        log.debug("REST request to get CorePrefixes by criteria: {}", criteria);
        List<CorePrefixDTO> page = corePrefixQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(page);
    }

    /**
    * GET  /core-prefixes/count : count all the corePrefixes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/core-prefixes/count")
    @Timed
    public ResponseEntity<Long> countCorePrefixes(CorePrefixCriteria criteria) {
        log.debug("REST request to count CorePrefixes by criteria: {}", criteria);
        return ResponseEntity.ok().body(corePrefixQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /core-prefixes/:id : get the "id" corePrefix.
     *
     * @param id the id of the corePrefixDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corePrefixDTO, or with status 404 (Not Found)
     */
    @GetMapping("/core-prefixes/{id}")
    @Timed
    public ResponseEntity<CorePrefixDTO> getCorePrefix(@PathVariable Long id) {
        log.debug("REST request to get CorePrefix : {}", id);
        Optional<CorePrefixDTO> corePrefixDTO = corePrefixService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corePrefixDTO);
    }

    /**
     * DELETE  /core-prefixes/:id : delete the "id" corePrefix.
     *
     * @param id the id of the corePrefixDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/core-prefixes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorePrefix(@PathVariable Long id) {
        log.debug("REST request to delete CorePrefix : {}", id);
        corePrefixService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
