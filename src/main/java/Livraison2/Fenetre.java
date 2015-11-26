package Livraison2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

public class Fenetre extends JFrame implements MouseListener {

	private JTable tableau=new JTable();
	private JSplitPane split;
	String link = "http://finance.yahoo.com/q;_ylc=X1MDMjE0MjQ3ODk0OARfcgMyBGZyA3VoM19maW5hbmNlX3dlYgRmcjIDc2EtZ3AEZ3ByaWQDBG5fZ3BzAzEwBG9yaWdpbgNmaW5hbmNlLnlhaG9vLmNvbQRwb3MDMQRwcXN0cgMEcXVlcnkDR09PRywEc2FjAzEEc2FvAzE-?p=http%3A%2F%2Ffinance.yahoo.com%2Fq%3Fs%3DGOOG%26ql%3D0&fr=uh3_finance_web&uhb=uh3_finance_vert&s=";
	String[] links;
	StockData sd = new StockData();

	String title[] = { "Symbole", "Nom", "Prix action", "Nbr actions", "Total",
			"Transaction", "Acheter", "Vendre" };
	Object[][] data = {
			{ sd.getSymbol(), sd.getName(), sd.getPrix_action("YHOO"), 0,
					sd.getProduit(), 0, "Acheter", "Vendre"},
			{ "TSLA", "Tesla", sd.getPrix_action("TSLA"), 0, sd.getProduit(),
					0, "Acheter", "Vendre"},
			{ "INTC", "INTEL", sd.getPrix_action("INTC"), 0, sd.getProduit(),
					0, "Acheter", "Vendre"},
			{ "AIR.PA", "Airbus", sd.getPrix_action("AIR.PA"), 0,
					sd.getProduit(), 0, "Acheter", "Vendre"},
			{ "Total", "", null, 0, 0, null, null, null, null } };
	private JPanel panneaugauche = new JPanel();
	private JTabbedPane panneaudroite = new JTabbedPane(SwingConstants.TOP);
	private JPanel onglet1 = new JPanel();
	private JPanel onglet2 = new JPanel();
	private JLabel truc = new JLabel("Faut mettre l'historique ici");

	public Fenetre() {
		this.setTitle("Porte-Feuille avec onglets");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1300, 600);
		ZModel zModel = new ZModel(data, title);
		this.tableau = new JTable(zModel);
		this.tableau.setFillsViewportHeight(true);
		Container container = this.getContentPane();
		this.tableau.getColumn("Acheter").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Acheter").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		this.tableau.getColumn("Vendre").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Vendre").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		// container.add(new JScrollPane(tableau), BorderLayout.CENTER);
		JScrollPane js = new JScrollPane(this.tableau);
		tableau.addMouseListener(this);

		// Panneau de droite
		onglet1.setBackground(Color.lightGray);
		onglet1.setLayout(new FlowLayout(0));
		onglet2.add(truc);
		panneaudroite.addTab("News", onglet1);
		panneaudroite.addTab("Historique", onglet2);
		container.add(panneaudroite, BorderLayout.EAST);
		links = new String[(tableau.getRowCount() - 1)];

		for (int i = 0; i < links.length; i++) {
			links[i] = link + tableau.getValueAt(i, 0);
		}
		// Panneau de gauche

		panneaugauche.setPreferredSize(new Dimension(700, 200));
		panneaugauche.setLayout(new BorderLayout());
		panneaugauche.add(js);
		container.add(panneaugauche);
		JButton jbActu = new JButton("Actualiser");
		container.add(jbActu, BorderLayout.SOUTH);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panneaugauche,
				panneaudroite);
		// Afin de rendre le split non mouvant
		/*
		 * { private final int location = 700; { setDividerLocation( location );
		 * }
		 * 
		 * @Override public int getDividerLocation() { return location ; }
		 * 
		 * @Override public int getLastDividerLocation() { return location ; }
		 * };
		 */
		container.add(split, BorderLayout.CENTER);

		this.setVisible(true);
		jbActu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh(arg0);
			}
		});

	}

	// Actualisation de la page
	public void refresh(ActionEvent arg0) {
		StockData sd = new StockData();
		tableau.getModel().setValueAt(sd.getPrix_action("YHOO"), 0, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(0, 2);
		tableau.getModel().setValueAt(sd.getPrix_action("TSLA"), 1, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(1, 2);
		tableau.getModel().setValueAt(sd.getPrix_action("INTC"), 2, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(2, 2);
		tableau.getModel().setValueAt(sd.getPrix_action("AIR.PA"), 3, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(3, 2);
	}

	public static void main(String[] args) {
	new Fenetre();
	}

	public void mouseClicked(MouseEvent e) {
		Map<String, String> map = new HashMap();
		Set<Map.Entry<String, String>> entryset = new HashSet();
		Iterator<Map.Entry<String, String>> it;
		if (tableau.isCellSelected(tableau.getSelectedRow(), 8)) {
			onglet1.removeAll();
			Link l = new Link(links[tableau.getSelectedRow()]);
			map = l.article();
			entryset = map.entrySet();
			it = entryset.iterator();
			while (it.hasNext()) {
				Entry<String, String> s = it.next();

				MyLabel ml = new MyLabel(s.getKey(), s.getValue());
				JLabel j = ml.jl;
				onglet1.add(j);
			}
			onglet1.revalidate();
			onglet1.repaint();
		}

	}

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

}
