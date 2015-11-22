package Livraison2;



import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.applet.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class News extends Applet implements ActionListener{
	//private Logger log = LogManager.getLogger(this.getClass().getName());
	JButton jb=new JButton("News");
	String yf="http://finance.yahoo.com/";
	URI uri=null;
	
	public void actionPerformed(ActionEvent e){
		Desktop desktop = Desktop.getDesktop();
		if(e.getSource()==jb){
			try{
				uri=new URI(yf);
				desktop.browse(uri);
				//getAppletContext().showDocument(url);
			} catch(Exception e1){
				e1.printStackTrace();
			}
			//log.debug(e);
			
		}
	}
	
	public News(){
		jb.addActionListener(this);
	}

}

