package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.FileConfigService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.FileConfigDTO;
import com.topica.checking.service.dto.FileConfigCriteria;
import com.topica.checking.service.FileConfigQueryService;
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
 * REST controller for managing FileConfig.
 */
@RestController
@RequestMapping("/api")
public class FileConfigResource {

    private final Logger log = LoggerFactory.getLogger(FileConfigResource.class);

    private static final String ENTITY_NAME = "fileConfig";

    private final FileConfigService fileConfigService;

    private final FileConfigQueryService fileConfigQueryService;

    public FileConfigResource(FileConfigService fileConfigService, FileConfigQueryService fileConfigQueryService) {
        this.fileConfigService = fileConfigService;
        this.fileConfigQueryService = fileConfigQueryService;
    }

    /**
     * POST  /file-configs : Create a new fileConfig.
     *
     * @param fileConfigDTO the fileConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileConfigDTO, or with status 400 (Bad Request) if the fileConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/file-configs")
    @Timed
    public ResponseEntity<FileConfigDTO> createFileConfig(@RequestBody FileConfigDTO fileConfigDTO) throws URISyntaxException {
        log.debug("REST request to save FileConfig : {}", fileConfigDTO);
        if (fileConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileConfigDTO result = fileConfigService.save(fileConfigDTO);
        return ResponseEntity.created(new URI("/api/file-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-configs : Updates an existing fileConfig.
     *
     * @param fileConfigDTO the fileConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileConfigDTO,
     * or with status 400 (Bad Request) if the fileConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the fileConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/file-configs")
    @Timed
    public ResponseEntity<FileConfigDTO> updateFileConfig(@RequestBody FileConfigDTO fileConfigDTO) throws URISyntaxException {
        log.debug("REST request to update FileConfig : {}", fileConfigDTO);
        if (fileConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileConfigDTO result = fileConfigService.save(fileConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-configs : get all the fileConfigs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of fileConfigs in body
     */
    @GetMapping("/file-configs")
    @Timed
    public ResponseEntity<List<FileConfigDTO>> getAllFileConfigs(FileConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FileConfigs by criteria: {}", criteria);
        Page<FileConfigDTO> page = fileConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/file-configs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /file-configs/count : count all the fileConfigs.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/file-configs/count")
    @Timed
    public ResponseEntity<Long> countFileConfigs(FileConfigCriteria criteria) {
        log.debug("REST request to count FileConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileConfigQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /file-configs/:id : get the "id" fileConfig.
     *
     * @param id the id of the fileConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/file-configs/{id}")
    @Timed
    public ResponseEntity<FileConfigDTO> getFileConfig(@PathVariable Long id) {
        log.debug("REST request to get FileConfig : {}", id);
        Optional<FileConfigDTO> fileConfigDTO = fileConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileConfigDTO);
    }

    /**
     * DELETE  /file-configs/:id : delete the "id" fileConfig.
     *
     * @param id the id of the fileConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/file-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteFileConfig(@PathVariable Long id) {
        log.debug("REST request to delete FileConfig : {}", id);
        fileConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
