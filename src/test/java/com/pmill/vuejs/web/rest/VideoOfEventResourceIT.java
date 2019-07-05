package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.PmillApp;
import com.pmill.vuejs.domain.VideoOfEvent;
import com.pmill.vuejs.repository.VideoOfEventRepository;
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
 * Integration tests for the {@Link VideoOfEventResource} REST controller.
 */
@SpringBootTest(classes = PmillApp.class)
public class VideoOfEventResourceIT {

    private static final LocalDate DEFAULT_VIDEO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VIDEO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_VIDEO_FILE = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_FILE = "BBBBBBBBBB";

    @Autowired
    private VideoOfEventRepository videoOfEventRepository;

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

    private MockMvc restVideoOfEventMockMvc;

    private VideoOfEvent videoOfEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoOfEventResource videoOfEventResource = new VideoOfEventResource(videoOfEventRepository);
        this.restVideoOfEventMockMvc = MockMvcBuilders.standaloneSetup(videoOfEventResource)
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
    public static VideoOfEvent createEntity(EntityManager em) {
        VideoOfEvent videoOfEvent = new VideoOfEvent()
            .videoDate(DEFAULT_VIDEO_DATE)
            .videoFile(DEFAULT_VIDEO_FILE);
        return videoOfEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoOfEvent createUpdatedEntity(EntityManager em) {
        VideoOfEvent videoOfEvent = new VideoOfEvent()
            .videoDate(UPDATED_VIDEO_DATE)
            .videoFile(UPDATED_VIDEO_FILE);
        return videoOfEvent;
    }

    @BeforeEach
    public void initTest() {
        videoOfEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoOfEvent() throws Exception {
        int databaseSizeBeforeCreate = videoOfEventRepository.findAll().size();

        // Create the VideoOfEvent
        restVideoOfEventMockMvc.perform(post("/api/video-of-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoOfEvent)))
            .andExpect(status().isCreated());

        // Validate the VideoOfEvent in the database
        List<VideoOfEvent> videoOfEventList = videoOfEventRepository.findAll();
        assertThat(videoOfEventList).hasSize(databaseSizeBeforeCreate + 1);
        VideoOfEvent testVideoOfEvent = videoOfEventList.get(videoOfEventList.size() - 1);
        assertThat(testVideoOfEvent.getVideoDate()).isEqualTo(DEFAULT_VIDEO_DATE);
        assertThat(testVideoOfEvent.getVideoFile()).isEqualTo(DEFAULT_VIDEO_FILE);
    }

    @Test
    @Transactional
    public void createVideoOfEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoOfEventRepository.findAll().size();

        // Create the VideoOfEvent with an existing ID
        videoOfEvent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoOfEventMockMvc.perform(post("/api/video-of-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoOfEvent)))
            .andExpect(status().isBadRequest());

        // Validate the VideoOfEvent in the database
        List<VideoOfEvent> videoOfEventList = videoOfEventRepository.findAll();
        assertThat(videoOfEventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkVideoFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoOfEventRepository.findAll().size();
        // set the field null
        videoOfEvent.setVideoFile(null);

        // Create the VideoOfEvent, which fails.

        restVideoOfEventMockMvc.perform(post("/api/video-of-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoOfEvent)))
            .andExpect(status().isBadRequest());

        List<VideoOfEvent> videoOfEventList = videoOfEventRepository.findAll();
        assertThat(videoOfEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoOfEvents() throws Exception {
        // Initialize the database
        videoOfEventRepository.saveAndFlush(videoOfEvent);

        // Get all the videoOfEventList
        restVideoOfEventMockMvc.perform(get("/api/video-of-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoOfEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].videoDate").value(hasItem(DEFAULT_VIDEO_DATE.toString())))
            .andExpect(jsonPath("$.[*].videoFile").value(hasItem(DEFAULT_VIDEO_FILE.toString())));
    }
    
    @Test
    @Transactional
    public void getVideoOfEvent() throws Exception {
        // Initialize the database
        videoOfEventRepository.saveAndFlush(videoOfEvent);

        // Get the videoOfEvent
        restVideoOfEventMockMvc.perform(get("/api/video-of-events/{id}", videoOfEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoOfEvent.getId().intValue()))
            .andExpect(jsonPath("$.videoDate").value(DEFAULT_VIDEO_DATE.toString()))
            .andExpect(jsonPath("$.videoFile").value(DEFAULT_VIDEO_FILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoOfEvent() throws Exception {
        // Get the videoOfEvent
        restVideoOfEventMockMvc.perform(get("/api/video-of-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoOfEvent() throws Exception {
        // Initialize the database
        videoOfEventRepository.saveAndFlush(videoOfEvent);

        int databaseSizeBeforeUpdate = videoOfEventRepository.findAll().size();

        // Update the videoOfEvent
        VideoOfEvent updatedVideoOfEvent = videoOfEventRepository.findById(videoOfEvent.getId()).get();
        // Disconnect from session so that the updates on updatedVideoOfEvent are not directly saved in db
        em.detach(updatedVideoOfEvent);
        updatedVideoOfEvent
            .videoDate(UPDATED_VIDEO_DATE)
            .videoFile(UPDATED_VIDEO_FILE);

        restVideoOfEventMockMvc.perform(put("/api/video-of-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoOfEvent)))
            .andExpect(status().isOk());

        // Validate the VideoOfEvent in the database
        List<VideoOfEvent> videoOfEventList = videoOfEventRepository.findAll();
        assertThat(videoOfEventList).hasSize(databaseSizeBeforeUpdate);
        VideoOfEvent testVideoOfEvent = videoOfEventList.get(videoOfEventList.size() - 1);
        assertThat(testVideoOfEvent.getVideoDate()).isEqualTo(UPDATED_VIDEO_DATE);
        assertThat(testVideoOfEvent.getVideoFile()).isEqualTo(UPDATED_VIDEO_FILE);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoOfEvent() throws Exception {
        int databaseSizeBeforeUpdate = videoOfEventRepository.findAll().size();

        // Create the VideoOfEvent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoOfEventMockMvc.perform(put("/api/video-of-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoOfEvent)))
            .andExpect(status().isBadRequest());

        // Validate the VideoOfEvent in the database
        List<VideoOfEvent> videoOfEventList = videoOfEventRepository.findAll();
        assertThat(videoOfEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVideoOfEvent() throws Exception {
        // Initialize the database
        videoOfEventRepository.saveAndFlush(videoOfEvent);

        int databaseSizeBeforeDelete = videoOfEventRepository.findAll().size();

        // Delete the videoOfEvent
        restVideoOfEventMockMvc.perform(delete("/api/video-of-events/{id}", videoOfEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VideoOfEvent> videoOfEventList = videoOfEventRepository.findAll();
        assertThat(videoOfEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoOfEvent.class);
        VideoOfEvent videoOfEvent1 = new VideoOfEvent();
        videoOfEvent1.setId(1L);
        VideoOfEvent videoOfEvent2 = new VideoOfEvent();
        videoOfEvent2.setId(videoOfEvent1.getId());
        assertThat(videoOfEvent1).isEqualTo(videoOfEvent2);
        videoOfEvent2.setId(2L);
        assertThat(videoOfEvent1).isNotEqualTo(videoOfEvent2);
        videoOfEvent1.setId(null);
        assertThat(videoOfEvent1).isNotEqualTo(videoOfEvent2);
    }
}
