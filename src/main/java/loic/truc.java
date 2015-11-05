package loic;

import java.io.IOException;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class truc {
	public static void main(String[] args) throws IOException {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		Map<String, Stock> stocks = YahooFinance.get(symbols); 

		for(Stock s : stocks.values()) {
		    System.out.println(s.getName() + ": " + s.getQuote().getPrice());
		}
	}

}
