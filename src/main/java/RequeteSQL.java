import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequeteSQL {
	private Connexion connection = new Connexion("BDD.sqlite");
	Statement statement =null;
	private PreparedStatement preparedStatement=null;


	public String getBDD(String requet) {
		connection.connect();
		try {

			String s = connection.query(requet).getString(1);
			connection.close();
			return s;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connection.close();
			return null;
		}

	}
	
	
	

}
