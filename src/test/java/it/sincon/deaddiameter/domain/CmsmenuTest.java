package it.sincon.deaddiameter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.sincon.deaddiameter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CmsmenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cmsmenu.class);
        Cmsmenu cmsmenu1 = new Cmsmenu();
        cmsmenu1.setId(1L);
        Cmsmenu cmsmenu2 = new Cmsmenu();
        cmsmenu2.setId(cmsmenu1.getId());
        assertThat(cmsmenu1).isEqualTo(cmsmenu2);
        cmsmenu2.setId(2L);
        assertThat(cmsmenu1).isNotEqualTo(cmsmenu2);
        cmsmenu1.setId(null);
        assertThat(cmsmenu1).isNotEqualTo(cmsmenu2);
    }
}
