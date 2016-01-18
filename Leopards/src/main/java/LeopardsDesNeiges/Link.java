package LeopardsDesNeiges;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Link {
	static String url;
	private static String symbole = null;

	public Link(String u) {
		url = u;

	}

	public void updateBDDFromYahoo(int linksLength) throws SQLException {
		Document doc;
		BDD bdd = new BDD("BDD.sqlite");
	
		
		for (int i = 0; i <linksLength ; i++) {
			
			try {
				doc = Jsoup.connect(url).get();
				Elements ListDiv = doc.getElementsByAttributeValue("id",
						"yfi_headlines");
				for (Element element : ListDiv) {
					Elements ListDiv2 = element.getElementsByAttributeValue(
							"class", "bd");
					for (Element element2 : ListDiv2) {
						Elements links = element2.getElementsByTag("a");
						for (Element link : links) {
							String linkHref = link.attr("href");
							String linkText = link.text().trim();
							bdd.insertNews(this.symbole, linkHref, linkText);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	
	}

	public Map<String, String> articleFromBDD() throws SQLException {
		BDD bdd = new BDD("BDD.sqlite");
		return bdd.getNews(this.symbole);
	}

	public void setSymboll(String s) {
		this.symbole = s;
	}
}
