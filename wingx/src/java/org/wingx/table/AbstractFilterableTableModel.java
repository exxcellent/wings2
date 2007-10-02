package org.wingx.table;

import javax.swing.table.AbstractTableModel;
import java.util.Map;
import java.util.HashMap;

/**
 * @author hengels
 */
public abstract class AbstractFilterableTableModel
    extends AbstractTableModel
    implements FilterableTableModel
{
    Map filters = new HashMap();

    public Object getFilter(int col) {
        return filters.get(new Integer(col));
    }

    public void setFilter(int col, Object value) {
        filters.put(new Integer(col), value);
    }
}
