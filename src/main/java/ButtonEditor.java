
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ButtonEditor extends DefaultCellEditor {

	protected JButton button;
	private boolean isPushed;
	private ButtonListener bListener = new ButtonListener();

	public ButtonEditor(JCheckBox checkBox) {
		super(checkBox);
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(bListener);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// On affecte le numero de ligne au listener
		bListener.setRow(row);
		// Idem pour le numero de colonne
		bListener.setColumn(column);
		// On passe aussi le tableau en parametre pour des actions potentielles
		bListener.setTable(table);
		// On reaffecte le libelle au bouton
		button.setText((value == null) ? "" : value.toString());
		// On renvoie le bouton
		return button;
	}

	// static //Notre listener pour le bouton
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
			// 6eme colonne c'est Acheter
			if (this.column == 6) {

				// Si rien n'est Ã©crit dans Transaction alors renvoit 0.
				// this.column-1= Transaction
				if (((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 1)) == null) {
					((AbstractTableModel) table.getModel()).setValueAt(0, this.row, (this.column - 1));
				}

				else {
				}
				;

				// Prend la valeur dans Transaction
				Object k = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 1));
				// Prend la valeur dans Nombre d'actions
				Object kbis = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 3));

				// On veut que k devienne un entier.

				int transaction = (Integer) k;
				int nombreactions = (Integer) kbis;

				if (transaction >= 0) {
					// Change la valeur de Nombre d'actions
					((AbstractTableModel) table.getModel()).setValueAt(transaction + nombreactions, this.row,
							(this.column - 3));
					// Permet de mettre ï¿½ jour directement la valeur, pas
					// besoin de cliquer sur un autre bouton pour que ï¿½a soit
					// le cas.
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 3));
					this.button = ((JButton) event.getSource());
				} else {
					System.out.println("Rentrez un entier positif");
					transaction = 0;
				}

				Object colonneprix = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 4));
				// On caste colonneprix en double
				Float p = (Float) colonneprix;
				// double prix=Double.parseDouble(p);
				double total = p * (transaction + nombreactions);
				// Chnage la valeur de Total
				((AbstractTableModel) table.getModel()).setValueAt(total, this.row, (this.column - 2));
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 2));

				// Remet Ã  0 la colonne Transaction
				((AbstractTableModel) table.getModel()).setValueAt(0, this.row, (this.column - 1));
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 1));
				// Récupère les valeurs de la colonne Total
				Object total0 = ((AbstractTableModel) table.getModel()).getValueAt(0, 4);
				Object total1 = ((AbstractTableModel) table.getModel()).getValueAt(1, 4);
				Object total2 = ((AbstractTableModel) table.getModel()).getValueAt(2, 4);
				Object total3 = ((AbstractTableModel) table.getModel()).getValueAt(3, 4);
				// Transforme les valeurs en double
				double t0 = (Double) total0;
				double t1 = (Double) total1;
				double t2 = (Double) total2;
				double t3 = (Double) total3;
				// Calcul la somme des totaux
				double sommet = t0 + t1 + t2 + t3;
				// Affiche la somme
				((AbstractTableModel) table.getModel()).setValueAt(sommet, 4, 4);
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(4, 4);
				// Récupère les valeurs de la colonne Nombre d'Actions
				Object nbaction0 = ((AbstractTableModel) table.getModel()).getValueAt(0, 3);
				Object nbaction1 = ((AbstractTableModel) table.getModel()).getValueAt(1, 3);
				Object nbaction2 = ((AbstractTableModel) table.getModel()).getValueAt(2, 3);
				Object nbaction3 = ((AbstractTableModel) table.getModel()).getValueAt(3, 3);
				// Transforme les valeurs en double
				int nba0 = (Integer) nbaction0;
				int nba1 = (Integer) nbaction1;
				int nba2 = (Integer) nbaction2;
				int nba3 = (Integer) nbaction3;
				// Calcul la somme des actions
				int sommea = nba0 + nba1 + nba2 + nba3;
				// Affiche la somme
				((AbstractTableModel) table.getModel()).setValueAt(sommea, 4, 3);
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(4, 3);

				this.button = ((JButton) event.getSource());
			}

			// dÃ©but de l'action Vendre
			else {
				// Prend la valeur de Transaction
				Object k = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 2));
				if (k == null) {
					k = 0;
				}
				// Prend la valeur de Nombre d'actions
				Object kbis = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 4));

				// On veut que k devienne un entier.

				int transaction = (Integer) k;
				int nombreactions = (Integer) kbis;

				if (transaction < 0) {

					System.out.println("Rentrez un entier positif");
					transaction = 0;

				} else if (nombreactions - transaction >= 0) {
					// Change la valeur de Nombre d'actions
					((AbstractTableModel) table.getModel()).setValueAt(nombreactions - transaction, this.row,
							(this.column - 4));
					// Permet de mettre ï¿½ jour directement la valeur, pas
					// besoin de cliquer sur un autre bouton pour que ï¿½a soit
					// le cas.
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column - 4);
					this.button = ((JButton) event.getSource());

					// si Transaction trop Ã©levÃ©e remet la colonne Nombre
					// d'action Ã  0
				} else {
					((AbstractTableModel) table.getModel()).setValueAt(0, this.row, (this.column - 4));
					((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column - 4);
				}

				// Remet ï¿½ 0 Transaction 0 aprï¿½s action du bouton
				((AbstractTableModel) table.getModel()).setValueAt(0, this.row, (this.column - 2));
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, this.column - 2);

				Object colonneprix = ((AbstractTableModel) table.getModel()).getValueAt(this.row, (this.column - 5));
				// On caste colonneprix en double
				Float p = (Float) colonneprix;
				// double prix=Double.parseDouble(p);
				double produit = p * (nombreactions - transaction);
				if (produit < 0) {
					produit = 0;
				}
				((AbstractTableModel) table.getModel()).setValueAt(produit, this.row, (this.column - 3));
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(this.row, (this.column - 3));
				// Récupère les valeurs de la colonne Total
				Object total0 = ((AbstractTableModel) table.getModel()).getValueAt(0, 4);
				Object total1 = ((AbstractTableModel) table.getModel()).getValueAt(1, 4);
				Object total2 = ((AbstractTableModel) table.getModel()).getValueAt(2, 4);
				Object total3 = ((AbstractTableModel) table.getModel()).getValueAt(3, 4);
				// Transforme les valeurs en double
				double t0 = (Double) total0;
				double t1 = (Double) total1;
				double t2 = (Double) total2;
				double t3 = (Double) total3;
				// Calcul la somme des totaux
				double sommet = t0 + t1 + t2 + t3;
				// Affiche la somme
				((AbstractTableModel) table.getModel()).setValueAt(sommet, 4, 4);
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(4, 4);
				// Récupère les valeurs de la colonne Nombre d'Actions
				Object nbaction0 = ((AbstractTableModel) table.getModel()).getValueAt(0, 3);
				Object nbaction1 = ((AbstractTableModel) table.getModel()).getValueAt(1, 3);
				Object nbaction2 = ((AbstractTableModel) table.getModel()).getValueAt(2, 3);
				Object nbaction3 = ((AbstractTableModel) table.getModel()).getValueAt(3, 3);
				// Transforme les valeurs en double
				int nba0 = (Integer) nbaction0;
				int nba1 = (Integer) nbaction1;
				int nba2 = (Integer) nbaction2;
				int nba3 = (Integer) nbaction3;
				// Calcul la somme des actions
				int sommea = nba0 + nba1 + nba2 + nba3;
				// Affiche la somme
				((AbstractTableModel) table.getModel()).setValueAt(sommea, 4, 3);
				((AbstractTableModel) table.getModel()).fireTableCellUpdated(4, 3);
				this.button = ((JButton) event.getSource());

			}
		}

	}
}