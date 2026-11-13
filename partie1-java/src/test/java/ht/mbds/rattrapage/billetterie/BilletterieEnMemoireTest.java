package ht.mbds.rattrapage.billetterie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BilletterieEnMemoireTest {

    private Billetterie billetterie;

    @BeforeEach
    void initialiser() {
        billetterie = new BilletterieEnMemoire();
        billetterie.ajouterEvenement(new Evenement("CON-001", "Kompa Live", Categorie.CONCERT,
                LocalDate.of(2030, 6, 15), 2, new BigDecimal("25.00")));
    }

    @Test
    void reservationConfirmee() {
        Reservation reservation = billetterie.reserver("CON-001", new Participant("anne@mail.ht", "Anne"), new TarifPlein());

        assertThat(reservation.id()).isEqualTo("R-0001");
        assertThat(reservation.statut()).isEqualTo(StatutReservation.CONFIRMEE);
        assertThat(reservation.montant()).isEqualByComparingTo("25.00");
        assertThat(billetterie.placesRestantes("CON-001")).isEqualTo(1);
    }

    @Test
    void evenementInconnu() {
        assertThatThrownBy(() -> billetterie.reserver("XXX-999", new Participant("anne@mail.ht", "Anne"), new TarifPlein()))
                .isInstanceOf(EvenementInconnuException.class);
    }

    @Test
    void listeAttenteQuandComplet() {
        billetterie.reserver("CON-001", new Participant("a@mail.ht", "A"), new TarifPlein());
        billetterie.reserver("CON-001", new Participant("b@mail.ht", "B"), new TarifPlein());

        Reservation troisieme = billetterie.reserver("CON-001", new Participant("c@mail.ht", "C"), new TarifPlein());

        assertThat(troisieme.statut()).isEqualTo(StatutReservation.EN_ATTENTE);
    }
}
