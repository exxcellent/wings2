package org.wingx.table;

import javax.swing.table.AbstractTableModel;
import java.util.Map;
import java.util.HashMap;

/**
 * @author hengels
 */
public abstract class XTableModel
    extends AbstractTableModel
    implements SortableTableModel, FilterableTableModel
{
    Map filters = new HashMap();

    public Object getFilter(int col) {
        return filters.get(new Integer(col));
    }

    public void setFilter(int col, Object value) {
        filters.put(new Integer(col), value);
    }

    Map sorts = new HashMap();

    public int getSort(int col) {
        Integer order = (Integer)sorts.get(new Integer(col));
        if (order == null) {
            order = new Integer(SORT_NONE);
            sorts.put(new Integer(col), order);
        }
        return order.intValue();
    }

    public void setSort(int col, int order) {
        sorts.put(new Integer(col), new Integer(order));
    }
}
