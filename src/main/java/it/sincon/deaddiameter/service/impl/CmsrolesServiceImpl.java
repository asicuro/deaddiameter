package it.sincon.deaddiameter.service.impl;

import it.sincon.deaddiameter.domain.Cmsroles;
import it.sincon.deaddiameter.repository.CmsrolesRepository;
import it.sincon.deaddiameter.service.CmsrolesService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cmsroles}.
 */
@Service
@Transactional
public class CmsrolesServiceImpl implements CmsrolesService {

    private final Logger log = LoggerFactory.getLogger(CmsrolesServiceImpl.class);

    private final CmsrolesRepository cmsrolesRepository;

    public CmsrolesServiceImpl(CmsrolesRepository cmsrolesRepository) {
        this.cmsrolesRepository = cmsrolesRepository;
    }

    @Override
    public Cmsroles save(Cmsroles cmsroles) {
        log.debug("Request to save Cmsroles : {}", cmsroles);
        return cmsrolesRepository.save(cmsroles);
    }

    @Override
    public Optional<Cmsroles> partialUpdate(Cmsroles cmsroles) {
        log.debug("Request to partially update Cmsroles : {}", cmsroles);

        return cmsrolesRepository
            .findById(cmsroles.getId())
            .map(existingCmsroles -> {
                if (cmsroles.getName() != null) {
                    existingCmsroles.setName(cmsroles.getName());
                }
                if (cmsroles.getDescription() != null) {
                    existingCmsroles.setDescription(cmsroles.getDescription());
                }
                if (cmsroles.getActive() != null) {
                    existingCmsroles.setActive(cmsroles.getActive());
                }

                return existingCmsroles;
            })
            .map(cmsrolesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cmsroles> findAll(Pageable pageable) {
        log.debug("Request to get all Cmsroles");
        return cmsrolesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cmsroles> findOne(Long id) {
        log.debug("Request to get Cmsroles : {}", id);
        return cmsrolesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cmsroles : {}", id);
        cmsrolesRepository.deleteById(id);
    }
}
