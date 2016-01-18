package LeopardsDesNeiges;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JCheckBoxRenderer extends JCheckBox implements TableCellRenderer {


	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int col) {
		 JCheckBox ck = new JCheckBox();
		 Component total = null;

		 if (row < table.getRowCount() - 1 && (Integer)value==0){
		 ck.setSelected(false);
		 ck.setHorizontalAlignment((int) 0.5f);
		 return ck;
		 } 
		 
		 if (row < table.getRowCount() - 1 && (Integer)value==1){
			 ck.setSelected(true);
			 ck.setHorizontalAlignment((int) 0.5f);
			 return ck;
			 } 

		 else {
			 this.setHorizontalAlignment((int) 0.5f);
			 return total;
		 }
	  
   
 }
}
