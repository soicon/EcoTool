package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.AppVersionService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.AppVersionDTO;
import com.topica.checking.service.dto.AppVersionCriteria;
import com.topica.checking.service.AppVersionQueryService;
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
 * REST controller for managing AppVersion.
 */
@RestController
@RequestMapping("/api")
public class AppVersionResource {

    private final Logger log = LoggerFactory.getLogger(AppVersionResource.class);

    private static final String ENTITY_NAME = "appVersion";

    private final AppVersionService appVersionService;

    private final AppVersionQueryService appVersionQueryService;

    public AppVersionResource(AppVersionService appVersionService, AppVersionQueryService appVersionQueryService) {
        this.appVersionService = appVersionService;
        this.appVersionQueryService = appVersionQueryService;
    }

    /**
     * POST  /app-versions : Create a new appVersion.
     *
     * @param appVersionDTO the appVersionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appVersionDTO, or with status 400 (Bad Request) if the appVersion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/app-versions")
    @Timed
    public ResponseEntity<AppVersionDTO> createAppVersion(@RequestBody AppVersionDTO appVersionDTO) throws URISyntaxException {
        log.debug("REST request to save AppVersion : {}", appVersionDTO);
        if (appVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new appVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppVersionDTO result = appVersionService.save(appVersionDTO);
        return ResponseEntity.created(new URI("/api/app-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-versions : Updates an existing appVersion.
     *
     * @param appVersionDTO the appVersionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appVersionDTO,
     * or with status 400 (Bad Request) if the appVersionDTO is not valid,
     * or with status 500 (Internal Server Error) if the appVersionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/app-versions")
    @Timed
    public ResponseEntity<AppVersionDTO> updateAppVersion(@RequestBody AppVersionDTO appVersionDTO) throws URISyntaxException {
        log.debug("REST request to update AppVersion : {}", appVersionDTO);
        if (appVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppVersionDTO result = appVersionService.save(appVersionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appVersionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-versions : get all the appVersions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of appVersions in body
     */
    @GetMapping("/app-versions")
    @Timed
    public ResponseEntity<List<AppVersionDTO>> getAllAppVersions(AppVersionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AppVersions by criteria: {}", criteria);
        Page<AppVersionDTO> page = appVersionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/app-versions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /app-versions/count : count all the appVersions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/app-versions/count")
    @Timed
    public ResponseEntity<Long> countAppVersions(AppVersionCriteria criteria) {
        log.debug("REST request to count AppVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(appVersionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /app-versions/:id : get the "id" appVersion.
     *
     * @param id the id of the appVersionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appVersionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/app-versions/{id}")
    @Timed
    public ResponseEntity<AppVersionDTO> getAppVersion(@PathVariable Long id) {
        log.debug("REST request to get AppVersion : {}", id);
        Optional<AppVersionDTO> appVersionDTO = appVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appVersionDTO);
    }

    /**
     * DELETE  /app-versions/:id : delete the "id" appVersion.
     *
     * @param id the id of the appVersionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/app-versions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppVersion(@PathVariable Long id) {
        log.debug("REST request to delete AppVersion : {}", id);
        appVersionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
