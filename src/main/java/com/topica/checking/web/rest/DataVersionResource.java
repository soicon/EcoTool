package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.DataVersionService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.DataVersionDTO;
import com.topica.checking.service.dto.DataVersionCriteria;
import com.topica.checking.service.DataVersionQueryService;
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
 * REST controller for managing DataVersion.
 */
@RestController
@RequestMapping("/api")
public class DataVersionResource {

    private final Logger log = LoggerFactory.getLogger(DataVersionResource.class);

    private static final String ENTITY_NAME = "dataVersion";

    private final DataVersionService dataVersionService;

    private final DataVersionQueryService dataVersionQueryService;

    public DataVersionResource(DataVersionService dataVersionService, DataVersionQueryService dataVersionQueryService) {
        this.dataVersionService = dataVersionService;
        this.dataVersionQueryService = dataVersionQueryService;
    }

    /**
     * POST  /data-versions : Create a new dataVersion.
     *
     * @param dataVersionDTO the dataVersionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataVersionDTO, or with status 400 (Bad Request) if the dataVersion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-versions")
    @Timed
    public ResponseEntity<DataVersionDTO> createDataVersion(@RequestBody DataVersionDTO dataVersionDTO) throws URISyntaxException {
        log.debug("REST request to save DataVersion : {}", dataVersionDTO);
        if (dataVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataVersionDTO result = dataVersionService.save(dataVersionDTO);
        return ResponseEntity.created(new URI("/api/data-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-versions : Updates an existing dataVersion.
     *
     * @param dataVersionDTO the dataVersionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataVersionDTO,
     * or with status 400 (Bad Request) if the dataVersionDTO is not valid,
     * or with status 500 (Internal Server Error) if the dataVersionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-versions")
    @Timed
    public ResponseEntity<DataVersionDTO> updateDataVersion(@RequestBody DataVersionDTO dataVersionDTO) throws URISyntaxException {
        log.debug("REST request to update DataVersion : {}", dataVersionDTO);
        if (dataVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataVersionDTO result = dataVersionService.save(dataVersionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataVersionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-versions : get all the dataVersions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of dataVersions in body
     */
    @GetMapping("/data-versions")
    @Timed
    public ResponseEntity<List<DataVersionDTO>> getAllDataVersions(DataVersionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DataVersions by criteria: {}", criteria);
        Page<DataVersionDTO> page = dataVersionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-versions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /data-versions/count : count all the dataVersions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/data-versions/count")
    @Timed
    public ResponseEntity<Long> countDataVersions(DataVersionCriteria criteria) {
        log.debug("REST request to count DataVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataVersionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /data-versions/:id : get the "id" dataVersion.
     *
     * @param id the id of the dataVersionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataVersionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/data-versions/{id}")
    @Timed
    public ResponseEntity<DataVersionDTO> getDataVersion(@PathVariable Long id) {
        log.debug("REST request to get DataVersion : {}", id);
        Optional<DataVersionDTO> dataVersionDTO = dataVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataVersionDTO);
    }

    /**
     * DELETE  /data-versions/:id : delete the "id" dataVersion.
     *
     * @param id the id of the dataVersionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-versions/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataVersion(@PathVariable Long id) {
        log.debug("REST request to delete DataVersion : {}", id);
        dataVersionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
