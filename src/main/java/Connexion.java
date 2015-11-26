import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;



public class Connexion {
	private String DBPath;
	private Connection connection = null;
	private Statement statement = null;

	public Connexion(String dBPath) {
		DBPath = dBPath;
	}

	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			statement = connection.createStatement();
			System.out.println("Connexion a " + DBPath + " avec succès");

		} catch (ClassNotFoundException notFoundException) {
			notFoundException.printStackTrace();
			System.out.println("ClassNotFoundException");
		} catch (SQLException ignore) {
			// sqlException.printStackTrace();
			System.out.println("SQLException");
		}
	}

	public ResultSet query(String requet) {
		// ResultSet est un tableau de données de la base de données
		ResultSet resultat = null;
		try {
			// On stocke la requete SQL dans un tableau de données
			resultat = statement.executeQuery(requet);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur dans la requet : " + requet);
		}
		return resultat;

	}
	
	 public void setBDD(String symbole,String nom,float prix, int nbr_actions,Date date) {
	        try {
	            PreparedStatement preparedStatement = connection
	                    .prepareStatement("INSERT INTO Entreprises VALUES(?,?,?,?,?)");
	            preparedStatement.setString(1, symbole);
	            preparedStatement.setString(2, nom);
	            preparedStatement.setFloat(3, prix);
	            preparedStatement.setInt(4, nbr_actions);
	            preparedStatement.setDate(5, date);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	 
	 

	public void close() {
		try {
			connection.close();
			statement.close();
		} catch (SQLException ignore) {
			// e.printStackTrace();
		}
	}

}
