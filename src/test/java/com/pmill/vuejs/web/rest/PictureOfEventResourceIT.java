// package com.pmill.vuejs.web.rest;

// import com.pmill.vuejs.PmillApp;
// import com.pmill.vuejs.domain.PictureOfEvent;
// import com.pmill.vuejs.repository.PictureOfEventRepository;
// import com.pmill.vuejs.web.rest.errors.ExceptionTranslator;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.MockitoAnnotations;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
// import org.springframework.http.MediaType;
// import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.validation.Validator;

// import javax.persistence.EntityManager;
// import java.time.LocalDate;
// import java.time.ZoneId;
// import java.util.List;

// import static com.pmill.vuejs.web.rest.TestUtil.createFormattingConversionService;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.hamcrest.Matchers.hasItem;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// /**
//  * Integration tests for the {@Link PictureOfEventResource} REST controller.
//  */
// @SpringBootTest(classes = PmillApp.class)
// public class PictureOfEventResourceIT {

//     private static final LocalDate DEFAULT_PIC_DATE = LocalDate.ofEpochDay(0L);
//     private static final LocalDate UPDATED_PIC_DATE = LocalDate.now(ZoneId.systemDefault());

//     private static final String DEFAULT_IMG_FILE = "AAAAAAAAAA";
//     private static final String UPDATED_IMG_FILE = "BBBBBBBBBB";

//     @Autowired
//     private PictureOfEventRepository pictureOfEventRepository;

//     @Autowired
//     private MappingJackson2HttpMessageConverter jacksonMessageConverter;

//     @Autowired
//     private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

//     @Autowired
//     private ExceptionTranslator exceptionTranslator;

//     @Autowired
//     private EntityManager em;

//     @Autowired
//     private Validator validator;

//     private MockMvc restPictureOfEventMockMvc;

//     private PictureOfEvent pictureOfEvent;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.initMocks(this);
//         final PictureOfEventResource pictureOfEventResource = new PictureOfEventResource(pictureOfEventRepository);
//         this.restPictureOfEventMockMvc = MockMvcBuilders.standaloneSetup(pictureOfEventResource)
//             .setCustomArgumentResolvers(pageableArgumentResolver)
//             .setControllerAdvice(exceptionTranslator)
//             .setConversionService(createFormattingConversionService())
//             .setMessageConverters(jacksonMessageConverter)
//             .setValidator(validator).build();
//     }

//     /**
//      * Create an entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static PictureOfEvent createEntity(EntityManager em) {
//         PictureOfEvent pictureOfEvent = new PictureOfEvent()
//             .picDate(DEFAULT_PIC_DATE)
//             .imgFile(DEFAULT_IMG_FILE);
//         return pictureOfEvent;
//     }
//     /**
//      * Create an updated entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static PictureOfEvent createUpdatedEntity(EntityManager em) {
//         PictureOfEvent pictureOfEvent = new PictureOfEvent()
//             .picDate(UPDATED_PIC_DATE)
//             .imgFile(UPDATED_IMG_FILE);
//         return pictureOfEvent;
//     }

//     @BeforeEach
//     public void initTest() {
//         pictureOfEvent = createEntity(em);
//     }

//     @Test
//     @Transactional
//     public void createPictureOfEvent() throws Exception {
//         int databaseSizeBeforeCreate = pictureOfEventRepository.findAll().size();

//         // Create the PictureOfEvent
//         restPictureOfEventMockMvc.perform(post("/api/picture-of-events")
//             .contentType(TestUtil.APPLICATION_JSON_UTF8)
//             .content(TestUtil.convertObjectToJsonBytes(pictureOfEvent)))
//             .andExpect(status().isCreated());

//         // Validate the PictureOfEvent in the database
//         List<PictureOfEvent> pictureOfEventList = pictureOfEventRepository.findAll();
//         assertThat(pictureOfEventList).hasSize(databaseSizeBeforeCreate + 1);
//         PictureOfEvent testPictureOfEvent = pictureOfEventList.get(pictureOfEventList.size() - 1);
//         assertThat(testPictureOfEvent.getPicDate()).isEqualTo(DEFAULT_PIC_DATE);
//         assertThat(testPictureOfEvent.getImgFile()).isEqualTo(DEFAULT_IMG_FILE);
//     }

//     @Test
//     @Transactional
//     public void createPictureOfEventWithExistingId() throws Exception {
//         int databaseSizeBeforeCreate = pictureOfEventRepository.findAll().size();

//         // Create the PictureOfEvent with an existing ID
//         pictureOfEvent.setId(1L);

//         // An entity with an existing ID cannot be created, so this API call must fail
//         restPictureOfEventMockMvc.perform(post("/api/picture-of-events")
//             .contentType(TestUtil.APPLICATION_JSON_UTF8)
//             .content(TestUtil.convertObjectToJsonBytes(pictureOfEvent)))
//             .andExpect(status().isBadRequest());

//         // Validate the PictureOfEvent in the database
//         List<PictureOfEvent> pictureOfEventList = pictureOfEventRepository.findAll();
//         assertThat(pictureOfEventList).hasSize(databaseSizeBeforeCreate);
//     }


//     @Test
//     @Transactional
//     public void checkImgFileIsRequired() throws Exception {
//         int databaseSizeBeforeTest = pictureOfEventRepository.findAll().size();
//         // set the field null
//         pictureOfEvent.setImgFile(null);

//         // Create the PictureOfEvent, which fails.

//         restPictureOfEventMockMvc.perform(post("/api/picture-of-events")
//             .contentType(TestUtil.APPLICATION_JSON_UTF8)
//             .content(TestUtil.convertObjectToJsonBytes(pictureOfEvent)))
//             .andExpect(status().isBadRequest());

