package LeopardsDesNeiges;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.table.AbstractTableModel;

import org.jfree.chart.ChartPanel;

public class Fenetre extends JFrame implements MouseListener, ActionListener {
	private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private JTable tableau = new JTable();
	private JSplitPane split;

	String link = "http://finance.yahoo.com/q;_ylc=X1MDMjE0MjQ3ODk0OARfcgMyBGZyA3VoM19maW5hbmNlX3dlYgRmcjIDc2EtZ3AEZ3ByaWQDBG5fZ3BzAzEwBG9yaWdpbgNmaW5hbmNlLnlhaG9vLmNvbQRwb3MDMQRwcXN0cgMEcXVlcnkDR09PRywEc2FjAzEEc2FvAzE-?p=http%3A%2F%2Ffinance.yahoo.com%2Fq%3Fs%3DGOOG%26ql%3D0&fr=uh3_finance_web&uhb=uh3_finance_vert&s=";
	String[] links;
	StockData sd = new StockData();
	BDD bdd = new BDD("BDD.sqlite");
	String a = "Limite d'achat: $" + bdd.getLimiteAchat();
	final JTextField tf = new JTextField(a);
	String title[] = { "Symbole", "Nom", "Prix action", "Nbr actions", "Total",
			"Achat/Vente", "Acheter", "Vendre", "Risque" };
	Object[][] data = { { "Total", "", null, null, 0.0, null, null, null, null } };

	private JPanel panneaugauche = new JPanel();
	private JTabbedPane panneaudroite = new JTabbedPane(SwingConstants.TOP);
	private JPanel onglet1 = new JPanel();
	private JPanel onglet4 = new JPanel();
	private ChartPanel onglet3;

	private JButton Quitter = new JButton("Quitter");
	private JButton refreshnews = new JButton("Actualiser News");
	private JPanel jpb = new JPanel();

	JTextField txtLimSup = new JTextField(20);
	JTextField txtLimInf = new JTextField(20);
	JTextField txtAchatAuto = new JTextField(20);
	JTextField txtVenteAuto = new JTextField(20);
	JTextField txtPrixAchatAuto = new JTextField(20);
	JTextField txtPrixVenteAuto = new JTextField(20);


	public Fenetre() throws SQLException {
		this.setTitle("Porte-Feuille avec onglets");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1300, 600);
		ZModel zModel = new ZModel(data, title);

		this.tableau = new JTable(zModel);

