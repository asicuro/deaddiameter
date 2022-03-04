package it.sincon.deaddiameter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.sincon.deaddiameter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CmsrolesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cmsroles.class);
        Cmsroles cmsroles1 = new Cmsroles();
        cmsroles1.setId(1L);
        Cmsroles cmsroles2 = new Cmsroles();
        cmsroles2.setId(cmsroles1.getId());
        assertThat(cmsroles1).isEqualTo(cmsroles2);
        cmsroles2.setId(2L);
        assertThat(cmsroles1).isNotEqualTo(cmsroles2);
        cmsroles1.setId(null);
        assertThat(cmsroles1).isNotEqualTo(cmsroles2);
    }
}
