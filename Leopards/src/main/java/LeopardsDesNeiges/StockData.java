package LeopardsDesNeiges;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockData {


	private double prix_action;
	private int nbr_action = 0;
	private JTable table = null;
	private static Map<String, Stock> stocks = null;
	private static Map<String, String> correspondance = null;


	public StockData() {
		prix_action = 0;
		nbr_action = 0;
	}


	// --------------------------------------------------------------------------------------

	public float getPrix_action(String symbol) {
		String[] symbols = new String[] { "AC.PA", "ACA.PA", "AI.PA", "AIR.PA",
				"ALO.PA", "ALU.PA", "BN.PA", "BNP.PA", "CA.PA", "CAP.PA",
				"CS.PA", "DG.PA", "EDF.PA", "EI.PA", "EN.PA", "FR.PA", "FR.PA",
				"GLE.PA", "GSZ.PA", "KER.PA", "LG.PA", "LR.PA", "MC.PA",
				"ML.PA", "MT.PA", "OR.PA", "ORA.PA", "PUB.PA", "RI.PA",
				"RNO.PA", "SAF.PA", "SAN.PA", "SGO.PA", "SOLB.PA", "SU.PA",
				"TEC.PA", "UG.PA", "UL.PA", "VIE.PA", "VIV.PA" };

		if (stocks == null) {
			try {
				stocks = YahooFinance.get(symbols);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // single request
		}
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

	// --------------------------------------------------------------------------------------

	public void setPrix_action(float prix_action) {
		this.prix_action = prix_action;
	}

	// --------------------------------------------------------------------------------------
	public int getNbr_action(int x) {

		Object nbaction0 = ((AbstractTableModel) table.getModel()).getValueAt(
				x, 3);
		int nba0 = (Integer) nbaction0;
		nbr_action = nba0;
		return nba0;
	}

	public void setNbr_action(int nbr_action) {
		this.nbr_action = nbr_action;
	}

	// --------------------------------------------------------------------------------------
	public double getProduit() {
		return nbr_action * prix_action;
	}


	public void correspNameSymbol() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AC.PA", "Accor SA");
		map.put("ACA.PA", "Credit Agricole SA");
		map.put("AI.PA", "Air Liquide SA");
		map.put("AIR.PA", "AIRBUS GROUP");
		map.put("ALO.PA", "Alstom SA");
		map.put("ALU.PA", "Alcatel-Lucent");
		map.put("BN.PA", "Danone");
		map.put("BNP.PA", "BNP Paris SA");
		map.put("CA.PA", "Carrefour SA");
		map.put("CAP.PA", "Cap Gemini SA");
		map.put("CS.PA", "AXA Group");
		map.put("DG.PA", "VINCI SA");
		map.put("EDF.PA", "Electricite de France SA");
		map.put("EI.PA", "Essilor International SA");
		map.put("EN.PA", "Bouygues SA");
		map.put("FP.PA", "TOTAL SA");
		map.put("FR.PA", "Valeo SA");
		map.put("GLE.PA", "Societe Generale");
		map.put("GSZ.PA", "ENGIE SA");
		map.put("KER.PA", "Kering SA");
		map.put("LG.PA", "Lafarge SA");
		map.put("LR.PA", "Legrand SA");
		map.put("MC.PA", "LVMH Moët Hennessy Louis Vuitton SA");
		map.put("ML.PA", "Compagnie Generale DES Etablissements Michelin SA");
		map.put("MT.PA", "ARCELORMITTAL REG");
		map.put("OR.PA", "L'Oreal SA");
		map.put("ORA.PA", "Orange");
		map.put("PUB.PA", "Publicis Groupe SA");
		map.put("RI.PA", "Pernod-Ricard SA");
		map.put("RNO.PA", "Renault SA");
		map.put("SAF.PA", "Safran SA");
		map.put("SAN.PA", "Sanofi");
		map.put("SGO.PA", "Compagnie de Saint-Gobain SA");
		map.put("SOLB.PA", "SOLVAY");
		map.put("SU.PA", "Schneider Electric SE");
		map.put("TEC.PA", "Technip SA");
		map.put("UG.PA", "Peugeot SA");
		map.put("UL.PA", "Uniball-Rodamco SE");
		map.put("VIE.PA", "Veolia Environnement SA");
		map.put("VIV.PA", "Vivendi SA");
		correspondance = map;
	}

	// ----------------------------------------------------------------------------------------

	public String getName(String symbole) {
		if (correspondance == null) {
			correspNameSymbol();
		}

		return correspondance.get(symbole);
	}

}
