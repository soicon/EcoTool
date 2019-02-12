package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.RunnerLog;
import com.topica.checking.domain.ApiVersion;
import com.topica.checking.domain.DataVersion;
import com.topica.checking.domain.InputVersion;
import com.topica.checking.domain.Source;
import com.topica.checking.domain.Question;
import com.topica.checking.domain.Answer;
import com.topica.checking.repository.RunnerLogRepository;
import com.topica.checking.service.RunnerLogService;
import com.topica.checking.service.dto.RunnerLogDTO;
import com.topica.checking.service.mapper.RunnerLogMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.RunnerLogCriteria;
import com.topica.checking.service.RunnerLogQueryService;

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
 * Test class for the RunnerLogResource REST controller.
 *
 * @see RunnerLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class RunnerLogResourceIntTest {

    @Autowired
    private RunnerLogRepository runnerLogRepository;

    @Autowired
    private RunnerLogMapper runnerLogMapper;

    @Autowired
    private RunnerLogService runnerLogService;

    @Autowired
    private RunnerLogQueryService runnerLogQueryService;

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

    private MockMvc restRunnerLogMockMvc;

    private RunnerLog runnerLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RunnerLogResource runnerLogResource = new RunnerLogResource(runnerLogService, runnerLogQueryService);
        this.restRunnerLogMockMvc = MockMvcBuilders.standaloneSetup(runnerLogResource)
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
    public static RunnerLog createEntity(EntityManager em) {
        RunnerLog runnerLog = new RunnerLog();
        return runnerLog;
    }

    @Before
    public void initTest() {
        runnerLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createRunnerLog() throws Exception {
        int databaseSizeBeforeCreate = runnerLogRepository.findAll().size();

        // Create the RunnerLog
        RunnerLogDTO runnerLogDTO = runnerLogMapper.toDto(runnerLog);
        restRunnerLogMockMvc.perform(post("/api/runner-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(runnerLogDTO)))
            .andExpect(status().isCreated());

        // Validate the RunnerLog in the database
        List<RunnerLog> runnerLogList = runnerLogRepository.findAll();
        assertThat(runnerLogList).hasSize(databaseSizeBeforeCreate + 1);
        RunnerLog testRunnerLog = runnerLogList.get(runnerLogList.size() - 1);
    }

    @Test
    @Transactional
    public void createRunnerLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = runnerLogRepository.findAll().size();

        // Create the RunnerLog with an existing ID
        runnerLog.setId(1L);
        RunnerLogDTO runnerLogDTO = runnerLogMapper.toDto(runnerLog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRunnerLogMockMvc.perform(post("/api/runner-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(runnerLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RunnerLog in the database
        List<RunnerLog> runnerLogList = runnerLogRepository.findAll();
        assertThat(runnerLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRunnerLogs() throws Exception {
        // Initialize the database
        runnerLogRepository.saveAndFlush(runnerLog);

        // Get all the runnerLogList
        restRunnerLogMockMvc.perform(get("/api/runner-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(runnerLog.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getRunnerLog() throws Exception {
        // Initialize the database
        runnerLogRepository.saveAndFlush(runnerLog);

        // Get the runnerLog
        restRunnerLogMockMvc.perform(get("/api/runner-logs/{id}", runnerLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(runnerLog.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllRunnerLogsByApiversionIsEqualToSomething() throws Exception {
        // Initialize the database
        ApiVersion apiversion = ApiVersionResourceIntTest.createEntity(em);
        em.persist(apiversion);
        em.flush();
        runnerLog.setApiversion(apiversion);
        runnerLogRepository.saveAndFlush(runnerLog);
        Long apiversionId = apiversion.getId();

        // Get all the runnerLogList where apiversion equals to apiversionId
        defaultRunnerLogShouldBeFound("apiversionId.equals=" + apiversionId);

        // Get all the runnerLogList where apiversion equals to apiversionId + 1
        defaultRunnerLogShouldNotBeFound("apiversionId.equals=" + (apiversionId + 1));
    }


    @Test
    @Transactional
    public void getAllRunnerLogsByDataversionIsEqualToSomething() throws Exception {
        // Initialize the database
        DataVersion dataversion = DataVersionResourceIntTest.createEntity(em);
        em.persist(dataversion);
        em.flush();
        runnerLog.setDataversion(dataversion);
        runnerLogRepository.saveAndFlush(runnerLog);
        Long dataversionId = dataversion.getId();

        // Get all the runnerLogList where dataversion equals to dataversionId
        defaultRunnerLogShouldBeFound("dataversionId.equals=" + dataversionId);

        // Get all the runnerLogList where dataversion equals to dataversionId + 1
        defaultRunnerLogShouldNotBeFound("dataversionId.equals=" + (dataversionId + 1));
    }


    @Test
    @Transactional
    public void getAllRunnerLogsByInputversionIsEqualToSomething() throws Exception {
        // Initialize the database
        InputVersion inputversion = InputVersionResourceIntTest.createEntity(em);
        em.persist(inputversion);
        em.flush();
        runnerLog.setInputversion(inputversion);
        runnerLogRepository.saveAndFlush(runnerLog);
        Long inputversionId = inputversion.getId();

        // Get all the runnerLogList where inputversion equals to inputversionId
        defaultRunnerLogShouldBeFound("inputversionId.equals=" + inputversionId);

        // Get all the runnerLogList where inputversion equals to inputversionId + 1
        defaultRunnerLogShouldNotBeFound("inputversionId.equals=" + (inputversionId + 1));
    }


    @Test
    @Transactional
    public void getAllRunnerLogsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        Source source = SourceResourceIntTest.createEntity(em);
        em.persist(source);
        em.flush();
        runnerLog.setSource(source);
        runnerLogRepository.saveAndFlush(runnerLog);
        Long sourceId = source.getId();

        // Get all the runnerLogList where source equals to sourceId
        defaultRunnerLogShouldBeFound("sourceId.equals=" + sourceId);

        // Get all the runnerLogList where source equals to sourceId + 1
        defaultRunnerLogShouldNotBeFound("sourceId.equals=" + (sourceId + 1));
    }


    @Test
    @Transactional
    public void getAllRunnerLogsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        Question question = QuestionResourceIntTest.createEntity(em);
        em.persist(question);
        em.flush();
        runnerLog.setQuestion(question);
        runnerLogRepository.saveAndFlush(runnerLog);
        Long questionId = question.getId();

        // Get all the runnerLogList where question equals to questionId
        defaultRunnerLogShouldBeFound("questionId.equals=" + questionId);

        // Get all the runnerLogList where question equals to questionId + 1
        defaultRunnerLogShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }


    @Test
    @Transactional
    public void getAllRunnerLogsByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        Answer answer = AnswerResourceIntTest.createEntity(em);
        em.persist(answer);
        em.flush();
        runnerLog.setAnswer(answer);
        runnerLogRepository.saveAndFlush(runnerLog);
        Long answerId = answer.getId();

        // Get all the runnerLogList where answer equals to answerId
        defaultRunnerLogShouldBeFound("answerId.equals=" + answerId);

        // Get all the runnerLogList where answer equals to answerId + 1
        defaultRunnerLogShouldNotBeFound("answerId.equals=" + (answerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRunnerLogShouldBeFound(String filter) throws Exception {
        restRunnerLogMockMvc.perform(get("/api/runner-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(runnerLog.getId().intValue())));

        // Check, that the count call also returns 1
        restRunnerLogMockMvc.perform(get("/api/runner-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRunnerLogShouldNotBeFound(String filter) throws Exception {
        restRunnerLogMockMvc.perform(get("/api/runner-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRunnerLogMockMvc.perform(get("/api/runner-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRunnerLog() throws Exception {
        // Get the runnerLog
        restRunnerLogMockMvc.perform(get("/api/runner-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRunnerLog() throws Exception {
        // Initialize the database
        runnerLogRepository.saveAndFlush(runnerLog);

        int databaseSizeBeforeUpdate = runnerLogRepository.findAll().size();

        // Update the runnerLog
        RunnerLog updatedRunnerLog = runnerLogRepository.findById(runnerLog.getId()).get();
        // Disconnect from session so that the updates on updatedRunnerLog are not directly saved in db
        em.detach(updatedRunnerLog);
        RunnerLogDTO runnerLogDTO = runnerLogMapper.toDto(updatedRunnerLog);

        restRunnerLogMockMvc.perform(put("/api/runner-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(runnerLogDTO)))
            .andExpect(status().isOk());

        // Validate the RunnerLog in the database
        List<RunnerLog> runnerLogList = runnerLogRepository.findAll();
        assertThat(runnerLogList).hasSize(databaseSizeBeforeUpdate);
        RunnerLog testRunnerLog = runnerLogList.get(runnerLogList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingRunnerLog() throws Exception {
        int databaseSizeBeforeUpdate = runnerLogRepository.findAll().size();

        // Create the RunnerLog
        RunnerLogDTO runnerLogDTO = runnerLogMapper.toDto(runnerLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRunnerLogMockMvc.perform(put("/api/runner-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(runnerLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RunnerLog in the database
        List<RunnerLog> runnerLogList = runnerLogRepository.findAll();
        assertThat(runnerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRunnerLog() throws Exception {
        // Initialize the database
        runnerLogRepository.saveAndFlush(runnerLog);

        int databaseSizeBeforeDelete = runnerLogRepository.findAll().size();

        // Get the runnerLog
        restRunnerLogMockMvc.perform(delete("/api/runner-logs/{id}", runnerLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RunnerLog> runnerLogList = runnerLogRepository.findAll();
        assertThat(runnerLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RunnerLog.class);
        RunnerLog runnerLog1 = new RunnerLog();
        runnerLog1.setId(1L);
        RunnerLog runnerLog2 = new RunnerLog();
        runnerLog2.setId(runnerLog1.getId());
        assertThat(runnerLog1).isEqualTo(runnerLog2);
        runnerLog2.setId(2L);
        assertThat(runnerLog1).isNotEqualTo(runnerLog2);
        runnerLog1.setId(null);
        assertThat(runnerLog1).isNotEqualTo(runnerLog2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RunnerLogDTO.class);
        RunnerLogDTO runnerLogDTO1 = new RunnerLogDTO();
        runnerLogDTO1.setId(1L);
        RunnerLogDTO runnerLogDTO2 = new RunnerLogDTO();
        assertThat(runnerLogDTO1).isNotEqualTo(runnerLogDTO2);
        runnerLogDTO2.setId(runnerLogDTO1.getId());
        assertThat(runnerLogDTO1).isEqualTo(runnerLogDTO2);
        runnerLogDTO2.setId(2L);
        assertThat(runnerLogDTO1).isNotEqualTo(runnerLogDTO2);
        runnerLogDTO1.setId(null);
        assertThat(runnerLogDTO1).isNotEqualTo(runnerLogDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(runnerLogMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(runnerLogMapper.fromId(null)).isNull();
    }
}
