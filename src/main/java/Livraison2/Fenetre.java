package Livraison2;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

public class Fenetre extends JFrame {

	private JTable tableau;
	private JSplitPane split;

	StockData sd = new StockData();

	String title[] = { "Symbole", "Nom", "Prix action", "Nbr actions", "Total", "Transaction", "Acheter", "Vendre" };
	Object[][] data = {
			{ sd.getSymbol(), sd.getName(), sd.getPrix_action("YHOO"), 0, sd.getProduit(), 0, "Acheter", "Vendre" },
			{ "TSLA", "Tesla", sd.getPrix_action("TSLA"), 0, sd.getProduit(), 0, "Acheter", "Vendre" },
			{ "INTC", "INTEL", sd.getPrix_action("INTC"), 0, sd.getProduit(), 0, "Acheter", "Vendre" }, { "AIR.PA",
					"Airbus", sd.getPrix_action("AIR.PA"), 0, sd.getProduit(), 0, "Acheter", "Vendre" },
			{ "Total", "des lignes", sd.getPrix_action("YHOO") + sd.getPrix_action("TSLA") + sd.getPrix_action("INTC")
					+ sd.getPrix_action("AIR.PA"), 0, 0, 0, "", "" } };

	public Fenetre() {

		this.setTitle("Gestionnaire de Porte-feuille augmenté");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		ZModel zModel = new ZModel(data, title);
		this.tableau = new JTable(zModel);
		this.tableau.setFillsViewportHeight(true);
		Container container = this.getContentPane();

		this.tableau.getColumn("Acheter").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Acheter").setCellEditor(new ButtonEditor(new JCheckBox()));
		this.tableau.getColumn("Vendre").setCellRenderer(new ButtonRenderer());
		this.tableau.getColumn("Vendre").setCellEditor(new ButtonEditor(new JCheckBox()));
		// container.add(new JScrollPane(tableau), BorderLayout.CENTER);

		JScrollPane js = new JScrollPane(this.tableau);

		// Panneau de droite
		JPanel jp1 = new JPanel();
		jp1.setBackground(Color.lightGray);

		// Panneau de gauche
		JPanel jp2 = new JPanel();
		jp2.setPreferredSize(new Dimension(700, 0));
		jp2.setLayout(new BorderLayout());
		jp2.add(js);

		JButton jbActu = new JButton("Actualiser");
		container.add(jbActu, BorderLayout.SOUTH);

		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp2, jp1);
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
}
