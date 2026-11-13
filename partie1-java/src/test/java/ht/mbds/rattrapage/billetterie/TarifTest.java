package ht.mbds.rattrapage.billetterie;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TarifTest {

    @Test
    void tarifReduit() {
        assertThat(new TarifReduit(30).appliquer(new BigDecimal("25.00"))).isEqualByComparingTo("17.50");
    }

    @Test
    void tarifGroupe() {
        assertThat(new TarifGroupe(6).appliquer(new BigDecimal("25.00"))).isEqualByComparingTo("127.50");
        assertThat(new TarifGroupe(6).places()).isEqualTo(6);
    }
}
