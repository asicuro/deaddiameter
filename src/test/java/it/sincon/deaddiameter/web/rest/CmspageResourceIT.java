package it.sincon.deaddiameter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.sincon.deaddiameter.IntegrationTest;
import it.sincon.deaddiameter.domain.Cmspage;
import it.sincon.deaddiameter.domain.enumeration.Cmslanguage;
import it.sincon.deaddiameter.repository.CmspageRepository;
import it.sincon.deaddiameter.service.CmspageService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CmspageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CmspageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Cmslanguage DEFAULT_LANGUAGE = Cmslanguage.ITALIAN;
    private static final Cmslanguage UPDATED_LANGUAGE = Cmslanguage.FRENCH;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cmspages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CmspageRepository cmspageRepository;

    @Mock
    private CmspageRepository cmspageRepositoryMock;

    @Mock
    private CmspageService cmspageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCmspageMockMvc;

    private Cmspage cmspage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmspage createEntity(EntityManager em) {
        Cmspage cmspage = new Cmspage()
            .name(DEFAULT_NAME)
            .alias(DEFAULT_ALIAS)
            .content(DEFAULT_CONTENT)
            .created(DEFAULT_CREATED)
            .published(DEFAULT_PUBLISHED)
            .order(DEFAULT_ORDER)
            .active(DEFAULT_ACTIVE)
            .language(DEFAULT_LANGUAGE)
            .lastModified(DEFAULT_LAST_MODIFIED);
        return cmspage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmspage createUpdatedEntity(EntityManager em) {
        Cmspage cmspage = new Cmspage()
            .name(UPDATED_NAME)
            .alias(UPDATED_ALIAS)
            .content(UPDATED_CONTENT)
            .created(UPDATED_CREATED)
            .published(UPDATED_PUBLISHED)
            .order(UPDATED_ORDER)
            .active(UPDATED_ACTIVE)
            .language(UPDATED_LANGUAGE)
            .lastModified(UPDATED_LAST_MODIFIED);
        return cmspage;
    }

    @BeforeEach
    public void initTest() {
        cmspage = createEntity(em);
    }

    @Test
    @Transactional
    void createCmspage() throws Exception {
        int databaseSizeBeforeCreate = cmspageRepository.findAll().size();
        // Create the Cmspage
        restCmspageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmspage)))
            .andExpect(status().isCreated());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeCreate + 1);
        Cmspage testCmspage = cmspageList.get(cmspageList.size() - 1);
        assertThat(testCmspage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCmspage.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testCmspage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCmspage.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCmspage.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testCmspage.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCmspage.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCmspage.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testCmspage.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void createCmspageWithExistingId() throws Exception {
        // Create the Cmspage with an existing ID
        cmspage.setId(1L);

        int databaseSizeBeforeCreate = cmspageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCmspageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmspage)))
            .andExpect(status().isBadRequest());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmspageRepository.findAll().size();
        // set the field null
        cmspage.setName(null);

        // Create the Cmspage, which fails.

        restCmspageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmspage)))
            .andExpect(status().isBadRequest());

        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmspageRepository.findAll().size();
        // set the field null
        cmspage.setAlias(null);

        // Create the Cmspage, which fails.

        restCmspageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmspage)))
            .andExpect(status().isBadRequest());

        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCmspages() throws Exception {
        // Initialize the database
        cmspageRepository.saveAndFlush(cmspage);

        // Get all the cmspageList
        restCmspageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cmspage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCmspagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cmspageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCmspageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cmspageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCmspagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cmspageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCmspageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cmspageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCmspage() throws Exception {
        // Initialize the database
        cmspageRepository.saveAndFlush(cmspage);

        // Get the cmspage
        restCmspageMockMvc
            .perform(get(ENTITY_API_URL_ID, cmspage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cmspage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCmspage() throws Exception {
        // Get the cmspage
        restCmspageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCmspage() throws Exception {
        // Initialize the database
        cmspageRepository.saveAndFlush(cmspage);

        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();

        // Update the cmspage
        Cmspage updatedCmspage = cmspageRepository.findById(cmspage.getId()).get();
        // Disconnect from session so that the updates on updatedCmspage are not directly saved in db
        em.detach(updatedCmspage);
        updatedCmspage
            .name(UPDATED_NAME)
            .alias(UPDATED_ALIAS)
            .content(UPDATED_CONTENT)
            .created(UPDATED_CREATED)
            .published(UPDATED_PUBLISHED)
            .order(UPDATED_ORDER)
            .active(UPDATED_ACTIVE)
            .language(UPDATED_LANGUAGE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restCmspageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCmspage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCmspage))
            )
            .andExpect(status().isOk());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
        Cmspage testCmspage = cmspageList.get(cmspageList.size() - 1);
        assertThat(testCmspage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmspage.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testCmspage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCmspage.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCmspage.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testCmspage.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCmspage.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCmspage.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCmspage.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingCmspage() throws Exception {
        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();
        cmspage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmspageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cmspage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cmspage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCmspage() throws Exception {
        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();
        cmspage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmspageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cmspage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCmspage() throws Exception {
        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();
        cmspage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmspageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmspage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCmspageWithPatch() throws Exception {
        // Initialize the database
        cmspageRepository.saveAndFlush(cmspage);

        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();

        // Update the cmspage using partial update
        Cmspage partialUpdatedCmspage = new Cmspage();
        partialUpdatedCmspage.setId(cmspage.getId());

        partialUpdatedCmspage.name(UPDATED_NAME).published(UPDATED_PUBLISHED).lastModified(UPDATED_LAST_MODIFIED);

        restCmspageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCmspage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCmspage))
            )
            .andExpect(status().isOk());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
        Cmspage testCmspage = cmspageList.get(cmspageList.size() - 1);
        assertThat(testCmspage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmspage.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testCmspage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCmspage.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCmspage.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testCmspage.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCmspage.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCmspage.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testCmspage.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateCmspageWithPatch() throws Exception {
        // Initialize the database
        cmspageRepository.saveAndFlush(cmspage);

        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();

        // Update the cmspage using partial update
        Cmspage partialUpdatedCmspage = new Cmspage();
        partialUpdatedCmspage.setId(cmspage.getId());

        partialUpdatedCmspage
            .name(UPDATED_NAME)
            .alias(UPDATED_ALIAS)
            .content(UPDATED_CONTENT)
            .created(UPDATED_CREATED)
            .published(UPDATED_PUBLISHED)
            .order(UPDATED_ORDER)
            .active(UPDATED_ACTIVE)
            .language(UPDATED_LANGUAGE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restCmspageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCmspage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCmspage))
            )
            .andExpect(status().isOk());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
        Cmspage testCmspage = cmspageList.get(cmspageList.size() - 1);
        assertThat(testCmspage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmspage.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testCmspage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCmspage.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCmspage.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testCmspage.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCmspage.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCmspage.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCmspage.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingCmspage() throws Exception {
        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();
        cmspage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmspageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cmspage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cmspage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCmspage() throws Exception {
        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();
        cmspage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmspageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cmspage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCmspage() throws Exception {
        int databaseSizeBeforeUpdate = cmspageRepository.findAll().size();
        cmspage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmspageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cmspage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cmspage in the database
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCmspage() throws Exception {
        // Initialize the database
        cmspageRepository.saveAndFlush(cmspage);

        int databaseSizeBeforeDelete = cmspageRepository.findAll().size();

        // Delete the cmspage
        restCmspageMockMvc
            .perform(delete(ENTITY_API_URL_ID, cmspage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cmspage> cmspageList = cmspageRepository.findAll();
        assertThat(cmspageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
