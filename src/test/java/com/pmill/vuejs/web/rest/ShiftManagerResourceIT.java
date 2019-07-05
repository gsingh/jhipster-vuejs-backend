package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.PmillApp;
import com.pmill.vuejs.domain.ShiftManager;
import com.pmill.vuejs.repository.ShiftManagerRepository;
import com.pmill.vuejs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.pmill.vuejs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ShiftManagerResource} REST controller.
 */
@SpringBootTest(classes = PmillApp.class)
public class ShiftManagerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private ShiftManagerRepository shiftManagerRepository;

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

    private MockMvc restShiftManagerMockMvc;

    private ShiftManager shiftManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShiftManagerResource shiftManagerResource = new ShiftManagerResource(shiftManagerRepository);
        this.restShiftManagerMockMvc = MockMvcBuilders.standaloneSetup(shiftManagerResource)
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
    public static ShiftManager createEntity(EntityManager em) {
        ShiftManager shiftManager = new ShiftManager()
            .name(DEFAULT_NAME)
            .designation(DEFAULT_DESIGNATION)
            .mobileNumber(DEFAULT_MOBILE_NUMBER);
        return shiftManager;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShiftManager createUpdatedEntity(EntityManager em) {
        ShiftManager shiftManager = new ShiftManager()
            .name(UPDATED_NAME)
            .designation(UPDATED_DESIGNATION)
            .mobileNumber(UPDATED_MOBILE_NUMBER);
        return shiftManager;
    }

    @BeforeEach
    public void initTest() {
        shiftManager = createEntity(em);
    }

    @Test
    @Transactional
    public void createShiftManager() throws Exception {
        int databaseSizeBeforeCreate = shiftManagerRepository.findAll().size();

        // Create the ShiftManager
        restShiftManagerMockMvc.perform(post("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shiftManager)))
            .andExpect(status().isCreated());

        // Validate the ShiftManager in the database
        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeCreate + 1);
        ShiftManager testShiftManager = shiftManagerList.get(shiftManagerList.size() - 1);
        assertThat(testShiftManager.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShiftManager.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testShiftManager.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void createShiftManagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shiftManagerRepository.findAll().size();

        // Create the ShiftManager with an existing ID
        shiftManager.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShiftManagerMockMvc.perform(post("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shiftManager)))
            .andExpect(status().isBadRequest());

        // Validate the ShiftManager in the database
        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shiftManagerRepository.findAll().size();
        // set the field null
        shiftManager.setName(null);

        // Create the ShiftManager, which fails.

        restShiftManagerMockMvc.perform(post("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shiftManager)))
            .andExpect(status().isBadRequest());

        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = shiftManagerRepository.findAll().size();
        // set the field null
        shiftManager.setDesignation(null);

        // Create the ShiftManager, which fails.

        restShiftManagerMockMvc.perform(post("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shiftManager)))
            .andExpect(status().isBadRequest());

        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMobileNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = shiftManagerRepository.findAll().size();
        // set the field null
        shiftManager.setMobileNumber(null);

        // Create the ShiftManager, which fails.

        restShiftManagerMockMvc.perform(post("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shiftManager)))
            .andExpect(status().isBadRequest());

        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShiftManagers() throws Exception {
        // Initialize the database
        shiftManagerRepository.saveAndFlush(shiftManager);

        // Get all the shiftManagerList
        restShiftManagerMockMvc.perform(get("/api/shift-managers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shiftManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getShiftManager() throws Exception {
        // Initialize the database
        shiftManagerRepository.saveAndFlush(shiftManager);

        // Get the shiftManager
        restShiftManagerMockMvc.perform(get("/api/shift-managers/{id}", shiftManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shiftManager.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShiftManager() throws Exception {
        // Get the shiftManager
        restShiftManagerMockMvc.perform(get("/api/shift-managers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShiftManager() throws Exception {
        // Initialize the database
        shiftManagerRepository.saveAndFlush(shiftManager);

        int databaseSizeBeforeUpdate = shiftManagerRepository.findAll().size();

        // Update the shiftManager
        ShiftManager updatedShiftManager = shiftManagerRepository.findById(shiftManager.getId()).get();
        // Disconnect from session so that the updates on updatedShiftManager are not directly saved in db
        em.detach(updatedShiftManager);
        updatedShiftManager
            .name(UPDATED_NAME)
            .designation(UPDATED_DESIGNATION)
            .mobileNumber(UPDATED_MOBILE_NUMBER);

        restShiftManagerMockMvc.perform(put("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShiftManager)))
            .andExpect(status().isOk());

        // Validate the ShiftManager in the database
        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeUpdate);
        ShiftManager testShiftManager = shiftManagerList.get(shiftManagerList.size() - 1);
        assertThat(testShiftManager.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShiftManager.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testShiftManager.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingShiftManager() throws Exception {
        int databaseSizeBeforeUpdate = shiftManagerRepository.findAll().size();

        // Create the ShiftManager

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShiftManagerMockMvc.perform(put("/api/shift-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shiftManager)))
            .andExpect(status().isBadRequest());

        // Validate the ShiftManager in the database
        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShiftManager() throws Exception {
        // Initialize the database
        shiftManagerRepository.saveAndFlush(shiftManager);

        int databaseSizeBeforeDelete = shiftManagerRepository.findAll().size();

        // Delete the shiftManager
        restShiftManagerMockMvc.perform(delete("/api/shift-managers/{id}", shiftManager.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShiftManager> shiftManagerList = shiftManagerRepository.findAll();
        assertThat(shiftManagerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShiftManager.class);
        ShiftManager shiftManager1 = new ShiftManager();
        shiftManager1.setId(1L);
        ShiftManager shiftManager2 = new ShiftManager();
        shiftManager2.setId(shiftManager1.getId());
        assertThat(shiftManager1).isEqualTo(shiftManager2);
        shiftManager2.setId(2L);
        assertThat(shiftManager1).isNotEqualTo(shiftManager2);
        shiftManager1.setId(null);
        assertThat(shiftManager1).isNotEqualTo(shiftManager2);
    }
}
