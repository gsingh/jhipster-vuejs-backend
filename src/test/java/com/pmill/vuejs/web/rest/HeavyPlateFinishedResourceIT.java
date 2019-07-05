package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.PmillApp;
import com.pmill.vuejs.domain.HeavyPlateFinished;
import com.pmill.vuejs.repository.HeavyPlateFinishedRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.pmill.vuejs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pmill.vuejs.domain.enumeration.Shift;
/**
 * Integration tests for the {@Link HeavyPlateFinishedResource} REST controller.
 */
@SpringBootTest(classes = PmillApp.class)
public class HeavyPlateFinishedResourceIT {

    private static final LocalDate DEFAULT_H_P_FINISHED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_H_P_FINISHED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Shift DEFAULT_SHIFT = Shift.A;
    private static final Shift UPDATED_SHIFT = Shift.B;

    private static final Integer DEFAULT_NO_OF_PLATES = 1;
    private static final Integer UPDATED_NO_OF_PLATES = 2;

    private static final Integer DEFAULT_H_P_FINISHED_TONNAGE = 1;
    private static final Integer UPDATED_H_P_FINISHED_TONNAGE = 2;

    @Autowired
    private HeavyPlateFinishedRepository heavyPlateFinishedRepository;

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

    private MockMvc restHeavyPlateFinishedMockMvc;

