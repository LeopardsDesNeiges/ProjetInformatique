package Livraison2;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockData {
	
	private String symbol ="Yhoo";
	private String name ="Yahoo";
	private double prix_action;
	private int nbr_action=0;
	private JTable table = null;
	
// Tous les getters et setters. Certains sont peut-être inutiles.
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
// --------------------------------------------------------------------------------------	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
// --------------------------------------------------------------------------------------	
	public float getPrix_action(String symbol) {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		Map<String, Stock> stocks = null;
		try {
			stocks = YahooFinance.get(symbols);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // single request
		Stock sd = stocks.get(symbol);
		BigDecimal price3 = null;
		try {
			price3 = sd.getQuote(true).getPrice();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prix_action = price3.floatValue();
		return price3.floatValue();
	}
	
	public void setPrix_action(float prix_action) {
		this.prix_action = prix_action;
	}
// --------------------------------------------------------------------------------------	
	public int getNbr_action(int x) {
	
		Object nbaction0=((AbstractTableModel)table.getModel()).getValueAt( x, 3);
		int nba0=(Integer) nbaction0;
	nbr_action = nba0;
		return nba0;
	}
	public void setNbr_action(int nbr_action) {
		this.nbr_action = nbr_action;
	}
// --------------------------------------------------------------------------------------	
	public double getProduit() {
				return nbr_action*prix_action;
	}

}
	




	
	