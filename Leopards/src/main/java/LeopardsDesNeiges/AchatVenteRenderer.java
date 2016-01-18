package LeopardsDesNeiges;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class AchatVenteRenderer extends DefaultTableCellRenderer {




	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		
		
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		
		if (row < table.getRowCount() - 1 ) {
			cell.setBackground(new Color(251,221,117));
			return cell;
		} 
		
		else {
			cell.setBackground(Color.WHITE);
			return cell;
		}


	}

}