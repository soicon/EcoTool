package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.FileStatus;
import com.topica.checking.repository.FileStatusRepository;
import com.topica.checking.service.FileStatusService;
import com.topica.checking.service.dto.FileStatusDTO;
import com.topica.checking.service.mapper.FileStatusMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.FileStatusCriteria;
import com.topica.checking.service.FileStatusQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.topica.checking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FileStatusResource REST controller.
 *
 * @see FileStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class FileStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DOWNLOAD_RESULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOWNLOAD_RESULT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_INFO = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_INFO = "BBBBBBBBBB";

    @Autowired
    private FileStatusRepository fileStatusRepository;

    @Autowired
    private FileStatusMapper fileStatusMapper;

    @Autowired
    private FileStatusService fileStatusService;

    @Autowired
    private FileStatusQueryService fileStatusQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFileStatusMockMvc;

    private FileStatus fileStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileStatusResource fileStatusResource = new FileStatusResource(fileStatusService, fileStatusQueryService);
        this.restFileStatusMockMvc = MockMvcBuilders.standaloneSetup(fileStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileStatus createEntity(EntityManager em) {
        FileStatus fileStatus = new FileStatus()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .result(DEFAULT_RESULT)
            .status(DEFAULT_STATUS)
            .download_result_url(DEFAULT_DOWNLOAD_RESULT_URL)
            .fileType(DEFAULT_FILE_TYPE)
            .versionInfo(DEFAULT_VERSION_INFO);
        return fileStatus;
    }

    @Before
    public void initTest() {
        fileStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileStatus() throws Exception {
        int databaseSizeBeforeCreate = fileStatusRepository.findAll().size();

        // Create the FileStatus
        FileStatusDTO fileStatusDTO = fileStatusMapper.toDto(fileStatus);
        restFileStatusMockMvc.perform(post("/api/file-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the FileStatus in the database
        List<FileStatus> fileStatusList = fileStatusRepository.findAll();
        assertThat(fileStatusList).hasSize(databaseSizeBeforeCreate + 1);
        FileStatus testFileStatus = fileStatusList.get(fileStatusList.size() - 1);
        assertThat(testFileStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFileStatus.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFileStatus.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testFileStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFileStatus.getDownload_result_url()).isEqualTo(DEFAULT_DOWNLOAD_RESULT_URL);
        assertThat(testFileStatus.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testFileStatus.getVersionInfo()).isEqualTo(DEFAULT_VERSION_INFO);
    }

    @Test
    @Transactional
    public void createFileStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileStatusRepository.findAll().size();

        // Create the FileStatus with an existing ID
        fileStatus.setId(1L);
        FileStatusDTO fileStatusDTO = fileStatusMapper.toDto(fileStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileStatusMockMvc.perform(post("/api/file-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileStatus in the database
        List<FileStatus> fileStatusList = fileStatusRepository.findAll();
        assertThat(fileStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFileStatuses() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList
        restFileStatusMockMvc.perform(get("/api/file-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].download_result_url").value(hasItem(DEFAULT_DOWNLOAD_RESULT_URL.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].versionInfo").value(hasItem(DEFAULT_VERSION_INFO.toString())));
    }
    
    @Test
    @Transactional
    public void getFileStatus() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get the fileStatus
        restFileStatusMockMvc.perform(get("/api/file-statuses/{id}", fileStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.download_result_url").value(DEFAULT_DOWNLOAD_RESULT_URL.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
            .andExpect(jsonPath("$.versionInfo").value(DEFAULT_VERSION_INFO.toString()));
    }

    @Test
    @Transactional
    public void getAllFileStatusesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where name equals to DEFAULT_NAME
        defaultFileStatusShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the fileStatusList where name equals to UPDATED_NAME
        defaultFileStatusShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFileStatusShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the fileStatusList where name equals to UPDATED_NAME
        defaultFileStatusShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where name is not null
        defaultFileStatusShouldBeFound("name.specified=true");

        // Get all the fileStatusList where name is null
        defaultFileStatusShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileStatusesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where url equals to DEFAULT_URL
        defaultFileStatusShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the fileStatusList where url equals to UPDATED_URL
        defaultFileStatusShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where url in DEFAULT_URL or UPDATED_URL
        defaultFileStatusShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the fileStatusList where url equals to UPDATED_URL
        defaultFileStatusShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where url is not null
        defaultFileStatusShouldBeFound("url.specified=true");

        // Get all the fileStatusList where url is null
        defaultFileStatusShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileStatusesByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where result equals to DEFAULT_RESULT
        defaultFileStatusShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the fileStatusList where result equals to UPDATED_RESULT
        defaultFileStatusShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByResultIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultFileStatusShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the fileStatusList where result equals to UPDATED_RESULT
        defaultFileStatusShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where result is not null
        defaultFileStatusShouldBeFound("result.specified=true");

        // Get all the fileStatusList where result is null
        defaultFileStatusShouldNotBeFound("result.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileStatusesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where status equals to DEFAULT_STATUS
        defaultFileStatusShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the fileStatusList where status equals to UPDATED_STATUS
        defaultFileStatusShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultFileStatusShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the fileStatusList where status equals to UPDATED_STATUS
        defaultFileStatusShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where status is not null
        defaultFileStatusShouldBeFound("status.specified=true");

        // Get all the fileStatusList where status is null
        defaultFileStatusShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileStatusesByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where status greater than or equals to DEFAULT_STATUS
        defaultFileStatusShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the fileStatusList where status greater than or equals to UPDATED_STATUS
        defaultFileStatusShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where status less than or equals to DEFAULT_STATUS
        defaultFileStatusShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the fileStatusList where status less than or equals to UPDATED_STATUS
        defaultFileStatusShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllFileStatusesByDownload_result_urlIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where download_result_url equals to DEFAULT_DOWNLOAD_RESULT_URL
        defaultFileStatusShouldBeFound("download_result_url.equals=" + DEFAULT_DOWNLOAD_RESULT_URL);

        // Get all the fileStatusList where download_result_url equals to UPDATED_DOWNLOAD_RESULT_URL
        defaultFileStatusShouldNotBeFound("download_result_url.equals=" + UPDATED_DOWNLOAD_RESULT_URL);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByDownload_result_urlIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where download_result_url in DEFAULT_DOWNLOAD_RESULT_URL or UPDATED_DOWNLOAD_RESULT_URL
        defaultFileStatusShouldBeFound("download_result_url.in=" + DEFAULT_DOWNLOAD_RESULT_URL + "," + UPDATED_DOWNLOAD_RESULT_URL);

        // Get all the fileStatusList where download_result_url equals to UPDATED_DOWNLOAD_RESULT_URL
        defaultFileStatusShouldNotBeFound("download_result_url.in=" + UPDATED_DOWNLOAD_RESULT_URL);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByDownload_result_urlIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where download_result_url is not null
        defaultFileStatusShouldBeFound("download_result_url.specified=true");

        // Get all the fileStatusList where download_result_url is null
        defaultFileStatusShouldNotBeFound("download_result_url.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileStatusesByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where fileType equals to DEFAULT_FILE_TYPE
        defaultFileStatusShouldBeFound("fileType.equals=" + DEFAULT_FILE_TYPE);

        // Get all the fileStatusList where fileType equals to UPDATED_FILE_TYPE
        defaultFileStatusShouldNotBeFound("fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where fileType in DEFAULT_FILE_TYPE or UPDATED_FILE_TYPE
        defaultFileStatusShouldBeFound("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE);

        // Get all the fileStatusList where fileType equals to UPDATED_FILE_TYPE
        defaultFileStatusShouldNotBeFound("fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where fileType is not null
        defaultFileStatusShouldBeFound("fileType.specified=true");

        // Get all the fileStatusList where fileType is null
        defaultFileStatusShouldNotBeFound("fileType.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileStatusesByVersionInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where versionInfo equals to DEFAULT_VERSION_INFO
        defaultFileStatusShouldBeFound("versionInfo.equals=" + DEFAULT_VERSION_INFO);

        // Get all the fileStatusList where versionInfo equals to UPDATED_VERSION_INFO
        defaultFileStatusShouldNotBeFound("versionInfo.equals=" + UPDATED_VERSION_INFO);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByVersionInfoIsInShouldWork() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where versionInfo in DEFAULT_VERSION_INFO or UPDATED_VERSION_INFO
        defaultFileStatusShouldBeFound("versionInfo.in=" + DEFAULT_VERSION_INFO + "," + UPDATED_VERSION_INFO);

        // Get all the fileStatusList where versionInfo equals to UPDATED_VERSION_INFO
        defaultFileStatusShouldNotBeFound("versionInfo.in=" + UPDATED_VERSION_INFO);
    }

    @Test
    @Transactional
    public void getAllFileStatusesByVersionInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        // Get all the fileStatusList where versionInfo is not null
        defaultFileStatusShouldBeFound("versionInfo.specified=true");

        // Get all the fileStatusList where versionInfo is null
        defaultFileStatusShouldNotBeFound("versionInfo.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFileStatusShouldBeFound(String filter) throws Exception {
        restFileStatusMockMvc.perform(get("/api/file-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].download_result_url").value(hasItem(DEFAULT_DOWNLOAD_RESULT_URL.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].versionInfo").value(hasItem(DEFAULT_VERSION_INFO.toString())));

        // Check, that the count call also returns 1
        restFileStatusMockMvc.perform(get("/api/file-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFileStatusShouldNotBeFound(String filter) throws Exception {
        restFileStatusMockMvc.perform(get("/api/file-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileStatusMockMvc.perform(get("/api/file-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFileStatus() throws Exception {
        // Get the fileStatus
        restFileStatusMockMvc.perform(get("/api/file-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileStatus() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        int databaseSizeBeforeUpdate = fileStatusRepository.findAll().size();

        // Update the fileStatus
        FileStatus updatedFileStatus = fileStatusRepository.findById(fileStatus.getId()).get();
        // Disconnect from session so that the updates on updatedFileStatus are not directly saved in db
        em.detach(updatedFileStatus);
        updatedFileStatus
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .result(UPDATED_RESULT)
            .status(UPDATED_STATUS)
            .download_result_url(UPDATED_DOWNLOAD_RESULT_URL)
            .fileType(UPDATED_FILE_TYPE)
            .versionInfo(UPDATED_VERSION_INFO);
        FileStatusDTO fileStatusDTO = fileStatusMapper.toDto(updatedFileStatus);

        restFileStatusMockMvc.perform(put("/api/file-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileStatusDTO)))
            .andExpect(status().isOk());

        // Validate the FileStatus in the database
        List<FileStatus> fileStatusList = fileStatusRepository.findAll();
        assertThat(fileStatusList).hasSize(databaseSizeBeforeUpdate);
        FileStatus testFileStatus = fileStatusList.get(fileStatusList.size() - 1);
        assertThat(testFileStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFileStatus.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFileStatus.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testFileStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFileStatus.getDownload_result_url()).isEqualTo(UPDATED_DOWNLOAD_RESULT_URL);
        assertThat(testFileStatus.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testFileStatus.getVersionInfo()).isEqualTo(UPDATED_VERSION_INFO);
    }

    @Test
    @Transactional
    public void updateNonExistingFileStatus() throws Exception {
        int databaseSizeBeforeUpdate = fileStatusRepository.findAll().size();

        // Create the FileStatus
        FileStatusDTO fileStatusDTO = fileStatusMapper.toDto(fileStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileStatusMockMvc.perform(put("/api/file-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileStatus in the database
        List<FileStatus> fileStatusList = fileStatusRepository.findAll();
        assertThat(fileStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileStatus() throws Exception {
        // Initialize the database
        fileStatusRepository.saveAndFlush(fileStatus);

        int databaseSizeBeforeDelete = fileStatusRepository.findAll().size();

        // Get the fileStatus
        restFileStatusMockMvc.perform(delete("/api/file-statuses/{id}", fileStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FileStatus> fileStatusList = fileStatusRepository.findAll();
        assertThat(fileStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileStatus.class);
        FileStatus fileStatus1 = new FileStatus();
        fileStatus1.setId(1L);
        FileStatus fileStatus2 = new FileStatus();
        fileStatus2.setId(fileStatus1.getId());
        assertThat(fileStatus1).isEqualTo(fileStatus2);
        fileStatus2.setId(2L);
        assertThat(fileStatus1).isNotEqualTo(fileStatus2);
        fileStatus1.setId(null);
        assertThat(fileStatus1).isNotEqualTo(fileStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileStatusDTO.class);
        FileStatusDTO fileStatusDTO1 = new FileStatusDTO();
        fileStatusDTO1.setId(1L);
        FileStatusDTO fileStatusDTO2 = new FileStatusDTO();
        assertThat(fileStatusDTO1).isNotEqualTo(fileStatusDTO2);
        fileStatusDTO2.setId(fileStatusDTO1.getId());
        assertThat(fileStatusDTO1).isEqualTo(fileStatusDTO2);
        fileStatusDTO2.setId(2L);
        assertThat(fileStatusDTO1).isNotEqualTo(fileStatusDTO2);
        fileStatusDTO1.setId(null);
        assertThat(fileStatusDTO1).isNotEqualTo(fileStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fileStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fileStatusMapper.fromId(null)).isNull();
    }
}
