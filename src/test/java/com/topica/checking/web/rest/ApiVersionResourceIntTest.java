package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.ApiVersion;
import com.topica.checking.repository.ApiVersionRepository;
import com.topica.checking.service.ApiVersionService;
import com.topica.checking.service.dto.ApiVersionDTO;
import com.topica.checking.service.mapper.ApiVersionMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.ApiVersionCriteria;
import com.topica.checking.service.ApiVersionQueryService;

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
 * Test class for the ApiVersionResource REST controller.
 *
 * @see ApiVersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class ApiVersionResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ApiVersionRepository apiVersionRepository;

    @Autowired
    private ApiVersionMapper apiVersionMapper;

    @Autowired
    private ApiVersionService apiVersionService;

    @Autowired
    private ApiVersionQueryService apiVersionQueryService;

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

    private MockMvc restApiVersionMockMvc;

    private ApiVersion apiVersion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApiVersionResource apiVersionResource = new ApiVersionResource(apiVersionService, apiVersionQueryService);
        this.restApiVersionMockMvc = MockMvcBuilders.standaloneSetup(apiVersionResource)
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
    public static ApiVersion createEntity(EntityManager em) {
        ApiVersion apiVersion = new ApiVersion()
            .version(DEFAULT_VERSION)
            .description(DEFAULT_DESCRIPTION);
        return apiVersion;
    }

    @Before
    public void initTest() {
        apiVersion = createEntity(em);
    }

    @Test
    @Transactional
    public void createApiVersion() throws Exception {
        int databaseSizeBeforeCreate = apiVersionRepository.findAll().size();

        // Create the ApiVersion
        ApiVersionDTO apiVersionDTO = apiVersionMapper.toDto(apiVersion);
        restApiVersionMockMvc.perform(post("/api/api-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiVersionDTO)))
            .andExpect(status().isCreated());

        // Validate the ApiVersion in the database
        List<ApiVersion> apiVersionList = apiVersionRepository.findAll();
        assertThat(apiVersionList).hasSize(databaseSizeBeforeCreate + 1);
        ApiVersion testApiVersion = apiVersionList.get(apiVersionList.size() - 1);
        assertThat(testApiVersion.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testApiVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createApiVersionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apiVersionRepository.findAll().size();

        // Create the ApiVersion with an existing ID
        apiVersion.setId(1L);
        ApiVersionDTO apiVersionDTO = apiVersionMapper.toDto(apiVersion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiVersionMockMvc.perform(post("/api/api-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiVersion in the database
        List<ApiVersion> apiVersionList = apiVersionRepository.findAll();
        assertThat(apiVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApiVersions() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList
        restApiVersionMockMvc.perform(get("/api/api-versions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getApiVersion() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get the apiVersion
        restApiVersionMockMvc.perform(get("/api/api-versions/{id}", apiVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apiVersion.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllApiVersionsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList where version equals to DEFAULT_VERSION
        defaultApiVersionShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the apiVersionList where version equals to UPDATED_VERSION
        defaultApiVersionShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllApiVersionsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultApiVersionShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the apiVersionList where version equals to UPDATED_VERSION
        defaultApiVersionShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllApiVersionsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList where version is not null
        defaultApiVersionShouldBeFound("version.specified=true");

        // Get all the apiVersionList where version is null
        defaultApiVersionShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiVersionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList where description equals to DEFAULT_DESCRIPTION
        defaultApiVersionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the apiVersionList where description equals to UPDATED_DESCRIPTION
        defaultApiVersionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApiVersionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultApiVersionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the apiVersionList where description equals to UPDATED_DESCRIPTION
        defaultApiVersionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApiVersionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        // Get all the apiVersionList where description is not null
        defaultApiVersionShouldBeFound("description.specified=true");

        // Get all the apiVersionList where description is null
        defaultApiVersionShouldNotBeFound("description.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultApiVersionShouldBeFound(String filter) throws Exception {
        restApiVersionMockMvc.perform(get("/api/api-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restApiVersionMockMvc.perform(get("/api/api-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultApiVersionShouldNotBeFound(String filter) throws Exception {
        restApiVersionMockMvc.perform(get("/api/api-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApiVersionMockMvc.perform(get("/api/api-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingApiVersion() throws Exception {
        // Get the apiVersion
        restApiVersionMockMvc.perform(get("/api/api-versions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApiVersion() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        int databaseSizeBeforeUpdate = apiVersionRepository.findAll().size();

        // Update the apiVersion
        ApiVersion updatedApiVersion = apiVersionRepository.findById(apiVersion.getId()).get();
        // Disconnect from session so that the updates on updatedApiVersion are not directly saved in db
        em.detach(updatedApiVersion);
        updatedApiVersion
            .version(UPDATED_VERSION)
            .description(UPDATED_DESCRIPTION);
        ApiVersionDTO apiVersionDTO = apiVersionMapper.toDto(updatedApiVersion);

        restApiVersionMockMvc.perform(put("/api/api-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiVersionDTO)))
            .andExpect(status().isOk());

        // Validate the ApiVersion in the database
        List<ApiVersion> apiVersionList = apiVersionRepository.findAll();
        assertThat(apiVersionList).hasSize(databaseSizeBeforeUpdate);
        ApiVersion testApiVersion = apiVersionList.get(apiVersionList.size() - 1);
        assertThat(testApiVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testApiVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingApiVersion() throws Exception {
        int databaseSizeBeforeUpdate = apiVersionRepository.findAll().size();

        // Create the ApiVersion
        ApiVersionDTO apiVersionDTO = apiVersionMapper.toDto(apiVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiVersionMockMvc.perform(put("/api/api-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiVersion in the database
        List<ApiVersion> apiVersionList = apiVersionRepository.findAll();
        assertThat(apiVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApiVersion() throws Exception {
        // Initialize the database
        apiVersionRepository.saveAndFlush(apiVersion);

        int databaseSizeBeforeDelete = apiVersionRepository.findAll().size();

        // Get the apiVersion
        restApiVersionMockMvc.perform(delete("/api/api-versions/{id}", apiVersion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApiVersion> apiVersionList = apiVersionRepository.findAll();
        assertThat(apiVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiVersion.class);
        ApiVersion apiVersion1 = new ApiVersion();
        apiVersion1.setId(1L);
        ApiVersion apiVersion2 = new ApiVersion();
        apiVersion2.setId(apiVersion1.getId());
        assertThat(apiVersion1).isEqualTo(apiVersion2);
        apiVersion2.setId(2L);
        assertThat(apiVersion1).isNotEqualTo(apiVersion2);
        apiVersion1.setId(null);
        assertThat(apiVersion1).isNotEqualTo(apiVersion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiVersionDTO.class);
        ApiVersionDTO apiVersionDTO1 = new ApiVersionDTO();
        apiVersionDTO1.setId(1L);
        ApiVersionDTO apiVersionDTO2 = new ApiVersionDTO();
        assertThat(apiVersionDTO1).isNotEqualTo(apiVersionDTO2);
        apiVersionDTO2.setId(apiVersionDTO1.getId());
        assertThat(apiVersionDTO1).isEqualTo(apiVersionDTO2);
        apiVersionDTO2.setId(2L);
        assertThat(apiVersionDTO1).isNotEqualTo(apiVersionDTO2);
        apiVersionDTO1.setId(null);
        assertThat(apiVersionDTO1).isNotEqualTo(apiVersionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(apiVersionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(apiVersionMapper.fromId(null)).isNull();
    }
}
