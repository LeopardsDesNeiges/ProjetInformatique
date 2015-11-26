package news;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Link {
	 static String url;
	 static Map m=new HashMap();
	 
    public Link(String u){
    	url=u;
    	
    }
	
	public Map<String,String > article() {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Elements ListDiv = doc.getElementsByAttributeValue("id","yfi_headlines");
            for (Element element : ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkText = link.text().trim();
                    m.put(linkText,linkHref);
                    //System.out.println(linkHref);
                    //System.out.println(linkText);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return m;

    }

}
