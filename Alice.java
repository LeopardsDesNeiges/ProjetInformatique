import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Container;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;

public class Alice /*implements ActionListener*/{
	
	String[] ColumnNames={"Code","Date","Ouvert","Haut","Bas","Ferme","Volume","Adj"};
	DataFromYahoo StockUtil = new DataFromYahoo();
	String date = String.format("%1$tF", new Date());
    StockData sd = StockUtil.getStockData("600629.ss", date);
    Object[][] data={
    		{
    			sd.getCode(),sd.getDate(),sd.getOpen(),sd.getHigh(),
    			sd.getLow(),sd.getClose(),sd.getVolume(),sd.getAdj()
    		}	
    };
    JTable tab=new JTable(data,ColumnNames);
    JScrollPane js=new JScrollPane(tab);
    JFrame fen=new JFrame("Alice");
    Container tmp=fen.getContentPane();
    JPanel jp1=new JPanel();
    JPanel jp2=new JPanel();
    JPanel jp3=new JPanel();
    
    JButton jb1=new JButton("+");
    JButton jb2=new JButton("-");
    JButton jb3=new JButton("Acheter");
    JButton jb4=new JButton("Vendre");
    JButton jb5=new JButton();
    News n=new News();
    
    public Alice(){
    	tab.setFillsViewportHeight(true);
    	jb5=n.jb;
    	fen.setSize(480 , 600);
    	tmp.setLayout(null);
    	jb1.setBounds(0, 0, 45, 40);
    	jb2.setBounds(42, 0, 45, 40);
    	jb3.setBounds(200, 30, 100, 50);
    	jb4.setBounds(310, 30, 100, 50);
    	jb5.setBounds(0, 30, 80, 80);
    	
    	jp1.setBounds(0, 0, 470, 50);
    	jp2.setBounds(0, 50, 470, 400);
    	jp3.setBounds(0, 450, 470, 110);
    	jp1.setLayout(null);
    	jp3.setLayout(null);
    	jp2.add(js);
    	jp1.add(jb1);
    	jp1.add(jb2);
    	jp3.add(jb5);
    	jp3.add(jb3);
    	jp3.add(jb4);
    	tmp.add(jp1);
    	tmp.add(jp2);
    	tmp.add(jp3);
    	
    	fen.setVisible(true);
    }
    public static void main(String[] args){
    	new Alice();
    }

}
