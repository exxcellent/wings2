package org.wingx.table;

import javax.swing.table.TableModel;

/**
 * @author hengels
 */
public interface FilterableTableModel
    extends TableModel, RefreshableModel
{
    Object getFilter(int col);

    void setFilter(int col, Object value);
}
