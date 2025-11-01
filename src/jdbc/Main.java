package src.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
  
  public static void main(String[] args) {

    System.out.println("Executing jdbc main file");

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

      //** GET **//
      
      // On récupère le résultat de l'éxecution de la query dans l'objet ResultSet
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

      //** UPDATE **//
      String nom = "Saber";
      int id = 4;
      String email = "saber@mail.com";
      String reqUpdate = "INSERT INTO customer(id, email, name) VALUES ('"+id+"', '"+email+"', '"+nom+"')";

      // on envoie la requete update (et on la récupère pour analyser son retour int)
      int resUpdate = st.executeUpdate(reqUpdate);

      if (resUpdate == 1) {
        System.out.println("New customer with name " + nom);
      }


      //** DELETE **//
      int resDelete = st.executeUpdate("delete from customer where name = 'Saber'");
      
      if (resDelete != 0) {
        System.out.println("Customer " + nom + " has been deleted");
      }

      // Un Statement est une ressource JDBC et il doit être fermé dès qu’il n’est plus nécessaire 
      st.close();
      System.out.println("Connection with database has been succesfully closed");

      // Un ResultSet est aussi une ressource JDBC et il doit être fermé dès qu’il n’est plus nécessaire 
      res.close();

    } catch (ClassNotFoundException e) {
            System.out.println("Pilote JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

    
  }
  
}
