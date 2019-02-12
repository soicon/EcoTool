package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.Answer;
import com.topica.checking.domain.Question;
import com.topica.checking.repository.AnswerRepository;
import com.topica.checking.service.AnswerService;
import com.topica.checking.service.dto.AnswerDTO;
import com.topica.checking.service.mapper.AnswerMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.AnswerCriteria;
import com.topica.checking.service.AnswerQueryService;

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
 * Test class for the AnswerResource REST controller.
 *
 * @see AnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class AnswerResourceIntTest {

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_ANSWER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_REVIEWER_ID = 1;
    private static final Integer UPDATED_REVIEWER_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AnswerQueryService answerQueryService;

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

    private MockMvc restAnswerMockMvc;

    private Answer answer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnswerResource answerResource = new AnswerResource(answerService, answerQueryService);
        this.restAnswerMockMvc = MockMvcBuilders.standaloneSetup(answerResource)
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
    public static Answer createEntity(EntityManager em) {
        Answer answer = new Answer()
            .status(DEFAULT_STATUS)
            .answer_text(DEFAULT_ANSWER_TEXT)
            .reviewer_id(DEFAULT_REVIEWER_ID)
            .user_id(DEFAULT_USER_ID);
        return answer;
    }

    @Before
    public void initTest() {
        answer = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);
        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAnswer.getAnswer_text()).isEqualTo(DEFAULT_ANSWER_TEXT);
        assertThat(testAnswer.getReviewer_id()).isEqualTo(DEFAULT_REVIEWER_ID);
        assertThat(testAnswer.getUser_id()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer with an existing ID
        answer.setId(1L);
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList
        restAnswerMockMvc.perform(get("/api/answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].answer_text").value(hasItem(DEFAULT_ANSWER_TEXT.toString())))
            .andExpect(jsonPath("$.[*].reviewer_id").value(hasItem(DEFAULT_REVIEWER_ID)))
            .andExpect(jsonPath("$.[*].user_id").value(hasItem(DEFAULT_USER_ID)));
    }
    
    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.answer_text").value(DEFAULT_ANSWER_TEXT.toString()))
            .andExpect(jsonPath("$.reviewer_id").value(DEFAULT_REVIEWER_ID))
            .andExpect(jsonPath("$.user_id").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    public void getAllAnswersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where status equals to DEFAULT_STATUS
        defaultAnswerShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the answerList where status equals to UPDATED_STATUS
        defaultAnswerShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAnswersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAnswerShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the answerList where status equals to UPDATED_STATUS
        defaultAnswerShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAnswersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where status is not null
        defaultAnswerShouldBeFound("status.specified=true");

        // Get all the answerList where status is null
        defaultAnswerShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where status greater than or equals to DEFAULT_STATUS
        defaultAnswerShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the answerList where status greater than or equals to UPDATED_STATUS
        defaultAnswerShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAnswersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where status less than or equals to DEFAULT_STATUS
        defaultAnswerShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the answerList where status less than or equals to UPDATED_STATUS
        defaultAnswerShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllAnswersByAnswer_textIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where answer_text equals to DEFAULT_ANSWER_TEXT
        defaultAnswerShouldBeFound("answer_text.equals=" + DEFAULT_ANSWER_TEXT);

        // Get all the answerList where answer_text equals to UPDATED_ANSWER_TEXT
        defaultAnswerShouldNotBeFound("answer_text.equals=" + UPDATED_ANSWER_TEXT);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnswer_textIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where answer_text in DEFAULT_ANSWER_TEXT or UPDATED_ANSWER_TEXT
        defaultAnswerShouldBeFound("answer_text.in=" + DEFAULT_ANSWER_TEXT + "," + UPDATED_ANSWER_TEXT);

        // Get all the answerList where answer_text equals to UPDATED_ANSWER_TEXT
        defaultAnswerShouldNotBeFound("answer_text.in=" + UPDATED_ANSWER_TEXT);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnswer_textIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where answer_text is not null
        defaultAnswerShouldBeFound("answer_text.specified=true");

        // Get all the answerList where answer_text is null
        defaultAnswerShouldNotBeFound("answer_text.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByReviewer_idIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where reviewer_id equals to DEFAULT_REVIEWER_ID
        defaultAnswerShouldBeFound("reviewer_id.equals=" + DEFAULT_REVIEWER_ID);

        // Get all the answerList where reviewer_id equals to UPDATED_REVIEWER_ID
        defaultAnswerShouldNotBeFound("reviewer_id.equals=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllAnswersByReviewer_idIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where reviewer_id in DEFAULT_REVIEWER_ID or UPDATED_REVIEWER_ID
        defaultAnswerShouldBeFound("reviewer_id.in=" + DEFAULT_REVIEWER_ID + "," + UPDATED_REVIEWER_ID);

        // Get all the answerList where reviewer_id equals to UPDATED_REVIEWER_ID
        defaultAnswerShouldNotBeFound("reviewer_id.in=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllAnswersByReviewer_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where reviewer_id is not null
        defaultAnswerShouldBeFound("reviewer_id.specified=true");

        // Get all the answerList where reviewer_id is null
        defaultAnswerShouldNotBeFound("reviewer_id.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByReviewer_idIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where reviewer_id greater than or equals to DEFAULT_REVIEWER_ID
        defaultAnswerShouldBeFound("reviewer_id.greaterOrEqualThan=" + DEFAULT_REVIEWER_ID);

        // Get all the answerList where reviewer_id greater than or equals to UPDATED_REVIEWER_ID
        defaultAnswerShouldNotBeFound("reviewer_id.greaterOrEqualThan=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllAnswersByReviewer_idIsLessThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where reviewer_id less than or equals to DEFAULT_REVIEWER_ID
        defaultAnswerShouldNotBeFound("reviewer_id.lessThan=" + DEFAULT_REVIEWER_ID);

        // Get all the answerList where reviewer_id less than or equals to UPDATED_REVIEWER_ID
        defaultAnswerShouldBeFound("reviewer_id.lessThan=" + UPDATED_REVIEWER_ID);
    }


    @Test
    @Transactional
    public void getAllAnswersByUser_idIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where user_id equals to DEFAULT_USER_ID
        defaultAnswerShouldBeFound("user_id.equals=" + DEFAULT_USER_ID);

        // Get all the answerList where user_id equals to UPDATED_USER_ID
        defaultAnswerShouldNotBeFound("user_id.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAnswersByUser_idIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where user_id in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultAnswerShouldBeFound("user_id.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the answerList where user_id equals to UPDATED_USER_ID
        defaultAnswerShouldNotBeFound("user_id.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAnswersByUser_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where user_id is not null
        defaultAnswerShouldBeFound("user_id.specified=true");

        // Get all the answerList where user_id is null
        defaultAnswerShouldNotBeFound("user_id.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByUser_idIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where user_id greater than or equals to DEFAULT_USER_ID
        defaultAnswerShouldBeFound("user_id.greaterOrEqualThan=" + DEFAULT_USER_ID);

        // Get all the answerList where user_id greater than or equals to UPDATED_USER_ID
        defaultAnswerShouldNotBeFound("user_id.greaterOrEqualThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAnswersByUser_idIsLessThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where user_id less than or equals to DEFAULT_USER_ID
        defaultAnswerShouldNotBeFound("user_id.lessThan=" + DEFAULT_USER_ID);

        // Get all the answerList where user_id less than or equals to UPDATED_USER_ID
        defaultAnswerShouldBeFound("user_id.lessThan=" + UPDATED_USER_ID);
    }


    @Test
    @Transactional
    public void getAllAnswersByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        Question question = QuestionResourceIntTest.createEntity(em);
        em.persist(question);
        em.flush();
        answer.setQuestion(question);
        answerRepository.saveAndFlush(answer);
        Long questionId = question.getId();

        // Get all the answerList where question equals to questionId
        defaultAnswerShouldBeFound("questionId.equals=" + questionId);

        // Get all the answerList where question equals to questionId + 1
        defaultAnswerShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAnswerShouldBeFound(String filter) throws Exception {
        restAnswerMockMvc.perform(get("/api/answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].answer_text").value(hasItem(DEFAULT_ANSWER_TEXT.toString())))
            .andExpect(jsonPath("$.[*].reviewer_id").value(hasItem(DEFAULT_REVIEWER_ID)))
            .andExpect(jsonPath("$.[*].user_id").value(hasItem(DEFAULT_USER_ID)));

        // Check, that the count call also returns 1
        restAnswerMockMvc.perform(get("/api/answers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAnswerShouldNotBeFound(String filter) throws Exception {
        restAnswerMockMvc.perform(get("/api/answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnswerMockMvc.perform(get("/api/answers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();
        // Disconnect from session so that the updates on updatedAnswer are not directly saved in db
        em.detach(updatedAnswer);
        updatedAnswer
            .status(UPDATED_STATUS)
            .answer_text(UPDATED_ANSWER_TEXT)
            .reviewer_id(UPDATED_REVIEWER_ID)
            .user_id(UPDATED_USER_ID);
        AnswerDTO answerDTO = answerMapper.toDto(updatedAnswer);

        restAnswerMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAnswer.getAnswer_text()).isEqualTo(UPDATED_ANSWER_TEXT);
        assertThat(testAnswer.getReviewer_id()).isEqualTo(UPDATED_REVIEWER_ID);
        assertThat(testAnswer.getUser_id()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Get the answer
        restAnswerMockMvc.perform(delete("/api/answers/{id}", answer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answer.class);
        Answer answer1 = new Answer();
        answer1.setId(1L);
        Answer answer2 = new Answer();
        answer2.setId(answer1.getId());
        assertThat(answer1).isEqualTo(answer2);
        answer2.setId(2L);
        assertThat(answer1).isNotEqualTo(answer2);
        answer1.setId(null);
        assertThat(answer1).isNotEqualTo(answer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerDTO.class);
        AnswerDTO answerDTO1 = new AnswerDTO();
        answerDTO1.setId(1L);
        AnswerDTO answerDTO2 = new AnswerDTO();
        assertThat(answerDTO1).isNotEqualTo(answerDTO2);
        answerDTO2.setId(answerDTO1.getId());
        assertThat(answerDTO1).isEqualTo(answerDTO2);
        answerDTO2.setId(2L);
        assertThat(answerDTO1).isNotEqualTo(answerDTO2);
        answerDTO1.setId(null);
        assertThat(answerDTO1).isNotEqualTo(answerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(answerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(answerMapper.fromId(null)).isNull();
    }
}
