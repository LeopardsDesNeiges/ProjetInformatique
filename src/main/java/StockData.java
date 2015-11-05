

import java.io.IOException;
import java.math.BigDecimal;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockData {
	
	private String symbol ="Yhoo";
	private String name ="Yahoo";
	private float prix_action;
	private float nbr_action=3;

	
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
	public float getPrix_action()  {
			Stock sd = null;
			try {
				sd = YahooFinance.get("YHOO");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BigDecimal price1 = sd.getQuote().getPrice();
			prix_action=price1.floatValue();
			return price1.floatValue();
	}
	public void setPrix_action(float prix_action) {
		this.prix_action = prix_action;
	}
// --------------------------------------------------------------------------------------	
	public float getNbr_action() {
		return nbr_action;
	}
	public void setNbr_action(float nbr_action) {
		this.nbr_action = nbr_action;
	}
// --------------------------------------------------------------------------------------	
	public float getProduit() {
				return nbr_action*prix_action;
	}
	
	
}



	
	