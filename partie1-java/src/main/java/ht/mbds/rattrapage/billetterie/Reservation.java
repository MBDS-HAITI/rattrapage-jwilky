package ht.mbds.rattrapage.billetterie;

import java.math.BigDecimal;

public record Reservation(String id, Evenement evenement, Participant participant, Tarif tarif, BigDecimal montant,
                          StatutReservation statut) {

    public Reservation avecStatut(StatutReservation nouveauStatut) {
        throw new UnsupportedOperationException();
    }
}
