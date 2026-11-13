package ht.mbds.rattrapage.billetterie;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Billetterie {

    void ajouterEvenement(Evenement evenement);

    Reservation reserver(String codeEvenement, Participant participant, Tarif tarif);

    Reservation annuler(String idReservation);

    int placesRestantes(String codeEvenement);

    List<Reservation> reservationsDe(String email);

    List<Reservation> listeAttente(String codeEvenement);

    Map<Categorie, BigDecimal> chiffreAffairesParCategorie();

    Optional<Evenement> evenementLePlusRempli();
}
