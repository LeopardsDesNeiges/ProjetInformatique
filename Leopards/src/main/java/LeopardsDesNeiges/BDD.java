package LeopardsDesNeiges;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDD {
	public static String username;
	private String DBPath;
	private Connection connection = null;
	private Statement statement = null;

	public BDD(String dBPath) {
		DBPath = dBPath;
	}

	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			statement = connection.createStatement();
		} catch (ClassNotFoundException notFoundException) {
			notFoundException.printStackTrace();
			System.out.println("ClassNotFoundException");
		} catch (SQLException ignore) {
			System.out.println("SQLException");
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public String getNom(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT name from main where Symbole=? AND username=?");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			String s = resultat.getString(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public double getLimiteAchat() {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT limite from Limte where username=?");
			preparedStatement.setString(1, this.username);
			ResultSet resultat = preparedStatement.executeQuery();
			double s = resultat.getDouble(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0.0;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

		public int getmemAchatAuto(String symbole) {
			connect();
			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT memAchatAuto from main where username=? and symbole=?");
				preparedStatement.setString(1, this.username);
				preparedStatement.setString(2, symbole);
				ResultSet resultat = preparedStatement.executeQuery();
				int s = resultat.getInt(1);
				return s;
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			} finally {
				close();
			}
		}
		
		// -----------------------------------------------------------------------------------------------------------------------------

				public int getmemVenteAuto(String symbole) {
					connect();
					try {
						PreparedStatement preparedStatement = connection
								.prepareStatement("SELECT memVenteAuto from main where username=? and symbole=?");
						preparedStatement.setString(1, this.username);
						preparedStatement.setString(2, symbole);
						ResultSet resultat = preparedStatement.executeQuery();
						int s = resultat.getInt(1);
						return s;
					} catch (SQLException e) {
						e.printStackTrace();
						return 0;
					} finally {
						close();
					}
				}
	
				// -----------------------------------------------------------------------------------------------------------------------------

				public double getmemPrixAchatAuto(String symbole) {
					connect();
					try {
						PreparedStatement preparedStatement = connection
								.prepareStatement("SELECT memPrixAchatAuto from main where username=? and symbole=?");
						preparedStatement.setString(1, this.username);
						preparedStatement.setString(2, symbole);
						ResultSet resultat = preparedStatement.executeQuery();
						double s = resultat.getDouble(1);
						return s;
					} catch (SQLException e) {
						e.printStackTrace();
						return 0;
					} finally {
						close();
					}
				}
				
				
				// -----------------------------------------------------------------------------------------------------------------------------

				public double getmemPrixVenteAuto(String symbole) {
					connect();
					try {
						PreparedStatement preparedStatement = connection
								.prepareStatement("SELECT memPrixVenteAuto from main where username=? and symbole=?");
						preparedStatement.setString(1, this.username);
						preparedStatement.setString(2, symbole);
						ResultSet resultat = preparedStatement.executeQuery();
						double s = resultat.getDouble(1);
						return s;
					} catch (SQLException e) {
						e.printStackTrace();
						return 0;
					} finally {
						close();
					}
				}
				
				
	// ----------------------------------------------------------------------------------------------------------------------------
	public Integer getNbrActionHistorique(String symbole, long histo) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT nbr_action FROM main where date=(SELECT MAX(date) from main where symbole=? AND username=? AND date=?)");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			preparedStatement.setLong(3, histo);
			ResultSet resultat = preparedStatement.executeQuery();
			int s = resultat.getInt(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Double getlimSup(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT limSup from main where symbole=? AND username=? And date=(Select max(date) from main where symbole=? AND username=?)");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.setString(4, this.username);
			ResultSet resultat = preparedStatement.executeQuery();
			Double s = resultat.getDouble(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Double getlimInf(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT limInf from main where symbole=? AND username=? And date=(Select max(date) from main where symbole=? AND username=?)");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.setString(4, this.username);
			ResultSet resultat = preparedStatement.executeQuery();
			Double s = resultat.getDouble(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public List<String> getAllSymbole() {
		connect();
		try {
			List<String> list = new ArrayList<String>();
			PreparedStatement preparedStatement = connection
					.prepareStatement("select distinct symbole from main where username='"
							+ username + "';");
			ResultSet resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
				String s = resultat.getString(1);
				list.add(s);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public String getSymboleI(int position) throws SQLException {
		List<String> resultat = getAllSymbole();
		return resultat.get(position);
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public Map<String, String> getNews(String symbole) {
		Map<String, String> map = new HashMap<String, String>();
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select lien,titre from News where symbole=?");
			preparedStatement.setString(1, symbole);
			ResultSet resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
				String lien = resultat.getString(1);
				String titre = resultat.getString(2);
				map.put(titre, lien);
			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;

		} finally {
			close();
		}
	}

	public Boolean compareUsername(String username) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT username from Login where username=?");
			preparedStatement.setString(1, username);
			ResultSet resultat = preparedStatement.executeQuery();
			if (!resultat.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Integer getNbrAction(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT nbr_action FROM main where date=(SELECT MAX(date) from main where symbole=? AND username=?)");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			int s = resultat.getInt(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Timestamp getDateTime(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT date from main where symbole=? AND username=?");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			Timestamp s = resultat.getTimestamp(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Date getDate(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT date from main where symbole=? AND username=?");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			Date s = resultat.getDate(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Date getPremiereDate(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT MIN(date) from main where symbole=? AND username=?");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			Date s = resultat.getDate(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public Timestamp getDateRecente(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT MAX(date) from main where symbole=? AND username=?");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			Timestamp s = resultat.getTimestamp(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public int getRisque(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT Risque from main where username=? and symbole=?");
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, symbole);
			ResultSet resultat = preparedStatement.executeQuery();
			int s = resultat.getInt(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public int getachatAuto(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT achatAuto from main where username=? and symbole=?");
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, symbole);
			ResultSet resultat = preparedStatement.executeQuery();
			int s = resultat.getInt(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public int getVenteAuto(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT venteAuto from main where username=? and symbole=?");
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, symbole);
			ResultSet resultat = preparedStatement.executeQuery();
			int s = resultat.getInt(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public double getprixAchatAuto(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT prixAchatAuto from main where username=? and symbole=?");
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, symbole);
			ResultSet resultat = preparedStatement.executeQuery();
			double s = resultat.getDouble(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
	public double getprixVenteAuto(String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT prixVenteAuto from main where username=? and symbole=?");
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, symbole);
			ResultSet resultat = preparedStatement.executeQuery();
			double s = resultat.getDouble(1);
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
	public void setRisque(String symbole, int b) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET Risque= ? Where username=? And symbole=?");
			preparedStatement.setInt(1, b);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
	public void setachatAuto(String symbole, int valeur) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET achatAuto= ? Where username=? And symbole=?");
			preparedStatement.setInt(1, valeur);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
	public void setVenteAuto(String symbole, int valeur) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET venteAuto= ? Where username=? And symbole=?");
			preparedStatement.setInt(1, valeur);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
	public void setprixAchatAuto(String symbole, double valeur) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET prixAchatAuto= ? Where username=? And symbole=?");
			preparedStatement.setDouble(1, valeur);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
	public void setprixVenteAuto(String symbole, double valeur) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET prixVenteAuto= ? Where username=? And symbole=?");
			preparedStatement.setDouble(1, valeur);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// -----------------------------------------------------------------------------------------------
		public void setmemAchat(String symbole, double prixAchat, int quantiteAchat) {
			connect();
			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement("UPDATE main SET memPrixAchatAuto= ?, memAchatAuto=? Where username=? And symbole=?");
				preparedStatement.setDouble(1, prixAchat );
				preparedStatement.setInt(2, quantiteAchat );
				preparedStatement.setString(3, this.username);
				preparedStatement.setString(4, symbole);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
		
		// -----------------------------------------------------------------------------------------------
				public void setmemVente(String symbole, double prixVente, int quantiteVente) {
					connect();
					try {
						PreparedStatement preparedStatement = connection
								.prepareStatement("UPDATE main SET memPrixVenteAuto= ?, memVenteAuto=? Where username=? And symbole=?");
						preparedStatement.setDouble(1, prixVente );
						preparedStatement.setInt(2, quantiteVente );
						preparedStatement.setString(3, this.username);
						preparedStatement.setString(4, symbole);
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						close();
					}
				}
	
	
	// ----------------------------------------------------------------------------------------------------------------------------
	public long[] getDateHistorique(String symbole) {
		connect();
		Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long[] truc = new long[30];
		int i = 1;
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT date from main WHERE symbole=? AND username=? ORDER BY date DESC");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			ResultSet resultat = preparedStatement.executeQuery();
			Timestamp s = resultat.getTimestamp(1);
			truc[0] = s.getTime();

			while (resultat.next()) {
				s = resultat.getTimestamp(1);
				String dp = format.format(s);
				String az = dp.split(" ")[0];
				Date chose = new Date(truc[i - 1]);
				String bidule = format.format(chose);
				if (comparator.compare(az, bidule) == 0) {
					truc[i] = truc[i - 1];
				} else {
					truc[i] = s.getTime();
				}

				i++;
			}

			return truc;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void createLimiteLogin(String jusername) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO Limte (username,limite) VALUES (?,?)");
			preparedStatement.setString(1, jusername);
			preparedStatement.setDouble(2, 1000);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void setLogin(String jusername, String jpassword) {
		connect();
		try {

			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO Login (username,pwd) VALUES (?,?)");
			preparedStatement.setString(1, jusername);
			preparedStatement.setString(2, jpassword);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void setLimiteAchat(double limite) {
		connect();
		try {

			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE Limte SET limite=? WHERE username=?");
			preparedStatement.setDouble(1, limite);
			preparedStatement.setString(2, this.username);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void setlimSup(Double limite, String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET limSup= ? Where username=? And symbole=?;");
			preparedStatement.setDouble(1, limite);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void setlimInf(Double limite, String symbole) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET limInf= ? Where username=? And symbole=?;");
			preparedStatement.setDouble(1, limite);
			preparedStatement.setString(2, this.username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void setdate(String symbole, Object date) {
		connect();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE main SET date= ? Where username=? And symbole=?;");
			preparedStatement.setObject(1, date);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, symbole);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void insertBDD(String symbole, int nbr_action) {
		connect();
		StockData sd = new StockData();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO main (username,symbole,name,nbr_action,date,limSup,limInf,Risque,achatAuto,prixAchatAuto,venteAuto,prixVenteAuto,memAchatAuto,memPrixAchatAuto,memVenteAuto,memPrixVenteAuto) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, symbole);
			preparedStatement.setString(3, sd.getName(symbole));
			preparedStatement.setInt(4, nbr_action);
			Timestamp dateTime = new Timestamp(System.currentTimeMillis());
			preparedStatement.setTimestamp(5, dateTime);
			preparedStatement.setInt(6, 100);
			preparedStatement.setInt(7, 20);
			preparedStatement.setInt(8, 0);
			preparedStatement.setInt(9, 0);
			preparedStatement.setInt(10, 10000);
			preparedStatement.setInt(11, 0);
			preparedStatement.setInt(12, 0);
			preparedStatement.setInt(13, 0);
			preparedStatement.setDouble(14, 0);
			preparedStatement.setInt(15, 0);
			preparedStatement.setDouble(16, 0);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
	
	// ----------------------------------------------------------------------------------------------------------------------------

		public void insertBDD(String symbole, int nbr_action, int achatAuto, double prixAchatAuto, int venteAuto, double prixVenteAuto, int memAchatAuto, double memPrixAchatAuto, int memVenteAuto, double memPrixVenteAuto) {
			connect();
			StockData sd = new StockData();
			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO main (username,symbole,name,nbr_action,date,limSup,limInf,Risque,achatAuto,prixAchatAuto,venteAuto,prixVenteAuto,memAchatAuto,memPrixAchatAuto,memVenteAuto,memPrixVenteAuto) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, symbole);
				preparedStatement.setString(3, sd.getName(symbole));
				preparedStatement.setInt(4, nbr_action);
				Timestamp dateTime = new Timestamp(System.currentTimeMillis());
				preparedStatement.setTimestamp(5, dateTime);
				preparedStatement.setInt(6, 100);
				preparedStatement.setInt(7, 20);
				preparedStatement.setInt(8, 0);
				preparedStatement.setInt(9, achatAuto);
				preparedStatement.setDouble(10, prixAchatAuto);
				preparedStatement.setInt(11, venteAuto);
				preparedStatement.setDouble(12, prixVenteAuto);
				preparedStatement.setInt(13, memAchatAuto);
				preparedStatement.setDouble(14, memPrixAchatAuto);
				preparedStatement.setInt(15, memVenteAuto);
				preparedStatement.setDouble(16, memPrixVenteAuto);
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}

		}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void insertNews(String symbole, String lien, String text) {

		try {
			connect();
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO News (symbole,lien,titre) VALUES(?,?,?)");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, lien);
			preparedStatement.setString(3, text);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------

	public void deleteNews() {
		try {
			connect();

			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM News");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public void removeBDD(String symbole) {
		connect();
		StockData sd = new StockData();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM main where symbole=? AND username=?");
			preparedStatement.setString(1, symbole);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------
	public void close() {
		try {
			statement.close();
		} catch (Exception ex) {
			System.out.println("Statement exception");
		}

		try {
			connection.close();
		} catch (Exception ex) {
			System.out.println("Error closing connection");

		}
	}

}