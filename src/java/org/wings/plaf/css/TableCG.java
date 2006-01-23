/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;


import org.wings.RequestURL;
import org.wings.SAbstractIconTextCompound;
import org.wings.SCellRendererPane;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SListSelectionModel;
import org.wings.STable;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;
import org.wings.style.CSSSelector;
import org.wings.table.SDefaultTableCellRenderer;
import org.wings.table.STableCellRenderer;
import org.wings.table.STableColumnModel;
import org.wings.table.STableColumn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableCG extends AbstractComponentCG implements org.wings.plaf.TableCG {

    protected String fixedTableBorderWidth;
    private final int SELECTION_COLUMN_WIDTH = 10;

    /**
     * Initialize properties from config
     */
    public TableCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        setFixedTableBorderWidth((String) manager.getObject("TableCG.fixedTableBorderWidth", String.class));
    }


    public void installCG(final SComponent comp) {
        super.installCG(comp);

        final STable table = (STable) comp;
        final CGManager manager = table.getSession().getCGManager();
        Object value;

        value = manager.getObject("STable.defaultRenderer", STableCellRenderer.class);
        if (value != null) {
            table.setDefaultRenderer((STableCellRenderer) value);
            if (value instanceof SDefaultTableCellRenderer) {
                SDefaultTableCellRenderer cellRenderer = (SDefaultTableCellRenderer) value;
                cellRenderer.setEditIcon(manager.getIcon("TableCG.editIcon"));
            }
        }

        value = manager.getObject("STable.headerRenderer", STableCellRenderer.class);
        if (value != null) {
            table.setHeaderRenderer((STableCellRenderer) value);
        }

        value = manager.getObject("STable.rowSelectionRenderer", org.wings.table.STableCellRenderer.class);
        if (value != null) {
            table.setRowSelectionRenderer((org.wings.table.STableCellRenderer) value);
        }

        InputMap inputMap = new InputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK, false), "left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK, false), "right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.ALT_DOWN_MASK, false), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.ALT_DOWN_MASK, false), "down");
        table.setInputMap(SComponent.WHEN_IN_FOCUSED_FRAME, inputMap);

        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!table.isEditing())
                    return;
                if (table.getEditingRow() > 0 && "up".equals(e.getActionCommand()))
                    table.setEditingRow(table.getEditingRow() - 1);
                if (table.getEditingRow() < table.getRowCount() -1 && "down".equals(e.getActionCommand()))
                    table.setEditingRow(table.getEditingRow() + 1);
                if (table.getEditingColumn() > 0 && "left".equals(e.getActionCommand()))
                    table.setEditingColumn(table.getEditingColumn() - 1);
                if (table.getEditingColumn() < table.getColumnCount() -1 && "right".equals(e.getActionCommand()))
                    table.setEditingColumn(table.getEditingColumn() + 1);
                table.requestFocus();
            }
        };
        ActionMap actionMap = new ActionMap();
        actionMap.put("up", action);
        actionMap.put("down", action);
        actionMap.put("left", action);
        actionMap.put("right", action);
        table.setActionMap(actionMap);
    }

    public void uninstallCG(SComponent component) {
        super.uninstallCG(component);
        final STable table = (STable) component;
        table.setHeaderRenderer(null);
        table.setDefaultRenderer(null);
        table.setRowSelectionRenderer(null);
        table.setActionMap(null);
        table.setInputMap(null);
    }

    /**
     * write a specific cell to the device
     */
    protected void writeCell(Device device, STable table, SCellRendererPane rendererPane, int row, int col)
            throws IOException {
        SComponent component = null;
        boolean isEditingCell = table.isEditing()
                && row == table.getEditingRow()
                && col == table.getEditingColumn();
        boolean selectable = table.getSelectionMode() != SListSelectionModel.NO_SELECTION && !table.isEditable();
        boolean showAsFormComponent = table.getShowAsFormComponent();

        if (isEditingCell) {
            component = table.getEditorComponent();
        } else {
            component = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
        }

        boolean selectableButClickable = selectable && (component instanceof SAbstractIconTextCompound);

        device.print("<td col=\"");
        device.print(col);
        device.print("\"");

        if (component == null) {
            device.print("></td>");
            return;
        }

        switch (component.getHorizontalAlignment()) {
            case LEFT_ALIGN:
                device.print(" align=\"left\"");
                break;
            case CENTER_ALIGN:
                device.print(" align=\"center\"");
                break;
            case RIGHT_ALIGN:
                device.print(" align=\"right\"");
                break;
        }

        switch (component.getVerticalAlignment()) {
            case TOP_ALIGN:
                device.print(" valign=\"top\"");
                break;
            case CENTER_ALIGN:
                device.print(" valign=\"center\"");
                break;
            case BOTTOM_ALIGN:
                device.print(" align=\"bottom\"");
                break;
        }
        device.print(">");

        String parameter = null;
        if (table.isEditable() && !isEditingCell && table.isCellEditable(row, col))
            parameter = table.getEditParameter(row, col);
        else if (selectable)
            parameter = table.getToggleSelectionParameter(row, col);

        if (parameter != null && !isEditingCell && !selectableButClickable) {
            if (showAsFormComponent) {
                writeButtonStart(device, table, parameter);
                device.print(" type=\"submit\" name=\"");
                Utils.write(device, Utils.event(table));
                device.print(SConstants.UID_DIVIDER);
                Utils.write(device, parameter);
                device.print("\" value=\"\">");
            } else {
                RequestURL selectionAddr = table.getRequestURL();
                selectionAddr.addParameter(Utils.event(table), parameter);

                writeLinkStart(device, selectionAddr);
            }
        } else
            device.print("<span>");

        rendererPane.writeComponent(device, component, table);

        if (parameter != null && !isEditingCell && !selectableButClickable) {
            if (showAsFormComponent)
                device.print("</button>");
            else
                device.print("</a>");
        } else
            device.print("</span>");

        device.print("</td>");
        Utils.printNewline(device, component);
    }


    /**
     * @param device
     * @param selectionAddr
     * @throws IOException
     */
    protected void writeLinkStart(Device device, RequestURL selectionAddr) throws IOException {
        device.print("<a href=\"");
        Utils.write(device, selectionAddr.toString());
        device.print("\">");
    }


    /**
     * @param device
     * @param table
     * @param parameter
     * @throws IOException
     */
    protected void writeButtonStart(Device device, STable table, String parameter) throws IOException {
        device.print("<button");
    }


    protected void writeHeaderCell(Device device, STable table,
                                   SCellRendererPane rendererPane,
                                   int c)
            throws IOException {
        SComponent comp = table.prepareHeaderRenderer(c);

        device.print("<th>");
        rendererPane.writeComponent(device, comp, table);
        device.print("</th>");
        Utils.printNewline(device, comp);
    }


    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        final STable table = (STable) _c;
        final SDimension intercellPadding = table.getIntercellPadding();
        final SDimension intercellSpacing = table.getIntercellSpacing();
        final SListSelectionModel selectionModel = table.getSelectionModel();
        final SCellRendererPane rendererPane = table.getCellRendererPane();
        final boolean childSelectorWorkaround = !table.getSession().getUserAgent().supportsCssChildSelector();
        final boolean needsSelectionRow = selectionModel.getSelectionMode() != SListSelectionModel.NO_SELECTION && table.isEditable();
        final boolean showAsFormComponent = table.getShowAsFormComponent();
        final SDimension tableWidthByColumnModel = determineTableWidthByColumnModel(table, needsSelectionRow);

        device.print("<table");
        if (tableWidthByColumnModel != null)
            Utils.optAttribute(device,"style",tableWidthByColumnModel.toString()); // apply table dimension if set
        else
            Utils.printCSSInlineFullSize(device, table.getPreferredSize()); // stretch if outer dimension has been set

        // TODO: border="" should be obsolete
        // TODO: cellspacing and cellpadding may be in conflict with border-collapse
        /* Tweaking: CG configured to have a fixed border="xy" width */
        Utils.optAttribute(device, "border", fixedTableBorderWidth);
        Utils.optAttribute(device, "cellspacing", ((intercellSpacing != null) ? ""+intercellSpacing.getWidthInt() : null));
        Utils.optAttribute(device, "cellpadding", ((intercellPadding != null) ? ""+intercellPadding.getHeightInt() : null));
        device.print(">");
        Utils.printNewline(device, table);

        /*
        * get viewable area
        */
        int startRow = 0;
        int startCol = 0;
        int endRow = table.getRowCount();
        int endCol = table.getColumnCount();
        final Rectangle viewport = table.getViewportSize();
        if (viewport != null) {
            startRow = viewport.y;
            startCol = viewport.x;
            endRow = Math.min(startRow + viewport.height, endRow);
            endCol = Math.min(startCol + viewport.width, endCol);
        }

        /*
         *  The column widths if set
         */
        if (table.getColumnModel() != null) {
            renderColumnWidths(device, table, needsSelectionRow, startCol, endCol);
        }

        /*
        * render the header
        */
        if (table.isHeaderVisible()) {
            device.print("<thead><tr>\n");

            if (needsSelectionRow)
                device.print("<th></th>");

            for (int c = startCol; c < endCol; c++)
                writeHeaderCell(device, table, rendererPane, table.convertColumnIndexToModel(c));

            device.print("</tr></thead>\n");
        }

        device.print("<tbody>\n");
        for (int r = startRow; r < endRow; r++) {
            StringBuffer rowClass = new StringBuffer(table.getRowStyle(r) != null ? table.getRowStyle(r)+" " : "");
            device.print("<tr");
            if (selectionModel.isSelectedIndex(r)){
                if(childSelectorWorkaround)
                    rowClass.append("selected ");
                else
                    device.print(" selected=\"true\"");
            }

            rowClass.append(r % 2 != 0 ? "odd" : "even");
            Utils.optAttribute(device, "class", rowClass);

            if (!childSelectorWorkaround) {
                if (r % 2 != 0)
                    device.print(" odd=\"true\"");
                else
                    device.print(" even=\"true\"");
            }
            device.print(">");

            if (needsSelectionRow) {
                renderSelectionRow(device, table, rendererPane, r, showAsFormComponent);
            }

            for (int c = startCol; c < endCol; c++)
                writeCell(device, table, rendererPane, r, table.convertColumnIndexToModel(c));

            device.print("</tr>\n");
        }
        device.print("</tbody>\n");
        device.print("</table>\n");
    }

    /**
     * Renders a COLGROUP html element to format the column widths
     */
    protected void renderColumnWidths(final Device device, final STable table,
                                      final boolean needsSelectionRow, final int startcol, final int endcol) throws IOException {
        final STableColumnModel columnModel = table.getColumnModel();
        final int totalWidth = columnModel.getTotalColumnWidth();
        if (totalWidth > 0) {
            int viewPortWidth = 0;
            final String totalWidthUnit = columnModel.getTotalColumnWidthUnit();
            for (int i = startcol; i <= endcol; i++) {
                final STableColumn column = columnModel.getColumn(i);
                if (column != null && !column.isHidden()) {
                    viewPortWidth+=column.getWidth();
                }
            }

            Utils.printNewline(device, table);
            device.print("<colgroup>");
            if (needsSelectionRow) {
                Utils.printNewline(device, table);
                device.print("\t<col width=\"").print(SELECTION_COLUMN_WIDTH).print("\"/>");
            }
            for (int i = startcol; i <= endcol; i++) {
                final STableColumn column = columnModel.getColumn(i);
                if (column != null && !column.isHidden()) {
                    Utils.printNewline(device, table);
                    device.print("\t<col width=\"");
                    if (totalWidthUnit == null) // relative
                        device.print(Math.round(100.0f/viewPortWidth*column.getWidth())).print("%");
                    else
                        device.print(column.getWidth()).print(column.getWidthUnit());
                    device.print("\"/>");
                }
            }
            Utils.printNewline(device, table);
            device.print("</colgroup>");
        }
    }

    /** Renders the row sometimes needed to allow row selection. */
    protected void renderSelectionRow(final Device device, final STable table, final SCellRendererPane rendererPane,
                                      final int row, final boolean showAsFormComponent)
            throws IOException {
        final STableCellRenderer rowSelectionRenderer = table.getRowSelectionRenderer();
        final String columnStyle = Utils.joinStyles((SComponent) rowSelectionRenderer, "numbering");

        device.print("<td");
        Utils.optAttribute(device, "col", "numbering");
        Utils.optAttribute(device, "class", columnStyle);
        Utils.optAttribute(device, "width", SELECTION_COLUMN_WIDTH); // TODO fixme static selection column width
        device.print(">");

        if (showAsFormComponent) {
            writeButtonStart(device, table, table.getToggleSelectionParameter(row, -1));
            device.print(" type=\"submit\" name=\"");
            Utils.write(device, Utils.event(table));
            device.print(SConstants.UID_DIVIDER);
            Utils.write(device, table.getToggleSelectionParameter(row, -1));
            device.print("\" value=\"\">");
            renderSelectionRowContent(device, row, table, rendererPane);
            device.print("</button>");
        } else {
            RequestURL selectionAddr = table.getRequestURL();
            selectionAddr.addParameter(Utils.event(table), table.getToggleSelectionParameter(row, -1));

            writeLinkStart(device, selectionAddr);
            renderSelectionRowContent(device, row, table, rendererPane);
            device.print("</a>");
        }
        device.print("</td>");
    }

    /** Renders the <b>content</b> of the row selection row. */
    private void renderSelectionRowContent(final Device device, int row, final STable table, final SCellRendererPane rendererPane)
            throws IOException {
        final STableCellRenderer rowSelectionRenderer = table.getRowSelectionRenderer();
        if (rowSelectionRenderer == null) {
            // simple case: just row number
            device.print(row);
        } else {
            // default case: use row selection renderer component
            final SComponent comp = rowSelectionRenderer.getTableCellRendererComponent(table,
                    table.getToggleSelectionParameter(row, -1),
                    table.isRowSelected(row),
                    row, -1);
            rendererPane.writeComponent(device, comp, table);
        }
    }

    /**
     * @return The total width for this table or <code>null</code> if none.
     */
    protected SDimension determineTableWidthByColumnModel(final STable table, final boolean needsSelectionRow) {
        if (table.getColumnModel() == null) {
            return null;
        } else {
            int totalWidth = table.getColumnModel().getTotalColumnWidth();
            if (totalWidth < 0) {
                return null;
            } else {
                String totalWidthUnit = table.getColumnModel().getTotalColumnWidthUnit();
                if (totalWidthUnit == null) {// relative width
                    return null;
                } else {
                    if (needsSelectionRow && "px".equalsIgnoreCase(totalWidthUnit))
                        totalWidth += SELECTION_COLUMN_WIDTH;
                    return new SDimension(totalWidth+totalWidthUnit, null);
                }
            }
        }
    }

    /**
     * Tweak property.
     */
    public String getFixedTableBorderWidth() {
        return fixedTableBorderWidth;
    }

    /**
     * Tweak property.
     */
    public void setFixedTableBorderWidth(String fixedTableBorderWidth) {
        this.fixedTableBorderWidth = fixedTableBorderWidth;
    }

    public CSSSelector  mapSelector(SComponent addressedComponent, CSSSelector selector) {
        final String mappedSelector = getResolvedPseudoSelectorMapping(selector);
        if (mappedSelector != null) {
            String cssSelector = mappedSelector.replaceAll("#compid", CSSSelector.getSelectorString(addressedComponent));
            return new CSSSelector(cssSelector);
        } else {
            return selector;
        }
    }

    protected String getResolvedPseudoSelectorMapping(CSSSelector selector) {
        return (String) PSEUDO_SELECTOR_MAPPING.get(selector);
    }

    private static final Map PSEUDO_SELECTOR_MAPPING = new HashMap();
    static {
        PSEUDO_SELECTOR_MAPPING.put(STable.SELECTOR_HEADER, "#compid THEAD");
        PSEUDO_SELECTOR_MAPPING.put(STable.SELECTOR_NUMBERING_COLUMN, "#compid .numbering");
        PSEUDO_SELECTOR_MAPPING.put(STable.SELECTOR_EVEN_ROWS, "#compid .even");
        PSEUDO_SELECTOR_MAPPING.put(STable.SELECTOR_ODD_ROWS, "#compid .odd");
        PSEUDO_SELECTOR_MAPPING.put(STable.SELECTOR_SELECTION, "#compid TR[selected=\"true\"]"); //#compid TR.selected 
    }

}
