package com.topica.checking.web.rest;

import com.topica.checking.EcoToolApp;

import com.topica.checking.domain.ServerStatus;
import com.topica.checking.repository.ServerStatusRepository;
import com.topica.checking.service.ServerStatusService;
import com.topica.checking.service.dto.ServerStatusDTO;
import com.topica.checking.service.mapper.ServerStatusMapper;
import com.topica.checking.web.rest.errors.ExceptionTranslator;

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
 * Test class for the ServerStatusResource REST controller.
 *
 * @see ServerStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoToolApp.class)
public class ServerStatusResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ServerStatusRepository serverStatusRepository;

    @Autowired
    private ServerStatusMapper serverStatusMapper;

    @Autowired
    private ServerStatusService serverStatusService;

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

    private MockMvc restServerStatusMockMvc;

    private ServerStatus serverStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServerStatusResource serverStatusResource = new ServerStatusResource(serverStatusService);
        this.restServerStatusMockMvc = MockMvcBuilders.standaloneSetup(serverStatusResource)
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
    public static ServerStatus createEntity(EntityManager em) {
        ServerStatus serverStatus = new ServerStatus()
            .address(DEFAULT_ADDRESS)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION);
        return serverStatus;
    }

    @Before
    public void initTest() {
        serverStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createServerStatus() throws Exception {
        int databaseSizeBeforeCreate = serverStatusRepository.findAll().size();

        // Create the ServerStatus
        ServerStatusDTO serverStatusDTO = serverStatusMapper.toDto(serverStatus);
        restServerStatusMockMvc.perform(post("/api/server-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the ServerStatus in the database
        List<ServerStatus> serverStatusList = serverStatusRepository.findAll();
        assertThat(serverStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ServerStatus testServerStatus = serverStatusList.get(serverStatusList.size() - 1);
        assertThat(testServerStatus.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testServerStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testServerStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createServerStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serverStatusRepository.findAll().size();

        // Create the ServerStatus with an existing ID
        serverStatus.setId(1L);
        ServerStatusDTO serverStatusDTO = serverStatusMapper.toDto(serverStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServerStatusMockMvc.perform(post("/api/server-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServerStatus in the database
        List<ServerStatus> serverStatusList = serverStatusRepository.findAll();
        assertThat(serverStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServerStatuses() throws Exception {
        // Initialize the database
        serverStatusRepository.saveAndFlush(serverStatus);

        // Get all the serverStatusList
        restServerStatusMockMvc.perform(get("/api/server-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serverStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getServerStatus() throws Exception {
        // Initialize the database
        serverStatusRepository.saveAndFlush(serverStatus);

        // Get the serverStatus
        restServerStatusMockMvc.perform(get("/api/server-statuses/{id}", serverStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serverStatus.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServerStatus() throws Exception {
        // Get the serverStatus
        restServerStatusMockMvc.perform(get("/api/server-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServerStatus() throws Exception {
        // Initialize the database
        serverStatusRepository.saveAndFlush(serverStatus);

        int databaseSizeBeforeUpdate = serverStatusRepository.findAll().size();

        // Update the serverStatus
        ServerStatus updatedServerStatus = serverStatusRepository.findById(serverStatus.getId()).get();
        // Disconnect from session so that the updates on updatedServerStatus are not directly saved in db
        em.detach(updatedServerStatus);
        updatedServerStatus
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);
        ServerStatusDTO serverStatusDTO = serverStatusMapper.toDto(updatedServerStatus);

        restServerStatusMockMvc.perform(put("/api/server-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverStatusDTO)))
            .andExpect(status().isOk());

        // Validate the ServerStatus in the database
        List<ServerStatus> serverStatusList = serverStatusRepository.findAll();
        assertThat(serverStatusList).hasSize(databaseSizeBeforeUpdate);
        ServerStatus testServerStatus = serverStatusList.get(serverStatusList.size() - 1);
        assertThat(testServerStatus.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testServerStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testServerStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingServerStatus() throws Exception {
        int databaseSizeBeforeUpdate = serverStatusRepository.findAll().size();

        // Create the ServerStatus
        ServerStatusDTO serverStatusDTO = serverStatusMapper.toDto(serverStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServerStatusMockMvc.perform(put("/api/server-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServerStatus in the database
        List<ServerStatus> serverStatusList = serverStatusRepository.findAll();
        assertThat(serverStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServerStatus() throws Exception {
        // Initialize the database
        serverStatusRepository.saveAndFlush(serverStatus);

        int databaseSizeBeforeDelete = serverStatusRepository.findAll().size();

        // Get the serverStatus
        restServerStatusMockMvc.perform(delete("/api/server-statuses/{id}", serverStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServerStatus> serverStatusList = serverStatusRepository.findAll();
        assertThat(serverStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServerStatus.class);
        ServerStatus serverStatus1 = new ServerStatus();
        serverStatus1.setId(1L);
        ServerStatus serverStatus2 = new ServerStatus();
        serverStatus2.setId(serverStatus1.getId());
        assertThat(serverStatus1).isEqualTo(serverStatus2);
        serverStatus2.setId(2L);
        assertThat(serverStatus1).isNotEqualTo(serverStatus2);
        serverStatus1.setId(null);
        assertThat(serverStatus1).isNotEqualTo(serverStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServerStatusDTO.class);
        ServerStatusDTO serverStatusDTO1 = new ServerStatusDTO();
        serverStatusDTO1.setId(1L);
        ServerStatusDTO serverStatusDTO2 = new ServerStatusDTO();
        assertThat(serverStatusDTO1).isNotEqualTo(serverStatusDTO2);
        serverStatusDTO2.setId(serverStatusDTO1.getId());
        assertThat(serverStatusDTO1).isEqualTo(serverStatusDTO2);
        serverStatusDTO2.setId(2L);
        assertThat(serverStatusDTO1).isNotEqualTo(serverStatusDTO2);
        serverStatusDTO1.setId(null);
        assertThat(serverStatusDTO1).isNotEqualTo(serverStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serverStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serverStatusMapper.fromId(null)).isNull();
    }
}
