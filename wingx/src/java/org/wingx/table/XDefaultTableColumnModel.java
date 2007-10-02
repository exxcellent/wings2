package org.wingx.table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.wings.table.SDefaultTableColumnModel;
import org.wings.table.STableColumn;

public class XDefaultTableColumnModel
    extends SDefaultTableColumnModel
    implements XTableColumnModel
{
	/**
	 * Map<String, STableColumn>
	 */
    private Map columnByName = new HashMap();

    public STableColumn getColumn(String identifier) {
        STableColumn column = (STableColumn) columnByName.get(identifier);
        if (column == null) {
            for (Iterator iterator = getColumns().iterator(); iterator.hasNext();) {
                STableColumn tableColumn = (STableColumn)iterator.next();
                if (identifier.equals(tableColumn.getIdentifier())) {
                    columnByName.put(identifier, tableColumn);
                    column = tableColumn;
                    break;
                }
            }
        }
        return column;
    }
}
