package org.wingx.table;

import org.wings.table.*;

public class XTableColumn
    extends STableColumn
{
    boolean sortable;
    boolean filterable;
    protected EditableTableCellRenderer filterRenderer;

    public XTableColumn(int modelIndex) {
        super(modelIndex);
    }

    public XTableColumn(int modelIndex, String width) {
        super(modelIndex, width);
    }

    public XTableColumn(int modelIndex, String width, STableCellRenderer cellRenderer, STableCellEditor cellEditor) {
        super(modelIndex, width, cellRenderer, cellEditor);
    }

    /**
     * Expect this to be replaced with STableCellEditor as soon as ajax makes the usability of the
     * standard cell editor mechanism acceptable.
     */
    public EditableTableCellRenderer getFilterRenderer() {
        return filterRenderer;
    }

    public void setFilterRenderer(EditableTableCellRenderer filterRenderer) {
        this.filterRenderer = filterRenderer;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }
}
