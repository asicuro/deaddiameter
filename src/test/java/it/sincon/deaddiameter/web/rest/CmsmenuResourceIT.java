package it.sincon.deaddiameter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.sincon.deaddiameter.IntegrationTest;
import it.sincon.deaddiameter.domain.Cmsmenu;
import it.sincon.deaddiameter.domain.enumeration.Cmslanguage;
import it.sincon.deaddiameter.repository.CmsmenuRepository;
import it.sincon.deaddiameter.service.CmsmenuService;
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

/**
 * Integration tests for the {@link CmsmenuResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CmsmenuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CSS = "AAAAAAAAAA";
    private static final String UPDATED_CSS = "BBBBBBBBBB";

    private static final Integer DEFAULT_MENU_TYPE = 1;
    private static final Integer UPDATED_MENU_TYPE = 2;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Cmslanguage DEFAULT_LANGUAGE = Cmslanguage.ITALIAN;
    private static final Cmslanguage UPDATED_LANGUAGE = Cmslanguage.FRENCH;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cmsmenus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CmsmenuRepository cmsmenuRepository;

    @Mock
    private CmsmenuRepository cmsmenuRepositoryMock;

    @Mock
    private CmsmenuService cmsmenuServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCmsmenuMockMvc;

    private Cmsmenu cmsmenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmsmenu createEntity(EntityManager em) {
        Cmsmenu cmsmenu = new Cmsmenu()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .css(DEFAULT_CSS)
            .menuType(DEFAULT_MENU_TYPE)
            .order(DEFAULT_ORDER)
            .active(DEFAULT_ACTIVE)
            .language(DEFAULT_LANGUAGE)
            .lastModified(DEFAULT_LAST_MODIFIED);
        return cmsmenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmsmenu createUpdatedEntity(EntityManager em) {
        Cmsmenu cmsmenu = new Cmsmenu()
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .css(UPDATED_CSS)
            .menuType(UPDATED_MENU_TYPE)
            .order(UPDATED_ORDER)
            .active(UPDATED_ACTIVE)
            .language(UPDATED_LANGUAGE)
            .lastModified(UPDATED_LAST_MODIFIED);
        return cmsmenu;
    }

    @BeforeEach
    public void initTest() {
        cmsmenu = createEntity(em);
    }

    @Test
    @Transactional
    void createCmsmenu() throws Exception {
        int databaseSizeBeforeCreate = cmsmenuRepository.findAll().size();
        // Create the Cmsmenu
        restCmsmenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isCreated());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeCreate + 1);
        Cmsmenu testCmsmenu = cmsmenuList.get(cmsmenuList.size() - 1);
        assertThat(testCmsmenu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCmsmenu.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCmsmenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCmsmenu.getCss()).isEqualTo(DEFAULT_CSS);
        assertThat(testCmsmenu.getMenuType()).isEqualTo(DEFAULT_MENU_TYPE);
        assertThat(testCmsmenu.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCmsmenu.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCmsmenu.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testCmsmenu.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void createCmsmenuWithExistingId() throws Exception {
        // Create the Cmsmenu with an existing ID
        cmsmenu.setId(1L);

        int databaseSizeBeforeCreate = cmsmenuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCmsmenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isBadRequest());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmsmenuRepository.findAll().size();
        // set the field null
        cmsmenu.setName(null);

        // Create the Cmsmenu, which fails.

        restCmsmenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isBadRequest());

        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmsmenuRepository.findAll().size();
        // set the field null
        cmsmenu.setTitle(null);

        // Create the Cmsmenu, which fails.

        restCmsmenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isBadRequest());

        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmsmenuRepository.findAll().size();
        // set the field null
        cmsmenu.setDescription(null);

        // Create the Cmsmenu, which fails.

        restCmsmenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isBadRequest());

        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCmsmenus() throws Exception {
        // Initialize the database
        cmsmenuRepository.saveAndFlush(cmsmenu);

        // Get all the cmsmenuList
        restCmsmenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cmsmenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].css").value(hasItem(DEFAULT_CSS)))
            .andExpect(jsonPath("$.[*].menuType").value(hasItem(DEFAULT_MENU_TYPE)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCmsmenusWithEagerRelationshipsIsEnabled() throws Exception {
        when(cmsmenuServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCmsmenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cmsmenuServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCmsmenusWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cmsmenuServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCmsmenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cmsmenuServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCmsmenu() throws Exception {
        // Initialize the database
        cmsmenuRepository.saveAndFlush(cmsmenu);

        // Get the cmsmenu
        restCmsmenuMockMvc
            .perform(get(ENTITY_API_URL_ID, cmsmenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cmsmenu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.css").value(DEFAULT_CSS))
            .andExpect(jsonPath("$.menuType").value(DEFAULT_MENU_TYPE))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCmsmenu() throws Exception {
        // Get the cmsmenu
        restCmsmenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCmsmenu() throws Exception {
        // Initialize the database
        cmsmenuRepository.saveAndFlush(cmsmenu);

        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();

        // Update the cmsmenu
        Cmsmenu updatedCmsmenu = cmsmenuRepository.findById(cmsmenu.getId()).get();
        // Disconnect from session so that the updates on updatedCmsmenu are not directly saved in db
        em.detach(updatedCmsmenu);
        updatedCmsmenu
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .css(UPDATED_CSS)
            .menuType(UPDATED_MENU_TYPE)
            .order(UPDATED_ORDER)
            .active(UPDATED_ACTIVE)
            .language(UPDATED_LANGUAGE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restCmsmenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCmsmenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCmsmenu))
            )
            .andExpect(status().isOk());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
        Cmsmenu testCmsmenu = cmsmenuList.get(cmsmenuList.size() - 1);
        assertThat(testCmsmenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmsmenu.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCmsmenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCmsmenu.getCss()).isEqualTo(UPDATED_CSS);
        assertThat(testCmsmenu.getMenuType()).isEqualTo(UPDATED_MENU_TYPE);
        assertThat(testCmsmenu.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCmsmenu.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCmsmenu.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCmsmenu.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingCmsmenu() throws Exception {
        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();
        cmsmenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmsmenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cmsmenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cmsmenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCmsmenu() throws Exception {
        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();
        cmsmenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsmenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cmsmenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCmsmenu() throws Exception {
        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();
        cmsmenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsmenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCmsmenuWithPatch() throws Exception {
        // Initialize the database
        cmsmenuRepository.saveAndFlush(cmsmenu);

        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();

        // Update the cmsmenu using partial update
        Cmsmenu partialUpdatedCmsmenu = new Cmsmenu();
        partialUpdatedCmsmenu.setId(cmsmenu.getId());

        partialUpdatedCmsmenu
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .css(UPDATED_CSS)
            .menuType(UPDATED_MENU_TYPE)
            .language(UPDATED_LANGUAGE);

        restCmsmenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCmsmenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCmsmenu))
            )
            .andExpect(status().isOk());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
        Cmsmenu testCmsmenu = cmsmenuList.get(cmsmenuList.size() - 1);
        assertThat(testCmsmenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmsmenu.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCmsmenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCmsmenu.getCss()).isEqualTo(UPDATED_CSS);
        assertThat(testCmsmenu.getMenuType()).isEqualTo(UPDATED_MENU_TYPE);
        assertThat(testCmsmenu.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testCmsmenu.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCmsmenu.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCmsmenu.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateCmsmenuWithPatch() throws Exception {
        // Initialize the database
        cmsmenuRepository.saveAndFlush(cmsmenu);

        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();

        // Update the cmsmenu using partial update
        Cmsmenu partialUpdatedCmsmenu = new Cmsmenu();
        partialUpdatedCmsmenu.setId(cmsmenu.getId());

        partialUpdatedCmsmenu
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .css(UPDATED_CSS)
            .menuType(UPDATED_MENU_TYPE)
            .order(UPDATED_ORDER)
            .active(UPDATED_ACTIVE)
            .language(UPDATED_LANGUAGE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restCmsmenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCmsmenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCmsmenu))
            )
            .andExpect(status().isOk());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
        Cmsmenu testCmsmenu = cmsmenuList.get(cmsmenuList.size() - 1);
        assertThat(testCmsmenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmsmenu.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCmsmenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCmsmenu.getCss()).isEqualTo(UPDATED_CSS);
        assertThat(testCmsmenu.getMenuType()).isEqualTo(UPDATED_MENU_TYPE);
        assertThat(testCmsmenu.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testCmsmenu.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCmsmenu.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCmsmenu.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingCmsmenu() throws Exception {
        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();
        cmsmenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmsmenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cmsmenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cmsmenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCmsmenu() throws Exception {
        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();
        cmsmenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsmenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cmsmenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCmsmenu() throws Exception {
        int databaseSizeBeforeUpdate = cmsmenuRepository.findAll().size();
        cmsmenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsmenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cmsmenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cmsmenu in the database
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCmsmenu() throws Exception {
        // Initialize the database
        cmsmenuRepository.saveAndFlush(cmsmenu);

        int databaseSizeBeforeDelete = cmsmenuRepository.findAll().size();

        // Delete the cmsmenu
        restCmsmenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, cmsmenu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cmsmenu> cmsmenuList = cmsmenuRepository.findAll();
        assertThat(cmsmenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
