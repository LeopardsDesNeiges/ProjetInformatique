package LeopardsDesNeiges;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

public class Signin extends JFrame implements ActionListener {

	JPanel register;
	JLabel UserName, Password;
	JButton exit, validation;
	JTextField txtUserName;
	JPasswordField pwdField;
	Dimension dsSize;
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	public Signin() {

		super("Fenêtre d'inscription");
		register = new JPanel();
		this.getContentPane().add(register);
		register.setLayout(null);
		UserName = new JLabel("Pseudo:");
		Password = new JLabel("Mot de passe:");
		txtUserName = new JTextField(20);
		pwdField = new JPasswordField(20);
		validation = new JButton("Valider");
		exit = new JButton("Quitter");
		exit.addActionListener(this);
		validation.addActionListener(this);

	
		register.setBackground(Color.WHITE);

		UserName.setBounds(10, 0, 90, 25);
		txtUserName.setBounds(120, 0, 180, 25);
		Password.setBounds(10, 30, 90, 25);
		pwdField.setBounds(120, 30, 180, 25);
		validation.setBounds(10, 60, 100, 25);
		exit.setBounds(235, 60, 100, 25);

		register.add(UserName);
		register.add(txtUserName);
		register.add(Password);
		register.add(exit);

		register.add(pwdField);
		register.add(validation);

		setResizable(false);
		setSize(350, 120);
		setVisible(true);
		dsSize = toolkit.getScreenSize();
		setLocation(dsSize.width / 2 - this.getWidth() / 2 +20, dsSize.height / 2
				- this.getHeight() / 2 + 20);

	}

	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == validation) {
			String jusername = txtUserName.getText().trim();
			char[] s = pwdField.getPassword();
			String jpassword = new String(s);
			BDD bdd = new BDD("BDD.sqlite");
			if (jusername.equals("") || jpassword.equals("")) {
				JOptionPane.showMessageDialog(this,
						"Entrez un pseudo et un mot de passe s'il vous plaît",
						"Erreur!", JOptionPane.ERROR_MESSAGE);
				txtUserName.setText("");
				pwdField.setText("");
			} 
			
			else if (bdd.compareUsername(jusername) == true) {
				JOptionPane.showMessageDialog(this,
						"Ce pseudo existe déjà, veuillez en choisir un autre",
						"Erreur!", JOptionPane.ERROR_MESSAGE);
				txtUserName.setText("");
				pwdField.setText("");
			} else {
				bdd.setLogin(jusername, jpassword);
				bdd.createLimiteLogin(jusername);
				JOptionPane.showMessageDialog(null, "Inscription terminée");
				this.dispose();

			}
		}

		if (ae.getSource() == exit)
			this.dispose();

	}
}
