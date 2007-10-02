package org.wingx.plaf.css;

import org.wings.plaf.css.*;
import org.wings.plaf.CGManager;
import org.wings.*;
import org.wings.session.SessionManager;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;
import org.wings.io.CachingDevice;
import org.wings.table.*;
import org.wingx.XTable;
import org.wingx.table.*;

import java.io.IOException;
import java.awt.*;

public class XTableCG
    extends AbstractComponentCG
    implements org.wings.plaf.TableCG
{
    private static final long serialVersionUID = 1L;
    protected String fixedTableBorderWidth;
    protected SIcon editIcon;
    protected String selectionColumnWidth = "22";
    protected final SLabel resetLabel = new SLabel(XTable.ICON_RESET);
    protected final SLabel refreshLabel = new SLabel(XTable.ICON_REFRESH);
    int horizontalOversize = 22;
    private String noDataLabel = "- - -";

    public int getHorizontalOversize() {
        return horizontalOversize;
    }

    public void setHorizontalOversize(int horizontalOversize) {
        this.horizontalOversize = horizontalOversize;
    }

    /**
     * Initialize properties from config
     */
    public XTableCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        setFixedTableBorderWidth((String)manager.getObject("TableCG.fixedTableBorderWidth", String.class));
        setEditIcon(manager.getIcon("TableCG.editIcon"));
        selectionColumnWidth = (String)manager.getObject("TableCG.selectionColumnWidth", String.class);
    }

    /**
     * Tweak property. Declares a deprecated BORDER=xxx attribute on the HTML TABLE element.
     */
    public String getFixedTableBorderWidth() {
        return fixedTableBorderWidth;
    }

    /**
     * Tweak property. Declares a deprecated BORDER=xxx attribute on the HTML TABLE element.
     */
    public void setFixedTableBorderWidth(String fixedTableBorderWidth) {
        this.fixedTableBorderWidth = fixedTableBorderWidth;
    }

    /**
     * Sets the icon used to indicated an editable cell (if content is not direct clickable).
     */
    public void setEditIcon(SIcon editIcon) {
        this.editIcon = editIcon;
    }

    /**
     * @return Returns the icon used to indicated an editable cell (if content is not direct clickable).
     */
    public SIcon getEditIcon() {
        return editIcon;
    }

    public String getNoDataLabel() {
        return noDataLabel;
    }

    public void setNoDataLabel(String noDataLabel) {
        this.noDataLabel = noDataLabel;
    }

    /**
     * @return The width of the (optional) row selection column in px
     */
    public String getSelectionColumnWidth() {
        return selectionColumnWidth;
    }

    /**
     * The width of the (optional) row selection column in px
     *
     * @param selectionColumnWidth The width of the (optional) row selection column with unit
     */
    public void setSelectionColumnWidth(String selectionColumnWidth) {
        this.selectionColumnWidth = selectionColumnWidth;
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);

        final STable table = (STable)comp;
        final CGManager manager = table.getSession().getCGManager();
        Object value;

        value = manager.getObject("STable.defaultRenderer", STableCellRenderer.class);
        if (value != null) {
            table.setDefaultRenderer((STableCellRenderer)value);
            if (value instanceof SDefaultTableCellRenderer) {
                SDefaultTableCellRenderer cellRenderer = (SDefaultTableCellRenderer)value;
                cellRenderer.setEditIcon(editIcon);
            }
        }

        value = manager.getObject("XTable.headerRenderer", STableCellRenderer.class);
        if (value != null)
            table.setHeaderRenderer((STableCellRenderer)value);
        else
            table.setHeaderRenderer(new XTable.HeaderRenderer());

        value = manager.getObject("STable.rowSelectionRenderer", org.wings.table.STableCellRenderer.class);
        if (value != null) {
            if (value instanceof SDefaultTableRowSelectionRenderer) {
                SDefaultTableRowSelectionRenderer rowSelectionRenderer = (SDefaultTableRowSelectionRenderer)value;
                rowSelectionRenderer.setUseIcons(true);
            }
            table.setRowSelectionRenderer((org.wings.table.STableCellRenderer)value);
        }
    }

    public void uninstallCG(SComponent component) {
        super.uninstallCG(component);
        final STable table = (STable)component;
        table.setHeaderRenderer(null);
        table.setDefaultRenderer(null);
        table.setRowSelectionRenderer(null);
    }

    /**
     * write a specific cell to the device
     */
    protected void renderCellContent(final Device device, final STable table, final SCellRendererPane rendererPane,
                                     final int row, final int col)
        throws IOException
    {
        final boolean isEditingCell = table.isEditing() && row == table.getEditingRow() && col == table.getEditingColumn();
        final boolean editableCell = table.isCellEditable(row, col);
        final boolean selectableCell = table.getSelectionMode() != SListSelectionModel.NO_SELECTION && !table.isEditable();

        final SComponent component;
        if (isEditingCell) {
            component = table.getEditorComponent();
        }
        else {
            component = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
        }

        final boolean isClickable = component instanceof SClickable;

        device.print("<td col=\"");
        device.print(col);
        device.print("\"");

        if (component == null) {
            device.print("></td>");
            return;
        }
        Utils.printTableCellAlignment(device, component, SConstants.LEFT, SConstants.TOP);
        Utils.optAttribute(device, "oversize", horizontalOversize);

        String parameter = null;
        if (table.isEditable() && !isEditingCell && editableCell)
            parameter = table.getEditParameter(row, col);
        else if (selectableCell)
            parameter = table.getToggleSelectionParameter(row, col);

        if (parameter != null && !isEditingCell && (selectableCell || editableCell) && !isClickable) {
            Utils.printClickability(device, table, parameter, true, table.getShowAsFormComponent());
            device.print(" class=\"cell clickable\"");
        }
        else
            device.print(" class=\"cell\"");
        device.print(">");

        rendererPane.writeComponent(device, component, table);

        device.print("</td>");
        Utils.printNewline(device, component);
    }

    protected void writeHeaderCell(final Device device, final XTable table,
                                   final SCellRendererPane rendererPane,
                                   final int col)
        throws IOException
    {
        final SComponent comp = table.prepareHeaderRenderer(table.getHeaderRenderer(col), col);

        device.print("<th col=\"");
        device.print(col);
        device.print("\"");

        Utils.printTableCellAlignment(device, comp, SConstants.CENTER, SConstants.CENTER);

        String parameter = table.getToggleSortParameter(col);

        if (table.getModel() instanceof SortableTableModel) {
            STableColumn column = table.getColumnModel().getColumn(col);
            if (!(column instanceof XTableColumn) || ((XTableColumn)column).isSortable()) {
                Utils.printClickability(device, table, parameter, true, table.getShowAsFormComponent());
                device.print(" class=\"clickable head\"");
            }
            else
                device.print(" class=\"head\"");
        }
        else
            device.print(" class=\"head\"");

        device.print(">");

        rendererPane.writeComponent(device, comp, table);

        device.print("</th>");
        Utils.printNewline(device, comp);
    }

    protected void writeFilterCell(Device device, XTable table,
                                   SCellRendererPane rendererPane,
                                   int c)
        throws IOException
    {
        STableColumn column = table.getColumnModel().getColumn(c);

        if (column instanceof XTableColumn) {
            XTableColumn xTableColumn = (XTableColumn)column;
            if (!xTableColumn.isFilterable()) {
                device.print("<th class=\"filter\"></th>");
                return;
            }
        }

        EditableTableCellRenderer editableTableCellRenderer = table.getFilterRenderer(c);
        if (editableTableCellRenderer == null) {
            device.print("<th class=\"filter\"></th>");
            return;
        }

        SComponent comp = table.prepareFilterRenderer(editableTableCellRenderer, c);

        device.print("<th valign=\"middle\" class=\"filter\" col=\"");
        device.print(c);
        device.print("\"");

        //Utils.printTableCellAlignment(device, comp, SConstants.LEFT, SConstants.CENTER);
        device.print(" align=\"left\">");

        rendererPane.writeComponent(device, comp, table);
        device.print("</th>");
        Utils.printNewline(device, comp);
    }

    public final void writeInternal(final Device _device, final SComponent _c) throws IOException {
        RenderHelper.getInstance(_c).forbidCaching();

        final XTable table = (XTable)_c;

        /*
        * Description: This is a FIREFOX bug workaround. Currently we render all components surrounded by a DIV/TABLE.
        * During heavy load and incremental delivery of a page this leads to disorted tables as the firefox seems
        * to have an bug.
        * Refer to http://jira.j-wings.org/browse/WGS-139 for screenshots
        *
        * This workaround tries to deliver the HTML code of a table at once.
        * This seems to resolve this issue to 99%.
        */
        final CachingDevice device = new CachingDevice(_device);

        try {
            device.print("<table");
            writeAllAttributes(device, table);
            writeTableAttributes(device, table);
            device.print("><thead>");
            Utils.printNewline(device, table);

            Rectangle currentViewport = table.getViewportSize();
            Rectangle maximalViewport = table.getScrollableViewportSize();
            int startX = 0;
            int endX = table.getVisibleColumnCount();
            int startY = 0;
            int endY = table.getRowCount();
            int emptyIndex = maximalViewport != null ? maximalViewport.height : endY;

            if (currentViewport != null) {
                startX = currentViewport.x;
                endX = startX + currentViewport.width;
                startY = currentViewport.y;
                endY = startY + currentViewport.height;
            }

            writeColumnWidths(device, table, startX, endX);

            writeHeader(device, table, startX, endX);

            device.print("</thead>");
            Utils.printNewline(device, table);
            device.print("<tbody>");

            if (table.getModel().getRowCount() == 0)
                writeNoData(device, table, startX, endX, endY);
            else
                writeBody(device, table, startX, endX, startY, endY, emptyIndex);

            writeFooter(device, table, startX, endX);

            device.print("</tbody></table>");
        }
        finally {
            /* Refer to description above. */
            device.close();
            //device = null;
            RenderHelper.getInstance(_c).allowCaching();
        }
    }

    private void writeTableAttributes(CachingDevice device, XTable table) throws IOException {
        final SDimension intercellPadding = table.getIntercellPadding();
        final SDimension intercellSpacing = table.getIntercellSpacing();
        Utils.writeEvents(device, table, null);

        // TODO: border="" should be obsolete
        // TODO: cellspacing and cellpadding may be in conflict with border-collapse
        /* Tweaking: CG configured to have a fixed border="xy" width */
        Utils.optAttribute(device, "border", fixedTableBorderWidth);
        Utils.optAttribute(device, "cellspacing", ((intercellSpacing != null) ? "" + intercellSpacing.getWidthInt() : null));
        Utils.optAttribute(device, "cellpadding", ((intercellPadding != null) ? "" + intercellPadding.getHeightInt() : null));
    }

    private void writeColumnWidths(CachingDevice device, XTable table, int startX, int endX) throws IOException {
        STableColumnModel columnModel = table.getColumnModel();
        if (columnModel != null && atLeastOneColumnWidthIsNotNull(columnModel)) {
            device.print("<colgroup>");
            writeCol(device, selectionColumnWidth);

            for (int i = startX; i < endX; ++i) {
                STableColumn column = columnModel.getColumn(i);
                if (!column.isHidden())
                    writeCol(device, column.getWidth());
                else
                    ++endX;
            }
            device.print("</colgroup>");
            Utils.printNewline(device, table);
        }
    }

    private void writeHeader(CachingDevice device, XTable table, int startX, int endX) throws IOException {
        if (!table.isHeaderVisible())
            return;

        final SCellRendererPane rendererPane = table.getCellRendererPane();
        STableColumnModel columnModel = table.getColumnModel();

        SStringBuilder headerArea = Utils.inlineStyles(table.getDynamicStyle(STable.SELECTOR_HEADER));
        device.print("<tr class=\"header\"");
        Utils.optAttribute(device, "style", headerArea);
        device.print(">");

        Utils.printNewline(device, table, 1);
        writeSelectionHeader(device, table);

        for (int i = startX; i < endX; ++i) {
            STableColumn column = columnModel.getColumn(i);
            if (!column.isHidden())
                writeHeaderCell(device, table, rendererPane, i);
            else
                ++endX;
        }
        device.print("</tr>");
        Utils.printNewline(device, table);

        if (table.getModel() instanceof FilterableTableModel) {
            device.print("<tr class=\"filter\">\n");
            Utils.printNewline(device, table, 1);
            writeSelectionFilter(device, table);

            for (int i = startX; i < endX; ++i) {
                STableColumn column = columnModel.getColumn(i);
                if (!column.isHidden())
                    writeFilterCell(device, table, rendererPane, i);
            }

            device.print("</tr>");
            Utils.printNewline(device, table);
        }
    }

    private void writeBody(CachingDevice device, XTable table,
            int startX, int endX, int startY, int endY, int emptyIndex) throws IOException {
        final SListSelectionModel selectionModel = table.getSelectionModel();

        SStringBuilder selectedArea = Utils.inlineStyles(table.getDynamicStyle(STable.SELECTOR_SELECTED));
        SStringBuilder evenArea = Utils.inlineStyles(table.getDynamicStyle(STable.SELECTOR_EVEN_ROWS));
        SStringBuilder oddArea = Utils.inlineStyles(table.getDynamicStyle(STable.SELECTOR_ODD_ROWS));
        final SCellRendererPane rendererPane = table.getCellRendererPane();
        STableColumnModel columnModel = table.getColumnModel();

        int backupEndX = endX; // Backup endX because we have to restore it after each inner loop.

        for (int r = startY; r < endY; ++r) {
            if (r >= emptyIndex) {
                int colspan = endX - startX;
                device.print("<tr>\n");
                device.print("  <td class=\"empty\"></td>\n");
                device.print("  <td class=\"empty\" colspan=\"" + colspan + "\">&nbsp;</td>\n");
                device.print("</tr>\n");
                continue;
            }

            String rowStyle = table.getRowStyle(r);
            SStringBuilder rowClass = new SStringBuilder(rowStyle != null ? rowStyle + " " : "");
            device.print("<tr");
            if (selectionModel.isSelectedIndex(r)) {
                Utils.optAttribute(device, "style", selectedArea);
                rowClass.append("selected ");
            }
            else if (r % 2 != 0)
                Utils.optAttribute(device, "style", oddArea);
            else
                Utils.optAttribute(device, "style", evenArea);

            rowClass.append(r % 2 != 0 ? "odd" : "even");
            Utils.optAttribute(device, "class", rowClass);
            device.print(">");

            writeSelectionBody(device, table, rendererPane, r);

            for (int c = startX; c < endX; ++c) {
                STableColumn column = columnModel.getColumn(c);
                if (!column.isHidden())
                    renderCellContent(device, table, rendererPane, r, c);
                else
                    ++endX;
            }
            endX = backupEndX;

            device.print("</tr>");
            Utils.printNewline(device, table);
        }
    }

    private void writeSelectionHeader(Device device, XTable table) throws IOException {
        device.print("<th valign=\"middle\"");
        Utils.optAttribute(device, "width", selectionColumnWidth);

        if (table.getModel() instanceof RefreshableModel) {
            String parameter = table.getRefreshParameter();
            Utils.printClickability(device, table, parameter, true, table.getShowAsFormComponent());
            device.print(" class=\"num clickable\">");

            refreshLabel.write(device);
        }
        else {
            device.print(" class=\"num\">");
        }
        device.print("</th>");
    }

    private void writeSelectionFilter(Device device, XTable table) throws IOException {
        device.print("<th valign=\"middle\"");
        Utils.optAttribute(device, "width", selectionColumnWidth);

        String parameter = table.getResetParameter();
        Utils.printClickability(device, table, parameter, true, table.getShowAsFormComponent());
        device.print(" class=\"num clickable\"");

        device.print(">");

        resetLabel.write(device);

        device.print("</th>");
    }

    private boolean atLeastOneColumnWidthIsNotNull(STableColumnModel columnModel) {
        int columnCount = columnModel.getColumnCount();
        for (int i = 0; i < columnCount; i++)
            if (columnModel.getColumn(i).getWidth() != null)
                return true;
        return false;
    }

    private void writeCol(Device device, String width) throws IOException {
        device.print("<col");
        Utils.optAttribute(device, "width", width);
        device.print("/>");
    }

    protected void writeFooter(CachingDevice device, XTable table, int startX, int endX) throws IOException {
    }

    private void writeNoData(Device device, XTable table, int startX, int endX, int endY) throws IOException {
        int colspan = endX - startX;
        device.print("<tr>\n");
        device.print("  <td class=\"empty\">&nbsp;</td>\n");
        device.print("  <td class=\"nodata\" colspan=\"" + colspan + "\" rowspan=\"" + endY + "\" align=\"center\" valign=\"middle\">");
        device.print(noDataLabel);
        device.print("</td>\n");
        device.print("</tr>\n");
        for (int i = 1; i < endY; ++i) {
            device.print("<tr><td class=\"empty\">&nbsp;</td></tr>");
        }
    }

    /**
     * Renders the row sometimes needed to allow row selection.
     */
    protected void writeSelectionBody(final Device device, final STable table, final SCellRendererPane rendererPane,
                                      final int row)
        throws IOException
    {
        final STableCellRenderer rowSelectionRenderer = table.getRowSelectionRenderer();
        if (rowSelectionRenderer instanceof SDefaultTableRowSelectionRenderer) {
            SDefaultTableRowSelectionRenderer defaultTableRowSelectionRenderer = (SDefaultTableRowSelectionRenderer)rowSelectionRenderer;
            defaultTableRowSelectionRenderer.setUseIcons(table.getSelectionMode() != SListSelectionModel.NO_SELECTION);
        }
        final String columnStyle = Utils.joinStyles((SComponent)rowSelectionRenderer, "num");

        device.print("<td valign=\"top\" align=\"right\"");
        Utils.optAttribute(device, "width", selectionColumnWidth);

        String value = table.getToggleSelectionParameter(row, -1);
        if (table.getSelectionMode() != SListSelectionModel.NO_SELECTION) {
            Utils.printClickability(device, table, value, true, table.getShowAsFormComponent());
            device.print(" class=\"clickable ");
            device.print(columnStyle);
            device.print("\"");
        }
        else {
            device.print(" class=\"");
            device.print(columnStyle);
            device.print("\"");
        }
        device.print(">");

        renderSelectionColumnContent(device, row, table, rendererPane);

        device.print("</td>");
    }

    /**
     * Renders the <b>content</b> of the row selection row.
     */
    private void renderSelectionColumnContent(final Device device, int row, final STable table, final SCellRendererPane rendererPane)
        throws IOException
    {
        final STableCellRenderer rowSelectionRenderer = table.getRowSelectionRenderer();
        if (rowSelectionRenderer == null) {
            // simple case: just row number
            device.print(row);
        }
        else {
            // default case: use row selection renderer component
            final SComponent comp = rowSelectionRenderer.getTableCellRendererComponent(table,
                                                                                       table.getToggleSelectionParameter(row, -1),
                                                                                       table.isRowSelected(row),
                                                                                       row, -1);
            rendererPane.writeComponent(device, comp, table);
        }
    }
}
