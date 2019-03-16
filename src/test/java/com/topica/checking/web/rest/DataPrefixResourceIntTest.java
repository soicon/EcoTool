package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.DataPrefix;
import com.topica.checking.repository.DataPrefixRepository;
import com.topica.checking.service.DataPrefixService;
import com.topica.checking.service.dto.DataPrefixDTO;
import com.topica.checking.service.mapper.DataPrefixMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.DataPrefixCriteria;
import com.topica.checking.service.DataPrefixQueryService;

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
 * Test class for the DataPrefixResource REST controller.
 *
 * @see DataPrefixResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class DataPrefixResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DataPrefixRepository dataPrefixRepository;

    @Autowired
    private DataPrefixMapper dataPrefixMapper;

    @Autowired
    private DataPrefixService dataPrefixService;

    @Autowired
    private DataPrefixQueryService dataPrefixQueryService;

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

    private MockMvc restDataPrefixMockMvc;

    private DataPrefix dataPrefix;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataPrefixResource dataPrefixResource = new DataPrefixResource(dataPrefixService, dataPrefixQueryService);
        this.restDataPrefixMockMvc = MockMvcBuilders.standaloneSetup(dataPrefixResource)
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
    public static DataPrefix createEntity(EntityManager em) {
        DataPrefix dataPrefix = new DataPrefix()
            .name(DEFAULT_NAME);
        return dataPrefix;
    }

    @Before
    public void initTest() {
        dataPrefix = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataPrefix() throws Exception {
        int databaseSizeBeforeCreate = dataPrefixRepository.findAll().size();

        // Create the DataPrefix
        DataPrefixDTO dataPrefixDTO = dataPrefixMapper.toDto(dataPrefix);
        restDataPrefixMockMvc.perform(post("/api/data-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataPrefixDTO)))
            .andExpect(status().isCreated());

        // Validate the DataPrefix in the database
        List<DataPrefix> dataPrefixList = dataPrefixRepository.findAll();
        assertThat(dataPrefixList).hasSize(databaseSizeBeforeCreate + 1);
        DataPrefix testDataPrefix = dataPrefixList.get(dataPrefixList.size() - 1);
        assertThat(testDataPrefix.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDataPrefixWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataPrefixRepository.findAll().size();

        // Create the DataPrefix with an existing ID
        dataPrefix.setId(1L);
        DataPrefixDTO dataPrefixDTO = dataPrefixMapper.toDto(dataPrefix);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataPrefixMockMvc.perform(post("/api/data-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataPrefixDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataPrefix in the database
        List<DataPrefix> dataPrefixList = dataPrefixRepository.findAll();
        assertThat(dataPrefixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataPrefixes() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        // Get all the dataPrefixList
        restDataPrefixMockMvc.perform(get("/api/data-prefixes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataPrefix.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getDataPrefix() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        // Get the dataPrefix
        restDataPrefixMockMvc.perform(get("/api/data-prefixes/{id}", dataPrefix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataPrefix.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllDataPrefixesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        // Get all the dataPrefixList where name equals to DEFAULT_NAME
        defaultDataPrefixShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the dataPrefixList where name equals to UPDATED_NAME
        defaultDataPrefixShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDataPrefixesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        // Get all the dataPrefixList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDataPrefixShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the dataPrefixList where name equals to UPDATED_NAME
        defaultDataPrefixShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDataPrefixesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        // Get all the dataPrefixList where name is not null
        defaultDataPrefixShouldBeFound("name.specified=true");

        // Get all the dataPrefixList where name is null
        defaultDataPrefixShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDataPrefixShouldBeFound(String filter) throws Exception {
        restDataPrefixMockMvc.perform(get("/api/data-prefixes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataPrefix.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));

        // Check, that the count call also returns 1
        restDataPrefixMockMvc.perform(get("/api/data-prefixes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDataPrefixShouldNotBeFound(String filter) throws Exception {
        restDataPrefixMockMvc.perform(get("/api/data-prefixes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataPrefixMockMvc.perform(get("/api/data-prefixes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDataPrefix() throws Exception {
        // Get the dataPrefix
        restDataPrefixMockMvc.perform(get("/api/data-prefixes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataPrefix() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        int databaseSizeBeforeUpdate = dataPrefixRepository.findAll().size();

        // Update the dataPrefix
        DataPrefix updatedDataPrefix = dataPrefixRepository.findById(dataPrefix.getId()).get();
        // Disconnect from session so that the updates on updatedDataPrefix are not directly saved in db
        em.detach(updatedDataPrefix);
        updatedDataPrefix
            .name(UPDATED_NAME);
        DataPrefixDTO dataPrefixDTO = dataPrefixMapper.toDto(updatedDataPrefix);

        restDataPrefixMockMvc.perform(put("/api/data-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataPrefixDTO)))
            .andExpect(status().isOk());

        // Validate the DataPrefix in the database
        List<DataPrefix> dataPrefixList = dataPrefixRepository.findAll();
        assertThat(dataPrefixList).hasSize(databaseSizeBeforeUpdate);
        DataPrefix testDataPrefix = dataPrefixList.get(dataPrefixList.size() - 1);
        assertThat(testDataPrefix.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDataPrefix() throws Exception {
        int databaseSizeBeforeUpdate = dataPrefixRepository.findAll().size();

        // Create the DataPrefix
        DataPrefixDTO dataPrefixDTO = dataPrefixMapper.toDto(dataPrefix);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataPrefixMockMvc.perform(put("/api/data-prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataPrefixDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataPrefix in the database
        List<DataPrefix> dataPrefixList = dataPrefixRepository.findAll();
        assertThat(dataPrefixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataPrefix() throws Exception {
        // Initialize the database
        dataPrefixRepository.saveAndFlush(dataPrefix);

        int databaseSizeBeforeDelete = dataPrefixRepository.findAll().size();

        // Get the dataPrefix
        restDataPrefixMockMvc.perform(delete("/api/data-prefixes/{id}", dataPrefix.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataPrefix> dataPrefixList = dataPrefixRepository.findAll();
        assertThat(dataPrefixList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataPrefix.class);
        DataPrefix dataPrefix1 = new DataPrefix();
        dataPrefix1.setId(1L);
        DataPrefix dataPrefix2 = new DataPrefix();
        dataPrefix2.setId(dataPrefix1.getId());
        assertThat(dataPrefix1).isEqualTo(dataPrefix2);
        dataPrefix2.setId(2L);
        assertThat(dataPrefix1).isNotEqualTo(dataPrefix2);
        dataPrefix1.setId(null);
        assertThat(dataPrefix1).isNotEqualTo(dataPrefix2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataPrefixDTO.class);
        DataPrefixDTO dataPrefixDTO1 = new DataPrefixDTO();
        dataPrefixDTO1.setId(1L);
        DataPrefixDTO dataPrefixDTO2 = new DataPrefixDTO();
        assertThat(dataPrefixDTO1).isNotEqualTo(dataPrefixDTO2);
        dataPrefixDTO2.setId(dataPrefixDTO1.getId());
        assertThat(dataPrefixDTO1).isEqualTo(dataPrefixDTO2);
        dataPrefixDTO2.setId(2L);
        assertThat(dataPrefixDTO1).isNotEqualTo(dataPrefixDTO2);
        dataPrefixDTO1.setId(null);
        assertThat(dataPrefixDTO1).isNotEqualTo(dataPrefixDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dataPrefixMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dataPrefixMapper.fromId(null)).isNull();
    }
}
