package org.wingx.table;

import org.wings.table.STableColumn;
import org.wings.table.SDefaultTableColumnModel;

import java.util.*;

public class XDefaultTableColumnModel
    extends SDefaultTableColumnModel
    implements XTableColumnModel
{
    private Map<String, STableColumn> columnByName = new HashMap<String, STableColumn>();

    public STableColumn getColumn(String identifier) {
        STableColumn column = columnByName.get(identifier);
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
