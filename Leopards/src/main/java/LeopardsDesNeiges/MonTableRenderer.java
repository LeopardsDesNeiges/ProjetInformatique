package LeopardsDesNeiges;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MonTableRenderer extends DefaultTableCellRenderer {


	private BDD bdd = new BDD("BDD.sqlite");

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		
		
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
	
		Double limSup =this.bdd.getlimSup((String)table.getValueAt(row, 0));
		Double limInf =this.bdd.getlimInf((String)table.getValueAt(row, 0));
		
		if (row < table.getRowCount() - 1 && (Float) value > limSup ) {
			cell.setBackground(new Color(204,32,32));
			return cell;
		} 
		if (row < table.getRowCount() - 1 && (Float) value < limInf ) {
			cell.setBackground(new Color(57,223,232));
			return cell;
		} 
		else {
			cell.setBackground(Color.WHITE);
			return cell;
		}


	}

}