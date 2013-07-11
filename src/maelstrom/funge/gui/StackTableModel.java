/**
 *
 */
package maelstrom.funge.gui;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;

import maelstrom.funge.event.*;


/**
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class StackTableModel extends AbstractTableModel implements StackChangeListener {

	private Vector<Long> stack;
	private AbstractTableModel stackDataModel = null;

	public StackTableModel() {
		super();
		this.stack = new Vector<Long>();
	}

	public int getColumnCount() { return 3; }
	public int getRowCount() {
		return this.stack.size();
	}

	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return "#";
			case 1:
				return "Value";
			case 2:
				return "ASCII";
		}
		return "";
	}

	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0:
				return Integer.class;
			case 1:
				return Long.class;
			case 2:
				return String.class;
		}
		return Object.class;
	}

	public Object getValueAt(int row, int col) {
		try {
			switch (col) {
				case 0:
					return new Integer(this.stack.size() - row);
				case 1:
					return this.stack.get(row);
				case 2:
					long data = this.stack.get(row);
					if (data > 32 && data < 128) {
						return (char) data;
					} else {
						return "";
					}
				default:
					return null;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// The table can update so fast that a row can be deleted before
			// the table finishes updating, resulting in an
			// ArrayIndexOutOfBoundsException.
			return null;
		}
	}

	@Override
	public void stackChanged(StackChangeEvent e) {

		switch (e.getState()) {
			case StackChangeEvent.PUSH:
				long data = e.getData();
				this.stack.add(0, data);
				this.fireTableRowsInserted(0, 0);
				break;

			case StackChangeEvent.POP:
				if (this.stack.size() > 0) {
					this.stack.remove(0);
					this.fireTableRowsDeleted(0, 2);
				}
				break;

			case StackChangeEvent.CLEAR:
				this.clear();
				this.fireTableDataChanged();
				break;
		}
	}

	public void clear() {
		stack = new Vector<Long>();
	}

	public JTable createTableForModel() {
		JTable table = new JTable(this);
		this.setColumnWidths(table);
		return table;
	}

	public void setColumnWidths(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();

		columnModel.getColumn(0).setMinWidth(50);
		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(0).setMaxWidth(100);

		columnModel.getColumn(1).setMinWidth(50);
		columnModel.getColumn(1).setPreferredWidth(80);

		columnModel.getColumn(2).setMinWidth(50);
		columnModel.getColumn(2).setPreferredWidth(60);
		columnModel.getColumn(2).setMaxWidth(70);
	}
}
