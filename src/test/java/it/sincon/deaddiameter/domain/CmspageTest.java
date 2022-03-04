package it.sincon.deaddiameter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.sincon.deaddiameter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CmspageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cmspage.class);
        Cmspage cmspage1 = new Cmspage();
        cmspage1.setId(1L);
        Cmspage cmspage2 = new Cmspage();
        cmspage2.setId(cmspage1.getId());
        assertThat(cmspage1).isEqualTo(cmspage2);
        cmspage2.setId(2L);
        assertThat(cmspage1).isNotEqualTo(cmspage2);
        cmspage1.setId(null);
        assertThat(cmspage1).isNotEqualTo(cmspage2);
    }
}
