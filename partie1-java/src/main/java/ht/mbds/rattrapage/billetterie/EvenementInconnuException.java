package ht.mbds.rattrapage.billetterie;

public class EvenementInconnuException extends BilletterieException {

    public EvenementInconnuException(String code) {
        super("Evenement inconnu : " + code);
    }
}
