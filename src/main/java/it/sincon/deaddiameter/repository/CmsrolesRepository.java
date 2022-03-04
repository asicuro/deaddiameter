package it.sincon.deaddiameter.repository;

import it.sincon.deaddiameter.domain.Cmsroles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cmsroles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CmsrolesRepository extends JpaRepository<Cmsroles, Long> {}