		for (int i = 0; i < bdd.getAllSymbole().size(); i++) {

			Object[] data1 = new Object[] {
					bdd.getSymboleI(i),
					sd.getName(bdd.getSymboleI(i)),
					sd.getPrix_action(bdd.getSymboleI(i)),
					bdd.getNbrAction(bdd.getSymboleI(i)),
					sd.getPrix_action(bdd.getSymboleI(i))
							* bdd.getNbrAction(bdd.getSymboleI(i)), "",
					"Acheter", "Vendre", bdd.getRisque(bdd.getSymboleI(i)) };
			((ZModel) tableau.getModel()).addRow(data1);
		}
		;
		for (int x = 0; x < tableau.getRowCount() - 1; x++) {
			Object[] Total1 = new Object[] { "Total", null, null, null, 0,
					null, null, null, null };
			Object total = ((AbstractTableModel) tableau.getModel())
					.getValueAt(x, 0);
			String s = (String) total;
			if (s == "Total") {
				((ZModel) tableau.getModel()).removeRow(x);
				((ZModel) tableau.getModel()).addRow(Total1);
			}
		}
		this.tableau.getColumn("Prix action").setCellRenderer(
				new MonTableRenderer());
		tableau.getColumn("Risque").setCellRenderer(new JCheckBoxRenderer());
		tableau.getColumn("Achat/Vente").setCellRenderer(new AchatVenteRenderer());
		this.tableau.setFillsViewportHeight(true);
		Container container = this.getContentPane();
		this.tableau.getColumn("Acheter").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Acheter").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		this.tableau.getColumn("Vendre").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Vendre").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		JScrollPane js = new JScrollPane(this.tableau);
		tableau.addMouseListener(this);


		onglet1.setBackground(new Color(228, 228, 228));
		onglet1.setLayout(new FlowLayout(0));
		panneaudroite.addTab("News", onglet1);
		panneaudroite.addTab("Historique Mensuel", onglet3);
		panneaudroite.addTab("Gestion", onglet4);
		container.add(panneaudroite, BorderLayout.EAST);
		links = new String[(tableau.getRowCount() - 1)];

		for (int i = 0; i < links.length; i++) {
			links[i] = link + tableau.getValueAt(i, 0);
		}

		panneaugauche.setPreferredSize(new Dimension(700, 200));
		panneaugauche.setLayout(new BorderLayout());
		panneaugauche.add(js);
		container.add(panneaugauche);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panneaugauche,
				panneaudroite);
		container.add(split, BorderLayout.CENTER);
		JButton jbActu = new JButton("Actualiser Prix");
		final JButton ajouter = new JButton("Ajouter une entreprise");
		panneaugauche.add(ajouter, BorderLayout.NORTH);

		refreshnews.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == refreshnews) {
					refreshNews();
				}
			}
		});

		//

		panneaudroite.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				String a = "Limite d'achat: $" + bdd.getLimiteAchat();
				tf.setText(a);
			}
		});

		tf.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				tf.setText("");
			}
		});

		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = tf.getText();
				if (isDouble(s)) {
					Double limi = Double.parseDouble(s);
					if (limi < 0) {
						JOptionPane jop = new JOptionPane();
						jop.showMessageDialog(null,
								"Entrez une limite d'achat positive",
								"Attention", JOptionPane.WARNING_MESSAGE);
						String a = "Limite d'achat: $" + bdd.getLimiteAchat();
						tf.setText(a);
					} else {
						double lim = Double.parseDouble(s);
						bdd.setLimiteAchat(lim);
						String a = "Limite d'achat: $" + bdd.getLimiteAchat();
						tf.setText(a);
					}
				} else {
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null, "Entrez un nombre positif",
							"Attention", JOptionPane.ERROR_MESSAGE);
					String a = "Limite d'achat: $" + bdd.getLimiteAchat();
					tf.setText(a);
				}
			}
		});
		GridLayout g1 = new GridLayout(1, 4);
		g1.setHgap(200);
		jpb.setLayout(g1);
		jpb.add(jbActu);
		jpb.add(refreshnews);
		jpb.add(tf);
		jpb.add(Quitter);
		container.add(jpb, BorderLayout.SOUTH);
		this.setVisible(true);
		JButton retirer = new JButton("Retirer une entreprise");
		panneaugauche.add(retirer, BorderLayout.SOUTH);

		jbActu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh(arg0);
			}
		});

		Quitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				Login log = new Login();

			}
		});

		ajouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFrame fsymbole = new JFrame("Ajoutez votre entreprise");
				JPanel fpanel = new JPanel();
				final JButton valider = new JButton("Ok");
				JButton quitter = new JButton("Quitter");
				JLabel symboles2 = new JLabel("Symbole");
				Container fcontainer = fsymbole.getContentPane();
				fcontainer.add(fpanel);
				fpanel.setLayout(null);
				final JTextField jsymbol = new JTextField(20);


				fpanel.setBackground(Color.white);
				symboles2.setBounds(5, 0, 50, 25);
				jsymbol.setBounds(60, 0, 121, 25);
				valider.setBounds(190, 0, 50, 25);
				quitter.setBounds(250, 0, 80, 25);

				fpanel.add(symboles2);
				fpanel.add(jsymbol);
				fpanel.add(valider);
				fpanel.add(quitter);

				fsymbole.setResizable(false);
				fsymbole.setSize(340, 60);
				fsymbole.setVisible(true);

				valider.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						fsymbole.setCursor(waitCursor);
						StockData sd = new StockData();
						String symbol = jsymbol.getText();
						if (sd.getName(symbol) != null) {
							float p1 = sd.getPrix_action(symbol);
							Object[] donnee = new Object[] { symbol,
									sd.getName(symbol), p1, 0, 0, "",
									"Acheter", "Vendre", 0 };
							bdd.insertBDD(symbol, 0);
							((ZModel) tableau.getModel()).addRow(donnee);
							for (int x = 0; x < tableau.getRowCount() - 1; x++) {
								Object[] Total1 = new Object[] { "Total", null,
										null, null, 0, null, null, null, 0 };
								Object total = ((AbstractTableModel) tableau
										.getModel()).getValueAt(x, 0);
								String s = (String) total;
								if (s == "Total") {
									((ZModel) tableau.getModel()).removeRow(x);
									((ZModel) tableau.getModel())
											.addRow(Total1);
								}
							}
							links = new String[(tableau.getRowCount() - 1)];

							for (int i = 0; i < links.length; i++) {
								links[i] = link + tableau.getValueAt(i, 0);
							}
						} else {
							JOptionPane.showMessageDialog(null,
									"Veuillez rentrer un symbole du CAC40",
									"Erreur!", JOptionPane.ERROR_MESSAGE);
							jsymbol.setText("");
						}

						refreshAjouter(symbol);
						fsymbole.setCursor(defaultCursor);
					}
				});

				quitter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						fsymbole.dispose();
					}
				});

			}
		});
		retirer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = tableau.getSelectedRow();
				if (row == tableau.getRowCount() - 1) {
					JOptionPane.showMessageDialog(null,
							"Cette ligne ne peut pas être retirée", "Attention",
							JOptionPane.WARNING_MESSAGE);

				} else {
					Object nbactionr = ((AbstractTableModel) tableau.getModel())
							.getValueAt(row, 3);
					int nbactioni = (Integer) nbactionr;
					if (nbactioni > 0) {
						JOptionPane jop = new JOptionPane();
						JOptionPane
								.showMessageDialog(
										null,
										"Vous possédez encore des actions de cette entreprise! "
												+ "Veuillez les vendre pour procéder à la suppression",
										"Attention",
										JOptionPane.WARNING_MESSAGE);

					} else {

						Object symbole1 = ((AbstractTableModel) tableau
								.getModel()).getValueAt(row, 0);
						String ssymbole1 = (String) symbole1;
						bdd.removeBDD(ssymbole1);
						((ZModel) tableau.getModel()).removeRow(row);
						links = new String[(tableau.getRowCount() - 1)];

						for (int i = 0; i < links.length; i++) {
							links[i] = link + tableau.getValueAt(i, 0);
						}
					}
				}
			}
		});
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	public void creationOnglet4() {

		final String symbole = (String) ((AbstractTableModel) tableau
				.getModel()).getValueAt(tableau.getSelectedRow(), 0);
		Double limiteSup = bdd.getlimSup(symbole);
		Double limiteInf = bdd.getlimInf(symbole);
		JLabel labLimSup = new JLabel("Limite supérieure => $" + limiteSup);
		JLabel labLimInf = new JLabel("Limite inférieure   => $" + limiteInf);
		JLabel labAchatAuto = new JLabel("Nombre d'action à acheter");
		JLabel labPrixAchatAuto = new JLabel("Prix limite inférieur");
		JLabel titreAchatAuto = new JLabel("Achat Automatique :");
		JLabel titreLimites = new JLabel("Paramètres de visualisation");
		JLabel labVenteAuto = new JLabel("Nombre d'action à vendre");
		JLabel labPrixVenteAuto = new JLabel("Prix limite supérieur");
		JLabel titreVenteAuto = new JLabel("Vente Automatique :");

		JLabel regleAchatAutoNon = new JLabel(
				"Vous n'avez actuellement aucune règle d'achat paramétrée.");
		JLabel regleAchatAutoOui = new JLabel(
						+ bdd.getachatAuto(symbole)
						+ " action(s) à acheter si le prix de l'action est inférieur $"
						+ bdd.getprixAchatAuto(symbole));

		JLabel regleVenteAutoNon = new JLabel(
				"Vous n'avez actuellement aucune règle de vente paramétrée.");
		JLabel regleVenteAutoOui = new JLabel(
						+ bdd.getVenteAuto(symbole)
						+ " action(s) à vendre si le prix de l'action est supérieur à $"
						+ bdd.getprixVenteAuto(symbole));

		JButton annulerRegleAchat = new JButton("Annuler");
		JButton annulerRegleVente = new JButton("Annuler");
		JButton ouiAncienneRègleAchat = new JButton("Oui");
		JButton ouiAncienneRègleVente = new JButton("Oui");

		JLabel ancienneRegles = new JLabel("Anciennes règles");
		JLabel reglememAchatAutoOui = new JLabel(
				"Activer "
						+ bdd.getmemAchatAuto(symbole)
						+ " action(s) à acheter si le prix de l'action est inférieur à $"
						+ bdd.getmemPrixAchatAuto(symbole) + "?");
		JLabel reglememVenteAutoOui = new JLabel(
				"Activer "
						+ bdd.getmemVenteAuto(symbole)
						+ " action(s) à vendre si le prix de l'action est supérieur à $"
						+ bdd.getmemPrixVenteAuto(symbole) + "?");
		JLabel reglememAchatAutoNon = new JLabel(
				"Aucune règle précédente pour l'achat automatique");
		JLabel reglememVenteAutoNon = new JLabel(
				"Aucune règle précédente pour la vente automatique");
		txtLimInf.addActionListener(this);
		txtLimSup.addActionListener(this);
		txtAchatAuto.addActionListener(this);
		txtPrixAchatAuto.addActionListener(this);
		txtVenteAuto.addActionListener(this);
		txtPrixVenteAuto.addActionListener(this);

		annulerRegleAchat.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				bdd.setachatAuto(symbole, 0);
				bdd.setprixAchatAuto(symbole, 10000);
				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();
			}
		});

		annulerRegleVente.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				bdd.setVenteAuto(symbole, 0);
				bdd.setprixVenteAuto(symbole, 0);
				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();
			}
		});

		ouiAncienneRègleAchat.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				int memAchatAuto = bdd.getmemAchatAuto(symbole);
				double memprixAchatAuto = bdd.getmemPrixAchatAuto(symbole);

				bdd.setachatAuto(symbole, memAchatAuto);
				bdd.setprixAchatAuto(symbole, memprixAchatAuto);
				bdd.setmemAchat(symbole, 0, 0);

				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();
			}
		});

		ouiAncienneRègleVente.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				int memVenteAuto = bdd.getmemVenteAuto(symbole);
				double memprixVenteAuto = bdd.getmemPrixVenteAuto(symbole);

				bdd.setVenteAuto(symbole, memVenteAuto);
				bdd.setprixVenteAuto(symbole, memprixVenteAuto);
				bdd.setmemVente(symbole, 10000, 0);

				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();
			}
		});

		titreLimites.setBounds(0, 0, 200, 25);
		titreLimites.setForeground(new Color(204, 32, 32));

		labLimSup.setBounds(0, 30, 200, 20);
		txtLimSup.setBounds(200, 30, 100, 20);

		labLimInf.setBounds(0, 55, 200, 20);
		txtLimInf.setBounds(200, 55, 100, 20);

		titreAchatAuto.setBounds(0, 85, 200, 20);
		titreAchatAuto.setForeground(new Color(204, 32, 32));

		labAchatAuto.setBounds(0, 115, 150, 20);
		txtAchatAuto.setBounds(200, 115, 100, 20);

		labPrixAchatAuto.setBounds(0, 140, 150, 20);
		txtPrixAchatAuto.setBounds(200, 140, 100, 20);

		if (bdd.getachatAuto(symbole) != 0
				&& bdd.getprixAchatAuto(symbole) != 10000) {
			regleAchatAutoOui.setBounds(0, 165, 1000, 20);
			regleAchatAutoOui.setForeground(new Color(1, 10, 116));
			annulerRegleAchat.setBounds(0, 185, 80, 15);
		} else {
			regleAchatAutoNon.setBounds(0, 165, 400, 20);
		}

		titreVenteAuto.setBounds(0, 205, 200, 20);
		titreVenteAuto.setForeground(new Color(204, 32, 32));

		labVenteAuto.setBounds(0, 235, 150, 20);
		txtVenteAuto.setBounds(200, 235, 100, 20);

		labPrixVenteAuto.setBounds(0, 260, 150, 20);
		txtPrixVenteAuto.setBounds(200, 260, 100, 20);

		if (bdd.getVenteAuto(symbole) != 0
				&& bdd.getprixVenteAuto(symbole) != 0) {
			regleVenteAutoOui.setBounds(0, 285, 1000, 20);
			regleVenteAutoOui.setForeground(new Color(1, 10, 116));
			annulerRegleVente.setBounds(0, 305, 80, 15);
		} else {
			regleVenteAutoNon.setBounds(0, 285, 400, 20);
		}

		ancienneRegles.setBounds(0, 335, 100, 20);
		ancienneRegles.setForeground(new Color(204, 32, 32));

		if (bdd.getmemAchatAuto(symbole) != 0) {
			reglememAchatAutoOui.setBounds(0, 365, 450, 20);
			ouiAncienneRègleAchat.setBounds(450, 365, 80, 20);
		} else {
			reglememAchatAutoNon.setBounds(0, 365, 450, 20);
		}

		if (bdd.getmemVenteAuto(symbole) != 0) {
			reglememVenteAutoOui.setBounds(0, 390, 1000, 20);
			ouiAncienneRègleVente.setBounds(450, 390, 80, 20);
		} else {
			reglememVenteAutoNon.setBounds(0, 390, 1000, 20);
		}

		onglet4.add(labLimSup);
		onglet4.add(txtLimSup);
		onglet4.add(labLimInf);
		onglet4.add(txtLimInf);
		onglet4.add(titreLimites);
		onglet4.add(titreAchatAuto);
		onglet4.add(labAchatAuto);
		onglet4.add(txtAchatAuto);
		onglet4.add(labPrixAchatAuto);
		onglet4.add(txtPrixAchatAuto);
		onglet4.add(titreVenteAuto);
		onglet4.add(labVenteAuto);
		onglet4.add(txtVenteAuto);
		onglet4.add(labPrixVenteAuto);
		onglet4.add(txtPrixVenteAuto);
		onglet4.add(regleAchatAutoNon);
		onglet4.add(regleAchatAutoOui);
		onglet4.add(regleVenteAutoNon);
		onglet4.add(regleVenteAutoOui);
		onglet4.add(annulerRegleAchat);
		onglet4.add(annulerRegleVente);
		onglet4.add(ancienneRegles);
		onglet4.add(reglememAchatAutoOui);
		onglet4.add(reglememVenteAutoOui);
		onglet4.add(reglememAchatAutoNon);
		onglet4.add(reglememVenteAutoNon);
		onglet4.add(ouiAncienneRègleAchat);
		onglet4.add(ouiAncienneRègleVente);

		onglet4.setLayout(null);
		onglet4.setBackground(new Color(228, 228, 228));
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public void refresh(ActionEvent arg0) {
		this.setCursor(waitCursor);
		StockData sd = new StockData();
		double t1 = 0;
		String symbols = "";
		for (int x = 0; x < tableau.getRowCount() - 1; x++) {
			String symbol = (String) ((AbstractTableModel) tableau.getModel())
					.getValueAt(x, 0);
			symbols = (String) symbol;
			float p1 = sd.getPrix_action(symbols);
			((AbstractTableModel) tableau.getModel()).setValueAt(p1, x, 2);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(x, 2);
			Object nbaction = ((AbstractTableModel) tableau.getModel())
					.getValueAt(x, 3);
			int nbaction0 = (Integer) nbaction;
			((AbstractTableModel) tableau.getModel()).setValueAt(
					p1 * nbaction0, x, 4);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(x, 4);
			t1 = p1 * nbaction0 + t1;



			if (bdd.getprixAchatAuto((String) symbol) >= p1
					&& bdd.getprixAchatAuto((String) symbol) != 10000) {

				nbaction0 = nbaction0 + bdd.getachatAuto((String) symbol);
				((AbstractTableModel) tableau.getModel()).setValueAt(nbaction0,
						x, 3);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 3);

				((AbstractTableModel) tableau.getModel()).setValueAt(p1
						* nbaction0, x, 4);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 4);
				t1 = t1 + p1 * bdd.getachatAuto((String) symbol);

				int achatAuto = bdd.getachatAuto(symbol);
				double prixAchatAuto = bdd.getprixAchatAuto(symbol);
				int memVenteAuto = bdd.getmemVenteAuto(symbol);
				double memprixVenteAuto = bdd.getmemPrixVenteAuto(symbol);
				int risque = bdd.getRisque(symbol);
				double limSup = bdd.getlimSup(symbol);
				double limInf = bdd.getlimInf(symbol);

				bdd.insertBDD(symbol, nbaction0);

				bdd.setmemAchat(symbol, prixAchatAuto, achatAuto);
				bdd.setmemVente(symbol, memprixVenteAuto, memVenteAuto);
				bdd.setachatAuto(symbol, 0);
				bdd.setprixAchatAuto(symbol, 10000);
				bdd.setRisque(symbol, risque);
				bdd.setlimSup(limSup, symbol);
				bdd.setlimInf(limInf, symbol);

			}

			if (bdd.getprixVenteAuto((String) symbol) <= p1
					&& bdd.getprixVenteAuto((String) symbol) != 0) {

				nbaction0 = nbaction0 - bdd.getVenteAuto((String) symbol);
				((AbstractTableModel) tableau.getModel()).setValueAt(nbaction0,
						x, 3);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 3);

				((AbstractTableModel) tableau.getModel()).setValueAt(p1
						* nbaction0, x, 4);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 4);
				t1 = t1 - p1 * bdd.getVenteAuto((String) symbol);

				int venteAuto = bdd.getVenteAuto(symbol);
				double prixVenteAuto = bdd.getprixVenteAuto(symbol);
				int memAchatAuto = bdd.getmemAchatAuto(symbol);
				double memprixAchatAuto = bdd.getmemPrixAchatAuto(symbol);
				int risque = bdd.getRisque(symbol);
				double limSup = bdd.getlimSup(symbol);
				double limInf = bdd.getlimInf(symbol);

				bdd.insertBDD(symbol, nbaction0);
				bdd.setmemAchat(symbol, memprixAchatAuto, memAchatAuto);
				bdd.setmemVente(symbol, prixVenteAuto, venteAuto);
				bdd.setVenteAuto(symbol, 0);
				bdd.setprixVenteAuto(symbol, 0);
				bdd.setRisque(symbol, risque);
				bdd.setlimSup(limSup, symbol);
				bdd.setlimInf(limInf, symbol);

			}

		}
		((AbstractTableModel) tableau.getModel()).setValueAt(t1,
				tableau.getRowCount() - 1, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
				tableau.getRowCount() - 1, 4);
		
		for (int row = 0; row < tableau.getRowCount() - 1; row++) {
		if (tableau.isRowSelected(row)){
			onglet4.removeAll();
			creationOnglet4();
			onglet4.revalidate();
			onglet4.repaint();
		}
		}

		this.setCursor(defaultCursor);
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public void refreshautomatique() {
		this.setCursor(waitCursor);
		StockData sd = new StockData();
		double t1 = 0;
		String symbols = "";
		for (int x = 0; x < tableau.getRowCount() - 1; x++) {
			String symbol = (String) ((AbstractTableModel) tableau.getModel())
					.getValueAt(x, 0);
			symbols = (String) symbol;
			float p1 = sd.getPrix_action(symbols);
			((AbstractTableModel) tableau.getModel()).setValueAt(p1, x, 2);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(x, 2);
			Object nbaction = ((AbstractTableModel) tableau.getModel())
					.getValueAt(x, 3);
			int nbaction0 = (Integer) nbaction;
			((AbstractTableModel) tableau.getModel()).setValueAt(
					p1 * nbaction0, x, 4);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(x, 4);
			t1 = p1 * nbaction0 + t1;



			if (bdd.getprixAchatAuto((String) symbol) >= p1
					&& bdd.getprixAchatAuto((String) symbol) != 10000) {

				nbaction0 = nbaction0 + bdd.getachatAuto((String) symbol);
				((AbstractTableModel) tableau.getModel()).setValueAt(nbaction0,
						x, 3);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 3);

				((AbstractTableModel) tableau.getModel()).setValueAt(p1
						* nbaction0, x, 4);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 4);
				t1 = t1 + p1 * bdd.getachatAuto((String) symbol);

				int achatAuto = bdd.getachatAuto(symbol);
				double prixAchatAuto = bdd.getprixAchatAuto(symbol);
				int memVenteAuto = bdd.getmemVenteAuto(symbol);
				double memprixVenteAuto = bdd.getmemPrixVenteAuto(symbol);
				int risque = bdd.getRisque(symbol);
				double limSup = bdd.getlimSup(symbol);
				double limInf = bdd.getlimInf(symbol);

				bdd.insertBDD(symbol, nbaction0);

				bdd.setmemAchat(symbol, prixAchatAuto, achatAuto);
				bdd.setmemVente(symbol, memprixVenteAuto, memVenteAuto);
				bdd.setachatAuto(symbol, 0);
				bdd.setprixAchatAuto(symbol, 10000);
				bdd.setRisque(symbol, risque);
				bdd.setlimSup(limSup, symbol);
				bdd.setlimInf(limInf, symbol);

			}

			if (bdd.getprixVenteAuto((String) symbol) <= p1
					&& bdd.getprixVenteAuto((String) symbol) != 0) {

				nbaction0 = nbaction0 - bdd.getVenteAuto((String) symbol);
				((AbstractTableModel) tableau.getModel()).setValueAt(nbaction0,
						x, 3);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 3);

				((AbstractTableModel) tableau.getModel()).setValueAt(p1
						* nbaction0, x, 4);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						x, 4);
				t1 = t1 - p1 * bdd.getVenteAuto((String) symbol);

				int venteAuto = bdd.getVenteAuto(symbol);
				double prixVenteAuto = bdd.getprixVenteAuto(symbol);
				int memAchatAuto = bdd.getmemAchatAuto(symbol);
				double memprixAchatAuto = bdd.getmemPrixAchatAuto(symbol);
				int risque = bdd.getRisque(symbol);
				double limSup = bdd.getlimSup(symbol);
				double limInf = bdd.getlimInf(symbol);

				bdd.insertBDD(symbol, nbaction0);
				bdd.setmemAchat(symbol, memprixAchatAuto, memAchatAuto);
				bdd.setmemVente(symbol, prixVenteAuto, venteAuto);
				bdd.setVenteAuto(symbol, 0);
				bdd.setprixVenteAuto(symbol, 0);
				bdd.setRisque(symbol, risque);
				bdd.setlimSup(limSup, symbol);
				bdd.setlimInf(limInf, symbol);

			}

		}
		((AbstractTableModel) tableau.getModel()).setValueAt(t1,
				tableau.getRowCount() - 1, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
				tableau.getRowCount() - 1, 4);
		
		for (int row = 0; row < tableau.getRowCount() - 1; row++) {
		if (tableau.isRowSelected(row)){
			onglet4.removeAll();
			creationOnglet4();
			onglet4.revalidate();
			onglet4.repaint();
		}
		}
		this.setCursor(defaultCursor);
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	public void refreshNews() {
		this.setCursor(waitCursor);
		bdd.deleteNews();
		for (int i = 0; i < links.length; i++) {
			Link l = new Link(links[i]);
			l.setSymboll((String) ((AbstractTableModel) tableau.getModel())
					.getValueAt(i, 0));
			try {
				l.updateBDDFromYahoo(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.setCursor(defaultCursor);
	}

	// -----------------------------------------------------------------------------------------------------------------------------

	public void refreshAjouter(String symbole) {
		StockData sd = new StockData();
		double t1 = 0;
		Object symbol = ((AbstractTableModel) tableau.getModel()).getValueAt(
				links.length - 1, 0);
		symbole = (String) symbol;
		float p1 = sd.getPrix_action(symbole);
		((AbstractTableModel) tableau.getModel()).setValueAt(p1,
				links.length - 1, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
				links.length - 1, 2);
		Object nbaction = ((AbstractTableModel) tableau.getModel()).getValueAt(
				links.length - 1, 3);
		int nbaction0 = (Integer) nbaction;
		((AbstractTableModel) tableau.getModel()).setValueAt(p1 * nbaction0,
				links.length - 1, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
				links.length, 4);
		t1 = p1 * nbaction0 + t1;

		((AbstractTableModel) tableau.getModel()).setValueAt(t1,
				tableau.getRowCount() - 1, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
				tableau.getRowCount() - 1, 4);

		Link l = new Link(links[links.length - 1]);
		l.setSymboll((String) ((AbstractTableModel) tableau.getModel())
				.getValueAt(links.length - 1, 0));
		try {
			l.updateBDDFromYahoo(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// -----------------------------------------------------------------------------------------------------------------------------
	public void mouseClicked(MouseEvent e) {
		String a = "Limite d'achat: $" + bdd.getLimiteAchat();
		tf.setText(a);
		int columnIndex = tableau.getSelectedColumn();
		int rowIndex = tableau.getSelectedRow();
		String symbole = (String) tableau.getValueAt(rowIndex, 0);
		if (columnIndex == 8) {
			int truc = (Integer) tableau.getValueAt(rowIndex, columnIndex);
			if (truc == 0) {
				tableau.setValueAt(1, rowIndex, columnIndex);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						tableau.getSelectedRow(), 8);
				bdd.setRisque(symbole, 1);
			} else {
				tableau.setValueAt(0, rowIndex, columnIndex);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						tableau.getSelectedRow(), 8);
				bdd.setRisque(symbole, 0);
			}
		} else {

			if (e.getClickCount() == 2
					|| panneaudroite.getSelectedComponent() == onglet3
					|| panneaudroite.getUI().tabForCoordinate(panneaudroite,
							e.getX(), e.getY()) == 0) {
				
				if (tableau.getSelectedRow() == tableau.getRowCount() - 1) {
					String[] truc = new String[tableau.getRowCount() - 1];
					for (int i = 0; i < truc.length; i++) {
						truc[i] = (String) ((AbstractTableModel) tableau
								.getModel()).getValueAt(i, 0);
					}
					LineChartTotal chartMonth = new LineChartTotal("Total",
							"Valeur du portefeuille ", truc);
					onglet3 = chartMonth.chartPanel;
					panneaudroite.setComponentAt(1, onglet3);
					panneaudroite.revalidate();
					panneaudroite.repaint();
					
					panneaudroite.setSelectedComponent(onglet3);
				} else {
					LineChart_AWT chartMonth = new LineChart_AWT("Month",
							"Valeur du total de "
									+ (String) ((AbstractTableModel) tableau
											.getModel()).getValueAt(
											tableau.getSelectedRow(), 1),
							(String) ((AbstractTableModel) tableau.getModel())
									.getValueAt(tableau.getSelectedRow(), 0));
					onglet3 = chartMonth.chartPanel;
					panneaudroite.setComponentAt(1, onglet3);
					panneaudroite.revalidate();
					panneaudroite.repaint();
					
					panneaudroite.setSelectedComponent(onglet3);
				}

			} else {

				for (int x = 0; x < tableau.getRowCount() - 1; x++) {
					((AbstractTableModel) tableau.getModel()).setValueAt(
							"", x, 5);
					((AbstractTableModel) tableau.getModel())
							.fireTableCellUpdated(x, 5);
				}
				((AbstractTableModel) tableau.getModel()).setValueAt("",
						tableau.getSelectedRow(), 5);
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						tableau.getSelectedRow(), 5);

				onglet1.removeAll();
				Map<String, String> map = new HashMap();
				Set<Map.Entry<String, String>> entryset = new HashSet();
				Iterator<Map.Entry<String, String>> it;
				map = bdd.getNews((String) ((AbstractTableModel) tableau
						.getModel()).getValueAt(tableau.getSelectedRow(), 0));
				entryset = map.entrySet();
				it = entryset.iterator();
				int k=0;
				while (it.hasNext()) {
					Entry<String, String> s = it.next();
					MyLabel ml = new MyLabel(s.getKey(), s.getValue());
					final JLabel j = ml.jl;
					
					Font font = new Font("Verdana", Font.PLAIN, 10);
					FontMetrics metrics = new FontMetrics(font) {
					};
					Rectangle2D bounds = metrics.getStringBounds(s.getKey(), null);
					int widthInPixels = (int) bounds.getWidth(); 

					
					j.setBounds(0, k, widthInPixels + 20, 30);
					onglet1.add(j);
					k+=30;
					
					
					
					j.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
						}

						public void mouseExited(MouseEvent arg0) {
							j.setForeground(Color.BLACK);
							String plainText = j.getText().replaceAll("<.*?>",
									"");
				
							String urlText = "<html>" + plainText + "</html>";
							j.setText(urlText);
						}

						public void mouseEntered(MouseEvent arg0) {
							j.setForeground(new Color(14, 3, 146));
							j.setCursor(new Cursor(Cursor.HAND_CURSOR));
							String plainText = j.getText().replaceAll("<.*?>",
									"");
					
							String urlText = "<html><u>" + plainText
									+ "</u></html>";
							j.setText(urlText);
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					onglet1.add(j);
				}
				onglet1.setLayout(null);
				onglet1.revalidate();
				onglet1.repaint();


				panneaudroite.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						TabbedPaneUI ui = panneaudroite.getUI();
						int tab = ui.tabForCoordinate(panneaudroite, e.getX(),
								e.getY());
						if (tab == 1) {
					
							if (tableau.getSelectedRow() == tableau
									.getRowCount() - 1) {
								String[] truc = new String[tableau
										.getRowCount() - 1];
								for (int i = 0; i < truc.length; i++) {
									truc[i] = (String) ((AbstractTableModel) tableau
											.getModel()).getValueAt(i, 0);
								}
								LineChartTotal chartMonth = new LineChartTotal(
										"Total", "Valeur du portefeuille ",
										truc);
								onglet3 = chartMonth.chartPanel;
								panneaudroite.setComponentAt(1, onglet3);
								panneaudroite.revalidate();
								panneaudroite.repaint();
							} else {
								LineChart_AWT chartMonth = new LineChart_AWT(
										"Month",
										"Valeur du total de "
												+ (String) ((AbstractTableModel) tableau
														.getModel()).getValueAt(
														tableau.getSelectedRow(),
														1),
										(String) ((AbstractTableModel) tableau
												.getModel()).getValueAt(
												tableau.getSelectedRow(), 0));
								onglet3 = chartMonth.chartPanel;
								panneaudroite.setComponentAt(1, onglet3);
								panneaudroite.revalidate();
								panneaudroite.repaint();
							}
						}
					}
				});

			}
			onglet4.removeAll();
			creationOnglet4();
			onglet4.revalidate();
			onglet4.repaint();
		}

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

	public void actionPerformed(ActionEvent arg0) {
		int row = tableau.getSelectedRow();
		JFrame frame = new JFrame("Fenêtre d'erreur");

		if (arg0.getSource() == txtLimSup) {
			String limiteSup1 = txtLimSup.getText().trim();
			if (!(limiteSup1.equals(""))){
			if (isDouble(limiteSup1) == false) {
				JOptionPane.showMessageDialog(null,
						"Veuillez entrer un nombre positif", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtLimSup.setText("");

			} else {

			if (Double.parseDouble(txtLimSup.getText()) < 0) {
				JOptionPane.showMessageDialog(frame,
						"Veuillez entrer une limite positive", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtLimSup.setText("");
			}
			if ((Double.parseDouble(txtLimSup.getText()) < bdd
					.getlimInf((String) ((AbstractTableModel) tableau
							.getModel()).getValueAt(row, 0)))) {

				JOptionPane
						.showMessageDialog(
								frame,
								"Veuillez entrer une limite supérieure à la limite inférieure actuelle de $"
										+ bdd.getlimInf((String) ((AbstractTableModel) tableau
												.getModel()).getValueAt(row, 0)),
								"Attention!", JOptionPane.WARNING_MESSAGE);
				txtLimSup.setText("");

			}

			else {
				double s = Double.parseDouble(txtLimSup.getText());
				bdd.setlimSup(s, (String) ((AbstractTableModel) tableau
						.getModel()).getValueAt(row, 0));
				txtLimSup.setText("");

				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						row, 2);
				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();

			}
			}
			}
		}

		if (arg0.getSource() == txtLimInf) {

			String limiteInf = txtLimInf.getText().trim();
			if (!(limiteInf.equals(""))){
			if (isDouble(limiteInf) == false) {
				JOptionPane.showMessageDialog(frame,
						"Veuillez entrer un nombre positive", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtLimInf.setText("");

			} else {
			if (Double.parseDouble(txtLimInf.getText()) < 0) {
				JOptionPane.showMessageDialog(frame,
						"Veuillez entrer une limite positive", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtLimInf.setText("");
			}
			if (Double.parseDouble(txtLimInf.getText()) > bdd
					.getlimSup((String) ((AbstractTableModel) tableau
							.getModel()).getValueAt(row, 0))) {
				JOptionPane
						.showMessageDialog(
								frame,
								"Veuillez entrer une limite inférieure à la limite supérieure actuelle de $"
										+ bdd.getlimSup((String) ((AbstractTableModel) tableau
												.getModel()).getValueAt(row, 0)),
								"Attention!", JOptionPane.WARNING_MESSAGE);
				txtLimInf.setText("");
			} else {
				bdd.setlimInf(Double.parseDouble(txtLimInf.getText()),
						(String) ((AbstractTableModel) tableau.getModel())
								.getValueAt(row, 0));
				txtLimInf.setText("");
				((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
						row, 2);

				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();
			}
			}
			}
		}

		if (arg0.getSource() == txtPrixAchatAuto) {
			String limiteSup1 = txtPrixAchatAuto.getText().trim();
			int rowIndex = tableau.getSelectedRow();
			String symbole = (String) tableau.getValueAt(rowIndex, 0);
			if (!(limiteSup1.equals(""))) {
				if (isDouble(limiteSup1) == false) {
					JOptionPane.showMessageDialog(null,
							"Veuillez entrer un nombre", "Erreur!",
							JOptionPane.ERROR_MESSAGE);
					txtPrixAchatAuto.setText("");
				} else {

				if (Double.parseDouble(txtPrixAchatAuto.getText()) <= 0) {
					JOptionPane.showMessageDialog(frame,
							"Veuillez entrer un nombre supérieur à 0",
							"Erreur!", JOptionPane.ERROR_MESSAGE);
					txtPrixAchatAuto.setText("");
				} else {
					if (Double.parseDouble(txtPrixAchatAuto.getText()) > bdd
							.getprixVenteAuto(symbole)
							&& bdd.getprixVenteAuto(symbole) != 0) {
						JOptionPane
								.showMessageDialog(
										frame,
										"Veuillez entrer un prix inférieur au prix de la règle de vente automatique ",
										"Attention!",
										JOptionPane.WARNING_MESSAGE);
						txtPrixAchatAuto.setText("");

					} else {
						double s = Double.parseDouble(txtPrixAchatAuto
								.getText());
						bdd.setprixAchatAuto(
								(String) ((AbstractTableModel) tableau
										.getModel()).getValueAt(row, 0), s);
						txtPrixAchatAuto.setText("");

						onglet4.removeAll();
						creationOnglet4();
						onglet4.revalidate();
						onglet4.repaint();
					}
				}
				}
			}
		}

		if (arg0.getSource() == txtAchatAuto) {
			String limiteSup1 = txtAchatAuto.getText().trim();
			if (!(limiteSup1.equals(""))){
			if (isInteger(limiteSup1) == false) {
				JOptionPane.showMessageDialog(null,
						"Veuillez entrer un nombre entier", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtAchatAuto.setText("");
			} else {

			if (Integer.parseInt(txtAchatAuto.getText()) <= 0) {
				JOptionPane.showMessageDialog(frame,
						"Veuillez entrer un entier supérieur à 0", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtAchatAuto.setText("");
			} else {
				int s = Integer.parseInt(txtAchatAuto.getText());
				bdd.setachatAuto((String) ((AbstractTableModel) tableau
						.getModel()).getValueAt(row, 0), s);
				txtAchatAuto.setText("");

				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();

			}
			}
			}
		}

		if (arg0.getSource() == txtPrixVenteAuto) {
			String limiteSup1 = txtPrixVenteAuto.getText().trim();
			int rowIndex = tableau.getSelectedRow();
			String symbole = (String) tableau.getValueAt(rowIndex, 0);
			if (!(limiteSup1.equals(""))) {
			if (isDouble(limiteSup1) == false) {
				JOptionPane.showMessageDialog(null,
						"Veuillez entrer un nombre", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtPrixVenteAuto.setText("");
			} else {

			if (Double.parseDouble(txtPrixVenteAuto.getText()) <= 0) {
				JOptionPane.showMessageDialog(frame,
						"Veuillez entrer un nombre supérieur à 0",
						"Erreur!", JOptionPane.ERROR_MESSAGE);
				txtPrixVenteAuto.setText("");
			} else {
				if (Double.parseDouble(txtPrixVenteAuto.getText()) < bdd
						.getprixAchatAuto(symbole)
						&& bdd.getprixAchatAuto(symbole) != 10000) {
					JOptionPane
							.showMessageDialog(
									frame,
									"Veuillez entrer un prix supérieur au prix de la règle d'achat automatique ",
									"Attention!",
									JOptionPane.WARNING_MESSAGE);
					txtPrixVenteAuto.setText("");
				} else {
				double s = Double.parseDouble(txtPrixVenteAuto.getText());
				bdd.setprixVenteAuto((String) ((AbstractTableModel) tableau
						.getModel()).getValueAt(row, 0), s);
				txtPrixVenteAuto.setText("");

				onglet4.removeAll();
				creationOnglet4();
				onglet4.revalidate();
				onglet4.repaint();
				}

			}
			}
		}
		}

		if (arg0.getSource() == txtVenteAuto) {
			String limiteSup1 = txtVenteAuto.getText().trim();
			if (!(limiteSup1.equals(""))){
			if ( isInteger(limiteSup1) == false) {
				JOptionPane.showMessageDialog(null,
						"Veuillez entrer un nombre entier", "Erreur!",
						JOptionPane.ERROR_MESSAGE);
				txtVenteAuto.setText("");
			} else {

				if (Integer.parseInt(txtVenteAuto.getText()) <= 0) {
					JOptionPane.showMessageDialog(frame,
							"Veuillez entrer un entier supérieur à 0",
							"Erreur!", JOptionPane.ERROR_MESSAGE);
					txtVenteAuto.setText("");
				} else {
					int s = Integer.parseInt(txtVenteAuto.getText());
					bdd.setVenteAuto((String) ((AbstractTableModel) tableau
							.getModel()).getValueAt(row, 0), s);
					txtVenteAuto.setText("");

					onglet4.removeAll();
					creationOnglet4();
					onglet4.revalidate();
					onglet4.repaint();
				}
			}
		}
		}

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}