package it.sincon.deaddiameter.repository;

import it.sincon.deaddiameter.domain.Cmsmenu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cmsmenu entity.
 */
@Repository
public interface CmsmenuRepository extends JpaRepository<Cmsmenu, Long> {
    @Query(
        value = "select distinct cmsmenu from Cmsmenu cmsmenu left join fetch cmsmenu.cmsroles left join fetch cmsmenu.cmspages",
        countQuery = "select count(distinct cmsmenu) from Cmsmenu cmsmenu"
    )
    Page<Cmsmenu> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct cmsmenu from Cmsmenu cmsmenu left join fetch cmsmenu.cmsroles left join fetch cmsmenu.cmspages")
    List<Cmsmenu> findAllWithEagerRelationships();

    @Query("select cmsmenu from Cmsmenu cmsmenu left join fetch cmsmenu.cmsroles left join fetch cmsmenu.cmspages where cmsmenu.id =:id")
    Optional<Cmsmenu> findOneWithEagerRelationships(@Param("id") Long id);
}
