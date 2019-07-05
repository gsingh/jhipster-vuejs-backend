package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.domain.VideoOfEvent;
import com.pmill.vuejs.repository.VideoOfEventRepository;
import com.pmill.vuejs.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pmill.vuejs.domain.VideoOfEvent}.
 */
@RestController
@RequestMapping("/api")
public class VideoOfEventResource {

    private final Logger log = LoggerFactory.getLogger(VideoOfEventResource.class);

    private static final String ENTITY_NAME = "videoOfEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoOfEventRepository videoOfEventRepository;

    public VideoOfEventResource(VideoOfEventRepository videoOfEventRepository) {
        this.videoOfEventRepository = videoOfEventRepository;
    }

    /**
     * {@code POST  /video-of-events} : Create a new videoOfEvent.
     *
     * @param videoOfEvent the videoOfEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videoOfEvent, or with status {@code 400 (Bad Request)} if the videoOfEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/video-of-events")
    public ResponseEntity<VideoOfEvent> createVideoOfEvent(@Valid @RequestBody VideoOfEvent videoOfEvent) throws URISyntaxException {
        log.debug("REST request to save VideoOfEvent : {}", videoOfEvent);
        if (videoOfEvent.getId() != null) {
            throw new BadRequestAlertException("A new videoOfEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoOfEvent result = videoOfEventRepository.save(videoOfEvent);
        return ResponseEntity.created(new URI("/api/video-of-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /video-of-events} : Updates an existing videoOfEvent.
     *
     * @param videoOfEvent the videoOfEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoOfEvent,
     * or with status {@code 400 (Bad Request)} if the videoOfEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videoOfEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/video-of-events")
    public ResponseEntity<VideoOfEvent> updateVideoOfEvent(@Valid @RequestBody VideoOfEvent videoOfEvent) throws URISyntaxException {
        log.debug("REST request to update VideoOfEvent : {}", videoOfEvent);
        if (videoOfEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VideoOfEvent result = videoOfEventRepository.save(videoOfEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, videoOfEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /video-of-events} : get all the videoOfEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videoOfEvents in body.
     */
    @GetMapping("/video-of-events")
    public List<VideoOfEvent> getAllVideoOfEvents() {
        log.debug("REST request to get all VideoOfEvents");
        return videoOfEventRepository.findAll();
    }

    /**
     * {@code GET  /video-of-events/:id} : get the "id" videoOfEvent.
     *
     * @param id the id of the videoOfEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoOfEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/video-of-events/{id}")
    public ResponseEntity<VideoOfEvent> getVideoOfEvent(@PathVariable Long id) {
        log.debug("REST request to get VideoOfEvent : {}", id);
        Optional<VideoOfEvent> videoOfEvent = videoOfEventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(videoOfEvent);
    }

    /**
     * {@code DELETE  /video-of-events/:id} : delete the "id" videoOfEvent.
     *
     * @param id the id of the videoOfEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/video-of-events/{id}")
    public ResponseEntity<Void> deleteVideoOfEvent(@PathVariable Long id) {
        log.debug("REST request to delete VideoOfEvent : {}", id);
        videoOfEventRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
