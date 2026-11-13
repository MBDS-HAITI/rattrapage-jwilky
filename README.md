# Rattrapage Java et Spring

## Démarrage

1. **Forkez ce dépôt** sur votre compte GitHub.
2. **Réalisez les exercices dans votre fork.** Clonez-le, travaillez dedans et poussez vos commits régulièrement.
3. **Soumettez à la surveillante l'adresse de votre fork** à la fin de l'épreuve. Seul ce qui est poussé sur votre fork au moment de la soumission sera corrigé.

---

**Durée : 2 heures.** Répartition conseillée : 50 minutes pour la partie 1, 70 minutes pour la partie 2. Travail individuel. Internet et tous les outils (IDE, documentation, assistants IA) sont autorisés. Toute communication avec une autre personne est interdite.

Le travail est noté sur deux matières : **Java (/20)** pour la partie 1 et **Spring (/20)** pour la partie 2.

## Consignes

- Le dépôt fourni contient deux projets Maven indépendants : `partie1-java` et `partie2-spring`. Java 17 ou plus récent est requis.
- **Ne renommez pas et ne déplacez pas** les classes, méthodes, paquetages et fichiers fournis. Votre code sera exécuté contre des tests de correction supplémentaires qui s'appuient sur ces noms.
- Les deux projets doivent compiler (`mvn test`). Un projet qui ne compile pas ne peut pas passer les tests de correction.
- Faites des **commits réguliers** (au moins un par exercice) et poussez votre travail avant la fin de l'épreuve. Seul ce qui est poussé sera corrigé.
- Une **soutenance individuelle** suit l'épreuve : vous devrez expliquer votre code et le modifier en direct. Un code que vous ne savez pas expliquer ne sera pas valorisé.

---

# Partie 1 — Java (projet `partie1-java`)

Paquetage : `ht.mbds.rattrapage.billetterie`. Aucune dépendance autre que JUnit et AssertJ (déjà configurées).

Une agence gère une billetterie d'événements (concerts, conférences, matchs, pièces de théâtre). Les classes fournies définissent les signatures ; vous devez écrire les règles.

Les classes `Categorie`, `StatutReservation`, `Tarif`, `Billetterie` et toutes les exceptions sont fournies complètes. Vous ne devez pas les modifier.

## Exercice 1 — Modèle (records)

