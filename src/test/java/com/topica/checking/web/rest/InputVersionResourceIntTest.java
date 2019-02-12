package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.InputVersion;
import com.topica.checking.repository.InputVersionRepository;
import com.topica.checking.service.InputVersionService;
import com.topica.checking.service.dto.InputVersionDTO;
import com.topica.checking.service.mapper.InputVersionMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.InputVersionCriteria;
import com.topica.checking.service.InputVersionQueryService;

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
 * Test class for the InputVersionResource REST controller.
 *
 * @see InputVersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class InputVersionResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private InputVersionRepository inputVersionRepository;

    @Autowired
    private InputVersionMapper inputVersionMapper;

    @Autowired
    private InputVersionService inputVersionService;

    @Autowired
    private InputVersionQueryService inputVersionQueryService;

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

    private MockMvc restInputVersionMockMvc;

    private InputVersion inputVersion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InputVersionResource inputVersionResource = new InputVersionResource(inputVersionService, inputVersionQueryService);
        this.restInputVersionMockMvc = MockMvcBuilders.standaloneSetup(inputVersionResource)
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
    public static InputVersion createEntity(EntityManager em) {
        InputVersion inputVersion = new InputVersion()
            .version(DEFAULT_VERSION)
            .description(DEFAULT_DESCRIPTION);
        return inputVersion;
    }

    @Before
    public void initTest() {
        inputVersion = createEntity(em);
    }

    @Test
    @Transactional
    public void createInputVersion() throws Exception {
        int databaseSizeBeforeCreate = inputVersionRepository.findAll().size();

        // Create the InputVersion
        InputVersionDTO inputVersionDTO = inputVersionMapper.toDto(inputVersion);
        restInputVersionMockMvc.perform(post("/api/input-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputVersionDTO)))
            .andExpect(status().isCreated());

        // Validate the InputVersion in the database
        List<InputVersion> inputVersionList = inputVersionRepository.findAll();
        assertThat(inputVersionList).hasSize(databaseSizeBeforeCreate + 1);
        InputVersion testInputVersion = inputVersionList.get(inputVersionList.size() - 1);
        assertThat(testInputVersion.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testInputVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInputVersionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inputVersionRepository.findAll().size();

        // Create the InputVersion with an existing ID
        inputVersion.setId(1L);
        InputVersionDTO inputVersionDTO = inputVersionMapper.toDto(inputVersion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInputVersionMockMvc.perform(post("/api/input-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InputVersion in the database
        List<InputVersion> inputVersionList = inputVersionRepository.findAll();
        assertThat(inputVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInputVersions() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList
        restInputVersionMockMvc.perform(get("/api/input-versions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inputVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getInputVersion() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get the inputVersion
        restInputVersionMockMvc.perform(get("/api/input-versions/{id}", inputVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inputVersion.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllInputVersionsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList where version equals to DEFAULT_VERSION
        defaultInputVersionShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the inputVersionList where version equals to UPDATED_VERSION
        defaultInputVersionShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllInputVersionsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultInputVersionShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the inputVersionList where version equals to UPDATED_VERSION
        defaultInputVersionShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllInputVersionsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList where version is not null
        defaultInputVersionShouldBeFound("version.specified=true");

        // Get all the inputVersionList where version is null
        defaultInputVersionShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllInputVersionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList where description equals to DEFAULT_DESCRIPTION
        defaultInputVersionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the inputVersionList where description equals to UPDATED_DESCRIPTION
        defaultInputVersionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInputVersionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInputVersionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the inputVersionList where description equals to UPDATED_DESCRIPTION
        defaultInputVersionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInputVersionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        // Get all the inputVersionList where description is not null
        defaultInputVersionShouldBeFound("description.specified=true");

        // Get all the inputVersionList where description is null
        defaultInputVersionShouldNotBeFound("description.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInputVersionShouldBeFound(String filter) throws Exception {
        restInputVersionMockMvc.perform(get("/api/input-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inputVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restInputVersionMockMvc.perform(get("/api/input-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInputVersionShouldNotBeFound(String filter) throws Exception {
        restInputVersionMockMvc.perform(get("/api/input-versions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInputVersionMockMvc.perform(get("/api/input-versions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInputVersion() throws Exception {
        // Get the inputVersion
        restInputVersionMockMvc.perform(get("/api/input-versions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInputVersion() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        int databaseSizeBeforeUpdate = inputVersionRepository.findAll().size();

        // Update the inputVersion
        InputVersion updatedInputVersion = inputVersionRepository.findById(inputVersion.getId()).get();
        // Disconnect from session so that the updates on updatedInputVersion are not directly saved in db
        em.detach(updatedInputVersion);
        updatedInputVersion
            .version(UPDATED_VERSION)
            .description(UPDATED_DESCRIPTION);
        InputVersionDTO inputVersionDTO = inputVersionMapper.toDto(updatedInputVersion);

        restInputVersionMockMvc.perform(put("/api/input-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputVersionDTO)))
            .andExpect(status().isOk());

        // Validate the InputVersion in the database
        List<InputVersion> inputVersionList = inputVersionRepository.findAll();
        assertThat(inputVersionList).hasSize(databaseSizeBeforeUpdate);
        InputVersion testInputVersion = inputVersionList.get(inputVersionList.size() - 1);
        assertThat(testInputVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testInputVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingInputVersion() throws Exception {
        int databaseSizeBeforeUpdate = inputVersionRepository.findAll().size();

        // Create the InputVersion
        InputVersionDTO inputVersionDTO = inputVersionMapper.toDto(inputVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputVersionMockMvc.perform(put("/api/input-versions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InputVersion in the database
        List<InputVersion> inputVersionList = inputVersionRepository.findAll();
        assertThat(inputVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInputVersion() throws Exception {
        // Initialize the database
        inputVersionRepository.saveAndFlush(inputVersion);

        int databaseSizeBeforeDelete = inputVersionRepository.findAll().size();

        // Get the inputVersion
        restInputVersionMockMvc.perform(delete("/api/input-versions/{id}", inputVersion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InputVersion> inputVersionList = inputVersionRepository.findAll();
        assertThat(inputVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputVersion.class);
        InputVersion inputVersion1 = new InputVersion();
        inputVersion1.setId(1L);
        InputVersion inputVersion2 = new InputVersion();
        inputVersion2.setId(inputVersion1.getId());
        assertThat(inputVersion1).isEqualTo(inputVersion2);
        inputVersion2.setId(2L);
        assertThat(inputVersion1).isNotEqualTo(inputVersion2);
        inputVersion1.setId(null);
        assertThat(inputVersion1).isNotEqualTo(inputVersion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputVersionDTO.class);
        InputVersionDTO inputVersionDTO1 = new InputVersionDTO();
        inputVersionDTO1.setId(1L);
        InputVersionDTO inputVersionDTO2 = new InputVersionDTO();
        assertThat(inputVersionDTO1).isNotEqualTo(inputVersionDTO2);
        inputVersionDTO2.setId(inputVersionDTO1.getId());
        assertThat(inputVersionDTO1).isEqualTo(inputVersionDTO2);
        inputVersionDTO2.setId(2L);
        assertThat(inputVersionDTO1).isNotEqualTo(inputVersionDTO2);
        inputVersionDTO1.setId(null);
        assertThat(inputVersionDTO1).isNotEqualTo(inputVersionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(inputVersionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(inputVersionMapper.fromId(null)).isNull();
    }
}
