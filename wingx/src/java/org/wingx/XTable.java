package org.wingx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.table.TableModel;

import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SIcon;
import org.wings.SPoint;
import org.wings.SResourceIcon;
import org.wings.STable;
import org.wings.event.SMouseEvent;
import org.wings.event.SMouseListener;
import org.wings.table.SDefaultTableCellRenderer;
import org.wings.table.STableCellRenderer;
import org.wings.table.STableColumn;
import org.wings.table.STableColumnModel;
import org.wings.util.SStringBuilder;
import org.wingx.plaf.css.XTableCG;
import org.wingx.table.EditableTableCellRenderer;
import org.wingx.table.FilterableTableModel;
import org.wingx.table.RefreshableModel;
import org.wingx.table.SortableTableModel;
import org.wingx.table.XDefaultTableColumnModel;
import org.wingx.table.XTableClickListener;
import org.wingx.table.XTableColumn;

public class XTable extends STable
{
    public static final String MESSAGE_PREFIX = "org.wingx.XTable.";
    public static final SIcon ICON_TRANS = new SResourceIcon("org/wings/icons/transdot.gif");
    public static final SIcon ICON_ASCENDING = new SResourceIcon("org/wingx/table/images/table_sort_down.png");
    public static final SIcon ICON_DESCENDING = new SResourceIcon("org/wingx/table/images/table_sort_up.png");
    public static final SIcon ICON_REFRESH = new SResourceIcon("org/wingx/table/images/table_refresh.png");
    public static final SIcon ICON_RESET = new SResourceIcon("org/wingx/table/images/table_clear_filter.png");

    /**
     * Map<Integer, java.util.List<XTableClickListener>>    
     */
    private Map column2Listeners = new HashMap();
    protected boolean resetFilter = false;
    protected int delayedSortColumn = -1;
    private EditableTableCellRenderer filterRenderer;
    private boolean refresh;
    private SMouseListener linkMouseListener;
    private SStringBuilder nameBuffer = new SStringBuilder();

    public XTable() {
    }

    public XTable(TableModel model, STableColumnModel columnModel) {
        super(model, columnModel);
    }

    public XTable(TableModel tableModel) {
        super(tableModel);
    }

    public EditableTableCellRenderer getFilterRenderer() {
        return filterRenderer;
    }

    public void setFilterRenderer(EditableTableCellRenderer filterRenderer) {
        this.filterRenderer = filterRenderer;
    }

    protected void nameFilterComponent(final SComponent component, final int col, final int num) {
        nameBuffer.setLength(0);
        nameBuffer
            .append(this.getName())
            .append("_f_")
            .append(col)
            .append("_")
            .append(num);
        component.setNameRaw(nameBuffer.toString());
    }

    private int nthVisibleColumn(int n) {
        n += 1;
        int c = 0;
        for (Iterator iterator = columnModel.getColumns().iterator(); iterator.hasNext();) {
            STableColumn column = (STableColumn)iterator.next();
            if (!column.isHidden())
                n --;

            if (n == 0)
                return c;

            c ++;
        }
        throw new RuntimeException("There is no " + n + "'th visible column.");
    }
    
    private boolean filtersDifferent(Object filter, Object value) {
        if (filter instanceof Object[] && value instanceof Object[]) {
            return !Arrays.equals((Object[]) filter, (Object[]) value);
        } else {
            return SComponent.isDifferent(filter, value);
        }
    }
    