### `Participant(String email, String nom)`
Le constructeur lève `IllegalArgumentException` si :
- l'email est `null` ou ne respecte pas la forme `texte@texte.texte` (un seul `@`, au moins un point après le `@`, aucun espace) ;
- le nom est `null` ou vide (ou composé uniquement d'espaces).

Les espaces en début et fin d'email sont ignorés. L'email est stocké **en minuscules** et le nom **sans espaces en début et fin**. Deux participants de même email (quelle que soit la casse) et de même nom sont donc égaux.

### `Evenement(String code, String titre, Categorie categorie, LocalDate date, int capacite, BigDecimal prixBase)`
Le constructeur lève `IllegalArgumentException` si :
- le code est `null` ou ne respecte pas le format **3 lettres majuscules, un tiret, 3 chiffres** (exemple : `CON-001`) ;
- le titre est `null` ou vide ;
- la catégorie ou la date est `null` ;
- la capacité est inférieure ou égale à 0 ;
- le prix est `null` ou négatif (un prix de 0 est accepté).

Le titre est stocké sans espaces en début et fin, et le prix avec **exactement 2 décimales** (arrondi `HALF_UP`).

### Tarifs (`TarifPlein`, `TarifReduit`, `TarifGroupe`)
Chaque tarif implémente l'interface scellée `Tarif` :
- `appliquer(prixBase)` renvoie le montant à payer, **arrondi à 2 décimales** (`HALF_UP`) ;
- `places()` renvoie le nombre de places consommées.

| Tarif | Contrainte (sinon `IllegalArgumentException`) | Montant | Places |
|---|---|---|---|
| `TarifPlein()` | — | prix de base | 1 |
| `TarifReduit(pourcentage)` | pourcentage entre 1 et 100 | prix × (100 − pourcentage) / 100 | 1 |
| `TarifGroupe(nombrePersonnes)` | nombre entre 5 et 50 | prix × nombre × 0,85 | nombre |

Exemples : 30 % sur 25,00 → 17,50 ; 15 % sur 19,99 → 16,99 ; un groupe de 5 à 19,99 → 84,96.

### `Reservation`
`avecStatut(nouveauStatut)` renvoie une **nouvelle** réservation identique, avec le statut demandé. L'objet d'origine n'est pas modifié.

## Exercice 2 — `BilletterieEnMemoire`

Implémentez l'interface `Billetterie` en mémoire.

**`ajouterEvenement(evenement)`** — Lève `EvenementDejaExistantException` si un événement de même code existe déjà.

**`reserver(codeEvenement, participant, tarif)`**
1. Lève `EvenementInconnuException` si l'événement n'existe pas.
2. Lève `ReservationDupliqueeException` si ce participant (même email) a déjà, pour cet événement, une réservation qui n'est pas annulée.
3. Si les places restantes suffisent pour **toutes** les places du tarif, la réservation est `CONFIRMEE` ; sinon elle est `EN_ATTENTE`. Un groupe n'est jamais confirmé partiellement.
4. Le montant est celui du tarif appliqué au prix de base de l'événement.
5. Les identifiants sont `R-0001`, `R-0002`, … dans l'ordre de création, tous événements confondus.

**`annuler(idReservation)`**
- Lève `ReservationInconnueException` si la réservation n'existe pas, et `IllegalStateException` si elle est déjà annulée.
- Passe la réservation à `ANNULEE` et la renvoie.
- **Promotion** : la liste d'attente de l'événement est ensuite parcourue dans l'ordre d'arrivée. Chaque réservation qui tient dans les places libres est confirmée. Celles qui ne tiennent pas restent en attente, sans bloquer les suivantes.

**`placesRestantes(code)`** — Capacité moins les places des réservations `CONFIRMEE`. Lève `EvenementInconnuException` si l'événement n'existe pas.

**`reservationsDe(email)`** — Toutes les réservations du participant, quel que soit leur statut. L'email est comparé sans tenir compte de la casse. Tri par date de l'événement, puis par identifiant de réservation.

**`listeAttente(code)`** — Les réservations `EN_ATTENTE` de l'événement, dans l'ordre d'arrivée. Lève `EvenementInconnuException` si l'événement n'existe pas.

**`chiffreAffairesParCategorie()`** — Somme des montants des réservations `CONFIRMEE`, par catégorie. Une catégorie sans réservation confirmée n'apparaît pas.

**`evenementLePlusRempli()`** — L'événement au plus fort taux de remplissage (places confirmées / capacité). En cas d'égalité, le plus proche dans le temps (date la plus ancienne), puis le code le plus petit dans l'ordre alphabétique. Renvoie `Optional.empty()` s'il n'y a aucun événement.

## Exercice 3 — Généricité : `Lots.decouper(elements, taille)`

Découpe une liste en lots consécutifs de `taille` éléments. Le dernier lot peut être plus court.
- Une liste vide donne une liste vide.
- Lève `IllegalArgumentException` si `taille` est inférieure ou égale à 0.
- Les lots renvoyés ne doivent pas changer si la liste source est modifiée ensuite.
- La signature `<T> List<List<T>> decouper(List<? extends T> elements, int taille)` doit être conservée.

Exemple : `decouper([1, 2, 3, 4, 5, 6, 7], 3)` donne `[[1, 2, 3], [4, 5, 6], [7]]`.

---

# Partie 2 — Spring Boot (projet `partie2-spring`)

Le projet fournit uniquement la classe `BilletterieApplication` (paquetage `ht.mbds.rattrapage.api`) et les dépendances : Web, Data JPA, Validation, Security, base H2 en mémoire. **Gardez `BilletterieApplication` à sa place.** Tout le reste est à concevoir.

Vous exposez la billetterie sous forme d'API REST, avec les mêmes règles métier que dans la partie 1 (tarifs, liste d'attente, promotion).

## Exigences techniques

- Architecture en couches : contrôleurs, services, repositories Spring Data JPA, entités JPA.
- Les entités JPA ne sont jamais exposées directement : utilisez des objets de requête et de réponse.
- Validation des entrées avec Bean Validation (`@Valid`).
- Gestion centralisée des erreurs (`@RestControllerAdvice`).
- Sécurité HTTP Basic avec deux comptes en mémoire :
  - `admin` / `admin123`, rôle `ADMIN` ;
  - `agent` / `agent123`, rôle `AGENT`.
- Vous devez écrire vos propres tests : au moins un test unitaire de service avec Mockito et au moins un test de contrôleur avec MockMvc.

## Format des erreurs

Toute erreur renvoie un corps JSON de la forme :

```json
{ "status": 404, "message": "Evenement inconnu : CON-999" }
```

