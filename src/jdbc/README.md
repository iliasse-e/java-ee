JDBC est une API développé par SUN permettant de se connecter et intéragir avec les bases de données (SGBD) avec JAVA.
Disponible dans le JDK.

Les étapes du traitement :

1. Connexion à la bd
2. Envoie de requête SQL
3. Manipulation du résultat

Fonctionne selon le principe client-server.

* Client : Le programme Java
* Server : Le SGBD

## Présentation du package `java.sql`

```java
import java.sql.DriverManager; // gestion des pilotes
import java.sql.Connection; // Connexion à la db
import java.sql.Statement; // requetes
import java.sql.ResultSet; // manipulation du résultat
import java.sql.SQLException; // gestion d'erreur
```


## Compilation & execution

Double clic sur le fichier `run-jdbc` pour compiler et éxecuter le fichier Main du package.


## Les requêtes SQL (Statement)

L'interface `Connection` permet de créer des *Statement* (un *Statement* est une interface qui permet d'effectuer des req SQL). Il y 3 types de *Statements* :

- `Statement` : Permet d’exécuter une requête SQL et d’en connaître le résultat.
- `PreparedStatement` : Comme le Statement, le PreparedStatement permet d’exécuter une requête SQL et d’en connaître le résultat. Le PreparedStatement est une requête paramétrable (permet aussi de se prémunir des injections SQL).
- `CallableStatement` : permet d’exécuter des procédures stockées sur le SGBDR.

### Statement

Un Statement est créé à partir d’une des méthodes ``createStatement``.
Cet objet offre ces méthodes :

- une méthode `execute` pour tous les types de requête SQL

- une méthode `executeQuery` (qui retourne un ResultSet) pour les requêtes SQL de type select

- une méthode `executeUpdate` pour toutes les requêtes SQL qui ne sont pas des select


Le renvoi de ces méthodes diffère :

```java
ResultSet resSet = st.executeQuery(); // Doit être fermé .close() après utilisation

boolean bool = st.execute();

int nb = st.executeUpdate();
```

### PreparedStatement

Offre les mêmes méthodes que Statement dont elle hérite.
Le PreparedStatement offre trois avantages :

- convertir efficacement les types Java en types SQL pour les données en entrée

- d’améliorer les performances si on désire exécuter plusieurs fois la même requête avec des paramètres différents (À noter que le PreparedStatement supporte lui aussi le mode batch).

- se prémunir de failles de sécurité telles que l’injection SQL.


```java
PreparedStatement ps = con.prepareStatement("SELECT * FROM customer WHERE name = ");

ps.setString(1, "Saber");

ps.executeQuery();
```

## Fermeture des connections

Les interfaces `Connection`, `Statement`, `ResultSet` héritent de l'interface `AutoCloseable`.
Celle ci permet d'accéder à la méthode `.close()`.
Chaque fin d'utilisation d'un de ces objet doit être marqué par une fermerture/deconnexion.

## Transaction

Dans cet exemple on doit ajouter un produit, et le déduire du stock, ce qui nécessite une transaction en SGBD.

```java
// si nécessaire on force la désactivation de l'auto commit
connection.setAutoCommit(false);
boolean transactionOk = false;

try {

  // on ajoute un produit avec une quantité donnée dans la facture
  String requeteAjoutProduit =
            "insert into ligne_facture (facture_id, produit_id, quantite) values (?, ?, ?)";

  try (PreparedStatement pstmt = connection.prepareStatement(requeteAjoutProduit)) {
    pstmt.setString(1, factureId);
    pstmt.setString(2, produitId);
    pstmt.setLong(3, quantite);

    pstmt.executeUpdate();
  }

  // on déstocke la quantité de produit qui a été ajoutée dans la facture
  String requeteDestockeProduit =
            "update stock_produit set quantite = (quantite - ?) where produit_id = ?";

  try (PreparedStatement pstmt = connection.prepareStatement(requeteDestockeProduit)) {
    pstmt.setLong(1, quantite);
    pstmt.setString(2, produitId);

    pstmt.executeUpdate();
  }

  transactionOk = true;
}
finally {
  // L'utilisation d'une transaction dans cet exemple permet d'éviter d'aboutir à
  // des états incohérents si un problème survient pendant l'exécution du code.
  // Par exemple, si le code ne parvient pas à exécuter la seconde requête SQL
  // (bug logiciel, perte de la connexion avec la base de données, ...) alors
  // une quantité d'un produit aura été ajoutée dans une facture sans avoir été
  // déstockée. Ceci est clairement un état incohérent du système. Dans ce cas,
  // on effectue un rollback de la transaction pour annuler l'insertion dans
  // la table ligne_facture.
  if (transactionOk) {
    connection.commit();
  }
  else {
    connection.rollback();
  }
}
```