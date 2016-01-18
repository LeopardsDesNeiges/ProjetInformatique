package LeopardsDesNeiges;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ButtonEditor extends DefaultCellEditor {

	protected JButton button;
	private ButtonListener bListener = new ButtonListener();
	BDD bdd = new BDD("BDD.sqlite");
	public ButtonEditor(JCheckBox checkBox) {
		super(checkBox);
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(bListener);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		bListener.setRow(row);
		bListener.setColumn(column);
		bListener.setTable(table);
		button.setText((value == null) ? "" : value.toString());
		return button;
	}

	class ButtonListener implements ActionListener {

		private int column, row;
		private JTable table;
		private JButton button;


		public void setColumn(int col) {
			this.column = col;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public void setTable(JTable table) {
			this.table = table;
		}

		public void actionPerformed(ActionEvent event) {

			if (this.column == 6) {

				if (((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 1)) == null
						|| ((AbstractTableModel) table.getModel()).getValueAt(this.row,
								(this.column - 1)) instanceof String) {

					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null, "Entrez un entier positif", "Attention", JOptionPane.WARNING_MESSAGE);
					((AbstractTableModel) table.getModel()).setValueAt("", this.row, (this.column - 1));
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column-1);
					
				}
				;
				Object colonneprix = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 4));

				Float p = (Float) colonneprix;
				
				Object k = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 1));
				
				Object kbis = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 3));

				

				int transaction = (Integer) k;
				int nombreactions = (Integer) kbis;
				String symbole = (String) ((AbstractTableModel) table.getModel()).getValueAt(this.row, 0);
				Float prixAction = (Float) ((AbstractTableModel)table.getModel()).getValueAt(this.row, 2);
				
				if (transaction >= 0) {
					if(transaction*p>bdd.getLimiteAchat())
					{
						JOptionPane jop = new JOptionPane();
						jop.showMessageDialog(null, "Vous dépassez la limite d'achat", "Attention", JOptionPane.WARNING_MESSAGE);
						((AbstractTableModel) table.getModel()).setValueAt("", this.row, (this.column-1));
						((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column-1);
					}
					else{
				
					((AbstractTableModel) table.getModel()).setValueAt(transaction + nombreactions, this.row,
							(this.column - 3));
				
					
					int venteAuto = bdd.getVenteAuto(symbole);
					double prixVenteAuto = bdd.getprixVenteAuto(symbole);
					int memAchatAuto = bdd.getmemAchatAuto(symbole);
					double memprixAchatAuto= bdd.getmemPrixAchatAuto(symbole);
					int achatAuto = bdd.getachatAuto(symbole);
					double prixAchatAuto = bdd.getprixAchatAuto(symbole);				
					int memVenteAuto = bdd.getmemVenteAuto(symbole);
					double memprixVenteAuto= bdd.getmemPrixVenteAuto(symbole);
					int risque = bdd.getRisque(symbole);
					double limSup = bdd.getlimSup(symbole);
					double limInf = bdd.getlimInf(symbole);
					
					bdd.insertBDD(symbole, transaction+ nombreactions);
					
					bdd.setmemAchat(symbole, prixAchatAuto, achatAuto);
					bdd.setmemVente(symbole, memprixVenteAuto, memVenteAuto);
					bdd.setmemAchat(symbole, memprixAchatAuto, memAchatAuto);
					bdd.setmemVente(symbole, prixVenteAuto, venteAuto);
					bdd.setachatAuto(symbole, achatAuto);
					bdd.setprixAchatAuto(symbole, prixAchatAuto);
					bdd.setVenteAuto(symbole, venteAuto);
					bdd.setprixVenteAuto(symbole, prixVenteAuto);
					bdd.setRisque(symbole, risque);
					bdd.setlimSup(limSup, symbole);
					bdd.setlimInf(limInf, symbole);

					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 3));
					this.button = ((JButton) event.getSource());
					
					double total = p * (transaction + nombreactions);
				
					((AbstractTableModel) table.getModel()).setValueAt(total, this.row, (this.column - 2));
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 2));

				
					((AbstractTableModel) table.getModel()).setValueAt("", this.row, (this.column - 1));
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 1));
					
					double t1=0;
					
					for (int x=0;x<table.getRowCount()-1;x++) {
					Object prix=((AbstractTableModel)table.getModel()).getValueAt( x, 2);
					Object nbractions=((AbstractTableModel)table.getModel()).getValueAt( x, 3);
				    Float p1 = (Float) prix;
				    int nbactions = (Integer) nbractions;
				    t1 = p1 * nbactions + t1;
					}
					((AbstractTableModel)table.getModel()).setValueAt(t1, table.getRowCount() -1 , 4);
					((AbstractTableModel)table.getModel()).fireTableCellUpdated(table.getRowCount() -1, 4);
					this.button = ((JButton) event.getSource());
					}
				} else {
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null, "Entrez un entier positif", "Attention", JOptionPane.WARNING_MESSAGE);
				}

			
				
			}

		
			else {
				if (((AbstractTableModel) table.getModel()).getValueAt(this.row, (5)) == null
						|| ((AbstractTableModel) table.getModel()).getValueAt(this.row, (5)) instanceof String) {
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null, "Entrez un entier positif", "Attention", JOptionPane.WARNING_MESSAGE);
					((AbstractTableModel) table.getModel()).setValueAt(0, this.row, (5));
				}
				
				Object k = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 2));
				if (k == null) {
					k = 0;
				}
			
				Object kbis = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 4));

		

				int transaction = (Integer) k;
				int nombreactions = (Integer) kbis;
				String symbole = (String) ((AbstractTableModel) table.getModel()).getValueAt(this.row, 0);
				Float prixAction = (Float) ((AbstractTableModel)table.getModel()).getValueAt(this.row, 2);

				if (transaction < 0) {
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null, "Entrez un entier positif", "Attention", JOptionPane.WARNING_MESSAGE);
					transaction = 0;

				}
				
				
				if (nombreactions==0){
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null, "Vous ne possédez pas d'action", "Attention", JOptionPane.WARNING_MESSAGE);
				}
				
				
				if (nombreactions - transaction >= 0) {
				
					((AbstractTableModel) table.getModel()).setValueAt(nombreactions - transaction, this.row,
							(this.column - 4));
		
					
					int venteAuto = bdd.getVenteAuto(symbole);
					double prixVenteAuto = bdd.getprixVenteAuto(symbole);
					int memAchatAuto = bdd.getmemAchatAuto(symbole);
					double memprixAchatAuto= bdd.getmemPrixAchatAuto(symbole);
					int achatAuto = bdd.getachatAuto(symbole);
					double prixAchatAuto = bdd.getprixAchatAuto(symbole);				
					int memVenteAuto = bdd.getmemVenteAuto(symbole);
					double memprixVenteAuto= bdd.getmemPrixVenteAuto(symbole);
					int risque = bdd.getRisque(symbole);
					double limSup = bdd.getlimSup(symbole);
					double limInf = bdd.getlimInf(symbole);
					
					bdd.insertBDD(symbole,nombreactions-transaction);
					
					bdd.setmemAchat(symbole, prixAchatAuto, achatAuto);
					bdd.setmemVente(symbole, memprixVenteAuto, memVenteAuto);
					bdd.setmemAchat(symbole, memprixAchatAuto, memAchatAuto);
					bdd.setmemVente(symbole, prixVenteAuto, venteAuto);
					bdd.setachatAuto(symbole, achatAuto);
					bdd.setprixAchatAuto(symbole, prixAchatAuto);
					bdd.setVenteAuto(symbole, venteAuto);
					bdd.setprixVenteAuto(symbole, prixVenteAuto);
					bdd.setRisque(symbole, risque);
					bdd.setlimSup(limSup, symbole);
					bdd.setlimInf(limInf, symbole);
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column - 4);
					this.button = ((JButton) event.getSource());

				} else {
					bdd.insertBDD((String) ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 7)),0);
					((AbstractTableModel) table.getModel()).setValueAt(0, this.row, (this.column - 4));
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column - 4);
				}

			
				((AbstractTableModel) table.getModel()).setValueAt("", this.row, (this.column - 2));
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column - 2);

				Object colonneprix = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 5));
	
				Float p = (Float) colonneprix;
				double produit = p * (nombreactions - transaction);
				if (produit < 0) {
					produit = 0;
				}
				((AbstractTableModel) table.getModel()).setValueAt(produit, this.row, (this.column - 3));
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 3));
			
				
				double t1=0;
				
				for (int x=0;x<table.getRowCount()-1;x++) {
				Object prix=((AbstractTableModel)table.getModel()).getValueAt( x, 2);
				Object nbractions=((AbstractTableModel)table.getModel()).getValueAt( x, 3);
			    Float p1 = (Float) prix;
			    int nbactions = (Integer) nbractions;
			    t1 = p1 * nbactions + t1;
				}
				((AbstractTableModel)table.getModel()).setValueAt(t1, table.getRowCount() -1, 4);
				((AbstractTableModel)table.getModel()).fireTableCellUpdated(table.getRowCount() - 1, 4);
				this.button = ((JButton) event.getSource());

			}
		}

	}
}