En cas d'erreur de validation (400), le corps contient en plus un objet `erreurs` qui associe **le nom de chaque champ en erreur** à un message :

```json
{ "status": 400, "message": "Requete invalide", "erreurs": { "capacite": "doit etre superieur ou egal a 1" } }
```

Un corps JSON illisible, une valeur d'énumération inconnue (catégorie, tarif, statut) ou un paramètre mal typé renvoient aussi 400. Pour les erreurs 401 et 403, seul le code HTTP est imposé.

## Contrat de l'API

Les montants et prix sont des nombres JSON à 2 décimales et les dates sont au format `AAAA-MM-JJ`.

### Événements

**`POST /api/evenements`** — rôle `ADMIN`

```json
{ "code": "CON-001", "titre": "Kompa Live", "categorie": "CONCERT", "date": "2030-06-15", "capacite": 500, "prixBase": 25.00 }
```

| Champ | Règle |
|---|---|
| `code` | obligatoire, format `[A-Z]{3}-[0-9]{3}` |
| `titre` | obligatoire, non vide, 100 caractères au plus |
| `categorie` | obligatoire : `CONCERT`, `CONFERENCE`, `SPORT` ou `THEATRE` |
| `date` | obligatoire, aujourd'hui ou dans le futur |
| `capacite` | obligatoire, entre 1 et 10000 |
| `prixBase` | obligatoire, positif ou nul, 2 décimales au plus |

Réponses :
- **201**, avec un en-tête `Location: /api/evenements/{code}` et le corps de l'événement :
  ```json
  { "code": "CON-001", "titre": "Kompa Live", "categorie": "CONCERT", "date": "2030-06-15", "capacite": 500, "prixBase": 25.00, "placesRestantes": 500 }
  ```
- 400 si la validation échoue (clés `erreurs` : noms des champs) ;
- 409 si le code existe déjà ;
- 401 sans authentification (ou avec un mauvais mot de passe), 403 pour `agent`.

**`GET /api/evenements?categorie=&page=0&taille=20`** — public
- Tous les paramètres sont facultatifs. Par défaut `page` vaut 0 et `taille` vaut 20.
- 400 si `page` est négatif, si `taille` n'est pas entre 1 et 100, ou si la catégorie est inconnue.
- Tri par date croissante, puis par code.
- Réponse 200 :
  ```json
  { "contenu": [ { "code": "...", "placesRestantes": 12, "...": "..." } ], "page": 0, "taille": 20, "totalElements": 42, "totalPages": 3 }
  ```

**`GET /api/evenements/{code}`** — public. 200, ou 404 si l'événement n'existe pas.

### Réservations

**`POST /api/evenements/{code}/reservations`** — public

```json
{ "email": "jean@mail.ht", "nom": "Jean", "tarif": "REDUIT", "pourcentage": 30, "nombrePersonnes": null }
```

| Champ | Règle |
|---|---|
| `email` | obligatoire, adresse email valide ; stocké en minuscules |
| `nom` | obligatoire, non vide |
| `tarif` | obligatoire : `PLEIN`, `REDUIT` ou `GROUPE` |
| `pourcentage` | obligatoire si `REDUIT`, entre 1 et 100 ; sinon 400 avec la clé `pourcentage` dans `erreurs` |
| `nombrePersonnes` | obligatoire si `GROUPE`, entre 5 et 50 ; sinon 400 avec la clé `nombrePersonnes` dans `erreurs` |

Réponses :
- **201**, avec un en-tête `Location: /api/reservations/{id}` et le corps :
  ```json
  { "id": 12, "codeEvenement": "CON-001", "email": "jean@mail.ht", "nom": "Jean", "tarif": "REDUIT", "places": 1, "montant": 17.50, "statut": "CONFIRMEE" }
  ```
- 404 si l'événement n'existe pas ;
- 409 si cet email (sans tenir compte de la casse) a déjà une réservation non annulée sur l'événement.

Le statut (`CONFIRMEE` ou `EN_ATTENTE`), les places et le montant suivent les règles de la partie 1.

**`GET /api/reservations/{id}`** — public. 200, ou 404.

**`DELETE /api/reservations/{id}`** — rôle `ADMIN` ou `AGENT`
- **200**, avec la réservation passée à `ANNULEE` dans le corps ; puis promotion de la liste d'attente selon les règles de la partie 1, en traitant les réservations par identifiant croissant.
- 404 si la réservation n'existe pas, 409 si elle est déjà annulée, 401 sans authentification.
