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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import org.jfree.chart.ChartPanel;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class Fenetre extends JFrame implements MouseListener {

	private JTable tableau = new JTable();
	private JSplitPane split;
	String link = "http://finance.yahoo.com/q;_ylc=X1MDMjE0MjQ3ODk0OARfcgMyBGZyA3VoM19maW5hbmNlX3dlYgRmcjIDc2EtZ3AEZ3ByaWQDBG5fZ3BzAzEwBG9yaWdpbgNmaW5hbmNlLnlhaG9vLmNvbQRwb3MDMQRwcXN0cgMEcXVlcnkDR09PRywEc2FjAzEEc2FvAzE-?p=http%3A%2F%2Ffinance.yahoo.com%2Fq%3Fs%3DGOOG%26ql%3D0&fr=uh3_finance_web&uhb=uh3_finance_vert&s=";
	String[] links;
	StockData sd = new StockData();
	BDD bdd = new BDD("BDD.sqlite");

	String title[] = { "Symbole", "Nom", "Prix action", "Nbr actions", "Total",
			"Transaction", "Acheter", "Vendre" };
	Object[][] data = {
			{ sd.getSymbol(), sd.getName(), sd.getPrix_action("YHOO"),
					bdd.getNbrAction("Yhoo"),
					sd.getPrix_action("YHOO") * bdd.getNbrAction("Yhoo"),
					"Montant?", "Acheter", "Vendre" },
			{ "TSLA", "Tesla", sd.getPrix_action("TSLA"),
					bdd.getNbrAction("TSLA"),
					sd.getPrix_action("TSLA") * bdd.getNbrAction("TSLA"),
					"Montant?", "Acheter", "Vendre" },
			{ "INTC", "INTEL", sd.getPrix_action("INTC"),
					bdd.getNbrAction("INTC"),
					sd.getPrix_action("INTC") * bdd.getNbrAction("INTC"),
					"Montant?", "Acheter", "Vendre" },
			{ "AIR.PA", "Airbus", sd.getPrix_action("AIR.PA"),
					bdd.getNbrAction("AIR.PA"),
					sd.getPrix_action("AIR.PA") * bdd.getNbrAction("AIR.PA"),
					"Montant?", "Acheter", "Vendre" },
			{ "Total", "", null, null, null, null, null, null, null } };
	private JPanel panneaugauche = new JPanel();
	private JTabbedPane panneaudroite = new JTabbedPane(SwingConstants.TOP);
	private JPanel onglet1 = new JPanel();
	private ChartPanel onglet2;
	private ChartPanel onglet3;
	private LineChart_AWT chartDay = new LineChart_AWT("Day",
			"Valeur de l'action Volkswagen","truc");
	//Historique des prix de Yahoo, affiché de base sans cliquer que quoique ce soit
	private LineChart_AWT chartMonth = new LineChart_AWT("Month",
			"Valeur de l'action Volkswagen", "YHOO");

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
		onglet2 = chartDay.chartPanel;
		onglet3 = chartMonth.chartPanel;
		// onglet2.add(chart);
		panneaudroite.addTab("News", onglet1);
		panneaudroite.addTab("Historique Journalier", onglet2);
		panneaudroite.addTab("Historique Mensuel", onglet3);
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
		// TOTAL
		Object nbaction0 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(0, 3);
		Object nbaction1 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(1, 3);
		Object nbaction2 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(2, 3);
		Object nbaction3 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(3, 3);
		// Transforme les valeurs en entiers
		int nba0 = (Integer) nbaction0;
		int nba1 = (Integer) nbaction1;
		int nba2 = (Integer) nbaction2;
		int nba3 = (Integer) nbaction3;
		Float p0 = sd.getPrix_action("YHOO");
		// double prix=Double.parseDouble(p);
		double total0 = p0 * nba0;
		// On caste colonneprix en double
		Float p1 = sd.getPrix_action("TSLA");
		// double prix=Double.parseDouble(p);
		double total1 = p1 * nba1;
		// On caste colonneprix en double
		Float p2 = sd.getPrix_action("INTC");
		// double prix=Double.parseDouble(p);
		double total2 = p2 * nba2;
		// On caste colonneprix en double
		Float p3 = sd.getPrix_action("AIR.PA");
		// double prix=Double.parseDouble(p);
		double total3 = p3 * nba3;
		double totaltotal = total0 + total1 + total2 + total3;
		tableau.getModel().setValueAt(p0, 0, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(0, 2);
		tableau.getModel().setValueAt(p1, 1, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(1, 2);
		tableau.getModel().setValueAt(p2, 2, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(2, 2);
		tableau.getModel().setValueAt(p3, 3, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(3, 2);
		((AbstractTableModel) tableau.getModel()).setValueAt(total0, 0, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(0, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(total1, 1, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(1, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(total2, 2, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(2, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(total3, 3, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(3, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(totaltotal, 4, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(4, 4);
	}

	public void refreshautomatique() {
		StockData sd = new StockData();
		// TOTAL
		Object nbaction0 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(0, 3);
		Object nbaction1 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(1, 3);
		Object nbaction2 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(2, 3);
		Object nbaction3 = ((AbstractTableModel) tableau.getModel())
				.getValueAt(3, 3);
		// Transforme les valeurs en entiers
		int nba0 = (Integer) nbaction0;
		int nba1 = (Integer) nbaction1;
		int nba2 = (Integer) nbaction2;
		int nba3 = (Integer) nbaction3;
		Float p0 = sd.getPrix_action("YHOO");
		// double prix=Double.parseDouble(p);
		double total0 = p0 * nba0;
		// On caste colonneprix en double
		Float p1 = sd.getPrix_action("TSLA");
		// double prix=Double.parseDouble(p);
		double total1 = p1 * nba1;
		// On caste colonneprix en double
		Float p2 = sd.getPrix_action("INTC");
		// double prix=Double.parseDouble(p);
		double total2 = p2 * nba2;
		// On caste colonneprix en double
		Float p3 = sd.getPrix_action("AIR.PA");
		// double prix=Double.parseDouble(p);
		double total3 = p3 * nba3;
		double totaltotal = total0 + total1 + total2 + total3;
		tableau.getModel().setValueAt(p0, 0, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(0, 2);
		tableau.getModel().setValueAt(p1, 1, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(1, 2);
		tableau.getModel().setValueAt(p2, 2, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(2, 2);
		tableau.getModel().setValueAt(p3, 3, 2);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(3, 2);
		((AbstractTableModel) tableau.getModel()).setValueAt(total0, 0, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(0, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(total1, 1, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(1, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(total2, 2, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(2, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(total3, 3, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(3, 4);
		((AbstractTableModel) tableau.getModel()).setValueAt(totaltotal, 4, 4);
		((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(4, 4);
	}

	public static void main(String[] args) throws IOException {

		Date date1 = new Date();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		String aujourdhui = simpleFormat.format(date1);
		final Fenetre f = new Fenetre();
		/*
		 * Timer timer=new Timer(); timer.schedule (new TimerTask(){ public void
		 * run() { f.refreshautomatique(); } },0,120000);
		 */
		
	}

	public void mouseClicked(MouseEvent e) {
		Map<String, String> map = new HashMap();
		Set<Map.Entry<String, String>> entryset = new HashSet();
		Iterator<Map.Entry<String, String>> it;
		if (tableau.isCellSelected(tableau.getSelectedRow(), 5)) {
			onglet1.removeAll();
			//j'enlève l'onglet3, pour le remettre plus tard #pasbien
			panneaudroite.remove(onglet3);
			((AbstractTableModel) tableau.getModel()).setValueAt("Montant?", 0,
					5);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(0, 5);
			((AbstractTableModel) tableau.getModel()).setValueAt("Montant?", 1,
					5);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(1, 5);
			((AbstractTableModel) tableau.getModel()).setValueAt("Montant?", 2,
					5);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(2, 5);
			((AbstractTableModel) tableau.getModel()).setValueAt("Montant?", 3,
					5);
			((AbstractTableModel) tableau.getModel())
					.fireTableCellUpdated(3, 5);
			((AbstractTableModel) tableau.getModel()).setValueAt("",
					tableau.getSelectedRow(), 5);
			((AbstractTableModel) tableau.getModel()).fireTableCellUpdated(
					tableau.getSelectedRow(), 5);
			Link l = new Link(links[tableau.getSelectedRow()]);
			map = l.article();
			entryset = map.entrySet();
			it = entryset.iterator();
			//recreer un historique mensuel avec le symbole de la ligne cliquée
			chartMonth = new LineChart_AWT("Month",
			"Valeur de l'action Volkswagen",(String) ((AbstractTableModel) tableau.getModel()).getValueAt( tableau.getSelectedRow(),
					0));
			// je recrée onglet 3 ici #pasbien
			onglet3=chartMonth.chartPanel;
			while (it.hasNext()) {
				Entry<String, String> s = it.next();

				MyLabel ml = new MyLabel(s.getKey(), s.getValue());
				JLabel j = ml.jl;
				onglet1.add(j);
			}
			// je rajoute onglet 3 au panneau de droite #pasbien
			panneaudroite.addTab("Historique Mensuel",onglet3);
			onglet1.revalidate();
			onglet1.repaint();
			panneaudroite.revalidate();
			panneaudroite.repaint();
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
