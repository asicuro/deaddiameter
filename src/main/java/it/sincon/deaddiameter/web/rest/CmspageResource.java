package it.sincon.deaddiameter.web.rest;

import it.sincon.deaddiameter.domain.Cmspage;
import it.sincon.deaddiameter.repository.CmspageRepository;
import it.sincon.deaddiameter.service.CmspageService;
import it.sincon.deaddiameter.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.sincon.deaddiameter.domain.Cmspage}.
 */
@RestController
@RequestMapping("/api")
public class CmspageResource {

    private final Logger log = LoggerFactory.getLogger(CmspageResource.class);

    private static final String ENTITY_NAME = "cmspage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CmspageService cmspageService;

    private final CmspageRepository cmspageRepository;

    public CmspageResource(CmspageService cmspageService, CmspageRepository cmspageRepository) {
        this.cmspageService = cmspageService;
        this.cmspageRepository = cmspageRepository;
    }

    /**
     * {@code POST  /cmspages} : Create a new cmspage.
     *
     * @param cmspage the cmspage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cmspage, or with status {@code 400 (Bad Request)} if the cmspage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cmspages")
    public ResponseEntity<Cmspage> createCmspage(@Valid @RequestBody Cmspage cmspage) throws URISyntaxException {
        log.debug("REST request to save Cmspage : {}", cmspage);
        if (cmspage.getId() != null) {
            throw new BadRequestAlertException("A new cmspage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cmspage result = cmspageService.save(cmspage);
        return ResponseEntity
            .created(new URI("/api/cmspages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cmspages/:id} : Updates an existing cmspage.
     *
     * @param id the id of the cmspage to save.
     * @param cmspage the cmspage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmspage,
     * or with status {@code 400 (Bad Request)} if the cmspage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cmspage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cmspages/{id}")
    public ResponseEntity<Cmspage> updateCmspage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cmspage cmspage
    ) throws URISyntaxException {
        log.debug("REST request to update Cmspage : {}, {}", id, cmspage);
        if (cmspage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmspage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmspageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cmspage result = cmspageService.save(cmspage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmspage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cmspages/:id} : Partial updates given fields of an existing cmspage, field will ignore if it is null
     *
     * @param id the id of the cmspage to save.
     * @param cmspage the cmspage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmspage,
     * or with status {@code 400 (Bad Request)} if the cmspage is not valid,
     * or with status {@code 404 (Not Found)} if the cmspage is not found,
     * or with status {@code 500 (Internal Server Error)} if the cmspage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cmspages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cmspage> partialUpdateCmspage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cmspage cmspage
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cmspage partially : {}, {}", id, cmspage);
        if (cmspage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmspage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmspageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cmspage> result = cmspageService.partialUpdate(cmspage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmspage.getId().toString())
        );
    }

    /**
     * {@code GET  /cmspages} : get all the cmspages.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cmspages in body.
     */
    @GetMapping("/cmspages")
    public ResponseEntity<List<Cmspage>> getAllCmspages(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Cmspages");
        Page<Cmspage> page;
        if (eagerload) {
            page = cmspageService.findAllWithEagerRelationships(pageable);
        } else {
            page = cmspageService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cmspages/:id} : get the "id" cmspage.
     *
     * @param id the id of the cmspage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cmspage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cmspages/{id}")
    public ResponseEntity<Cmspage> getCmspage(@PathVariable Long id) {
        log.debug("REST request to get Cmspage : {}", id);
        Optional<Cmspage> cmspage = cmspageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cmspage);
    }

    /**
     * {@code DELETE  /cmspages/:id} : delete the "id" cmspage.
     *
     * @param id the id of the cmspage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cmspages/{id}")
    public ResponseEntity<Void> deleteCmspage(@PathVariable Long id) {
        log.debug("REST request to delete Cmspage : {}", id);
        cmspageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
