package it.sincon.deaddiameter.service.impl;

import it.sincon.deaddiameter.domain.Cmspage;
import it.sincon.deaddiameter.repository.CmspageRepository;
import it.sincon.deaddiameter.service.CmspageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cmspage}.
 */
@Service
@Transactional
public class CmspageServiceImpl implements CmspageService {

    private final Logger log = LoggerFactory.getLogger(CmspageServiceImpl.class);

    private final CmspageRepository cmspageRepository;

    public CmspageServiceImpl(CmspageRepository cmspageRepository) {
        this.cmspageRepository = cmspageRepository;
    }

    @Override
    public Cmspage save(Cmspage cmspage) {
        log.debug("Request to save Cmspage : {}", cmspage);
        return cmspageRepository.save(cmspage);
    }

    @Override
    public Optional<Cmspage> partialUpdate(Cmspage cmspage) {
        log.debug("Request to partially update Cmspage : {}", cmspage);

        return cmspageRepository
            .findById(cmspage.getId())
            .map(existingCmspage -> {
                if (cmspage.getName() != null) {
                    existingCmspage.setName(cmspage.getName());
                }
                if (cmspage.getAlias() != null) {
                    existingCmspage.setAlias(cmspage.getAlias());
                }
                if (cmspage.getContent() != null) {
                    existingCmspage.setContent(cmspage.getContent());
                }
                if (cmspage.getCreated() != null) {
                    existingCmspage.setCreated(cmspage.getCreated());
                }
                if (cmspage.getPublished() != null) {
                    existingCmspage.setPublished(cmspage.getPublished());
                }
                if (cmspage.getOrder() != null) {
                    existingCmspage.setOrder(cmspage.getOrder());
                }
                if (cmspage.getActive() != null) {
                    existingCmspage.setActive(cmspage.getActive());
                }
                if (cmspage.getLanguage() != null) {
                    existingCmspage.setLanguage(cmspage.getLanguage());
                }
                if (cmspage.getLastModified() != null) {
                    existingCmspage.setLastModified(cmspage.getLastModified());
                }

                return existingCmspage;
            })
            .map(cmspageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cmspage> findAll(Pageable pageable) {
        log.debug("Request to get all Cmspages");
        return cmspageRepository.findAll(pageable);
    }

    public Page<Cmspage> findAllWithEagerRelationships(Pageable pageable) {
        return cmspageRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cmspage> findOne(Long id) {
        log.debug("Request to get Cmspage : {}", id);
        return cmspageRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cmspage : {}", id);
        cmspageRepository.deleteById(id);
    }
}
