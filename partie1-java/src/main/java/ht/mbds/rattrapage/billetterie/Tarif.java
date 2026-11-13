package ht.mbds.rattrapage.billetterie;

import java.math.BigDecimal;

public sealed interface Tarif permits TarifPlein, TarifReduit, TarifGroupe {

    BigDecimal appliquer(BigDecimal prixBase);

    int places();
}
