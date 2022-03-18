package it.sincon.deaddiameter.service.impl;

import it.sincon.deaddiameter.domain.Cmsmenu;
import it.sincon.deaddiameter.repository.CmsmenuRepository;
import it.sincon.deaddiameter.service.CmsmenuService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cmsmenu}.
 */
@Service
@Transactional
public class CmsmenuServiceImpl implements CmsmenuService {

    private final Logger log = LoggerFactory.getLogger(CmsmenuServiceImpl.class);

    private final CmsmenuRepository cmsmenuRepository;

    public CmsmenuServiceImpl(CmsmenuRepository cmsmenuRepository) {
        this.cmsmenuRepository = cmsmenuRepository;
    }

    @Override
    public Cmsmenu save(Cmsmenu cmsmenu) {
        log.debug("Request to save Cmsmenu : {}", cmsmenu);
        return cmsmenuRepository.save(cmsmenu);
    }

    @Override
    public Optional<Cmsmenu> partialUpdate(Cmsmenu cmsmenu) {
        log.debug("Request to partially update Cmsmenu : {}", cmsmenu);

        return cmsmenuRepository
            .findById(cmsmenu.getId())
            .map(existingCmsmenu -> {
                if (cmsmenu.getName() != null) {
                    existingCmsmenu.setName(cmsmenu.getName());
                }
                if (cmsmenu.getTitle() != null) {
                    existingCmsmenu.setTitle(cmsmenu.getTitle());
                }
                if (cmsmenu.getDescription() != null) {
                    existingCmsmenu.setDescription(cmsmenu.getDescription());
                }
                if (cmsmenu.getCss() != null) {
                    existingCmsmenu.setCss(cmsmenu.getCss());
                }
                if (cmsmenu.getMenuType() != null) {
                    existingCmsmenu.setMenuType(cmsmenu.getMenuType());
                }
                if (cmsmenu.getOrder() != null) {
                    existingCmsmenu.setOrder(cmsmenu.getOrder());
                }
                if (cmsmenu.getActive() != null) {
                    existingCmsmenu.setActive(cmsmenu.getActive());
                }
                if (cmsmenu.getLanguage() != null) {
                    existingCmsmenu.setLanguage(cmsmenu.getLanguage());
                }
                if (cmsmenu.getLastModified() != null) {
                    existingCmsmenu.setLastModified(cmsmenu.getLastModified());
                }

                return existingCmsmenu;
            })
            .map(cmsmenuRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cmsmenu> findAll(Pageable pageable) {
        log.debug("Request to get all Cmsmenus");

        return cmsmenuRepository.findAll(pageable);
    }

    public Page<Cmsmenu> findAllWithEagerRelationships(Pageable pageable) {
        return cmsmenuRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cmsmenu> findOne(Long id) {
        log.debug("Request to get Cmsmenu : {}", id);
        return cmsmenuRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cmsmenu : {}", id);
        cmsmenuRepository.deleteById(id);
    }
}
