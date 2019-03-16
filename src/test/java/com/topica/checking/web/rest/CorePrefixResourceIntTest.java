package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.CorePrefix;
import com.topica.checking.repository.CorePrefixRepository;
import com.topica.checking.service.CorePrefixService;
import com.topica.checking.service.dto.CorePrefixDTO;
import com.topica.checking.service.mapper.CorePrefixMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.CorePrefixCriteria;
import com.topica.checking.service.CorePrefixQueryService;

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
 * Test class for the CorePrefixResource REST controller.
 *
 * @see CorePrefixResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class CorePrefixResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CorePrefixRepository corePrefixRepository;

    @Autowired
    private CorePrefixMapper corePrefixMapper;

    @Autowired
    private CorePrefixService corePrefixService;

    @Autowired
    private CorePrefixQueryService corePrefixQueryService;

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

    private MockMvc restCorePrefixMockMvc;

    private CorePrefix corePrefix;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorePrefixResource corePrefixResource = new CorePrefixResource(corePrefixService, corePrefixQueryService);
        this.restCorePrefixMockMvc = MockMvcBuilders.standaloneSetup(corePrefixResource)
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
    public static CorePrefix createEntity(EntityManager em) {
        CorePrefix corePrefix = new CorePrefix()
            .name(DEFAULT_NAME);
        return corePrefix;
    }

    @Before
    public void initTest() {
        corePrefix = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorePrefix() throws Exception {
        int databaseSizeBeforeCreate = corePrefixRepository.findAll().size();

        // Create the CorePrefix
        CorePrefixDTO corePrefixDTO = corePrefixMapper.toDto(corePrefix);
        restCorePrefixMockMvc.perform(post("/api/core-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corePrefixDTO)))
            .andExpect(status().isCreated());

        // Validate the CorePrefix in the database
        List<CorePrefix> corePrefixList = corePrefixRepository.findAll();
        assertThat(corePrefixList).hasSize(databaseSizeBeforeCreate + 1);
        CorePrefix testCorePrefix = corePrefixList.get(corePrefixList.size() - 1);
        assertThat(testCorePrefix.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCorePrefixWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = corePrefixRepository.findAll().size();

        // Create the CorePrefix with an existing ID
        corePrefix.setId(1L);
        CorePrefixDTO corePrefixDTO = corePrefixMapper.toDto(corePrefix);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorePrefixMockMvc.perform(post("/api/core-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corePrefixDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CorePrefix in the database
        List<CorePrefix> corePrefixList = corePrefixRepository.findAll();
        assertThat(corePrefixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCorePrefixes() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        // Get all the corePrefixList
        restCorePrefixMockMvc.perform(get("/api/core-prefixes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corePrefix.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getCorePrefix() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        // Get the corePrefix
        restCorePrefixMockMvc.perform(get("/api/core-prefixes/{id}", corePrefix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corePrefix.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllCorePrefixesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        // Get all the corePrefixList where name equals to DEFAULT_NAME
        defaultCorePrefixShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the corePrefixList where name equals to UPDATED_NAME
        defaultCorePrefixShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCorePrefixesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        // Get all the corePrefixList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCorePrefixShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the corePrefixList where name equals to UPDATED_NAME
        defaultCorePrefixShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCorePrefixesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        // Get all the corePrefixList where name is not null
        defaultCorePrefixShouldBeFound("name.specified=true");

        // Get all the corePrefixList where name is null
        defaultCorePrefixShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCorePrefixShouldBeFound(String filter) throws Exception {
        restCorePrefixMockMvc.perform(get("/api/core-prefixes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corePrefix.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));

        // Check, that the count call also returns 1
        restCorePrefixMockMvc.perform(get("/api/core-prefixes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCorePrefixShouldNotBeFound(String filter) throws Exception {
        restCorePrefixMockMvc.perform(get("/api/core-prefixes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorePrefixMockMvc.perform(get("/api/core-prefixes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCorePrefix() throws Exception {
        // Get the corePrefix
        restCorePrefixMockMvc.perform(get("/api/core-prefixes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorePrefix() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        int databaseSizeBeforeUpdate = corePrefixRepository.findAll().size();

        // Update the corePrefix
        CorePrefix updatedCorePrefix = corePrefixRepository.findById(corePrefix.getId()).get();
        // Disconnect from session so that the updates on updatedCorePrefix are not directly saved in db
        em.detach(updatedCorePrefix);
        updatedCorePrefix
            .name(UPDATED_NAME);
        CorePrefixDTO corePrefixDTO = corePrefixMapper.toDto(updatedCorePrefix);

        restCorePrefixMockMvc.perform(put("/api/core-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corePrefixDTO)))
            .andExpect(status().isOk());

        // Validate the CorePrefix in the database
        List<CorePrefix> corePrefixList = corePrefixRepository.findAll();
        assertThat(corePrefixList).hasSize(databaseSizeBeforeUpdate);
        CorePrefix testCorePrefix = corePrefixList.get(corePrefixList.size() - 1);
        assertThat(testCorePrefix.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCorePrefix() throws Exception {
        int databaseSizeBeforeUpdate = corePrefixRepository.findAll().size();

        // Create the CorePrefix
        CorePrefixDTO corePrefixDTO = corePrefixMapper.toDto(corePrefix);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorePrefixMockMvc.perform(put("/api/core-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corePrefixDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CorePrefix in the database
        List<CorePrefix> corePrefixList = corePrefixRepository.findAll();
        assertThat(corePrefixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCorePrefix() throws Exception {
        // Initialize the database
        corePrefixRepository.saveAndFlush(corePrefix);

        int databaseSizeBeforeDelete = corePrefixRepository.findAll().size();

        // Get the corePrefix
        restCorePrefixMockMvc.perform(delete("/api/core-prefixes/{id}", corePrefix.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CorePrefix> corePrefixList = corePrefixRepository.findAll();
        assertThat(corePrefixList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorePrefix.class);
        CorePrefix corePrefix1 = new CorePrefix();
        corePrefix1.setId(1L);
        CorePrefix corePrefix2 = new CorePrefix();
        corePrefix2.setId(corePrefix1.getId());
        assertThat(corePrefix1).isEqualTo(corePrefix2);
        corePrefix2.setId(2L);
        assertThat(corePrefix1).isNotEqualTo(corePrefix2);
        corePrefix1.setId(null);
        assertThat(corePrefix1).isNotEqualTo(corePrefix2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorePrefixDTO.class);
        CorePrefixDTO corePrefixDTO1 = new CorePrefixDTO();
        corePrefixDTO1.setId(1L);
        CorePrefixDTO corePrefixDTO2 = new CorePrefixDTO();
        assertThat(corePrefixDTO1).isNotEqualTo(corePrefixDTO2);
        corePrefixDTO2.setId(corePrefixDTO1.getId());
        assertThat(corePrefixDTO1).isEqualTo(corePrefixDTO2);
        corePrefixDTO2.setId(2L);
        assertThat(corePrefixDTO1).isNotEqualTo(corePrefixDTO2);
        corePrefixDTO1.setId(null);
        assertThat(corePrefixDTO1).isNotEqualTo(corePrefixDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(corePrefixMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(corePrefixMapper.fromId(null)).isNull();
    }
}
