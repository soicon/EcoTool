package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.topica.checking.domain.Answer;
import com.topica.checking.domain.FileStatus;
import com.topica.checking.domain.Question;
import com.topica.checking.service.*;
import com.topica.checking.service.dto.*;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.util.FetchingAPI;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * REST controller for managing Viewtool.
 */
@RestController
@RequestMapping("/api")
public class SourceResource {

    private final Logger log = LoggerFactory.getLogger(SourceResource.class);

    private static final String ENTITY_NAME = "source";

    private static  String API_URL = "";

    private static final String FILE_NAME = "test.xlsx";

    private static String url = "http://183.91.11.89:8105/#/entity/source/select/";

    private final SourceService sourceService;

    private final QuestionService questionService;

    private final DataVersionService dataVersionService;

    private final ApiVersionService apiVersionService;

    private final InputVersionService inputVersionService;

    @Autowired
    private  FileStorageServices fileStorageServices;

    private final RunnerLogService runnerLogService;

    private final FileStatusService fileStatusService;

    private final AnswerService answerService;

    private final SourceQueryService sourceQueryService;

    public SourceResource(SourceService sourceService, SourceQueryService sourceQueryService, QuestionService questionService, AnswerService answerService,
                          DataVersionService dataVersionService,
                          ApiVersionService apiVersionService,
                          InputVersionService inputVersionService,
                          RunnerLogService runnerLogService,
                          FileStatusService fileStatusService) {
        this.sourceService = sourceService;
        this.sourceQueryService = sourceQueryService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.dataVersionService = dataVersionService;
        this.apiVersionService = apiVersionService;
        this.inputVersionService = inputVersionService;
        this.runnerLogService = runnerLogService;
        this.fileStatusService = fileStatusService;
    }

    /**
     * POST  /sources : Create a new source.
     *
     * @param sourceDTO the sourceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sourceDTO, or with status 400 (Bad Request) if the source has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sources")
    @Timed
    public ResponseEntity<SourceDTO> createSource(@RequestBody SourceDTO sourceDTO) throws URISyntaxException {
        log.debug("REST request to save Viewtool : {}", sourceDTO);
        if (sourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new source cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceDTO result = sourceService.save(sourceDTO);
        return ResponseEntity.created(new URI("/api/sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/sources/select/{id}")
    public ResponseEntity<List<Object>> findSourceWithQA(@PathVariable long id) {
        log.debug("REST request to update Viewtool : {}", id);
        if (id == -1) {
            throw new BadRequestAlertException("invalid id", ENTITY_NAME, "id null");
        }
        List<Object> res = sourceService.findQA(id);
        return ResponseEntity.ok().body(res);
    }

    /**
     * PUT  /sources : Updates an existing source.
     *
     * @param sourceDTO the sourceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sourceDTO,
     * or with status 400 (Bad Request) if the sourceDTO is not valid,
     * or with status 500 (Internal Server Error) if the sourceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sources")
    @Timed
    public ResponseEntity<SourceDTO> updateSource(@RequestBody SourceDTO sourceDTO) throws URISyntaxException {
        log.debug("REST request to update Viewtool : {}", sourceDTO);
        if (sourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SourceDTO result = sourceService.save(sourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sources : get all the sources.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sources in body
     */
    @GetMapping("/sources")
    @Timed
    public ResponseEntity<List<SourceDTO>> getAllSources(SourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sources by criteria: {}", criteria);
        Page<SourceDTO> page = sourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sources");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * GET  /sources : get all the sources.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sources in body
     */
    @GetMapping("/sources/filter")
    @Timed
    public ResponseEntity<Object> filterAllSources(Pageable pageable) {
        Page<SourceDTO> page = sourceService.filter(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sources");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/sources/finding/file/{filename}/dataver/{dataver}/")
    @Timed
    public ResponseEntity<String> finding(@PathVariable String filename,@PathVariable String dataver) {
        Optional<DataVersionDTO> dataVersionDTO = dataVersionService.findByVersion(dataver);
        Optional<FileStatusDTO> fileStatusDTO = fileStatusService.findByName(filename);

        fileStatusDTO.get().setStatus(1);
        fileStatusDTO.get().setVersionInfo(dataVersionDTO.get().getVersion());
        fileStatusService.save(fileStatusDTO.get());
        File fileData = fileStorageServices.loadFileAsFile(filename);
        API_URL = dataVersionDTO.get().getDescription().trim().replace(" ","");
        List<RunnerLogDTO> runnerLogDTOList = new ArrayList<>();
        FetchingAPI fetchingAPI = new FetchingAPI(fileStorageServices);
        UriComponentsBuilder baseUrl  =   ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = fetchingAPI.doJob(dataVersionDTO,fileData,sourceService,API_URL,url,questionService
                    ,answerService,runnerLogService,runnerLogDTOList,filename);
                log.info(res);
                if(!res.equals("error")) {
                    String downloadURI = baseUrl.path(res)
                        .toUriString();
                    fileStatusDTO.get().setDownload_result_url(downloadURI);
                    fileStatusDTO.get().setResult(res);
                    fileStatusDTO.get().setStatus(2);
                    fileStatusService.save(fileStatusDTO.get());
                }else{
                    fileStatusDTO.get().setStatus(3);
                    fileStatusService.save(fileStatusDTO.get());
                }
            }
        }).start();



        return ResponseEntity.ok().body("");
    }

    /**
     * GET  /sources/count : count all the sources.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/sources/count")
    @Timed
    public ResponseEntity<Long> countSources(SourceCriteria criteria) {
        log.debug("REST request to count Sources by criteria: {}", criteria);
        return ResponseEntity.ok().body(sourceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /sources/:id : get the "id" source.
     *
     * @param id the id of the sourceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sourceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sources/{id}")
    @Timed
    public ResponseEntity<SourceDTO> getSource(@PathVariable Long id) {
        log.debug("REST request to get Viewtool : {}", id);
        Optional<SourceDTO> sourceDTO = sourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceDTO);
    }

    /**
     * DELETE  /sources/:id : delete the "id" source.
     *
     * @param id the id of the sourceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sources/{id}")
    @Timed
    public ResponseEntity<Void> deleteSource(@PathVariable Long id) {
        log.debug("REST request to delete Viewtool : {}", id);
        sourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
