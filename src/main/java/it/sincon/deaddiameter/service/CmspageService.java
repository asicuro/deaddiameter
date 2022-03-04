package it.sincon.deaddiameter.service;

import it.sincon.deaddiameter.domain.Cmspage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cmspage}.
 */
public interface CmspageService {
    /**
     * Save a cmspage.
     *
     * @param cmspage the entity to save.
     * @return the persisted entity.
     */
    Cmspage save(Cmspage cmspage);

    /**
     * Partially updates a cmspage.
     *
     * @param cmspage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cmspage> partialUpdate(Cmspage cmspage);

    /**
     * Get all the cmspages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cmspage> findAll(Pageable pageable);

    /**
     * Get all the cmspages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cmspage> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cmspage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cmspage> findOne(Long id);

    /**
     * Delete the "id" cmspage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