    private HeavyPlateFinished heavyPlateFinished;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HeavyPlateFinishedResource heavyPlateFinishedResource = new HeavyPlateFinishedResource(heavyPlateFinishedRepository);
        this.restHeavyPlateFinishedMockMvc = MockMvcBuilders.standaloneSetup(heavyPlateFinishedResource)
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
    public static HeavyPlateFinished createEntity(EntityManager em) {
        HeavyPlateFinished heavyPlateFinished = new HeavyPlateFinished()
            .hPFinishedDate(DEFAULT_H_P_FINISHED_DATE)
            .shift(DEFAULT_SHIFT)
            .noOfPlates(DEFAULT_NO_OF_PLATES)
            .hPFinishedTonnage(DEFAULT_H_P_FINISHED_TONNAGE);
        return heavyPlateFinished;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HeavyPlateFinished createUpdatedEntity(EntityManager em) {
        HeavyPlateFinished heavyPlateFinished = new HeavyPlateFinished()
            .hPFinishedDate(UPDATED_H_P_FINISHED_DATE)
            .shift(UPDATED_SHIFT)
            .noOfPlates(UPDATED_NO_OF_PLATES)
            .hPFinishedTonnage(UPDATED_H_P_FINISHED_TONNAGE);
        return heavyPlateFinished;
    }

    @BeforeEach
    public void initTest() {
        heavyPlateFinished = createEntity(em);
    }

    @Test
    @Transactional
    public void createHeavyPlateFinished() throws Exception {
        int databaseSizeBeforeCreate = heavyPlateFinishedRepository.findAll().size();

        // Create the HeavyPlateFinished
        restHeavyPlateFinishedMockMvc.perform(post("/api/heavy-plate-finisheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heavyPlateFinished)))
            .andExpect(status().isCreated());

        // Validate the HeavyPlateFinished in the database
        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeCreate + 1);
        HeavyPlateFinished testHeavyPlateFinished = heavyPlateFinishedList.get(heavyPlateFinishedList.size() - 1);
        assertThat(testHeavyPlateFinished.gethPFinishedDate()).isEqualTo(DEFAULT_H_P_FINISHED_DATE);
        assertThat(testHeavyPlateFinished.getShift()).isEqualTo(DEFAULT_SHIFT);
        assertThat(testHeavyPlateFinished.getNoOfPlates()).isEqualTo(DEFAULT_NO_OF_PLATES);
        assertThat(testHeavyPlateFinished.gethPFinishedTonnage()).isEqualTo(DEFAULT_H_P_FINISHED_TONNAGE);
    }

    @Test
    @Transactional
    public void createHeavyPlateFinishedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = heavyPlateFinishedRepository.findAll().size();

        // Create the HeavyPlateFinished with an existing ID
        heavyPlateFinished.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeavyPlateFinishedMockMvc.perform(post("/api/heavy-plate-finisheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heavyPlateFinished)))
            .andExpect(status().isBadRequest());

        // Validate the HeavyPlateFinished in the database
        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkhPFinishedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = heavyPlateFinishedRepository.findAll().size();
        // set the field null
        heavyPlateFinished.sethPFinishedDate(null);

        // Create the HeavyPlateFinished, which fails.

        restHeavyPlateFinishedMockMvc.perform(post("/api/heavy-plate-finisheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heavyPlateFinished)))
            .andExpect(status().isBadRequest());

        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShiftIsRequired() throws Exception {
        int databaseSizeBeforeTest = heavyPlateFinishedRepository.findAll().size();
        // set the field null
        heavyPlateFinished.setShift(null);

        // Create the HeavyPlateFinished, which fails.

        restHeavyPlateFinishedMockMvc.perform(post("/api/heavy-plate-finisheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heavyPlateFinished)))
            .andExpect(status().isBadRequest());

        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHeavyPlateFinisheds() throws Exception {
        // Initialize the database
        heavyPlateFinishedRepository.saveAndFlush(heavyPlateFinished);

        // Get all the heavyPlateFinishedList
        restHeavyPlateFinishedMockMvc.perform(get("/api/heavy-plate-finisheds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(heavyPlateFinished.getId().intValue())))
            .andExpect(jsonPath("$.[*].hPFinishedDate").value(hasItem(DEFAULT_H_P_FINISHED_DATE.toString())))
            .andExpect(jsonPath("$.[*].shift").value(hasItem(DEFAULT_SHIFT.toString())))
            .andExpect(jsonPath("$.[*].noOfPlates").value(hasItem(DEFAULT_NO_OF_PLATES)))
            .andExpect(jsonPath("$.[*].hPFinishedTonnage").value(hasItem(DEFAULT_H_P_FINISHED_TONNAGE)));
    }
    
    @Test
    @Transactional
    public void getHeavyPlateFinished() throws Exception {
        // Initialize the database
        heavyPlateFinishedRepository.saveAndFlush(heavyPlateFinished);

        // Get the heavyPlateFinished
        restHeavyPlateFinishedMockMvc.perform(get("/api/heavy-plate-finisheds/{id}", heavyPlateFinished.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(heavyPlateFinished.getId().intValue()))
            .andExpect(jsonPath("$.hPFinishedDate").value(DEFAULT_H_P_FINISHED_DATE.toString()))
            .andExpect(jsonPath("$.shift").value(DEFAULT_SHIFT.toString()))
            .andExpect(jsonPath("$.noOfPlates").value(DEFAULT_NO_OF_PLATES))
            .andExpect(jsonPath("$.hPFinishedTonnage").value(DEFAULT_H_P_FINISHED_TONNAGE));
    }

    @Test
    @Transactional
    public void getNonExistingHeavyPlateFinished() throws Exception {
        // Get the heavyPlateFinished
        restHeavyPlateFinishedMockMvc.perform(get("/api/heavy-plate-finisheds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeavyPlateFinished() throws Exception {
        // Initialize the database
        heavyPlateFinishedRepository.saveAndFlush(heavyPlateFinished);

        int databaseSizeBeforeUpdate = heavyPlateFinishedRepository.findAll().size();

        // Update the heavyPlateFinished
        HeavyPlateFinished updatedHeavyPlateFinished = heavyPlateFinishedRepository.findById(heavyPlateFinished.getId()).get();
        // Disconnect from session so that the updates on updatedHeavyPlateFinished are not directly saved in db
        em.detach(updatedHeavyPlateFinished);
        updatedHeavyPlateFinished
            .hPFinishedDate(UPDATED_H_P_FINISHED_DATE)
            .shift(UPDATED_SHIFT)
            .noOfPlates(UPDATED_NO_OF_PLATES)
            .hPFinishedTonnage(UPDATED_H_P_FINISHED_TONNAGE);

        restHeavyPlateFinishedMockMvc.perform(put("/api/heavy-plate-finisheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHeavyPlateFinished)))
            .andExpect(status().isOk());

        // Validate the HeavyPlateFinished in the database
        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeUpdate);
        HeavyPlateFinished testHeavyPlateFinished = heavyPlateFinishedList.get(heavyPlateFinishedList.size() - 1);
        assertThat(testHeavyPlateFinished.gethPFinishedDate()).isEqualTo(UPDATED_H_P_FINISHED_DATE);
        assertThat(testHeavyPlateFinished.getShift()).isEqualTo(UPDATED_SHIFT);
        assertThat(testHeavyPlateFinished.getNoOfPlates()).isEqualTo(UPDATED_NO_OF_PLATES);
        assertThat(testHeavyPlateFinished.gethPFinishedTonnage()).isEqualTo(UPDATED_H_P_FINISHED_TONNAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingHeavyPlateFinished() throws Exception {
        int databaseSizeBeforeUpdate = heavyPlateFinishedRepository.findAll().size();

        // Create the HeavyPlateFinished

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeavyPlateFinishedMockMvc.perform(put("/api/heavy-plate-finisheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(heavyPlateFinished)))
            .andExpect(status().isBadRequest());

        // Validate the HeavyPlateFinished in the database
        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHeavyPlateFinished() throws Exception {
        // Initialize the database
        heavyPlateFinishedRepository.saveAndFlush(heavyPlateFinished);

        int databaseSizeBeforeDelete = heavyPlateFinishedRepository.findAll().size();

        // Delete the heavyPlateFinished
        restHeavyPlateFinishedMockMvc.perform(delete("/api/heavy-plate-finisheds/{id}", heavyPlateFinished.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HeavyPlateFinished> heavyPlateFinishedList = heavyPlateFinishedRepository.findAll();
        assertThat(heavyPlateFinishedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HeavyPlateFinished.class);
        HeavyPlateFinished heavyPlateFinished1 = new HeavyPlateFinished();
        heavyPlateFinished1.setId(1L);
        HeavyPlateFinished heavyPlateFinished2 = new HeavyPlateFinished();
        heavyPlateFinished2.setId(heavyPlateFinished1.getId());
        assertThat(heavyPlateFinished1).isEqualTo(heavyPlateFinished2);
        heavyPlateFinished2.setId(2L);
        assertThat(heavyPlateFinished1).isNotEqualTo(heavyPlateFinished2);
        heavyPlateFinished1.setId(null);
        assertThat(heavyPlateFinished1).isNotEqualTo(heavyPlateFinished2);
    }
}
