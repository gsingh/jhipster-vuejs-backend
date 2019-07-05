package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.PmillApp;
import com.pmill.vuejs.domain.Production;
import com.pmill.vuejs.repository.ProductionRepository;
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
 * Integration tests for the {@Link ProductionResource} REST controller.
 */
@SpringBootTest(classes = PmillApp.class)
public class ProductionResourceIT {

    private static final LocalDate DEFAULT_PROD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Shift DEFAULT_SHIFT = Shift.A;
    private static final Shift UPDATED_SHIFT = Shift.B;

    private static final Integer DEFAULT_NO_OF_PLATES = 1;
    private static final Integer UPDATED_NO_OF_PLATES = 2;

    private static final Integer DEFAULT_PROD_TONNAGE = 1;
    private static final Integer UPDATED_PROD_TONNAGE = 2;

    @Autowired
    private ProductionRepository productionRepository;

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

    private MockMvc restProductionMockMvc;

    private Production production;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductionResource productionResource = new ProductionResource(productionRepository);
        this.restProductionMockMvc = MockMvcBuilders.standaloneSetup(productionResource)
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
    public static Production createEntity(EntityManager em) {
        Production production = new Production()
            .prodDate(DEFAULT_PROD_DATE)
            .shift(DEFAULT_SHIFT)
            .noOfPlates(DEFAULT_NO_OF_PLATES)
            .prodTonnage(DEFAULT_PROD_TONNAGE);
        return production;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Production createUpdatedEntity(EntityManager em) {
        Production production = new Production()
            .prodDate(UPDATED_PROD_DATE)
            .shift(UPDATED_SHIFT)
            .noOfPlates(UPDATED_NO_OF_PLATES)
            .prodTonnage(UPDATED_PROD_TONNAGE);
        return production;
    }

    @BeforeEach
    public void initTest() {
        production = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduction() throws Exception {
        int databaseSizeBeforeCreate = productionRepository.findAll().size();

        // Create the Production
        restProductionMockMvc.perform(post("/api/productions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(production)))
            .andExpect(status().isCreated());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeCreate + 1);
        Production testProduction = productionList.get(productionList.size() - 1);
        assertThat(testProduction.getProdDate()).isEqualTo(DEFAULT_PROD_DATE);
        assertThat(testProduction.getShift()).isEqualTo(DEFAULT_SHIFT);
        assertThat(testProduction.getNoOfPlates()).isEqualTo(DEFAULT_NO_OF_PLATES);
        assertThat(testProduction.getProdTonnage()).isEqualTo(DEFAULT_PROD_TONNAGE);
    }

    @Test
    @Transactional
    public void createProductionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productionRepository.findAll().size();

        // Create the Production with an existing ID
        production.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductionMockMvc.perform(post("/api/productions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(production)))
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProdDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productionRepository.findAll().size();
        // set the field null
        production.setProdDate(null);

        // Create the Production, which fails.

        restProductionMockMvc.perform(post("/api/productions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(production)))
            .andExpect(status().isBadRequest());

        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShiftIsRequired() throws Exception {
        int databaseSizeBeforeTest = productionRepository.findAll().size();
        // set the field null
        production.setShift(null);

        // Create the Production, which fails.

        restProductionMockMvc.perform(post("/api/productions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(production)))
            .andExpect(status().isBadRequest());

        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductions() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        // Get all the productionList
        restProductionMockMvc.perform(get("/api/productions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(production.getId().intValue())))
            .andExpect(jsonPath("$.[*].prodDate").value(hasItem(DEFAULT_PROD_DATE.toString())))
            .andExpect(jsonPath("$.[*].shift").value(hasItem(DEFAULT_SHIFT.toString())))
            .andExpect(jsonPath("$.[*].noOfPlates").value(hasItem(DEFAULT_NO_OF_PLATES)))
            .andExpect(jsonPath("$.[*].prodTonnage").value(hasItem(DEFAULT_PROD_TONNAGE)));
    }
    
    @Test
    @Transactional
    public void getProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        // Get the production
        restProductionMockMvc.perform(get("/api/productions/{id}", production.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(production.getId().intValue()))
            .andExpect(jsonPath("$.prodDate").value(DEFAULT_PROD_DATE.toString()))
            .andExpect(jsonPath("$.shift").value(DEFAULT_SHIFT.toString()))
            .andExpect(jsonPath("$.noOfPlates").value(DEFAULT_NO_OF_PLATES))
            .andExpect(jsonPath("$.prodTonnage").value(DEFAULT_PROD_TONNAGE));
    }

    @Test
    @Transactional
    public void getNonExistingProduction() throws Exception {
        // Get the production
        restProductionMockMvc.perform(get("/api/productions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        int databaseSizeBeforeUpdate = productionRepository.findAll().size();

        // Update the production
        Production updatedProduction = productionRepository.findById(production.getId()).get();
        // Disconnect from session so that the updates on updatedProduction are not directly saved in db
        em.detach(updatedProduction);
        updatedProduction
            .prodDate(UPDATED_PROD_DATE)
            .shift(UPDATED_SHIFT)
            .noOfPlates(UPDATED_NO_OF_PLATES)
            .prodTonnage(UPDATED_PROD_TONNAGE);

        restProductionMockMvc.perform(put("/api/productions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduction)))
            .andExpect(status().isOk());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
        Production testProduction = productionList.get(productionList.size() - 1);
        assertThat(testProduction.getProdDate()).isEqualTo(UPDATED_PROD_DATE);
        assertThat(testProduction.getShift()).isEqualTo(UPDATED_SHIFT);
        assertThat(testProduction.getNoOfPlates()).isEqualTo(UPDATED_NO_OF_PLATES);
        assertThat(testProduction.getProdTonnage()).isEqualTo(UPDATED_PROD_TONNAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();

        // Create the Production

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionMockMvc.perform(put("/api/productions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(production)))
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        int databaseSizeBeforeDelete = productionRepository.findAll().size();

        // Delete the production
        restProductionMockMvc.perform(delete("/api/productions/{id}", production.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Production.class);
        Production production1 = new Production();
        production1.setId(1L);
        Production production2 = new Production();
        production2.setId(production1.getId());
        assertThat(production1).isEqualTo(production2);
        production2.setId(2L);
        assertThat(production1).isNotEqualTo(production2);
        production1.setId(null);
        assertThat(production1).isNotEqualTo(production2);
    }
}