    public void processLowLevelEvent(String action, String[] values) {
        if (model instanceof FilterableTableModel && action.indexOf("_f_") != -1) {
            FilterableTableModel filterableTableModel = (FilterableTableModel)getModel();

            StringTokenizer tokens = new StringTokenizer(action, "_");
            tokens.nextToken(); // tableName
            tokens.nextToken(); // f
            int col = Integer.parseInt(tokens.nextToken()); // col

            EditableTableCellRenderer editableTableCellRenderer = getFilterRenderer(col);
            editableTableCellRenderer.getLowLevelEventListener(this, -1, col).processLowLevelEvent(action, values);
            Object value = editableTableCellRenderer.getValue();

            Object filter = filterableTableModel.getFilter(convertColumnIndexToModel(col));
            if (filtersDifferent(filter, value)) {
                filterableTableModel.setFilter(convertColumnIndexToModel(col), value);
                SForm.addArmedComponent(this);
                refresh = true;
            }
        }
        else if (isEditing() && action.indexOf("_e_") != -1 && cellEditorComponent != null) {
            cellEditorComponent.processLowLevelEvent(action, values);
        }
        else if (action.indexOf("_") != -1) {
            StringTokenizer tokens = new StringTokenizer(action, "_");
            tokens.nextToken(); // tableName
            int row = Integer.parseInt(tokens.nextToken()); // row
            int col = Integer.parseInt(tokens.nextToken()); // col

            STableCellRenderer cellRenderer = getCellRenderer(row, col);
            if (cellRenderer instanceof EditableTableCellRenderer) {
                EditableTableCellRenderer editableCellRenderer = (EditableTableCellRenderer)cellRenderer;
                editableCellRenderer.getLowLevelEventListener(this, row, col).processLowLevelEvent(action, values);
                Object value = editableCellRenderer.getValue();
                getModel().setValueAt(value, row, convertColumnIndexToModel(col));
            }
        }
        else {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                char modus = value.charAt(0);
                try {
                    // editor event
                    switch (modus) {
                        case 'o':
                            delayedSortColumn = Integer.parseInt(value.substring(1));
                            SForm.addArmedComponent(this);
                            break;
                        case 'c':
                            resetFilter = true;
                            break;
                        case 'r':
                            refresh = true;
                            break;
                    }
                }
                catch (NumberFormatException ex) {
                    // ignored
                }
            }
            super.processLowLevelEvent(action, values);
        }
    }
    
    /**
     * @Override
     */
    public void fireFinalEvents() {
        super.fireFinalEvents();
        /*
        * check for sort
        */
        if (delayedSortColumn != -1) {
            SortableTableModel sortableModel = (SortableTableModel)model;
            int order = sortableModel.getSort(delayedSortColumn);
            order += 1;
            order %= 3;
            sortableModel.setSort(delayedSortColumn, order);
            delayedSortColumn = -1;
            refresh = true;
        }
        /*
        * check for filter reset
        */
        if (resetFilter) {
            resetFilter = false;
            resetFilter();
            refresh = true;
        }

        if (refresh) {
            refresh = false;
            refresh();
        }
    }

    /**
     * refresh the table
     */
    public void refresh() {
        if (getModel() instanceof RefreshableModel)
            ((RefreshableModel)getModel()).refresh();
    }

    public void resetFilter() {
        FilterableTableModel filterableTableModel = (FilterableTableModel)getModel();
        for (int i=0; i < filterableTableModel.getColumnCount(); i++)
            filterableTableModel.setFilter(i, null);
    }

    public void updateCG() {
        setCG(new XTableCG());
    }

    public String getToggleSortParameter(int col) {
        return "o" + col;
    }

    public String getRefreshParameter() {
        return "r";
    }

    public String getResetParameter() {
        return "c";
    }

    /**
     * Returns the header renderer for the given header cell.
     * @param col Table column
     * @return The header renderer for the given header cell.
     */
    public EditableTableCellRenderer getFilterRenderer( int col ) {
        STableColumnModel columnModel = getColumnModel();
        if (columnModel != null) {
            STableColumn column  = columnModel.getColumn(col);
            if (column != null) {
                STableCellRenderer renderer = column instanceof XTableColumn ? ((XTableColumn)column).getFilterRenderer() : column.getCellRenderer();
                if (renderer instanceof EditableTableCellRenderer)
                   return (EditableTableCellRenderer)renderer;
            }
        }
        return getFilterRenderer();
    }

    /**
     * Prepares and returns the renderer to render the column filter
     * @param col Column number to render. Starts with <code>0</code>. May be <code>-1</code> for row selection column.
     * @return The renderer to render the column filter
     */
    public SComponent prepareFilterRenderer(EditableTableCellRenderer filterRenderer, int col ) {
        Object filterValue = col >= 0 ? ((FilterableTableModel)model).getFilter(convertColumnIndexToModel(col)) : null;
        SComponent component = filterRenderer.getTableCellRendererComponent(this, filterValue, false, -1, col);
        nameFilterComponent(component, col);
        return component;
    }

    protected void nameFilterComponent(final SComponent component, final int col) {
        nameBuffer.setLength(0);
        nameBuffer.append(this.getName()).append("_f_");
        nameBuffer.append(col);
        component.setNameRaw(nameBuffer.toString());
    }

    public void addClickListener(int index, XTableClickListener listener) {
        List l = (List)column2Listeners.get(new Integer(index));
        if (l == null) {
            l = new ArrayList();
            column2Listeners.put(new Integer(index), l);
        }
        l.add(listener);
        if (linkMouseListener == null) {
            linkMouseListener = new SMouseListener() {
                public void mouseClicked(SMouseEvent e) {
                    SPoint point = e.getPoint();
                    int col = XTable.this.columnAtPoint(point);
                    List listeners = (List) column2Listeners.get(new Integer(col));
                    if (listeners == null) {
                        return;
                    }
                    for (Iterator iter = listeners.iterator(); iter.hasNext();) {
						XTableClickListener listener = (XTableClickListener) iter.next();
						
                        listener.clickOccured(XTable.this.rowAtPoint(point), col);
                    }
                }
            };
            addMouseListener(linkMouseListener);
        }
    }

    public boolean isCellEditable(int row, int col) {
        if (isClickListenerSet(col)) {
            return true;
        }
        return super.isCellEditable(row, col);
    }

    private boolean isClickListenerSet(int col) {
        List l = (List) column2Listeners.get(new Integer(col));
        return (l != null && !l.isEmpty());
    }

    public SComponent prepareRenderer(STableCellRenderer r, int row, int col) {
        SComponent component = super.prepareRenderer(r, row, col);
        if (isClickListenerSet(col)) {
            if (component.getStyle().indexOf(" link ")==-1) {
                component.setStyle(component.getStyle() + " link ");
            }
        } else if (component.getStyle() != null) {
            component.setStyle(component.getStyle().replaceAll(" link ", " "));
        }
        return component;
    }
    
    public boolean editCellAt(int row, int column, EventObject eo) {
        if (isClickListenerSet(column)) {
            return false;
        }
        return super.editCellAt(row, column, eo);
    }

    public static class HeaderRenderer
        extends SDefaultTableCellRenderer
    {
        public HeaderRenderer() {
            setHorizontalTextPosition(SConstants.LEFT);
            setPreferredSize(SDimension.FULLWIDTH);
        }

        public SComponent getTableCellRendererComponent(STable table, Object value, boolean selected, int row, final int col) {
            if (table.getModel() instanceof SortableTableModel) {
                SortableTableModel sortableTableModel = (SortableTableModel)table.getModel();

                setIcon(null);
                setText(value != null ? value.toString() : null);

                switch (sortableTableModel.getSort(col)) {
                    case SortableTableModel.SORT_NONE:
                        setIcon(null);
                        break;
                    case SortableTableModel.SORT_ASCENDING:
                        setIcon(ICON_ASCENDING);
                        break;
                    case SortableTableModel.SORT_DESCENDING:
                        setIcon(ICON_DESCENDING);
                        break;
                }
                return this;
            }
            else
                return super.getTableCellRendererComponent(table, value, selected, row, col);
        }
    }

    public void createDefaultColumnsFromModel() {
        TableModel tm = getModel();

        if (tm != null) {
            STableColumnModel columnModel = getColumnModel();
            while (columnModel.getColumnCount() > 0)
                columnModel.removeColumn(columnModel.getColumn(0));

            for ( int i = 0; i < tm.getColumnCount(); i++ ) {
                XTableColumn column = new XTableColumn( i );
                String columnName = tm.getColumnName( i );
                column.setHeaderValue( columnName );
                this.columnModel.addColumn( column );
            }
        }
    }

    protected STableColumnModel createDefaultColumnModel() {
        return new XDefaultTableColumnModel();
    }

    private static class Trigger
        implements LowLevelEventListener
    {
        XTable table;

        public Trigger(XTable table) {
            this.table = table;
        }

        public void processLowLevelEvent(String name, String[] values) {
        }

        public String getLowLevelEventId() {
            return null;
        }

        public String getEncodedLowLevelEventId() {
            return null;
        }

        public String getName() {
            return null;
        }

        public void fireIntermediateEvents() {
        }

        public void fireFinalEvents() {
            table.refresh();
        }

        public boolean isEnabled() {
            return false;
        }

        public boolean isEpochCheckEnabled() {
            return false;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final XTable.Trigger trigger = (XTable.Trigger)o;

            if (!table.getName().equals(trigger.table.getName())) return false;

            return true;
        }

        public int hashCode() {
            return table.getName().hashCode();
        }
    }
}
