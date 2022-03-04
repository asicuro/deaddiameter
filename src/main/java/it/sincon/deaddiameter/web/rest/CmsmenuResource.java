package it.sincon.deaddiameter.web.rest;

import it.sincon.deaddiameter.domain.Cmsmenu;
import it.sincon.deaddiameter.repository.CmsmenuRepository;
import it.sincon.deaddiameter.service.CmsmenuService;
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
 * REST controller for managing {@link it.sincon.deaddiameter.domain.Cmsmenu}.
 */
@RestController
@RequestMapping("/api")
public class CmsmenuResource {

    private final Logger log = LoggerFactory.getLogger(CmsmenuResource.class);

    private static final String ENTITY_NAME = "cmsmenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CmsmenuService cmsmenuService;

    private final CmsmenuRepository cmsmenuRepository;

    public CmsmenuResource(CmsmenuService cmsmenuService, CmsmenuRepository cmsmenuRepository) {
        this.cmsmenuService = cmsmenuService;
        this.cmsmenuRepository = cmsmenuRepository;
    }

    /**
     * {@code POST  /cmsmenus} : Create a new cmsmenu.
     *
     * @param cmsmenu the cmsmenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cmsmenu, or with status {@code 400 (Bad Request)} if the cmsmenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cmsmenus")
    public ResponseEntity<Cmsmenu> createCmsmenu(@Valid @RequestBody Cmsmenu cmsmenu) throws URISyntaxException {
        log.debug("REST request to save Cmsmenu : {}", cmsmenu);
        if (cmsmenu.getId() != null) {
            throw new BadRequestAlertException("A new cmsmenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cmsmenu result = cmsmenuService.save(cmsmenu);
        return ResponseEntity
            .created(new URI("/api/cmsmenus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cmsmenus/:id} : Updates an existing cmsmenu.
     *
     * @param id the id of the cmsmenu to save.
     * @param cmsmenu the cmsmenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmsmenu,
     * or with status {@code 400 (Bad Request)} if the cmsmenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cmsmenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cmsmenus/{id}")
    public ResponseEntity<Cmsmenu> updateCmsmenu(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cmsmenu cmsmenu
    ) throws URISyntaxException {
        log.debug("REST request to update Cmsmenu : {}, {}", id, cmsmenu);
        if (cmsmenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmsmenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmsmenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cmsmenu result = cmsmenuService.save(cmsmenu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsmenu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cmsmenus/:id} : Partial updates given fields of an existing cmsmenu, field will ignore if it is null
     *
     * @param id the id of the cmsmenu to save.
     * @param cmsmenu the cmsmenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmsmenu,
     * or with status {@code 400 (Bad Request)} if the cmsmenu is not valid,
     * or with status {@code 404 (Not Found)} if the cmsmenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the cmsmenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cmsmenus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cmsmenu> partialUpdateCmsmenu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cmsmenu cmsmenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cmsmenu partially : {}, {}", id, cmsmenu);
        if (cmsmenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmsmenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmsmenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cmsmenu> result = cmsmenuService.partialUpdate(cmsmenu);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsmenu.getId().toString())
        );
    }

    /**
     * {@code GET  /cmsmenus} : get all the cmsmenus.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cmsmenus in body.
     */
    @GetMapping("/cmsmenus")
    public ResponseEntity<List<Cmsmenu>> getAllCmsmenus(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Cmsmenus");
        Page<Cmsmenu> page;
        if (eagerload) {
            page = cmsmenuService.findAllWithEagerRelationships(pageable);
        } else {
            page = cmsmenuService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cmsmenus/:id} : get the "id" cmsmenu.
     *
     * @param id the id of the cmsmenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cmsmenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cmsmenus/{id}")
    public ResponseEntity<Cmsmenu> getCmsmenu(@PathVariable Long id) {
        log.debug("REST request to get Cmsmenu : {}", id);
        Optional<Cmsmenu> cmsmenu = cmsmenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cmsmenu);
    }

    /**
     * {@code DELETE  /cmsmenus/:id} : delete the "id" cmsmenu.
     *
     * @param id the id of the cmsmenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cmsmenus/{id}")
    public ResponseEntity<Void> deleteCmsmenu(@PathVariable Long id) {
        log.debug("REST request to delete Cmsmenu : {}", id);
        cmsmenuService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
