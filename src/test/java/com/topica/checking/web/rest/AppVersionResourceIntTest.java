package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.AppVersion;
import com.topica.checking.repository.AppVersionRepository;
import com.topica.checking.service.AppVersionService;
import com.topica.checking.service.dto.AppVersionDTO;
import com.topica.checking.service.mapper.AppVersionMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.AppVersionCriteria;
import com.topica.checking.service.AppVersionQueryService;

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
 * Test class for the AppVersionResource REST controller.
 *
 * @see AppVersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class AppVersionResourceIntTest {

    private static final String DEFAULT_API_VER = "AAAAAAAAAA";
    private static final String UPDATED_API_VER = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_VER = "AAAAAAAAAA";
    private static final String UPDATED_DATA_VER = "BBBBBBBBBB";

    private static final String DEFAULT_INPUT_VER = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_VER = "BBBBBBBBBB";

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private AppVersionQueryService appVersionQueryService;

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

    private MockMvc restAppVersionMockMvc;

    private AppVersion appVersion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppVersionResource appVersionResource = new AppVersionResource(appVersionService, appVersionQueryService);
        this.restAppVersionMockMvc = MockMvcBuilders.standaloneSetup(appVersionResource)
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
    public static AppVersion createEntity(EntityManager em) {
        AppVersion appVersion = new AppVersion()
            .apiVer(DEFAULT_API_VER)
            .dataVer(DEFAULT_DATA_VER)
            .inputVer(DEFAULT_INPUT_VER);
        return appVersion;
    }

    @Before
    public void initTest() {
        appVersion = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppVersion() throws Exception {
        int databaseSizeBeforeCreate = appVersionRepository.findAll().size();

        // Create the AppVersion
        AppVersionDTO appVersionDTO = appVersionMapper.toDto(appVersion);
        restAppVersionMockMvc.perform(post("/api/app-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appVersionDTO)))
            .andExpect(status().isCreated());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeCreate + 1);
        AppVersion testAppVersion = appVersionList.get(appVersionList.size() - 1);
        assertThat(testAppVersion.getApiVer()).isEqualTo(DEFAULT_API_VER);
        assertThat(testAppVersion.getDataVer()).isEqualTo(DEFAULT_DATA_VER);
        assertThat(testAppVersion.getInputVer()).isEqualTo(DEFAULT_INPUT_VER);
    }

    @Test
    @Transactional
    public void createAppVersionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appVersionRepository.findAll().size();

        // Create the AppVersion with an existing ID
        appVersion.setId(1L);
        AppVersionDTO appVersionDTO = appVersionMapper.toDto(appVersion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppVersionMockMvc.perform(post("/api/app-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppVersions() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList
        restAppVersionMockMvc.perform(get("/api/app-versions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].apiVer").value(hasItem(DEFAULT_API_VER.toString())))
            .andExpect(jsonPath("$.[*].dataVer").value(hasItem(DEFAULT_DATA_VER.toString())))
            .andExpect(jsonPath("$.[*].inputVer").value(hasItem(DEFAULT_INPUT_VER.toString())));
    }
    
    @Test
    @Transactional
    public void getAppVersion() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get the appVersion
        restAppVersionMockMvc.perform(get("/api/app-versions/{id}", appVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appVersion.getId().intValue()))
            .andExpect(jsonPath("$.apiVer").value(DEFAULT_API_VER.toString()))
            .andExpect(jsonPath("$.dataVer").value(DEFAULT_DATA_VER.toString()))
            .andExpect(jsonPath("$.inputVer").value(DEFAULT_INPUT_VER.toString()));
    }

    @Test
    @Transactional
    public void getAllAppVersionsByApiVerIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where apiVer equals to DEFAULT_API_VER
        defaultAppVersionShouldBeFound("apiVer.equals=" + DEFAULT_API_VER);

        // Get all the appVersionList where apiVer equals to UPDATED_API_VER
        defaultAppVersionShouldNotBeFound("apiVer.equals=" + UPDATED_API_VER);
    }

    @Test
    @Transactional
    public void getAllAppVersionsByApiVerIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where apiVer in DEFAULT_API_VER or UPDATED_API_VER
        defaultAppVersionShouldBeFound("apiVer.in=" + DEFAULT_API_VER + "," + UPDATED_API_VER);

        // Get all the appVersionList where apiVer equals to UPDATED_API_VER
        defaultAppVersionShouldNotBeFound("apiVer.in=" + UPDATED_API_VER);
    }

    @Test
    @Transactional
    public void getAllAppVersionsByApiVerIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where apiVer is not null
        defaultAppVersionShouldBeFound("apiVer.specified=true");

        // Get all the appVersionList where apiVer is null
        defaultAppVersionShouldNotBeFound("apiVer.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppVersionsByDataVerIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where dataVer equals to DEFAULT_DATA_VER
        defaultAppVersionShouldBeFound("dataVer.equals=" + DEFAULT_DATA_VER);

        // Get all the appVersionList where dataVer equals to UPDATED_DATA_VER
        defaultAppVersionShouldNotBeFound("dataVer.equals=" + UPDATED_DATA_VER);
    }

    @Test
    @Transactional
    public void getAllAppVersionsByDataVerIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where dataVer in DEFAULT_DATA_VER or UPDATED_DATA_VER
        defaultAppVersionShouldBeFound("dataVer.in=" + DEFAULT_DATA_VER + "," + UPDATED_DATA_VER);

        // Get all the appVersionList where dataVer equals to UPDATED_DATA_VER
        defaultAppVersionShouldNotBeFound("dataVer.in=" + UPDATED_DATA_VER);
    }

    @Test
    @Transactional
    public void getAllAppVersionsByDataVerIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where dataVer is not null
        defaultAppVersionShouldBeFound("dataVer.specified=true");

        // Get all the appVersionList where dataVer is null
        defaultAppVersionShouldNotBeFound("dataVer.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppVersionsByInputVerIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where inputVer equals to DEFAULT_INPUT_VER
        defaultAppVersionShouldBeFound("inputVer.equals=" + DEFAULT_INPUT_VER);

        // Get all the appVersionList where inputVer equals to UPDATED_INPUT_VER
        defaultAppVersionShouldNotBeFound("inputVer.equals=" + UPDATED_INPUT_VER);
    }

    @Test
    @Transactional
    public void getAllAppVersionsByInputVerIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where inputVer in DEFAULT_INPUT_VER or UPDATED_INPUT_VER
        defaultAppVersionShouldBeFound("inputVer.in=" + DEFAULT_INPUT_VER + "," + UPDATED_INPUT_VER);

        // Get all the appVersionList where inputVer equals to UPDATED_INPUT_VER
        defaultAppVersionShouldNotBeFound("inputVer.in=" + UPDATED_INPUT_VER);
    }

    @Test
    @Transactional
    public void getAllAppVersionsByInputVerIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where inputVer is not null
        defaultAppVersionShouldBeFound("inputVer.specified=true");

        // Get all the appVersionList where inputVer is null
        defaultAppVersionShouldNotBeFound("inputVer.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppVersionShouldBeFound(String filter) throws Exception {
        restAppVersionMockMvc.perform(get("/api/app-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].apiVer").value(hasItem(DEFAULT_API_VER.toString())))
            .andExpect(jsonPath("$.[*].dataVer").value(hasItem(DEFAULT_DATA_VER.toString())))
            .andExpect(jsonPath("$.[*].inputVer").value(hasItem(DEFAULT_INPUT_VER.toString())));

        // Check, that the count call also returns 1
        restAppVersionMockMvc.perform(get("/api/app-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppVersionShouldNotBeFound(String filter) throws Exception {
        restAppVersionMockMvc.perform(get("/api/app-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppVersionMockMvc.perform(get("/api/app-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAppVersion() throws Exception {
        // Get the appVersion
        restAppVersionMockMvc.perform(get("/api/app-versions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppVersion() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();

        // Update the appVersion
        AppVersion updatedAppVersion = appVersionRepository.findById(appVersion.getId()).get();
        // Disconnect from session so that the updates on updatedAppVersion are not directly saved in db
        em.detach(updatedAppVersion);
        updatedAppVersion
            .apiVer(UPDATED_API_VER)
            .dataVer(UPDATED_DATA_VER)
            .inputVer(UPDATED_INPUT_VER);
        AppVersionDTO appVersionDTO = appVersionMapper.toDto(updatedAppVersion);

        restAppVersionMockMvc.perform(put("/api/app-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appVersionDTO)))
            .andExpect(status().isOk());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
        AppVersion testAppVersion = appVersionList.get(appVersionList.size() - 1);
        assertThat(testAppVersion.getApiVer()).isEqualTo(UPDATED_API_VER);
        assertThat(testAppVersion.getDataVer()).isEqualTo(UPDATED_DATA_VER);
        assertThat(testAppVersion.getInputVer()).isEqualTo(UPDATED_INPUT_VER);
    }

    @Test
    @Transactional
    public void updateNonExistingAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();

        // Create the AppVersion
        AppVersionDTO appVersionDTO = appVersionMapper.toDto(appVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppVersionMockMvc.perform(put("/api/app-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppVersion() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeDelete = appVersionRepository.findAll().size();

        // Get the appVersion
        restAppVersionMockMvc.perform(delete("/api/app-versions/{id}", appVersion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppVersion.class);
        AppVersion appVersion1 = new AppVersion();
        appVersion1.setId(1L);
        AppVersion appVersion2 = new AppVersion();
        appVersion2.setId(appVersion1.getId());
        assertThat(appVersion1).isEqualTo(appVersion2);
        appVersion2.setId(2L);
        assertThat(appVersion1).isNotEqualTo(appVersion2);
        appVersion1.setId(null);
        assertThat(appVersion1).isNotEqualTo(appVersion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppVersionDTO.class);
        AppVersionDTO appVersionDTO1 = new AppVersionDTO();
        appVersionDTO1.setId(1L);
        AppVersionDTO appVersionDTO2 = new AppVersionDTO();
        assertThat(appVersionDTO1).isNotEqualTo(appVersionDTO2);
        appVersionDTO2.setId(appVersionDTO1.getId());
        assertThat(appVersionDTO1).isEqualTo(appVersionDTO2);
        appVersionDTO2.setId(2L);
        assertThat(appVersionDTO1).isNotEqualTo(appVersionDTO2);
        appVersionDTO1.setId(null);
        assertThat(appVersionDTO1).isNotEqualTo(appVersionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appVersionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appVersionMapper.fromId(null)).isNull();
    }
}
