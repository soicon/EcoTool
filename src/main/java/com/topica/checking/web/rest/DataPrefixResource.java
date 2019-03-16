package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.DataPrefixService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.DataPrefixDTO;
import com.topica.checking.service.dto.DataPrefixCriteria;
import com.topica.checking.service.DataPrefixQueryService;
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
 * REST controller for managing DataPrefix.
 */
@RestController
@RequestMapping("/api")
public class DataPrefixResource {

    private final Logger log = LoggerFactory.getLogger(DataPrefixResource.class);

    private static final String ENTITY_NAME = "dataPrefix";

    private final DataPrefixService dataPrefixService;

    private final DataPrefixQueryService dataPrefixQueryService;

    public DataPrefixResource(DataPrefixService dataPrefixService, DataPrefixQueryService dataPrefixQueryService) {
        this.dataPrefixService = dataPrefixService;
        this.dataPrefixQueryService = dataPrefixQueryService;
    }

    /**
     * POST  /data-prefixes : Create a new dataPrefix.
     *
     * @param dataPrefixDTO the dataPrefixDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataPrefixDTO, or with status 400 (Bad Request) if the dataPrefix has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-prefixes")
    @Timed
    public ResponseEntity<DataPrefixDTO> createDataPrefix(@RequestBody DataPrefixDTO dataPrefixDTO) throws URISyntaxException {
        log.debug("REST request to save DataPrefix : {}", dataPrefixDTO);
        if (dataPrefixDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataPrefix cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataPrefixDTO result = dataPrefixService.save(dataPrefixDTO);
        return ResponseEntity.created(new URI("/api/data-prefixes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-prefixes : Updates an existing dataPrefix.
     *
     * @param dataPrefixDTO the dataPrefixDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataPrefixDTO,
     * or with status 400 (Bad Request) if the dataPrefixDTO is not valid,
     * or with status 500 (Internal Server Error) if the dataPrefixDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-prefixes")
    @Timed
    public ResponseEntity<DataPrefixDTO> updateDataPrefix(@RequestBody DataPrefixDTO dataPrefixDTO) throws URISyntaxException {
        log.debug("REST request to update DataPrefix : {}", dataPrefixDTO);
        if (dataPrefixDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataPrefixDTO result = dataPrefixService.save(dataPrefixDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataPrefixDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-prefixes : get all the dataPrefixes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of dataPrefixes in body
     */
    @GetMapping("/data-prefixes")
    @Timed
    public ResponseEntity<List<DataPrefixDTO>> getAllDataPrefixes(DataPrefixCriteria criteria) {
        log.debug("REST request to get DataPrefixes by criteria: {}", criteria);
        List<DataPrefixDTO> page = dataPrefixQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(page);
    }

    /**
    * GET  /data-prefixes/count : count all the dataPrefixes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/data-prefixes/count")
    @Timed
    public ResponseEntity<Long> countDataPrefixes(DataPrefixCriteria criteria) {
        log.debug("REST request to count DataPrefixes by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataPrefixQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /data-prefixes/:id : get the "id" dataPrefix.
     *
     * @param id the id of the dataPrefixDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataPrefixDTO, or with status 404 (Not Found)
     */
    @GetMapping("/data-prefixes/{id}")
    @Timed
    public ResponseEntity<DataPrefixDTO> getDataPrefix(@PathVariable Long id) {
        log.debug("REST request to get DataPrefix : {}", id);
        Optional<DataPrefixDTO> dataPrefixDTO = dataPrefixService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataPrefixDTO);
    }

    /**
     * DELETE  /data-prefixes/:id : delete the "id" dataPrefix.
     *
     * @param id the id of the dataPrefixDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-prefixes/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataPrefix(@PathVariable Long id) {
        log.debug("REST request to delete DataPrefix : {}", id);
        dataPrefixService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
