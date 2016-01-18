package LeopardsDesNeiges;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class LineChartTotal extends ApplicationFrame {
	JFreeChart lineChart;
	ChartPanel chartPanel;
	BDD bdd = new BDD("BDD.sqlite");

	private TimeSeriesCollection historiquetotalmensuel(String name, String[] symboles)
			throws IOException, ParseException {
		TimeSeries series = new TimeSeries(name);
		Float[] valeurs = valeurmensuel(symboles[0]);
		for(int i=1;i<symboles.length;i++){
			Float[] values = valeurmensuel(symboles[i]);
			for(int j=0;j<values.length;j++){
				valeurs[j]=valeurs[j]+values[j];
			}
		}
		double value;
		RegularTimePeriod t = new Day();
		t = t.previous();
		for (int i = 0; i < valeurs.length; i++) {
			if (valeurs[i] == null && i == 0) {
				valeurs[i] = (float) 0;
				value = valeurs[i];
				series.add(t, value);
				t = t.previous();
			} else {
				if (valeurs[i] == null) {
					valeurs[i] = valeurs[i - 1];
				}
				value = valeurs[i];
				series.add(t, value);
				t = t.previous();
			}
		}
		return new TimeSeriesCollection(series);
	}

	public Float[] valeurmensuel(String s) throws IOException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.DAY_OF_YEAR, -32);
		Stock stock = YahooFinance.get(s);
		List<HistoricalQuote> googleHistQuotes = stock.getHistory(from, to,
				Interval.DAILY);
		Object[] historique = googleHistQuotes.toArray();
		Float[] valeurs = new Float[30];
		String[] datesyahoo = new String[30];
		String[] jours = new String[30];
		int k = 1;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

		for (int i = 0; i < jours.length; i++) {
			String jour = format.format(cal.getTime());
			jours[i] = jour;
			cal.add(Calendar.DAY_OF_YEAR, -1);
		}

		for (int i = 0; i < historique.length; i++) {
			String hist = historique[i].toString();
			String d = hist.split("@")[1];
			String dd = d.split(":")[0];
			datesyahoo[i] = dd;
		}
		Calendar calen = Calendar.getInstance();
		String today = format.format(calen.getTime());
		String hist = historique[k].toString();

		String h = hist.split("\\(")[1];
		String prixhistorique = h.split("\\)")[0];
		Float ph = Float.parseFloat(prixhistorique);
		int p = 0;
		long[] BDDateLong = bdd.getDateHistorique(s);
		String[] BDDateString = new String[100];
		Date[] BDDate = new Date[100];
		for (int i = 0; i < BDDateLong.length; i++) {
			BDDate[i] = new Date(BDDateLong[i]);
			String dp = format.format(BDDate[i]);
			String az = dp.split(" ")[0];
			BDDateString[i] = az;
			
		}
		while (comparator.compare(today, BDDateString[p]) == 0) {
			p++;
		}
		if(bdd.getNbrActionHistorique(s, BDDateLong[p])!=null){
		valeurs[0] = ph * bdd.getNbrActionHistorique(s, BDDateLong[p]);
		p++;
		int mem = p - 1;
		int nbactionprecedent=0;
		if(bdd.getNbrActionHistorique(s, BDDateLong[p])!=null){
		 nbactionprecedent = bdd.getNbrActionHistorique(s, BDDateLong[p]);
		}
	
		float premierprix = ph;
		float prixprecedent = (float) 1;
		for (int i = 1; i < valeurs.length; i++) {
		if(comparator.compare(BDDateString[p+1], jours[i]) == 0){
			p++;
		}
			if (comparator.compare(datesyahoo[k], (jours[i])) == 0) {
				if (comparator.compare(BDDateString[p], jours[i]) == 0) {
					hist = historique[k].toString();
					h = hist.split("\\(")[1];
					prixhistorique = h.split("\\)")[0];
					ph = Float.parseFloat(prixhistorique);
					valeurs[i] = ph
							* bdd.getNbrActionHistorique(s, BDDateLong[p]);
					nbactionprecedent = bdd.getNbrActionHistorique(s,
							BDDateLong[p]);
					k++;
				} else {
					while (BDDateLong[p] == BDDateLong[i]
							&& p < BDDateLong.length - 1) {
						p++;
					}
					hist = historique[k].toString();
					h = hist.split("\\(")[1];
					prixhistorique = h.split("\\)")[0];
					Date date = format.parse(datesyahoo[k]);
					long datelong = date.getTime();
					if (valeurs[i - 1] == null
							|| ph == null
							|| datelong < bdd.getPremiereDate(s).getTime()
							|| bdd.getNbrActionHistorique(s, BDDateLong[p]) == null) {
						valeurs[i] = (float) 0;
					} else {
						Float valeurprecedent = valeurs[i - 1] / ph;
						ph = Float.parseFloat(prixhistorique);
						valeurs[i] = ph
								* bdd.getNbrActionHistorique(s, BDDateLong[p]);
					}
					k++;
				
				}
			} else {
				if (nbactionprecedent > 0
						&& bdd.getNbrActionHistorique(s, BDDateLong[p]) != null) {
					valeurs[i] = (valeurs[i - 1] * bdd.getNbrActionHistorique(
							s, BDDateLong[p])) / nbactionprecedent;
					nbactionprecedent = bdd.getNbrActionHistorique(s,
							BDDateLong[p]);
				} else {
					valeurs[i] = valeurs[i - 1];
				}
			}
			if (comparator.compare(BDDateString[mem], (jours[0])) == 0
					&& comparator.compare(BDDateString[mem], (datesyahoo[0])) == 0) {
				premierprix = (float) 1;
			} else {
				if (i == 1) {
					prixprecedent = ph;
				}
			}
		}
		return valeurs;
		}
		else{
			for(int n=0; n<valeurs.length;n++){
				valeurs[n]=(float)0;
			}
		}
		return valeurs;
	}

	public LineChartTotal(String applicationTitle, String chartTitle,
			String[] symboles) {
		super(applicationTitle);
		if (applicationTitle == "Total") {

			try {
				try {
					lineChart = ChartFactory
							.createTimeSeriesChart(
									chartTitle,
									"Jours",
									"Total ($)",
									historiquetotalmensuel(
											"Valeur du total en fonction de la date",
											symboles), true, true, true);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(550, 367));
		setContentPane(chartPanel);

	}
}

