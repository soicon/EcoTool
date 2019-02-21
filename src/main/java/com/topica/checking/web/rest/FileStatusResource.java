package com.topica.checking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.io.Files;
import com.topica.checking.domain.FileStatus;
import com.topica.checking.service.FileStatusService;
import com.topica.checking.web.rest.errors.BadRequestAlertException;
import com.topica.checking.web.rest.errors.MyFileNotFoundException;
import com.topica.checking.web.rest.util.HeaderUtil;
import com.topica.checking.web.rest.util.PaginationUtil;
import com.topica.checking.service.dto.FileStatusDTO;
import com.topica.checking.service.dto.FileStatusCriteria;
import com.topica.checking.service.FileStatusQueryService;
import com.topica.checking.web.rest.vm.FileUploadResponse;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import com.topica.checking.service.FileStorageServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FileStatus.
 */
@RestController
@RequestMapping("/api")
public class FileStatusResource {

    private final Logger log = LoggerFactory.getLogger(FileStatusResource.class);

    private static final String ENTITY_NAME = "fileStatus";

    private final FileStatusService fileStatusService;

    private final FileStatusQueryService fileStatusQueryService;

    @Autowired
    private FileStorageServices fileStorageService;

    public FileStatusResource(FileStatusService fileStatusService, FileStatusQueryService fileStatusQueryService) {
        this.fileStatusService = fileStatusService;
        this.fileStatusQueryService = fileStatusQueryService;
    }

    /**
     * POST  /file-statuses : Create a new fileStatus.
     *
     * @param fileStatusDTO the fileStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileStatusDTO, or with status 400 (Bad Request) if the fileStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/file-statuses")
    @Timed
    public ResponseEntity<FileStatusDTO> createFileStatus(@RequestBody FileStatusDTO fileStatusDTO) throws URISyntaxException {
        log.debug("REST request to save FileStatus : {}", fileStatusDTO);
        if (fileStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileStatusDTO result = fileStatusService.save(fileStatusDTO);
        return ResponseEntity.created(new URI("/api/file-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @PostMapping("/upload-file")
    @Timed
    public ResponseEntity<?> uploadFile(@RequestParam("filepond") MultipartFile file) throws  URISyntaxException{
           log.info(file.getOriginalFilename());

           try {
               Resource resource = fileStorageService.loadFileAsResource(file.getOriginalFilename());
               throw new BadRequestAlertException("Tren he thong da co file nay roi", ENTITY_NAME, "");
           }catch (MyFileNotFoundException ex) {
               String filename = fileStorageService.storeFile(file);
               String downloadURI = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/downloadFile/").path(filename)
                   .toUriString();
               FileStatusDTO fileStatusDTO = new FileStatusDTO();
               fileStatusDTO.setName(filename);
               fileStatusDTO.setStatus(0);
               fileStatusDTO.setUrl(downloadURI);
               fileStatusDTO.setFileType(file.getContentType());
               fileStatusService.save(fileStatusDTO);
               return ResponseEntity.ok().
                   body(new FileUploadResponse(filename, downloadURI, file.getContentType(), file.getSize()));
           }

    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws  IOException{
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
            .body(resource);
    }



    /**
     * PUT  /file-statuses : Updates an existing fileStatus.
     *
     * @param fileStatusDTO the fileStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileStatusDTO,
     * or with status 400 (Bad Request) if the fileStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the fileStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/file-statuses")
    @Timed
    public ResponseEntity<FileStatusDTO> updateFileStatus(@RequestBody FileStatusDTO fileStatusDTO) throws URISyntaxException {
        log.debug("REST request to update FileStatus : {}", fileStatusDTO);
        if (fileStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileStatusDTO result = fileStatusService.save(fileStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-statuses : get all the fileStatuses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of fileStatuses in body
     */
    @GetMapping("/file-statuses")
    @Timed
    public ResponseEntity<List<FileStatusDTO>> getAllFileStatuses(FileStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FileStatuses by criteria: {}", criteria);
        Page<FileStatusDTO> page = fileStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/file-statuses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /file-statuses/count : count all the fileStatuses.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/file-statuses/count")
    @Timed
    public ResponseEntity<Long> countFileStatuses(FileStatusCriteria criteria) {
        log.debug("REST request to count FileStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileStatusQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /file-statuses/:id : get the "id" fileStatus.
     *
     * @param id the id of the fileStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/file-statuses/{id}")
    @Timed
    public ResponseEntity<FileStatusDTO> getFileStatus(@PathVariable Long id) {
        log.debug("REST request to get FileStatus : {}", id);
        Optional<FileStatusDTO> fileStatusDTO = fileStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileStatusDTO);
    }

    /**
     * DELETE  /file-statuses/:id : delete the "id" fileStatus.
     *
     * @param id the id of the fileStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/file-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteFileStatus(@PathVariable Long id) {
        log.debug("REST request to delete FileStatus : {}", id);
        Optional<FileStatusDTO> fileStatusDTO = fileStatusService.findOne(id);
        Resource resource = fileStorageService.loadFileAsResource(fileStatusDTO.get().getName());
        try {
            File file = new File(resource.getURL().getPath());
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
