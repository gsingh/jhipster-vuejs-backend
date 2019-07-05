package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.domain.ShiftManager;
import com.pmill.vuejs.repository.ShiftManagerRepository;
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
 * REST controller for managing {@link com.pmill.vuejs.domain.ShiftManager}.
 */
@RestController
@RequestMapping("/api")
public class ShiftManagerResource {

    private final Logger log = LoggerFactory.getLogger(ShiftManagerResource.class);

    private static final String ENTITY_NAME = "shiftManager";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShiftManagerRepository shiftManagerRepository;

    public ShiftManagerResource(ShiftManagerRepository shiftManagerRepository) {
        this.shiftManagerRepository = shiftManagerRepository;
    }

    /**
     * {@code POST  /shift-managers} : Create a new shiftManager.
     *
     * @param shiftManager the shiftManager to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shiftManager, or with status {@code 400 (Bad Request)} if the shiftManager has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shift-managers")
    public ResponseEntity<ShiftManager> createShiftManager(@Valid @RequestBody ShiftManager shiftManager) throws URISyntaxException {
        log.debug("REST request to save ShiftManager : {}", shiftManager);
        if (shiftManager.getId() != null) {
            throw new BadRequestAlertException("A new shiftManager cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShiftManager result = shiftManagerRepository.save(shiftManager);
        return ResponseEntity.created(new URI("/api/shift-managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shift-managers} : Updates an existing shiftManager.
     *
     * @param shiftManager the shiftManager to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shiftManager,
     * or with status {@code 400 (Bad Request)} if the shiftManager is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shiftManager couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shift-managers")
    public ResponseEntity<ShiftManager> updateShiftManager(@Valid @RequestBody ShiftManager shiftManager) throws URISyntaxException {
        log.debug("REST request to update ShiftManager : {}", shiftManager);
        if (shiftManager.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShiftManager result = shiftManagerRepository.save(shiftManager);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shiftManager.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shift-managers} : get all the shiftManagers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shiftManagers in body.
     */
    @GetMapping("/shift-managers")
    public List<ShiftManager> getAllShiftManagers() {
        log.debug("REST request to get all ShiftManagers");
        return shiftManagerRepository.findAll();
    }

    /**
     * {@code GET  /shift-managers/:id} : get the "id" shiftManager.
     *
     * @param id the id of the shiftManager to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shiftManager, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shift-managers/{id}")
    public ResponseEntity<ShiftManager> getShiftManager(@PathVariable Long id) {
        log.debug("REST request to get ShiftManager : {}", id);
        Optional<ShiftManager> shiftManager = shiftManagerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shiftManager);
    }

    /**
     * {@code DELETE  /shift-managers/:id} : delete the "id" shiftManager.
     *
     * @param id the id of the shiftManager to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shift-managers/{id}")
    public ResponseEntity<Void> deleteShiftManager(@PathVariable Long id) {
        log.debug("REST request to delete ShiftManager : {}", id);
        shiftManagerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
