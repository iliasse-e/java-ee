package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
  
  public static void main(String[] args) {

    try {
      // Chargement du pilote
      Class.forName("com.mysql.cj.jdbc.Driver");

      // Ouverture de la connexion
      Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/e-bank",
        "root",
        "root"
      );

      System.out.println("Connection successful");

      // L'objet Connection créé, on peut réaliser des requêtes via l'objet Statement
      Statement st = con.createStatement();
      
      // On récupère le résultat dans l'objet ResultSet
      ResultSet res = st.executeQuery("select * from customer");

      // Après la requête, le curseur est positionné juste avant la première ligne du résultat
      // La méthode .next() permet d'avancer séquentiellement d'un enregistrement à l'autre

      // Traitement du résultat
      while (res.next()) {
        System.out.println("************ Customer ************");
        System.out.println("ID : " + res.getInt("id"));
        System.out.println("Email : " + res.getString("email"));
        System.out.println("Nom : " + res.getString("name"));
      }

    } catch (ClassNotFoundException e) {
            System.out.println("Pilote JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

    
  }
  
}
