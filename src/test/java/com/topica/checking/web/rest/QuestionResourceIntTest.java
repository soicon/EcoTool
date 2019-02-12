package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.Question;
import com.topica.checking.domain.Source;
import com.topica.checking.domain.Answer;
import com.topica.checking.repository.QuestionRepository;
import com.topica.checking.service.QuestionService;
import com.topica.checking.service.dto.QuestionDTO;
import com.topica.checking.service.mapper.QuestionMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;
import com.topica.checking.service.dto.QuestionCriteria;
import com.topica.checking.service.QuestionQueryService;

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
 * Test class for the QuestionResource REST controller.
 *
 * @see QuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class QuestionResourceIntTest {

    private static final String DEFAULT_QUESTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_VISIBLE = 1;
    private static final Integer UPDATED_VISIBLE = 2;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionQueryService questionQueryService;

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

    private MockMvc restQuestionMockMvc;

    private Question question;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionResource questionResource = new QuestionResource(questionService, questionQueryService);
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource)
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
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .question_text(DEFAULT_QUESTION_TEXT)
            .visible(DEFAULT_VISIBLE);
        return question;
    }

    @Before
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestion_text()).isEqualTo(DEFAULT_QUESTION_TEXT);
        assertThat(testQuestion.getVisible()).isEqualTo(DEFAULT_VISIBLE);
    }

    @Test
    @Transactional
    public void createQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].question_text").value(hasItem(DEFAULT_QUESTION_TEXT.toString())))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE)));
    }
    
    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.question_text").value(DEFAULT_QUESTION_TEXT.toString()))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE));
    }

    @Test
    @Transactional
    public void getAllQuestionsByQuestion_textIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where question_text equals to DEFAULT_QUESTION_TEXT
        defaultQuestionShouldBeFound("question_text.equals=" + DEFAULT_QUESTION_TEXT);

        // Get all the questionList where question_text equals to UPDATED_QUESTION_TEXT
        defaultQuestionShouldNotBeFound("question_text.equals=" + UPDATED_QUESTION_TEXT);
    }

    @Test
    @Transactional
    public void getAllQuestionsByQuestion_textIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where question_text in DEFAULT_QUESTION_TEXT or UPDATED_QUESTION_TEXT
        defaultQuestionShouldBeFound("question_text.in=" + DEFAULT_QUESTION_TEXT + "," + UPDATED_QUESTION_TEXT);

        // Get all the questionList where question_text equals to UPDATED_QUESTION_TEXT
        defaultQuestionShouldNotBeFound("question_text.in=" + UPDATED_QUESTION_TEXT);
    }

    @Test
    @Transactional
    public void getAllQuestionsByQuestion_textIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where question_text is not null
        defaultQuestionShouldBeFound("question_text.specified=true");

        // Get all the questionList where question_text is null
        defaultQuestionShouldNotBeFound("question_text.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByVisibleIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where visible equals to DEFAULT_VISIBLE
        defaultQuestionShouldBeFound("visible.equals=" + DEFAULT_VISIBLE);

        // Get all the questionList where visible equals to UPDATED_VISIBLE
        defaultQuestionShouldNotBeFound("visible.equals=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByVisibleIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where visible in DEFAULT_VISIBLE or UPDATED_VISIBLE
        defaultQuestionShouldBeFound("visible.in=" + DEFAULT_VISIBLE + "," + UPDATED_VISIBLE);

        // Get all the questionList where visible equals to UPDATED_VISIBLE
        defaultQuestionShouldNotBeFound("visible.in=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByVisibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where visible is not null
        defaultQuestionShouldBeFound("visible.specified=true");

        // Get all the questionList where visible is null
        defaultQuestionShouldNotBeFound("visible.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByVisibleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where visible greater than or equals to DEFAULT_VISIBLE
        defaultQuestionShouldBeFound("visible.greaterOrEqualThan=" + DEFAULT_VISIBLE);

        // Get all the questionList where visible greater than or equals to UPDATED_VISIBLE
        defaultQuestionShouldNotBeFound("visible.greaterOrEqualThan=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByVisibleIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where visible less than or equals to DEFAULT_VISIBLE
        defaultQuestionShouldNotBeFound("visible.lessThan=" + DEFAULT_VISIBLE);

        // Get all the questionList where visible less than or equals to UPDATED_VISIBLE
        defaultQuestionShouldBeFound("visible.lessThan=" + UPDATED_VISIBLE);
    }


    @Test
    @Transactional
    public void getAllQuestionsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        Source source = SourceResourceIntTest.createEntity(em);
        em.persist(source);
        em.flush();
        question.setSource(source);
        questionRepository.saveAndFlush(question);
        Long sourceId = source.getId();

        // Get all the questionList where source equals to sourceId
        defaultQuestionShouldBeFound("sourceId.equals=" + sourceId);

        // Get all the questionList where source equals to sourceId + 1
        defaultQuestionShouldNotBeFound("sourceId.equals=" + (sourceId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionsByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        Answer answer = AnswerResourceIntTest.createEntity(em);
        em.persist(answer);
        em.flush();
        question.setAnswer(answer);
        answer.setQuestion(question);
        questionRepository.saveAndFlush(question);
        Long answerId = answer.getId();

        // Get all the questionList where answer equals to answerId
        defaultQuestionShouldBeFound("answerId.equals=" + answerId);

        // Get all the questionList where answer equals to answerId + 1
        defaultQuestionShouldNotBeFound("answerId.equals=" + (answerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].question_text").value(hasItem(DEFAULT_QUESTION_TEXT.toString())))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE)));

        // Check, that the count call also returns 1
        restQuestionMockMvc.perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc.perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .question_text(UPDATED_QUESTION_TEXT)
            .visible(UPDATED_VISIBLE);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestion_text()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testQuestion.getVisible()).isEqualTo(UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Get the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = new Question();
        question1.setId(1L);
        Question question2 = new Question();
        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);
        question2.setId(2L);
        assertThat(question1).isNotEqualTo(question2);
        question1.setId(null);
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionDTO.class);
        QuestionDTO questionDTO1 = new QuestionDTO();
        questionDTO1.setId(1L);
        QuestionDTO questionDTO2 = new QuestionDTO();
        assertThat(questionDTO1).isNotEqualTo(questionDTO2);
        questionDTO2.setId(questionDTO1.getId());
        assertThat(questionDTO1).isEqualTo(questionDTO2);
        questionDTO2.setId(2L);
        assertThat(questionDTO1).isNotEqualTo(questionDTO2);
        questionDTO1.setId(null);
        assertThat(questionDTO1).isNotEqualTo(questionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(questionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(questionMapper.fromId(null)).isNull();
    }
}
