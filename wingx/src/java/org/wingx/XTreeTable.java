package org.wingx;

import java.util.StringTokenizer;

import javax.swing.table.TableModel;

import org.wings.STable;
import org.wings.event.SMouseEvent;
import org.wings.event.SMouseListener;
import org.wings.table.STableCellEditor;
import org.wings.table.STableCellRenderer;
import org.wings.table.STableColumn;
import org.wings.table.STableColumnModel;
import org.wingx.table.EditableTableCellRenderer;
import org.wingx.treetable.TreeColumnRenderer;
import org.wingx.treetable.XTreeTableModel;
import org.wingx.treetable.XTreeTableNode;

public class XTreeTable extends STable {
	public XTreeTable(XTreeTableModel model) {
		this();
		setModel(model);
	}

	public XTreeTable() {
		setEditable(true);
		setSelectionMode(STable.NO_SELECTION);
	}

	public void setModel(TableModel tm) {
		super.setModel(tm);
	}

	public void createDefaultColumnsFromModel() {
		super.createDefaultColumnsFromModel();
		TableModel tm = getModel();

		if (tm instanceof XTreeTableModel) {
			columnModel.getColumn(0).setCellRenderer(new TreeColumnRenderer());
			boolean registered = false;
			Object[] list = getListenerList();
			for (int i = 0; i < list.length; i++) {
				Object o = list[i];
				if (o instanceof ExpansionListener) {
					registered = true;
					break;
				}
			}

			if (!registered) {
				addMouseListener(new ExpansionListener());
			}
		}
	}

	public STableCellRenderer getCellRenderer(int row, int col) {
		STableColumnModel columnModel = getColumnModel();
		if (columnModel != null) {
			STableColumn column = columnModel.getColumn(col);
			if (column != null) {
				STableCellRenderer renderer = column.getCellRenderer();
				if (renderer != null)
					return renderer;
			}
		}

		XTreeTableNode rowNode = (XTreeTableNode) getTreeTableModel()
				.getNodeModel().get(row);
		if (rowNode.getNodeClass() != null)
			return getDefaultRenderer(rowNode.getNodeClass());
		else
			return getDefaultRenderer(getColumnClass(col));
	}

	public STableCellEditor getCellEditor(int row, int col) {
		STableColumnModel columnModel = getColumnModel();
		if (columnModel != null) {
			STableColumn column = columnModel.getColumn(col);
			if (column != null) {
				STableCellEditor editor = column.getCellEditor();
				if (editor != null)
					return editor;
			}
		}

		XTreeTableNode rowNode = (XTreeTableNode) getTreeTableModel()
				.getNodeModel().get(row);
		if (rowNode.getNodeClass() != null) {
			return getDefaultEditor(rowNode.getNodeClass());
		} else
			return getDefaultEditor(getColumnClass(col));
	}

	public boolean isCellEditable(int row, int col) {
		return col == 0;
	}

	public void processLowLevelEvent(String action, String[] values) {
		// delegate events to editors
		if (action.indexOf("_") != -1) {
			StringTokenizer tokens = new StringTokenizer(action, "_");
			tokens.nextToken(); // tableName
			String rowToken = tokens.nextToken();
			int row = Integer.parseInt(rowToken); // row
			String colToken = tokens.nextToken();
			int col = Integer.parseInt(colToken); // col

			STableCellRenderer cellRenderer = getCellRenderer(row, col);
			if (cellRenderer instanceof EditableTableCellRenderer) {
				EditableTableCellRenderer editableCellRenderer = (EditableTableCellRenderer) cellRenderer;
				editableCellRenderer.getLowLevelEventListener(this, row, col)
						.processLowLevelEvent(action, values);
				Object value = editableCellRenderer.getValue();
				getTreeTableModel().setValueAt(value, row, col);
			}
        }
        else
			super.processLowLevelEvent(action, values);
	}

	/**
	 * @return Returns the model casted as a XTreeTableModel
	 */
	public XTreeTableModel getTreeTableModel() {
		return (XTreeTableModel) getModel();
	}

	class ExpansionListener implements SMouseListener {
		public void mouseClicked(SMouseEvent e) {
			int row = XTreeTable.this.rowAtPoint(e.getPoint());
			int col = XTreeTable.this.columnAtPoint(e.getPoint());
			if (col == 0) {
				getTreeTableModel().toggleExpanded(row);
				e.consume();
			}
		}
	}
}
