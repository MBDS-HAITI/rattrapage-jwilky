package ht.mbds.rattrapage.billetterie;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BilletterieEnMemoire implements Billetterie {

    @Override
    public void ajouterEvenement(Evenement evenement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reservation reserver(String codeEvenement, Participant participant, Tarif tarif) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reservation annuler(String idReservation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int placesRestantes(String codeEvenement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Reservation> reservationsDe(String email) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Reservation> listeAttente(String codeEvenement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Categorie, BigDecimal> chiffreAffairesParCategorie() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Evenement> evenementLePlusRempli() {
        throw new UnsupportedOperationException();
    }
}
