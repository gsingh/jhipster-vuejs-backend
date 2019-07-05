package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.PmillApp;
import com.pmill.vuejs.domain.EventOfPlateMill;
import com.pmill.vuejs.repository.EventOfPlateMillRepository;
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

/**
 * Integration tests for the {@Link EventOfPlateMillResource} REST controller.
 */
@SpringBootTest(classes = PmillApp.class)
public class EventOfPlateMillResourceIT {

    private static final LocalDate DEFAULT_EVENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    @Autowired
    private EventOfPlateMillRepository eventOfPlateMillRepository;

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

    private MockMvc restEventOfPlateMillMockMvc;

    private EventOfPlateMill eventOfPlateMill;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventOfPlateMillResource eventOfPlateMillResource = new EventOfPlateMillResource(eventOfPlateMillRepository);
        this.restEventOfPlateMillMockMvc = MockMvcBuilders.standaloneSetup(eventOfPlateMillResource)
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
    public static EventOfPlateMill createEntity(EntityManager em) {
        EventOfPlateMill eventOfPlateMill = new EventOfPlateMill()
            .eventDate(DEFAULT_EVENT_DATE)
            .eventName(DEFAULT_EVENT_NAME);
        return eventOfPlateMill;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventOfPlateMill createUpdatedEntity(EntityManager em) {
        EventOfPlateMill eventOfPlateMill = new EventOfPlateMill()
            .eventDate(UPDATED_EVENT_DATE)
            .eventName(UPDATED_EVENT_NAME);
        return eventOfPlateMill;
    }

    @BeforeEach
    public void initTest() {
        eventOfPlateMill = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventOfPlateMill() throws Exception {
        int databaseSizeBeforeCreate = eventOfPlateMillRepository.findAll().size();

        // Create the EventOfPlateMill
        restEventOfPlateMillMockMvc.perform(post("/api/event-of-plate-mills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventOfPlateMill)))
            .andExpect(status().isCreated());

        // Validate the EventOfPlateMill in the database
        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeCreate + 1);
        EventOfPlateMill testEventOfPlateMill = eventOfPlateMillList.get(eventOfPlateMillList.size() - 1);
        assertThat(testEventOfPlateMill.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEventOfPlateMill.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
    }

    @Test
    @Transactional
    public void createEventOfPlateMillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventOfPlateMillRepository.findAll().size();

        // Create the EventOfPlateMill with an existing ID
        eventOfPlateMill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventOfPlateMillMockMvc.perform(post("/api/event-of-plate-mills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventOfPlateMill)))
            .andExpect(status().isBadRequest());

        // Validate the EventOfPlateMill in the database
        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEventDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOfPlateMillRepository.findAll().size();
        // set the field null
        eventOfPlateMill.setEventDate(null);

        // Create the EventOfPlateMill, which fails.

        restEventOfPlateMillMockMvc.perform(post("/api/event-of-plate-mills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventOfPlateMill)))
            .andExpect(status().isBadRequest());

        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOfPlateMillRepository.findAll().size();
        // set the field null
        eventOfPlateMill.setEventName(null);

        // Create the EventOfPlateMill, which fails.

        restEventOfPlateMillMockMvc.perform(post("/api/event-of-plate-mills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventOfPlateMill)))
            .andExpect(status().isBadRequest());

        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventOfPlateMills() throws Exception {
        // Initialize the database
        eventOfPlateMillRepository.saveAndFlush(eventOfPlateMill);

        // Get all the eventOfPlateMillList
        restEventOfPlateMillMockMvc.perform(get("/api/event-of-plate-mills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventOfPlateMill.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getEventOfPlateMill() throws Exception {
        // Initialize the database
        eventOfPlateMillRepository.saveAndFlush(eventOfPlateMill);

        // Get the eventOfPlateMill
        restEventOfPlateMillMockMvc.perform(get("/api/event-of-plate-mills/{id}", eventOfPlateMill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventOfPlateMill.getId().intValue()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventOfPlateMill() throws Exception {
        // Get the eventOfPlateMill
        restEventOfPlateMillMockMvc.perform(get("/api/event-of-plate-mills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventOfPlateMill() throws Exception {
        // Initialize the database
        eventOfPlateMillRepository.saveAndFlush(eventOfPlateMill);

        int databaseSizeBeforeUpdate = eventOfPlateMillRepository.findAll().size();

        // Update the eventOfPlateMill
        EventOfPlateMill updatedEventOfPlateMill = eventOfPlateMillRepository.findById(eventOfPlateMill.getId()).get();
        // Disconnect from session so that the updates on updatedEventOfPlateMill are not directly saved in db
        em.detach(updatedEventOfPlateMill);
        updatedEventOfPlateMill
            .eventDate(UPDATED_EVENT_DATE)
            .eventName(UPDATED_EVENT_NAME);

        restEventOfPlateMillMockMvc.perform(put("/api/event-of-plate-mills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventOfPlateMill)))
            .andExpect(status().isOk());

        // Validate the EventOfPlateMill in the database
        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeUpdate);
        EventOfPlateMill testEventOfPlateMill = eventOfPlateMillList.get(eventOfPlateMillList.size() - 1);
        assertThat(testEventOfPlateMill.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEventOfPlateMill.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEventOfPlateMill() throws Exception {
        int databaseSizeBeforeUpdate = eventOfPlateMillRepository.findAll().size();

        // Create the EventOfPlateMill

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventOfPlateMillMockMvc.perform(put("/api/event-of-plate-mills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventOfPlateMill)))
            .andExpect(status().isBadRequest());

        // Validate the EventOfPlateMill in the database
        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventOfPlateMill() throws Exception {
        // Initialize the database
        eventOfPlateMillRepository.saveAndFlush(eventOfPlateMill);

        int databaseSizeBeforeDelete = eventOfPlateMillRepository.findAll().size();

        // Delete the eventOfPlateMill
        restEventOfPlateMillMockMvc.perform(delete("/api/event-of-plate-mills/{id}", eventOfPlateMill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventOfPlateMill> eventOfPlateMillList = eventOfPlateMillRepository.findAll();
        assertThat(eventOfPlateMillList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventOfPlateMill.class);
        EventOfPlateMill eventOfPlateMill1 = new EventOfPlateMill();
        eventOfPlateMill1.setId(1L);
        EventOfPlateMill eventOfPlateMill2 = new EventOfPlateMill();
        eventOfPlateMill2.setId(eventOfPlateMill1.getId());
        assertThat(eventOfPlateMill1).isEqualTo(eventOfPlateMill2);
        eventOfPlateMill2.setId(2L);
        assertThat(eventOfPlateMill1).isNotEqualTo(eventOfPlateMill2);
        eventOfPlateMill1.setId(null);
        assertThat(eventOfPlateMill1).isNotEqualTo(eventOfPlateMill2);
    }
}
