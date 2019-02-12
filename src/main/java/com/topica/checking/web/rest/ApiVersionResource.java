package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.ApiVersionService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.ApiVersionDTO;
import com.topica.checking.service.dto.ApiVersionCriteria;
import com.topica.checking.service.ApiVersionQueryService;
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
 * REST controller for managing ApiVersion.
 */
@RestController
@RequestMapping("/api")
public class ApiVersionResource {

    private final Logger log = LoggerFactory.getLogger(ApiVersionResource.class);

    private static final String ENTITY_NAME = "apiVersion";

    private final ApiVersionService apiVersionService;

    private final ApiVersionQueryService apiVersionQueryService;

    public ApiVersionResource(ApiVersionService apiVersionService, ApiVersionQueryService apiVersionQueryService) {
        this.apiVersionService = apiVersionService;
        this.apiVersionQueryService = apiVersionQueryService;
    }

    /**
     * POST  /api-versions : Create a new apiVersion.
     *
     * @param apiVersionDTO the apiVersionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apiVersionDTO, or with status 400 (Bad Request) if the apiVersion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api-versions")
    @Timed
    public ResponseEntity<ApiVersionDTO> createApiVersion(@RequestBody ApiVersionDTO apiVersionDTO) throws URISyntaxException {
        log.debug("REST request to save ApiVersion : {}", apiVersionDTO);
        if (apiVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new apiVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiVersionDTO result = apiVersionService.save(apiVersionDTO);
        return ResponseEntity.created(new URI("/api/api-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /api-versions : Updates an existing apiVersion.
     *
     * @param apiVersionDTO the apiVersionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apiVersionDTO,
     * or with status 400 (Bad Request) if the apiVersionDTO is not valid,
     * or with status 500 (Internal Server Error) if the apiVersionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api-versions")
    @Timed
    public ResponseEntity<ApiVersionDTO> updateApiVersion(@RequestBody ApiVersionDTO apiVersionDTO) throws URISyntaxException {
        log.debug("REST request to update ApiVersion : {}", apiVersionDTO);
        if (apiVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiVersionDTO result = apiVersionService.save(apiVersionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, apiVersionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /api-versions : get all the apiVersions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of apiVersions in body
     */
    @GetMapping("/api-versions")
    @Timed
    public ResponseEntity<List<ApiVersionDTO>> getAllApiVersions(ApiVersionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ApiVersions by criteria: {}", criteria);
        Page<ApiVersionDTO> page = apiVersionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/api-versions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /api-versions/count : count all the apiVersions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/api-versions/count")
    @Timed
    public ResponseEntity<Long> countApiVersions(ApiVersionCriteria criteria) {
        log.debug("REST request to count ApiVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(apiVersionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /api-versions/:id : get the "id" apiVersion.
     *
     * @param id the id of the apiVersionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apiVersionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/api-versions/{id}")
    @Timed
    public ResponseEntity<ApiVersionDTO> getApiVersion(@PathVariable Long id) {
        log.debug("REST request to get ApiVersion : {}", id);
        Optional<ApiVersionDTO> apiVersionDTO = apiVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiVersionDTO);
    }

    /**
     * DELETE  /api-versions/:id : delete the "id" apiVersion.
     *
     * @param id the id of the apiVersionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api-versions/{id}")
    @Timed
    public ResponseEntity<Void> deleteApiVersion(@PathVariable Long id) {
        log.debug("REST request to delete ApiVersion : {}", id);
        apiVersionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
