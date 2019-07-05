package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.domain.HeavyPlateFinished;
import com.pmill.vuejs.repository.HeavyPlateFinishedRepository;
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
 * REST controller for managing {@link com.pmill.vuejs.domain.HeavyPlateFinished}.
 */
@RestController
@RequestMapping("/api")
public class HeavyPlateFinishedResource {

    private final Logger log = LoggerFactory.getLogger(HeavyPlateFinishedResource.class);

    private static final String ENTITY_NAME = "heavyPlateFinished";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HeavyPlateFinishedRepository heavyPlateFinishedRepository;

    public HeavyPlateFinishedResource(HeavyPlateFinishedRepository heavyPlateFinishedRepository) {
        this.heavyPlateFinishedRepository = heavyPlateFinishedRepository;
    }

    /**
     * {@code POST  /heavy-plate-finisheds} : Create a new heavyPlateFinished.
     *
     * @param heavyPlateFinished the heavyPlateFinished to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new heavyPlateFinished, or with status {@code 400 (Bad Request)} if the heavyPlateFinished has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/heavy-plate-finisheds")
    public ResponseEntity<HeavyPlateFinished> createHeavyPlateFinished(@Valid @RequestBody HeavyPlateFinished heavyPlateFinished) throws URISyntaxException {
        log.debug("REST request to save HeavyPlateFinished : {}", heavyPlateFinished);
        if (heavyPlateFinished.getId() != null) {
            throw new BadRequestAlertException("A new heavyPlateFinished cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HeavyPlateFinished result = heavyPlateFinishedRepository.save(heavyPlateFinished);
        return ResponseEntity.created(new URI("/api/heavy-plate-finisheds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /heavy-plate-finisheds} : Updates an existing heavyPlateFinished.
     *
     * @param heavyPlateFinished the heavyPlateFinished to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated heavyPlateFinished,
     * or with status {@code 400 (Bad Request)} if the heavyPlateFinished is not valid,
     * or with status {@code 500 (Internal Server Error)} if the heavyPlateFinished couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/heavy-plate-finisheds")
    public ResponseEntity<HeavyPlateFinished> updateHeavyPlateFinished(@Valid @RequestBody HeavyPlateFinished heavyPlateFinished) throws URISyntaxException {
        log.debug("REST request to update HeavyPlateFinished : {}", heavyPlateFinished);
        if (heavyPlateFinished.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HeavyPlateFinished result = heavyPlateFinishedRepository.save(heavyPlateFinished);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, heavyPlateFinished.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /heavy-plate-finisheds} : get all the heavyPlateFinisheds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of heavyPlateFinisheds in body.
     */
    @GetMapping("/heavy-plate-finisheds")
    public List<HeavyPlateFinished> getAllHeavyPlateFinisheds() {
        log.debug("REST request to get all HeavyPlateFinisheds");
        return heavyPlateFinishedRepository.findAll();
    }

    /**
     * {@code GET  /heavy-plate-finisheds/:id} : get the "id" heavyPlateFinished.
     *
     * @param id the id of the heavyPlateFinished to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the heavyPlateFinished, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/heavy-plate-finisheds/{id}")
    public ResponseEntity<HeavyPlateFinished> getHeavyPlateFinished(@PathVariable Long id) {
        log.debug("REST request to get HeavyPlateFinished : {}", id);
        Optional<HeavyPlateFinished> heavyPlateFinished = heavyPlateFinishedRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(heavyPlateFinished);
    }

    /**
     * {@code DELETE  /heavy-plate-finisheds/:id} : delete the "id" heavyPlateFinished.
     *
     * @param id the id of the heavyPlateFinished to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/heavy-plate-finisheds/{id}")
    public ResponseEntity<Void> deleteHeavyPlateFinished(@PathVariable Long id) {
        log.debug("REST request to delete HeavyPlateFinished : {}", id);
        heavyPlateFinishedRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
