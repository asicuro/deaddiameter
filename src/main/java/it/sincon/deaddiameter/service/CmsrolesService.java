package it.sincon.deaddiameter.service;

import it.sincon.deaddiameter.domain.Cmsroles;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cmsroles}.
 */
public interface CmsrolesService {
    /**
     * Save a cmsroles.
     *
     * @param cmsroles the entity to save.
     * @return the persisted entity.
     */
    Cmsroles save(Cmsroles cmsroles);

    /**
     * Partially updates a cmsroles.
     *
     * @param cmsroles the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cmsroles> partialUpdate(Cmsroles cmsroles);

    /**
     * Get all the cmsroles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cmsroles> findAll(Pageable pageable);

    /**
     * Get the "id" cmsroles.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cmsroles> findOne(Long id);

    /**
     * Delete the "id" cmsroles.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
