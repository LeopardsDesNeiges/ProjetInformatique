package LeopardsDesNeiges;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class Login extends JFrame implements ActionListener, MouseListener {
	private String Path = "BDD.sqlite";
	JPanel pnlLogin;
	JLabel UserName, Password;
	JButton login1, exit1, inscription;
	JTextField txtUserName;
	JPasswordField alicefield;
	Dimension dsSize;
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	
	public Login() {
		super("Fenetre de connexion");
		pnlLogin = new JPanel();
		this.getContentPane().add(pnlLogin);
		pnlLogin.setLayout(null);
		UserName = new JLabel("Pseudo:");
		Password = new JLabel("Mot de passe:");
		txtUserName = new JTextField(20);
		alicefield = new JPasswordField(20);
		login1 = new JButton("Connexion");
		exit1 = new JButton("Quitter");


		inscription = new JButton("Inscription");
		inscription.setMnemonic('V');
		login1.addActionListener(this);
		exit1.addMouseListener(this);
		inscription.addMouseListener(this);
		pnlLogin.setBackground(Color.white);

		UserName.setBounds(10, 0, 90, 25);
		txtUserName.setBounds(120, 0, 180, 25);
		Password.setBounds(10, 30, 90, 25);
		alicefield.setBounds(120, 30, 180, 25);
		login1.setBounds(10, 60, 100, 25);
		exit1.setBounds(235, 60, 100, 25);
		inscription.setBounds(125, 60, 100, 25);

		pnlLogin.add(UserName);
		pnlLogin.add(txtUserName);
		pnlLogin.add(Password);

		pnlLogin.add(alicefield);
		pnlLogin.add(login1);
		pnlLogin.add(exit1);
		pnlLogin.add(inscription);

		setResizable(false);
		setSize(350, 120);
		setVisible(true);
		dsSize = toolkit.getScreenSize();
		setLocation(dsSize.width / 2 - this.getWidth() / 2, dsSize.height / 2
				- this.getHeight() / 2);
		this.getRootPane().setDefaultButton(login1);
	}

	public void actionPerformed(ActionEvent ae) {
			String jusername = txtUserName.getText().trim();
			char[] s = alicefield.getPassword();
			String jpassword = new String(s);
			if (jusername.equals("") || jpassword.equals("")) {
				JOptionPane
						.showMessageDialog(
								this,
								"Entrez votre pseudo et votre mot de passe s'il vous plait",
								"Erreur!", JOptionPane.ERROR_MESSAGE);
				txtUserName.setText("");
				alicefield.setText("");
			} else {
				try {
					Connection conn = DriverManager
							.getConnection("jdbc:sqlite:" + Path);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt
							.executeQuery("select username,pwd from Login where username='"
									+ jusername + "';");
					if (rs.next()) {

						if (rs.getString("pwd").equals(jpassword)) {
							BDD.username = jusername;
							rs.close();
							conn.close();
							setCursor(new Cursor(Cursor.WAIT_CURSOR));
							final Fenetre f = new Fenetre();
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								public void run() {
									f.refreshautomatique();
								}
							}, 0, 120000);
							timer.schedule(new TimerTask() {
								public void run() {
									f.refreshNews();
								}
							}, 60000, 600000);
							super.setVisible(false);
						}

						else {
							JOptionPane
									.showMessageDialog(
											this,
											"Votre mot de passe est erroné veuillez réessayer.",
											"Erreur!",
											JOptionPane.ERROR_MESSAGE);

							alicefield.setText("");

						}
					} else {
						JOptionPane.showMessageDialog(this,
								"Votre pseudo est erroné veuillez réessayer",
								"Erreur!", JOptionPane.ERROR_MESSAGE);
						txtUserName.setText("");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
			
	// -----------------------------------------------------------------------------------------------------------------------------
		public void mouseClicked(MouseEvent ae) {	
			
			if (ae.getSource() == inscription) {
			new Signin();

		}

		if (ae.getSource() == login1) {
			String jusername = txtUserName.getText().trim();
			char[] s = alicefield.getPassword();
			String jpassword = new String(s);
			if (jusername.equals("") || jpassword.equals("")) {
				JOptionPane
						.showMessageDialog(
								this,
								"Entrer votre pseudo et votre mot de passe s'il vous plait",
								"Erreur!", JOptionPane.ERROR_MESSAGE);
				txtUserName.setText("");
				alicefield.setText("");
			} else {
				try {
					Connection conn = DriverManager
							.getConnection("jdbc:sqlite:" + Path);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt
							.executeQuery("select username,pwd from Login where username='"
									+ jusername + "';");
					if (rs.next()) {

						if (rs.getString("pwd").equals(jpassword)) {
							BDD.username = jusername;
							rs.close();
							conn.close();
							setCursor(new Cursor(Cursor.WAIT_CURSOR));
							final Fenetre f = new Fenetre();
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								public void run() {
									f.refreshautomatique();
								}
							}, 0, 120000);
							timer.schedule(new TimerTask() {
								public void run() {
									f.refreshNews();
								}
							}, 60000, 600000);
							super.setVisible(false);
						}

						else {
							JOptionPane
									.showMessageDialog(
											this,
											"Votre mot de passe est erroné veuillez réessayer.",
											"Erreur!",
											JOptionPane.ERROR_MESSAGE);

							alicefield.setText("");

						}
					} else {
						JOptionPane.showMessageDialog(this,
								"Votre pseudo est erroné veuillez réessayer",
								"Erreur!", JOptionPane.ERROR_MESSAGE);
						txtUserName.setText("");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (ae.getSource() == exit1)
			System.exit(0);
		}

		// -----------------------------------------------------------------------------------------------------------------------------
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	public static void main(String args[]) throws IOException {
		new Login();
		
	}

}