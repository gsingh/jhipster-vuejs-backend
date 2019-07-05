package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.PmillApp;
import com.pmill.vuejs.domain.Shipping;
import com.pmill.vuejs.repository.ShippingRepository;
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
 * Integration tests for the {@Link ShippingResource} REST controller.
 */
@SpringBootTest(classes = PmillApp.class)
public class ShippingResourceIT {

    private static final LocalDate DEFAULT_SHIPPING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SHIPPING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Shift DEFAULT_SHIFT = Shift.A;
    private static final Shift UPDATED_SHIFT = Shift.B;

    private static final Integer DEFAULT_NO_OF_WAGONS = 1;
    private static final Integer UPDATED_NO_OF_WAGONS = 2;

    private static final Integer DEFAULT_NO_OF_TRAILERS = 1;
    private static final Integer UPDATED_NO_OF_TRAILERS = 2;

    private static final Integer DEFAULT_SHIPPED_TONNAGE = 1;
    private static final Integer UPDATED_SHIPPED_TONNAGE = 2;

    @Autowired
    private ShippingRepository shippingRepository;

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

    private MockMvc restShippingMockMvc;

    private Shipping shipping;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShippingResource shippingResource = new ShippingResource(shippingRepository);
        this.restShippingMockMvc = MockMvcBuilders.standaloneSetup(shippingResource)
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
    public static Shipping createEntity(EntityManager em) {
        Shipping shipping = new Shipping()
            .shippingDate(DEFAULT_SHIPPING_DATE)
            .shift(DEFAULT_SHIFT)
            .noOfWagons(DEFAULT_NO_OF_WAGONS)
            .noOfTrailers(DEFAULT_NO_OF_TRAILERS)
            .shippedTonnage(DEFAULT_SHIPPED_TONNAGE);
        return shipping;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createUpdatedEntity(EntityManager em) {
        Shipping shipping = new Shipping()
            .shippingDate(UPDATED_SHIPPING_DATE)
            .shift(UPDATED_SHIFT)
            .noOfWagons(UPDATED_NO_OF_WAGONS)
            .noOfTrailers(UPDATED_NO_OF_TRAILERS)
            .shippedTonnage(UPDATED_SHIPPED_TONNAGE);
        return shipping;
    }

    @BeforeEach
    public void initTest() {
        shipping = createEntity(em);
    }

    @Test
    @Transactional
    public void createShipping() throws Exception {
        int databaseSizeBeforeCreate = shippingRepository.findAll().size();

        // Create the Shipping
        restShippingMockMvc.perform(post("/api/shippings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isCreated());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeCreate + 1);
        Shipping testShipping = shippingList.get(shippingList.size() - 1);
        assertThat(testShipping.getShippingDate()).isEqualTo(DEFAULT_SHIPPING_DATE);
        assertThat(testShipping.getShift()).isEqualTo(DEFAULT_SHIFT);
        assertThat(testShipping.getNoOfWagons()).isEqualTo(DEFAULT_NO_OF_WAGONS);
        assertThat(testShipping.getNoOfTrailers()).isEqualTo(DEFAULT_NO_OF_TRAILERS);
        assertThat(testShipping.getShippedTonnage()).isEqualTo(DEFAULT_SHIPPED_TONNAGE);
    }

    @Test
    @Transactional
    public void createShippingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shippingRepository.findAll().size();

        // Create the Shipping with an existing ID
        shipping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMockMvc.perform(post("/api/shippings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShippingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingRepository.findAll().size();
        // set the field null
        shipping.setShippingDate(null);

        // Create the Shipping, which fails.

        restShippingMockMvc.perform(post("/api/shippings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isBadRequest());

        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShiftIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingRepository.findAll().size();
        // set the field null
        shipping.setShift(null);

        // Create the Shipping, which fails.

        restShippingMockMvc.perform(post("/api/shippings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isBadRequest());

        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShippings() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList
        restShippingMockMvc.perform(get("/api/shippings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipping.getId().intValue())))
            .andExpect(jsonPath("$.[*].shippingDate").value(hasItem(DEFAULT_SHIPPING_DATE.toString())))
            .andExpect(jsonPath("$.[*].shift").value(hasItem(DEFAULT_SHIFT.toString())))
            .andExpect(jsonPath("$.[*].noOfWagons").value(hasItem(DEFAULT_NO_OF_WAGONS)))
            .andExpect(jsonPath("$.[*].noOfTrailers").value(hasItem(DEFAULT_NO_OF_TRAILERS)))
            .andExpect(jsonPath("$.[*].shippedTonnage").value(hasItem(DEFAULT_SHIPPED_TONNAGE)));
    }
    
    @Test
    @Transactional
    public void getShipping() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        // Get the shipping
        restShippingMockMvc.perform(get("/api/shippings/{id}", shipping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shipping.getId().intValue()))
            .andExpect(jsonPath("$.shippingDate").value(DEFAULT_SHIPPING_DATE.toString()))
            .andExpect(jsonPath("$.shift").value(DEFAULT_SHIFT.toString()))
            .andExpect(jsonPath("$.noOfWagons").value(DEFAULT_NO_OF_WAGONS))
            .andExpect(jsonPath("$.noOfTrailers").value(DEFAULT_NO_OF_TRAILERS))
            .andExpect(jsonPath("$.shippedTonnage").value(DEFAULT_SHIPPED_TONNAGE));
    }

    @Test
    @Transactional
    public void getNonExistingShipping() throws Exception {
        // Get the shipping
        restShippingMockMvc.perform(get("/api/shippings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShipping() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();

        // Update the shipping
        Shipping updatedShipping = shippingRepository.findById(shipping.getId()).get();
        // Disconnect from session so that the updates on updatedShipping are not directly saved in db
        em.detach(updatedShipping);
        updatedShipping
            .shippingDate(UPDATED_SHIPPING_DATE)
            .shift(UPDATED_SHIFT)
            .noOfWagons(UPDATED_NO_OF_WAGONS)
            .noOfTrailers(UPDATED_NO_OF_TRAILERS)
            .shippedTonnage(UPDATED_SHIPPED_TONNAGE);

        restShippingMockMvc.perform(put("/api/shippings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShipping)))
            .andExpect(status().isOk());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
        Shipping testShipping = shippingList.get(shippingList.size() - 1);
        assertThat(testShipping.getShippingDate()).isEqualTo(UPDATED_SHIPPING_DATE);
        assertThat(testShipping.getShift()).isEqualTo(UPDATED_SHIFT);
        assertThat(testShipping.getNoOfWagons()).isEqualTo(UPDATED_NO_OF_WAGONS);
        assertThat(testShipping.getNoOfTrailers()).isEqualTo(UPDATED_NO_OF_TRAILERS);
        assertThat(testShipping.getShippedTonnage()).isEqualTo(UPDATED_SHIPPED_TONNAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingShipping() throws Exception {
        int databaseSizeBeforeUpdate = shippingRepository.findAll().size();

        // Create the Shipping

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc.perform(put("/api/shippings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipping)))
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShipping() throws Exception {
        // Initialize the database
        shippingRepository.saveAndFlush(shipping);

        int databaseSizeBeforeDelete = shippingRepository.findAll().size();

        // Delete the shipping
        restShippingMockMvc.perform(delete("/api/shippings/{id}", shipping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shipping> shippingList = shippingRepository.findAll();
        assertThat(shippingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipping.class);
        Shipping shipping1 = new Shipping();
        shipping1.setId(1L);
        Shipping shipping2 = new Shipping();
        shipping2.setId(shipping1.getId());
        assertThat(shipping1).isEqualTo(shipping2);
        shipping2.setId(2L);
        assertThat(shipping1).isNotEqualTo(shipping2);
        shipping1.setId(null);
        assertThat(shipping1).isNotEqualTo(shipping2);
    }
}
