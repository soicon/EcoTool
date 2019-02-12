package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.DataVersion;
import com.topica.checking.repository.DataVersionRepository;
import com.topica.checking.service.DataVersionService;
import com.topica.checking.service.dto.DataVersionDTO;
import com.topica.checking.service.mapper.DataVersionMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.DataVersionCriteria;
import com.topica.checking.service.DataVersionQueryService;

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
 * Test class for the DataVersionResource REST controller.
 *
 * @see DataVersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class DataVersionResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DataVersionRepository dataVersionRepository;

    @Autowired
    private DataVersionMapper dataVersionMapper;

    @Autowired
    private DataVersionService dataVersionService;

    @Autowired
    private DataVersionQueryService dataVersionQueryService;

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

    private MockMvc restDataVersionMockMvc;

    private DataVersion dataVersion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataVersionResource dataVersionResource = new DataVersionResource(dataVersionService, dataVersionQueryService);
        this.restDataVersionMockMvc = MockMvcBuilders.standaloneSetup(dataVersionResource)
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
    public static DataVersion createEntity(EntityManager em) {
        DataVersion dataVersion = new DataVersion()
            .version(DEFAULT_VERSION)
            .description(DEFAULT_DESCRIPTION);
        return dataVersion;
    }

    @Before
    public void initTest() {
        dataVersion = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataVersion() throws Exception {
        int databaseSizeBeforeCreate = dataVersionRepository.findAll().size();

        // Create the DataVersion
        DataVersionDTO dataVersionDTO = dataVersionMapper.toDto(dataVersion);
        restDataVersionMockMvc.perform(post("/api/data-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataVersionDTO)))
            .andExpect(status().isCreated());

        // Validate the DataVersion in the database
        List<DataVersion> dataVersionList = dataVersionRepository.findAll();
        assertThat(dataVersionList).hasSize(databaseSizeBeforeCreate + 1);
        DataVersion testDataVersion = dataVersionList.get(dataVersionList.size() - 1);
        assertThat(testDataVersion.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testDataVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDataVersionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataVersionRepository.findAll().size();

        // Create the DataVersion with an existing ID
        dataVersion.setId(1L);
        DataVersionDTO dataVersionDTO = dataVersionMapper.toDto(dataVersion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataVersionMockMvc.perform(post("/api/data-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataVersion in the database
        List<DataVersion> dataVersionList = dataVersionRepository.findAll();
        assertThat(dataVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataVersions() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList
        restDataVersionMockMvc.perform(get("/api/data-versions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getDataVersion() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get the dataVersion
        restDataVersionMockMvc.perform(get("/api/data-versions/{id}", dataVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataVersion.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllDataVersionsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList where version equals to DEFAULT_VERSION
        defaultDataVersionShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the dataVersionList where version equals to UPDATED_VERSION
        defaultDataVersionShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllDataVersionsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultDataVersionShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the dataVersionList where version equals to UPDATED_VERSION
        defaultDataVersionShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllDataVersionsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList where version is not null
        defaultDataVersionShouldBeFound("version.specified=true");

        // Get all the dataVersionList where version is null
        defaultDataVersionShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataVersionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList where description equals to DEFAULT_DESCRIPTION
        defaultDataVersionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the dataVersionList where description equals to UPDATED_DESCRIPTION
        defaultDataVersionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDataVersionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDataVersionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the dataVersionList where description equals to UPDATED_DESCRIPTION
        defaultDataVersionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDataVersionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        // Get all the dataVersionList where description is not null
        defaultDataVersionShouldBeFound("description.specified=true");

        // Get all the dataVersionList where description is null
        defaultDataVersionShouldNotBeFound("description.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDataVersionShouldBeFound(String filter) throws Exception {
        restDataVersionMockMvc.perform(get("/api/data-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restDataVersionMockMvc.perform(get("/api/data-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDataVersionShouldNotBeFound(String filter) throws Exception {
        restDataVersionMockMvc.perform(get("/api/data-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataVersionMockMvc.perform(get("/api/data-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDataVersion() throws Exception {
        // Get the dataVersion
        restDataVersionMockMvc.perform(get("/api/data-versions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataVersion() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        int databaseSizeBeforeUpdate = dataVersionRepository.findAll().size();

        // Update the dataVersion
        DataVersion updatedDataVersion = dataVersionRepository.findById(dataVersion.getId()).get();
        // Disconnect from session so that the updates on updatedDataVersion are not directly saved in db
        em.detach(updatedDataVersion);
        updatedDataVersion
            .version(UPDATED_VERSION)
            .description(UPDATED_DESCRIPTION);
        DataVersionDTO dataVersionDTO = dataVersionMapper.toDto(updatedDataVersion);

        restDataVersionMockMvc.perform(put("/api/data-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataVersionDTO)))
            .andExpect(status().isOk());

        // Validate the DataVersion in the database
        List<DataVersion> dataVersionList = dataVersionRepository.findAll();
        assertThat(dataVersionList).hasSize(databaseSizeBeforeUpdate);
        DataVersion testDataVersion = dataVersionList.get(dataVersionList.size() - 1);
        assertThat(testDataVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDataVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDataVersion() throws Exception {
        int databaseSizeBeforeUpdate = dataVersionRepository.findAll().size();

        // Create the DataVersion
        DataVersionDTO dataVersionDTO = dataVersionMapper.toDto(dataVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataVersionMockMvc.perform(put("/api/data-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataVersion in the database
        List<DataVersion> dataVersionList = dataVersionRepository.findAll();
        assertThat(dataVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataVersion() throws Exception {
        // Initialize the database
        dataVersionRepository.saveAndFlush(dataVersion);

        int databaseSizeBeforeDelete = dataVersionRepository.findAll().size();

        // Get the dataVersion
        restDataVersionMockMvc.perform(delete("/api/data-versions/{id}", dataVersion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataVersion> dataVersionList = dataVersionRepository.findAll();
        assertThat(dataVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataVersion.class);
        DataVersion dataVersion1 = new DataVersion();
        dataVersion1.setId(1L);
        DataVersion dataVersion2 = new DataVersion();
        dataVersion2.setId(dataVersion1.getId());
        assertThat(dataVersion1).isEqualTo(dataVersion2);
        dataVersion2.setId(2L);
        assertThat(dataVersion1).isNotEqualTo(dataVersion2);
        dataVersion1.setId(null);
        assertThat(dataVersion1).isNotEqualTo(dataVersion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataVersionDTO.class);
        DataVersionDTO dataVersionDTO1 = new DataVersionDTO();
        dataVersionDTO1.setId(1L);
        DataVersionDTO dataVersionDTO2 = new DataVersionDTO();
        assertThat(dataVersionDTO1).isNotEqualTo(dataVersionDTO2);
        dataVersionDTO2.setId(dataVersionDTO1.getId());
        assertThat(dataVersionDTO1).isEqualTo(dataVersionDTO2);
        dataVersionDTO2.setId(2L);
        assertThat(dataVersionDTO1).isNotEqualTo(dataVersionDTO2);
        dataVersionDTO1.setId(null);
        assertThat(dataVersionDTO1).isNotEqualTo(dataVersionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dataVersionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dataVersionMapper.fromId(null)).isNull();
    }
}
