package ht.mbds.rattrapage.billetterie;

import java.math.BigDecimal;

public record TarifReduit(int pourcentage) implements Tarif {

    @Override
    public BigDecimal appliquer(BigDecimal prixBase) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int places() {
        throw new UnsupportedOperationException();
    }
}
