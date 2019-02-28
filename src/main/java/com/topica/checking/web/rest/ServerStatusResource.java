package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.service.ServerStatusService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.service.dto.ServerStatusDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ServerStatus.
 */
@RestController
@RequestMapping("/api")
public class ServerStatusResource {

    private final Logger log = LoggerFactory.getLogger(ServerStatusResource.class);

    private static final String ENTITY_NAME = "serverStatus";

    private final ServerStatusService serverStatusService;

    public ServerStatusResource(ServerStatusService serverStatusService) {
        this.serverStatusService = serverStatusService;
    }

    /**
     * POST  /server-statuses : Create a new serverStatus.
     *
     * @param serverStatusDTO the serverStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serverStatusDTO, or with status 400 (Bad Request) if the serverStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/server-statuses")
    @Timed
    public ResponseEntity<ServerStatusDTO> createServerStatus(@RequestBody ServerStatusDTO serverStatusDTO) throws URISyntaxException {
        log.debug("REST request to save ServerStatus : {}", serverStatusDTO);
        if (serverStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new serverStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServerStatusDTO result = serverStatusService.save(serverStatusDTO);
        return ResponseEntity.created(new URI("/api/server-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /server-statuses : Updates an existing serverStatus.
     *
     * @param serverStatusDTO the serverStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serverStatusDTO,
     * or with status 400 (Bad Request) if the serverStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the serverStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/server-statuses")
    @Timed
    public ResponseEntity<ServerStatusDTO> updateServerStatus(@RequestBody ServerStatusDTO serverStatusDTO) throws URISyntaxException {
        log.debug("REST request to update ServerStatus : {}", serverStatusDTO);
        if (serverStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServerStatusDTO result = serverStatusService.save(serverStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serverStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /server-statuses : get all the serverStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serverStatuses in body
     */
    @GetMapping("/server-statuses")
    @Timed
    public List<ServerStatusDTO> getAllServerStatuses() {
        log.debug("REST request to get all ServerStatuses");
        return serverStatusService.findAll();
    }

    /**
     * GET  /server-statuses/:id : get the "id" serverStatus.
     *
     * @param id the id of the serverStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serverStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/server-statuses/{id}")
    @Timed
    public ResponseEntity<ServerStatusDTO> getServerStatus(@PathVariable Long id) {
        log.debug("REST request to get ServerStatus : {}", id);
        Optional<ServerStatusDTO> serverStatusDTO = serverStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serverStatusDTO);
    }

    /**
     * DELETE  /server-statuses/:id : delete the "id" serverStatus.
     *
     * @param id the id of the serverStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/server-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteServerStatus(@PathVariable Long id) {
        log.debug("REST request to delete ServerStatus : {}", id);
        serverStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