//         List<PictureOfEvent> pictureOfEventList = pictureOfEventRepository.findAll();
//         assertThat(pictureOfEventList).hasSize(databaseSizeBeforeTest);
//     }

//     @Test
//     @Transactional
//     public void getAllPictureOfEvents() throws Exception {
//         // Initialize the database
//         pictureOfEventRepository.saveAndFlush(pictureOfEvent);

//         // Get all the pictureOfEventList
//         restPictureOfEventMockMvc.perform(get("/api/picture-of-events?sort=id,desc"))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//             .andExpect(jsonPath("$.[*].id").value(hasItem(pictureOfEvent.getId().intValue())))
//             .andExpect(jsonPath("$.[*].picDate").value(hasItem(DEFAULT_PIC_DATE.toString())))
//             .andExpect(jsonPath("$.[*].imgFile").value(hasItem(DEFAULT_IMG_FILE.toString())));
//     }
    
//     @Test
//     @Transactional
//     public void getPictureOfEvent() throws Exception {
//         // Initialize the database
//         pictureOfEventRepository.saveAndFlush(pictureOfEvent);

//         // Get the pictureOfEvent
//         restPictureOfEventMockMvc.perform(get("/api/picture-of-events/{id}", pictureOfEvent.getId()))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//             .andExpect(jsonPath("$.id").value(pictureOfEvent.getId().intValue()))
//             .andExpect(jsonPath("$.picDate").value(DEFAULT_PIC_DATE.toString()))
//             .andExpect(jsonPath("$.imgFile").value(DEFAULT_IMG_FILE.toString()));
//     }

//     @Test
//     @Transactional
//     public void getNonExistingPictureOfEvent() throws Exception {
//         // Get the pictureOfEvent
//         restPictureOfEventMockMvc.perform(get("/api/picture-of-events/{id}", Long.MAX_VALUE))
//             .andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     public void updatePictureOfEvent() throws Exception {
//         // Initialize the database
//         pictureOfEventRepository.saveAndFlush(pictureOfEvent);

//         int databaseSizeBeforeUpdate = pictureOfEventRepository.findAll().size();

//         // Update the pictureOfEvent
//         PictureOfEvent updatedPictureOfEvent = pictureOfEventRepository.findById(pictureOfEvent.getId()).get();
//         // Disconnect from session so that the updates on updatedPictureOfEvent are not directly saved in db
//         em.detach(updatedPictureOfEvent);
//         updatedPictureOfEvent
//             .picDate(UPDATED_PIC_DATE)
//             .imgFile(UPDATED_IMG_FILE);

//         restPictureOfEventMockMvc.perform(put("/api/picture-of-events")
//             .contentType(TestUtil.APPLICATION_JSON_UTF8)
//             .content(TestUtil.convertObjectToJsonBytes(updatedPictureOfEvent)))
//             .andExpect(status().isOk());

//         // Validate the PictureOfEvent in the database
//         List<PictureOfEvent> pictureOfEventList = pictureOfEventRepository.findAll();
//         assertThat(pictureOfEventList).hasSize(databaseSizeBeforeUpdate);
//         PictureOfEvent testPictureOfEvent = pictureOfEventList.get(pictureOfEventList.size() - 1);
//         assertThat(testPictureOfEvent.getPicDate()).isEqualTo(UPDATED_PIC_DATE);
//         assertThat(testPictureOfEvent.getImgFile()).isEqualTo(UPDATED_IMG_FILE);
//     }

//     @Test
//     @Transactional
//     public void updateNonExistingPictureOfEvent() throws Exception {
//         int databaseSizeBeforeUpdate = pictureOfEventRepository.findAll().size();

//         // Create the PictureOfEvent

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restPictureOfEventMockMvc.perform(put("/api/picture-of-events")
//             .contentType(TestUtil.APPLICATION_JSON_UTF8)
//             .content(TestUtil.convertObjectToJsonBytes(pictureOfEvent)))
//             .andExpect(status().isBadRequest());

//         // Validate the PictureOfEvent in the database
//         List<PictureOfEvent> pictureOfEventList = pictureOfEventRepository.findAll();
//         assertThat(pictureOfEventList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     public void deletePictureOfEvent() throws Exception {
//         // Initialize the database
//         pictureOfEventRepository.saveAndFlush(pictureOfEvent);

//         int databaseSizeBeforeDelete = pictureOfEventRepository.findAll().size();

//         // Delete the pictureOfEvent
//         restPictureOfEventMockMvc.perform(delete("/api/picture-of-events/{id}", pictureOfEvent.getId())
//             .accept(TestUtil.APPLICATION_JSON_UTF8))
//             .andExpect(status().isNoContent());

//         // Validate the database contains one less item
//         List<PictureOfEvent> pictureOfEventList = pictureOfEventRepository.findAll();
//         assertThat(pictureOfEventList).hasSize(databaseSizeBeforeDelete - 1);
//     }

//     @Test
//     @Transactional
//     public void equalsVerifier() throws Exception {
//         TestUtil.equalsVerifier(PictureOfEvent.class);
//         PictureOfEvent pictureOfEvent1 = new PictureOfEvent();
//         pictureOfEvent1.setId(1L);
//         PictureOfEvent pictureOfEvent2 = new PictureOfEvent();
//         pictureOfEvent2.setId(pictureOfEvent1.getId());
//         assertThat(pictureOfEvent1).isEqualTo(pictureOfEvent2);
//         pictureOfEvent2.setId(2L);
//         assertThat(pictureOfEvent1).isNotEqualTo(pictureOfEvent2);
//         pictureOfEvent1.setId(null);
//         assertThat(pictureOfEvent1).isNotEqualTo(pictureOfEvent2);
//     }
// }
