package it.sincon.deaddiameter.web.rest;

import it.sincon.deaddiameter.domain.Cmsroles;
import it.sincon.deaddiameter.repository.CmsrolesRepository;
import it.sincon.deaddiameter.security.AuthoritiesConstants;
import it.sincon.deaddiameter.service.CmsrolesService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.sincon.deaddiameter.domain.Cmsroles}.
 */
@RestController
@RequestMapping("/api")
public class CmsrolesResource {

    private final Logger log = LoggerFactory.getLogger(CmsrolesResource.class);

    private static final String ENTITY_NAME = "cmsroles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CmsrolesService cmsrolesService;

    private final CmsrolesRepository cmsrolesRepository;

    public CmsrolesResource(CmsrolesService cmsrolesService, CmsrolesRepository cmsrolesRepository) {
        this.cmsrolesService = cmsrolesService;
        this.cmsrolesRepository = cmsrolesRepository;
    }

    /**
     * {@code POST  /cmsroles} : Create a new cmsroles.
     *
     * @param cmsroles the cmsroles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cmsroles, or with status {@code 400 (Bad Request)} if the cmsroles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cmsroles")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Cmsroles> createCmsroles(@Valid @RequestBody Cmsroles cmsroles) throws URISyntaxException {
        log.debug("REST request to save Cmsroles : {}", cmsroles);
        if (cmsroles.getId() != null) {
            throw new BadRequestAlertException("A new cmsroles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cmsroles result = cmsrolesService.save(cmsroles);
        return ResponseEntity
            .created(new URI("/api/cmsroles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cmsroles/:id} : Updates an existing cmsroles.
     *
     * @param id the id of the cmsroles to save.
     * @param cmsroles the cmsroles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmsroles,
     * or with status {@code 400 (Bad Request)} if the cmsroles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cmsroles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cmsroles/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Cmsroles> updateCmsroles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cmsroles cmsroles
    ) throws URISyntaxException {
        log.debug("REST request to update Cmsroles : {}, {}", id, cmsroles);
        if (cmsroles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmsroles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmsrolesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cmsroles result = cmsrolesService.save(cmsroles);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsroles.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cmsroles/:id} : Partial updates given fields of an existing cmsroles, field will ignore if it is null
     *
     * @param id the id of the cmsroles to save.
     * @param cmsroles the cmsroles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmsroles,
     * or with status {@code 400 (Bad Request)} if the cmsroles is not valid,
     * or with status {@code 404 (Not Found)} if the cmsroles is not found,
     * or with status {@code 500 (Internal Server Error)} if the cmsroles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cmsroles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Cmsroles> partialUpdateCmsroles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cmsroles cmsroles
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cmsroles partially : {}, {}", id, cmsroles);
        if (cmsroles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmsroles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmsrolesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cmsroles> result = cmsrolesService.partialUpdate(cmsroles);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsroles.getId().toString())
        );
    }

    /**
     * {@code GET  /cmsroles} : get all the cmsroles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cmsroles in body.
     */
    @GetMapping("/cmsroles")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<Cmsroles>> getAllCmsroles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cmsroles");
        Page<Cmsroles> page = cmsrolesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cmsroles/:id} : get the "id" cmsroles.
     *
     * @param id the id of the cmsroles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cmsroles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cmsroles/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Cmsroles> getCmsroles(@PathVariable Long id) {
        log.debug("REST request to get Cmsroles : {}", id);
        Optional<Cmsroles> cmsroles = cmsrolesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cmsroles);
    }

    /**
     * {@code DELETE  /cmsroles/:id} : delete the "id" cmsroles.
     *
     * @param id the id of the cmsroles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cmsroles/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteCmsroles(@PathVariable Long id) {
        log.debug("REST request to delete Cmsroles : {}", id);
        cmsrolesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
