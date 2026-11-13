package ht.mbds.rattrapage.billetterie;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Evenement(String code, String titre, Categorie categorie, LocalDate date, int capacite, BigDecimal prixBase) {
}
