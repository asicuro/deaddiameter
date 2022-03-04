package it.sincon.deaddiameter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.sincon.deaddiameter.IntegrationTest;
import it.sincon.deaddiameter.domain.Cmsroles;
import it.sincon.deaddiameter.repository.CmsrolesRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CmsrolesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CmsrolesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/cmsroles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CmsrolesRepository cmsrolesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCmsrolesMockMvc;

    private Cmsroles cmsroles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmsroles createEntity(EntityManager em) {
        Cmsroles cmsroles = new Cmsroles().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).active(DEFAULT_ACTIVE);
        return cmsroles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmsroles createUpdatedEntity(EntityManager em) {
        Cmsroles cmsroles = new Cmsroles().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);
        return cmsroles;
    }

    @BeforeEach
    public void initTest() {
        cmsroles = createEntity(em);
    }

    @Test
    @Transactional
    void createCmsroles() throws Exception {
        int databaseSizeBeforeCreate = cmsrolesRepository.findAll().size();
        // Create the Cmsroles
        restCmsrolesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsroles)))
            .andExpect(status().isCreated());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeCreate + 1);
        Cmsroles testCmsroles = cmsrolesList.get(cmsrolesList.size() - 1);
        assertThat(testCmsroles.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCmsroles.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCmsroles.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createCmsrolesWithExistingId() throws Exception {
        // Create the Cmsroles with an existing ID
        cmsroles.setId(1L);

        int databaseSizeBeforeCreate = cmsrolesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCmsrolesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsroles)))
            .andExpect(status().isBadRequest());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmsrolesRepository.findAll().size();
        // set the field null
        cmsroles.setName(null);

        // Create the Cmsroles, which fails.

        restCmsrolesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsroles)))
            .andExpect(status().isBadRequest());

        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmsrolesRepository.findAll().size();
        // set the field null
        cmsroles.setDescription(null);

        // Create the Cmsroles, which fails.

        restCmsrolesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsroles)))
            .andExpect(status().isBadRequest());

        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCmsroles() throws Exception {
        // Initialize the database
        cmsrolesRepository.saveAndFlush(cmsroles);

        // Get all the cmsrolesList
        restCmsrolesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cmsroles.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCmsroles() throws Exception {
        // Initialize the database
        cmsrolesRepository.saveAndFlush(cmsroles);

        // Get the cmsroles
        restCmsrolesMockMvc
            .perform(get(ENTITY_API_URL_ID, cmsroles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cmsroles.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCmsroles() throws Exception {
        // Get the cmsroles
        restCmsrolesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCmsroles() throws Exception {
        // Initialize the database
        cmsrolesRepository.saveAndFlush(cmsroles);

        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();

        // Update the cmsroles
        Cmsroles updatedCmsroles = cmsrolesRepository.findById(cmsroles.getId()).get();
        // Disconnect from session so that the updates on updatedCmsroles are not directly saved in db
        em.detach(updatedCmsroles);
        updatedCmsroles.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restCmsrolesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCmsroles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCmsroles))
            )
            .andExpect(status().isOk());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
        Cmsroles testCmsroles = cmsrolesList.get(cmsrolesList.size() - 1);
        assertThat(testCmsroles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmsroles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCmsroles.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingCmsroles() throws Exception {
        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();
        cmsroles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmsrolesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cmsroles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cmsroles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCmsroles() throws Exception {
        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();
        cmsroles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsrolesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cmsroles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCmsroles() throws Exception {
        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();
        cmsroles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsrolesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cmsroles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCmsrolesWithPatch() throws Exception {
        // Initialize the database
        cmsrolesRepository.saveAndFlush(cmsroles);

        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();

        // Update the cmsroles using partial update
        Cmsroles partialUpdatedCmsroles = new Cmsroles();
        partialUpdatedCmsroles.setId(cmsroles.getId());

        partialUpdatedCmsroles.name(UPDATED_NAME);

        restCmsrolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCmsroles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCmsroles))
            )
            .andExpect(status().isOk());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
        Cmsroles testCmsroles = cmsrolesList.get(cmsrolesList.size() - 1);
        assertThat(testCmsroles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmsroles.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCmsroles.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCmsrolesWithPatch() throws Exception {
        // Initialize the database
        cmsrolesRepository.saveAndFlush(cmsroles);

        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();

        // Update the cmsroles using partial update
        Cmsroles partialUpdatedCmsroles = new Cmsroles();
        partialUpdatedCmsroles.setId(cmsroles.getId());

        partialUpdatedCmsroles.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restCmsrolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCmsroles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCmsroles))
            )
            .andExpect(status().isOk());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
        Cmsroles testCmsroles = cmsrolesList.get(cmsrolesList.size() - 1);
        assertThat(testCmsroles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCmsroles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCmsroles.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCmsroles() throws Exception {
        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();
        cmsroles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmsrolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cmsroles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cmsroles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCmsroles() throws Exception {
        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();
        cmsroles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsrolesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cmsroles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCmsroles() throws Exception {
        int databaseSizeBeforeUpdate = cmsrolesRepository.findAll().size();
        cmsroles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCmsrolesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cmsroles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cmsroles in the database
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCmsroles() throws Exception {
        // Initialize the database
        cmsrolesRepository.saveAndFlush(cmsroles);

        int databaseSizeBeforeDelete = cmsrolesRepository.findAll().size();

        // Delete the cmsroles
        restCmsrolesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cmsroles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cmsroles> cmsrolesList = cmsrolesRepository.findAll();
        assertThat(cmsrolesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
