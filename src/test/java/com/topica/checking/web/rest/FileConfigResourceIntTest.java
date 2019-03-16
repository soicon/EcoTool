package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.FileConfig;
import com.topica.checking.domain.FileStatus;
import com.topica.checking.repository.FileConfigRepository;
import com.topica.checking.service.FileConfigService;
import com.topica.checking.service.dto.FileConfigDTO;
import com.topica.checking.service.mapper.FileConfigMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.FileConfigCriteria;
import com.topica.checking.service.FileConfigQueryService;

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
 * Test class for the FileConfigResource REST controller.
 *
 * @see FileConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class FileConfigResourceIntTest {

    @Autowired
    private FileConfigRepository fileConfigRepository;

    @Autowired
    private FileConfigMapper fileConfigMapper;

    @Autowired
    private FileConfigService fileConfigService;

    @Autowired
    private FileConfigQueryService fileConfigQueryService;

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

    private MockMvc restFileConfigMockMvc;

    private FileConfig fileConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileConfigResource fileConfigResource = new FileConfigResource(fileConfigService, fileConfigQueryService);
        this.restFileConfigMockMvc = MockMvcBuilders.standaloneSetup(fileConfigResource)
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
    public static FileConfig createEntity(EntityManager em) {
        FileConfig fileConfig = new FileConfig();
        return fileConfig;
    }

    @Before
    public void initTest() {
        fileConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileConfig() throws Exception {
        int databaseSizeBeforeCreate = fileConfigRepository.findAll().size();

        // Create the FileConfig
        FileConfigDTO fileConfigDTO = fileConfigMapper.toDto(fileConfig);
        restFileConfigMockMvc.perform(post("/api/file-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the FileConfig in the database
        List<FileConfig> fileConfigList = fileConfigRepository.findAll();
        assertThat(fileConfigList).hasSize(databaseSizeBeforeCreate + 1);
        FileConfig testFileConfig = fileConfigList.get(fileConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void createFileConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileConfigRepository.findAll().size();

        // Create the FileConfig with an existing ID
        fileConfig.setId(1L);
        FileConfigDTO fileConfigDTO = fileConfigMapper.toDto(fileConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileConfigMockMvc.perform(post("/api/file-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileConfig in the database
        List<FileConfig> fileConfigList = fileConfigRepository.findAll();
        assertThat(fileConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFileConfigs() throws Exception {
        // Initialize the database
        fileConfigRepository.saveAndFlush(fileConfig);

        // Get all the fileConfigList
        restFileConfigMockMvc.perform(get("/api/file-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileConfig.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getFileConfig() throws Exception {
        // Initialize the database
        fileConfigRepository.saveAndFlush(fileConfig);

        // Get the fileConfig
        restFileConfigMockMvc.perform(get("/api/file-configs/{id}", fileConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileConfig.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllFileConfigsByFileStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        FileStatus fileStatus = FileStatusResourceIntTest.createEntity(em);
        em.persist(fileStatus);
        em.flush();
        fileConfig.setFileStatus(fileStatus);
        fileConfigRepository.saveAndFlush(fileConfig);
        Long fileStatusId = fileStatus.getId();

        // Get all the fileConfigList where fileStatus equals to fileStatusId
        defaultFileConfigShouldBeFound("fileStatusId.equals=" + fileStatusId);

        // Get all the fileConfigList where fileStatus equals to fileStatusId + 1
        defaultFileConfigShouldNotBeFound("fileStatusId.equals=" + (fileStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFileConfigShouldBeFound(String filter) throws Exception {
        restFileConfigMockMvc.perform(get("/api/file-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileConfig.getId().intValue())));

        // Check, that the count call also returns 1
        restFileConfigMockMvc.perform(get("/api/file-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFileConfigShouldNotBeFound(String filter) throws Exception {
        restFileConfigMockMvc.perform(get("/api/file-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileConfigMockMvc.perform(get("/api/file-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFileConfig() throws Exception {
        // Get the fileConfig
        restFileConfigMockMvc.perform(get("/api/file-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileConfig() throws Exception {
        // Initialize the database
        fileConfigRepository.saveAndFlush(fileConfig);

        int databaseSizeBeforeUpdate = fileConfigRepository.findAll().size();

        // Update the fileConfig
        FileConfig updatedFileConfig = fileConfigRepository.findById(fileConfig.getId()).get();
        // Disconnect from session so that the updates on updatedFileConfig are not directly saved in db
        em.detach(updatedFileConfig);
        FileConfigDTO fileConfigDTO = fileConfigMapper.toDto(updatedFileConfig);

        restFileConfigMockMvc.perform(put("/api/file-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileConfigDTO)))
            .andExpect(status().isOk());

        // Validate the FileConfig in the database
        List<FileConfig> fileConfigList = fileConfigRepository.findAll();
        assertThat(fileConfigList).hasSize(databaseSizeBeforeUpdate);
        FileConfig testFileConfig = fileConfigList.get(fileConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFileConfig() throws Exception {
        int databaseSizeBeforeUpdate = fileConfigRepository.findAll().size();

        // Create the FileConfig
        FileConfigDTO fileConfigDTO = fileConfigMapper.toDto(fileConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileConfigMockMvc.perform(put("/api/file-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileConfig in the database
        List<FileConfig> fileConfigList = fileConfigRepository.findAll();
        assertThat(fileConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileConfig() throws Exception {
        // Initialize the database
        fileConfigRepository.saveAndFlush(fileConfig);

        int databaseSizeBeforeDelete = fileConfigRepository.findAll().size();

        // Get the fileConfig
        restFileConfigMockMvc.perform(delete("/api/file-configs/{id}", fileConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FileConfig> fileConfigList = fileConfigRepository.findAll();
        assertThat(fileConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileConfig.class);
        FileConfig fileConfig1 = new FileConfig();
        fileConfig1.setId(1L);
        FileConfig fileConfig2 = new FileConfig();
        fileConfig2.setId(fileConfig1.getId());
        assertThat(fileConfig1).isEqualTo(fileConfig2);
        fileConfig2.setId(2L);
        assertThat(fileConfig1).isNotEqualTo(fileConfig2);
        fileConfig1.setId(null);
        assertThat(fileConfig1).isNotEqualTo(fileConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileConfigDTO.class);
        FileConfigDTO fileConfigDTO1 = new FileConfigDTO();
        fileConfigDTO1.setId(1L);
        FileConfigDTO fileConfigDTO2 = new FileConfigDTO();
        assertThat(fileConfigDTO1).isNotEqualTo(fileConfigDTO2);
        fileConfigDTO2.setId(fileConfigDTO1.getId());
        assertThat(fileConfigDTO1).isEqualTo(fileConfigDTO2);
        fileConfigDTO2.setId(2L);
        assertThat(fileConfigDTO1).isNotEqualTo(fileConfigDTO2);
        fileConfigDTO1.setId(null);
        assertThat(fileConfigDTO1).isNotEqualTo(fileConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fileConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fileConfigMapper.fromId(null)).isNull();
    }
}
