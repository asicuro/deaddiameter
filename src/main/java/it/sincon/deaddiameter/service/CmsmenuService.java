package it.sincon.deaddiameter.service;

import it.sincon.deaddiameter.domain.Cmsmenu;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cmsmenu}.
 */
public interface CmsmenuService {
    /**
     * Save a cmsmenu.
     *
     * @param cmsmenu the entity to save.
     * @return the persisted entity.
     */
    Cmsmenu save(Cmsmenu cmsmenu);

    /**
     * Partially updates a cmsmenu.
     *
     * @param cmsmenu the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cmsmenu> partialUpdate(Cmsmenu cmsmenu);

    /**
     * Get all the cmsmenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cmsmenu> findAll(Pageable pageable);

    /**
     * Get all the cmsmenus with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cmsmenu> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cmsmenu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cmsmenu> findOne(Long id);

    /**
     * Delete the "id" cmsmenu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
