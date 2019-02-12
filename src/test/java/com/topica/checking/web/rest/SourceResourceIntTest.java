package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.Source;
import com.topica.checking.domain.Question;
import com.topica.checking.repository.SourceRepository;
import com.topica.checking.service.*;
import com.topica.checking.service.dto.SourceDTO;
import com.topica.checking.service.mapper.SourceMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.SourceCriteria;

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
import static org.assertj.core.api.Assertions.in;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SourceResource REST controller.
 *
 * @see SourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class SourceResourceIntTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Integer DEFAULT_NEED_RE_ANSWER = 1;
    private static final Integer UPDATED_NEED_RE_ANSWER = 2;

    private static final Integer DEFAULT_QUESTION_ID = 1;
    private static final Integer UPDATED_QUESTION_ID = 2;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceMapper sourceMapper;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ApiVersionService apiVersionService;

    @Autowired
    private InputVersionService inputVersionService;

    @Autowired
    private RunnerLogService runnerLogService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private SourceQueryService sourceQueryService;

    @Autowired
    private DataVersionService dataVersionService;

    @Autowired
    private FileStatusService fileStatusService;

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

    private MockMvc restSourceMockMvc;

    private Source source;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SourceResource sourceResource = new SourceResource(sourceService, sourceQueryService,questionService,answerService,dataVersionService,apiVersionService,inputVersionService,runnerLogService,fileStatusService);
        this.restSourceMockMvc = MockMvcBuilders.standaloneSetup(sourceResource)
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
    public static Source createEntity(EntityManager em) {
        Source source = new Source()
            .path(DEFAULT_PATH)
            .device_id(DEFAULT_DEVICE_ID)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .need_re_answer(DEFAULT_NEED_RE_ANSWER)
            .question_id(DEFAULT_QUESTION_ID);
        return source;
    }

    @Before
    public void initTest() {
        source = createEntity(em);
    }

    @Test
    @Transactional
    public void createSource() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Viewtool
        SourceDTO sourceDTO = sourceMapper.toDto(source);
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isCreated());

        // Validate the Viewtool in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate + 1);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testSource.getDevice_id()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testSource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSource.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSource.getNeed_re_answer()).isEqualTo(DEFAULT_NEED_RE_ANSWER);
        assertThat(testSource.getQuestion_id()).isEqualTo(DEFAULT_QUESTION_ID);
    }

    @Test
    @Transactional
    public void createSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Viewtool with an existing ID
        source.setId(1L);
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Viewtool in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSources() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc.perform(get("/api/sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].device_id").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].need_re_answer").value(hasItem(DEFAULT_NEED_RE_ANSWER)))
            .andExpect(jsonPath("$.[*].question_id").value(hasItem(DEFAULT_QUESTION_ID)));
    }
    
    @Test
    @Transactional
    public void getSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.device_id").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.need_re_answer").value(DEFAULT_NEED_RE_ANSWER))
            .andExpect(jsonPath("$.question_id").value(DEFAULT_QUESTION_ID));
    }

    @Test
    @Transactional
    public void getAllSourcesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where path equals to DEFAULT_PATH
        defaultSourceShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the sourceList where path equals to UPDATED_PATH
        defaultSourceShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllSourcesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where path in DEFAULT_PATH or UPDATED_PATH
        defaultSourceShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the sourceList where path equals to UPDATED_PATH
        defaultSourceShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllSourcesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where path is not null
        defaultSourceShouldBeFound("path.specified=true");

        // Get all the sourceList where path is null
        defaultSourceShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllSourcesByDevice_idIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where device_id equals to DEFAULT_DEVICE_ID
        defaultSourceShouldBeFound("device_id.equals=" + DEFAULT_DEVICE_ID);

        // Get all the sourceList where device_id equals to UPDATED_DEVICE_ID
        defaultSourceShouldNotBeFound("device_id.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllSourcesByDevice_idIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where device_id in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultSourceShouldBeFound("device_id.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the sourceList where device_id equals to UPDATED_DEVICE_ID
        defaultSourceShouldNotBeFound("device_id.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllSourcesByDevice_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where device_id is not null
        defaultSourceShouldBeFound("device_id.specified=true");

        // Get all the sourceList where device_id is null
        defaultSourceShouldNotBeFound("device_id.specified=false");
    }

    @Test
    @Transactional
    public void getAllSourcesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where type equals to DEFAULT_TYPE
        defaultSourceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the sourceList where type equals to UPDATED_TYPE
        defaultSourceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSourcesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSourceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the sourceList where type equals to UPDATED_TYPE
        defaultSourceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSourcesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where type is not null
        defaultSourceShouldBeFound("type.specified=true");

        // Get all the sourceList where type is null
        defaultSourceShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllSourcesByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where type greater than or equals to DEFAULT_TYPE
        defaultSourceShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the sourceList where type greater than or equals to UPDATED_TYPE
        defaultSourceShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSourcesByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where type less than or equals to DEFAULT_TYPE
        defaultSourceShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the sourceList where type less than or equals to UPDATED_TYPE
        defaultSourceShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllSourcesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where status equals to DEFAULT_STATUS
        defaultSourceShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the sourceList where status equals to UPDATED_STATUS
        defaultSourceShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSourcesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSourceShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the sourceList where status equals to UPDATED_STATUS
        defaultSourceShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSourcesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where status is not null
        defaultSourceShouldBeFound("status.specified=true");

        // Get all the sourceList where status is null
        defaultSourceShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllSourcesByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where status greater than or equals to DEFAULT_STATUS
        defaultSourceShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the sourceList where status greater than or equals to UPDATED_STATUS
        defaultSourceShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSourcesByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where status less than or equals to DEFAULT_STATUS
        defaultSourceShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the sourceList where status less than or equals to UPDATED_STATUS
        defaultSourceShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllSourcesByNeed_re_answerIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where need_re_answer equals to DEFAULT_NEED_RE_ANSWER
        defaultSourceShouldBeFound("need_re_answer.equals=" + DEFAULT_NEED_RE_ANSWER);

        // Get all the sourceList where need_re_answer equals to UPDATED_NEED_RE_ANSWER
        defaultSourceShouldNotBeFound("need_re_answer.equals=" + UPDATED_NEED_RE_ANSWER);
    }

    @Test
    @Transactional
    public void getAllSourcesByNeed_re_answerIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where need_re_answer in DEFAULT_NEED_RE_ANSWER or UPDATED_NEED_RE_ANSWER
        defaultSourceShouldBeFound("need_re_answer.in=" + DEFAULT_NEED_RE_ANSWER + "," + UPDATED_NEED_RE_ANSWER);

        // Get all the sourceList where need_re_answer equals to UPDATED_NEED_RE_ANSWER
        defaultSourceShouldNotBeFound("need_re_answer.in=" + UPDATED_NEED_RE_ANSWER);
    }

    @Test
    @Transactional
    public void getAllSourcesByNeed_re_answerIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where need_re_answer is not null
        defaultSourceShouldBeFound("need_re_answer.specified=true");

        // Get all the sourceList where need_re_answer is null
        defaultSourceShouldNotBeFound("need_re_answer.specified=false");
    }

    @Test
    @Transactional
    public void getAllSourcesByNeed_re_answerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where need_re_answer greater than or equals to DEFAULT_NEED_RE_ANSWER
        defaultSourceShouldBeFound("need_re_answer.greaterOrEqualThan=" + DEFAULT_NEED_RE_ANSWER);

        // Get all the sourceList where need_re_answer greater than or equals to UPDATED_NEED_RE_ANSWER
        defaultSourceShouldNotBeFound("need_re_answer.greaterOrEqualThan=" + UPDATED_NEED_RE_ANSWER);
    }

    @Test
    @Transactional
    public void getAllSourcesByNeed_re_answerIsLessThanSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where need_re_answer less than or equals to DEFAULT_NEED_RE_ANSWER
        defaultSourceShouldNotBeFound("need_re_answer.lessThan=" + DEFAULT_NEED_RE_ANSWER);

        // Get all the sourceList where need_re_answer less than or equals to UPDATED_NEED_RE_ANSWER
        defaultSourceShouldBeFound("need_re_answer.lessThan=" + UPDATED_NEED_RE_ANSWER);
    }


    @Test
    @Transactional
    public void getAllSourcesByQuestion_idIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where question_id equals to DEFAULT_QUESTION_ID
        defaultSourceShouldBeFound("question_id.equals=" + DEFAULT_QUESTION_ID);

        // Get all the sourceList where question_id equals to UPDATED_QUESTION_ID
        defaultSourceShouldNotBeFound("question_id.equals=" + UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    public void getAllSourcesByQuestion_idIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where question_id in DEFAULT_QUESTION_ID or UPDATED_QUESTION_ID
        defaultSourceShouldBeFound("question_id.in=" + DEFAULT_QUESTION_ID + "," + UPDATED_QUESTION_ID);

        // Get all the sourceList where question_id equals to UPDATED_QUESTION_ID
        defaultSourceShouldNotBeFound("question_id.in=" + UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    public void getAllSourcesByQuestion_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where question_id is not null
        defaultSourceShouldBeFound("question_id.specified=true");

        // Get all the sourceList where question_id is null
        defaultSourceShouldNotBeFound("question_id.specified=false");
    }

    @Test
    @Transactional
    public void getAllSourcesByQuestion_idIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where question_id greater than or equals to DEFAULT_QUESTION_ID
        defaultSourceShouldBeFound("question_id.greaterOrEqualThan=" + DEFAULT_QUESTION_ID);

        // Get all the sourceList where question_id greater than or equals to UPDATED_QUESTION_ID
        defaultSourceShouldNotBeFound("question_id.greaterOrEqualThan=" + UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    public void getAllSourcesByQuestion_idIsLessThanSomething() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList where question_id less than or equals to DEFAULT_QUESTION_ID
        defaultSourceShouldNotBeFound("question_id.lessThan=" + DEFAULT_QUESTION_ID);

        // Get all the sourceList where question_id less than or equals to UPDATED_QUESTION_ID
        defaultSourceShouldBeFound("question_id.lessThan=" + UPDATED_QUESTION_ID);
    }


    @Test
    @Transactional
    public void getAllSourcesByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        Question question = QuestionResourceIntTest.createEntity(em);
        em.persist(question);
        em.flush();
        source.addQuestion(question);
        sourceRepository.saveAndFlush(source);
        Long questionId = question.getId();

        // Get all the sourceList where question equals to questionId
        defaultSourceShouldBeFound("questionId.equals=" + questionId);

        // Get all the sourceList where question equals to questionId + 1
        defaultSourceShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSourceShouldBeFound(String filter) throws Exception {
        restSourceMockMvc.perform(get("/api/sources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].device_id").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].need_re_answer").value(hasItem(DEFAULT_NEED_RE_ANSWER)))
            .andExpect(jsonPath("$.[*].question_id").value(hasItem(DEFAULT_QUESTION_ID)));

        // Check, that the count call also returns 1
        restSourceMockMvc.perform(get("/api/sources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSourceShouldNotBeFound(String filter) throws Exception {
        restSourceMockMvc.perform(get("/api/sources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSourceMockMvc.perform(get("/api/sources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).get();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource
            .path(UPDATED_PATH)
            .device_id(UPDATED_DEVICE_ID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .need_re_answer(UPDATED_NEED_RE_ANSWER)
            .question_id(UPDATED_QUESTION_ID);
        SourceDTO sourceDTO = sourceMapper.toDto(updatedSource);

        restSourceMockMvc.perform(put("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isOk());

        // Validate the Viewtool in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testSource.getDevice_id()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSource.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSource.getNeed_re_answer()).isEqualTo(UPDATED_NEED_RE_ANSWER);
        assertThat(testSource.getQuestion_id()).isEqualTo(UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Create the Viewtool
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc.perform(put("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Viewtool in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeDelete = sourceRepository.findAll().size();

        // Get the source
        restSourceMockMvc.perform(delete("/api/sources/{id}", source.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Source.class);
        Source source1 = new Source();
        source1.setId(1L);
        Source source2 = new Source();
        source2.setId(source1.getId());
        assertThat(source1).isEqualTo(source2);
        source2.setId(2L);
        assertThat(source1).isNotEqualTo(source2);
        source1.setId(null);
        assertThat(source1).isNotEqualTo(source2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceDTO.class);
        SourceDTO sourceDTO1 = new SourceDTO();
        sourceDTO1.setId(1L);
        SourceDTO sourceDTO2 = new SourceDTO();
        assertThat(sourceDTO1).isNotEqualTo(sourceDTO2);
        sourceDTO2.setId(sourceDTO1.getId());
        assertThat(sourceDTO1).isEqualTo(sourceDTO2);
        sourceDTO2.setId(2L);
        assertThat(sourceDTO1).isNotEqualTo(sourceDTO2);
        sourceDTO1.setId(null);
        assertThat(sourceDTO1).isNotEqualTo(sourceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sourceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sourceMapper.fromId(null)).isNull();
    }
}
