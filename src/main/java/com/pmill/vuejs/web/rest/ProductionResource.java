package com.pmill.vuejs.web.rest;

import com.pmill.vuejs.domain.Production;
import com.pmill.vuejs.repository.ProductionRepository;
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
 * REST controller for managing {@link com.pmill.vuejs.domain.Production}.
 */
@RestController
@RequestMapping("/api")
public class ProductionResource {

    private final Logger log = LoggerFactory.getLogger(ProductionResource.class);

    private static final String ENTITY_NAME = "production";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductionRepository productionRepository;

    public ProductionResource(ProductionRepository productionRepository) {
        this.productionRepository = productionRepository;
    }

    /**
     * {@code POST  /productions} : Create a new production.
     *
     * @param production the production to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new production, or with status {@code 400 (Bad Request)} if the production has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productions")
    public ResponseEntity<Production> createProduction(@Valid @RequestBody Production production) throws URISyntaxException {
        log.debug("REST request to save Production : {}", production);
        if (production.getId() != null) {
            throw new BadRequestAlertException("A new production cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Production result = productionRepository.save(production);
        return ResponseEntity.created(new URI("/api/productions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productions} : Updates an existing production.
     *
     * @param production the production to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated production,
     * or with status {@code 400 (Bad Request)} if the production is not valid,
     * or with status {@code 500 (Internal Server Error)} if the production couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/productions")
    public ResponseEntity<Production> updateProduction(@Valid @RequestBody Production production) throws URISyntaxException {
        log.debug("REST request to update Production : {}", production);
        if (production.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Production result = productionRepository.save(production);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, production.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /productions} : get all the productions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productions in body.
     */
    @GetMapping("/productions")
    public List<Production> getAllProductions() {
        log.debug("REST request to get all Productions");
        return productionRepository.findAll();
    }

    /**
     * {@code GET  /productions/:id} : get the "id" production.
     *
     * @param id the id of the production to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the production, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productions/{id}")
    public ResponseEntity<Production> getProduction(@PathVariable Long id) {
        log.debug("REST request to get Production : {}", id);
        Optional<Production> production = productionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(production);
    }

    /**
     * {@code DELETE  /productions/:id} : delete the "id" production.
     *
     * @param id the id of the production to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/productions/{id}")
    public ResponseEntity<Void> deleteProduction(@PathVariable Long id) {
        log.debug("REST request to delete Production : {}", id);
        productionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
