package ht.mbds.rattrapage.billetterie;

public class ReservationInconnueException extends BilletterieException {

    public ReservationInconnueException(String id) {
        super("Reservation inconnue : " + id);
    }
}
