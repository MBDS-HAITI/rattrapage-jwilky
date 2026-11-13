package ht.mbds.rattrapage.billetterie;

public class ReservationDupliqueeException extends BilletterieException {

    public ReservationDupliqueeException(String email, String codeEvenement) {
        super("Reservation deja existante pour " + email + " sur " + codeEvenement);
    }
}
