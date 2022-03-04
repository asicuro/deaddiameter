package it.sincon.deaddiameter.repository;

import it.sincon.deaddiameter.domain.Cmspage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cmspage entity.
 */
@Repository
public interface CmspageRepository extends JpaRepository<Cmspage, Long> {
    @Query(
        value = "select distinct cmspage from Cmspage cmspage left join fetch cmspage.cmsroles",
        countQuery = "select count(distinct cmspage) from Cmspage cmspage"
    )
    Page<Cmspage> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct cmspage from Cmspage cmspage left join fetch cmspage.cmsroles")
    List<Cmspage> findAllWithEagerRelationships();

    @Query("select cmspage from Cmspage cmspage left join fetch cmspage.cmsroles where cmspage.id =:id")
    Optional<Cmspage> findOneWithEagerRelationships(@Param("id") Long id);
}
