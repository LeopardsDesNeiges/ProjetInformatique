package LeopardsDesNeiges;




import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer extends JButton implements TableCellRenderer{

	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int col) {

		  Component total = null;
		    if (row < table.getRowCount() - 1){
		    
			    setText((value != null) ? value.toString() : "");
		
			    return this;
		    }else {
		    	return total;
		    }
	  }
}
	
