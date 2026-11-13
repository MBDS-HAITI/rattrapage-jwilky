package ht.mbds.rattrapage.billetterie;

public class EvenementDejaExistantException extends BilletterieException {

    public EvenementDejaExistantException(String code) {
        super("Evenement deja existant : " + code);
    }
}
