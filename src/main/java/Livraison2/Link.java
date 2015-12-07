package Livraison2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Link {
	static String url;

	public Link(String u) {
		url = u;

	}

	public Map<String, String> article() {
		Document doc;
		Map m = new HashMap();
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
						m.put(linkText, linkHref);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;

	}
}
