package LeopardsDesNeiges;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JLabel;

public class MyLabel implements MouseListener{
	String titre;
	String link;
	JLabel jl=new JLabel();
	
	public MyLabel(String t,String l){
		titre=t;
		link=l;
		String Newligne=System.getProperty("line.separator"); 
		jl.setText(titre+Newligne);
		jl.addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getSource()==jl){
			try {
				Desktop.getDesktop().browse(new java.net.URI(link));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {

		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}