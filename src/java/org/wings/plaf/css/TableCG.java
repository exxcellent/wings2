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


import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;
import org.wings.table.STableCellRenderer;
import org.wings.table.SDefaultTableCellRenderer;

import java.awt.*;
import java.io.IOException;

public class TableCG extends AbstractComponentCG implements
        org.wings.plaf.TableCG {

    private String fixedTableBorderWidth;

    /**
     * Initialize properties from config
     */
    public TableCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        setFixedTableBorderWidth((String) manager.getObject("TableCG.fixedTableBorderWidth", String.class));
    }


    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final STable component = (STable) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("STable.defaultRenderer", STableCellRenderer.class);
        if (value != null) {
            component.setDefaultRenderer((STableCellRenderer) value);
            if (value instanceof SDefaultTableCellRenderer) {
                SDefaultTableCellRenderer cellRenderer = (SDefaultTableCellRenderer) value;
                cellRenderer.setEditIcon(manager.getIcon("TableCG.editIcon"));
            }
        }
        value = manager.getObject("STable.headerRenderer", STableCellRenderer.class);
        if (value != null) {
            component.setHeaderRenderer((STableCellRenderer) value);
        }
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

        if (isEditingCell)
            component = table.getEditorComponent();
        else
            component = table.prepareRenderer(table.getCellRenderer(row, col), row, col);

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

        if (parameter != null && !isEditingCell) {
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

        if (parameter != null && !isEditingCell) {
            if (showAsFormComponent)
                device.print("</button>");
            else
                device.print("</a>");
        } else
            device.print("</span>");

        device.print("</td>\n");
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
        device.print("</th>\n");
    }


    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        final STable component = (STable) _c;

        STable table = (STable) component;
        boolean childSelectorWorkaround = !component.getSession().getUserAgent().supportsCssChildSelector();

        SDimension intercellPadding = table.getIntercellPadding();
        SDimension intercellSpacing = table.getIntercellSpacing();

        device.print("<table");
        Utils.printCSSInlineFullSize(device, component.getPreferredSize());

        // TODO: border="" should be obsolete
        // TODO: cellspacing and cellpadding may be in conflict with border-collapse
        /* Tweaking: CG configured to have a fixed border="xy" width */
        Utils.optAttribute(device, "border", fixedTableBorderWidth);
        Utils.optAttribute(device, "cellspacing", ((intercellSpacing != null) ? ""+intercellSpacing.getIntWidth() : null));
        Utils.optAttribute(device, "cellpadding", ((intercellPadding != null) ? ""+intercellPadding.getIntHeight() : null));
        device.print(">\n");
        /*
        * get viewable area
        */
        int startRow = 0;
        int startCol = 0;
        int endRow = table.getRowCount();
        int endCol = table.getColumnCount();
        Rectangle viewport = table.getViewportSize();
        if (viewport != null) {
            startRow = viewport.y;
            startCol = viewport.x;
            endRow = Math.min(startRow + viewport.height, endRow);
            endCol = Math.min(startCol + viewport.width, endCol);
        }

        SCellRendererPane rendererPane = table.getCellRendererPane();

        /*
        * render the header 
        */
        SListSelectionModel selectionModel = table.getSelectionModel();
        boolean numbering = selectionModel.getSelectionMode() != SListSelectionModel.NO_SELECTION && table.isEditable();
        boolean showAsFormComponent = table.getShowAsFormComponent();

        if (table.isHeaderVisible()) {
            device.print("<thead><tr>\n");

            if (numbering)
                device.print("<th></th>");

            for (int c = startCol; c < endCol; c++)
                writeHeaderCell(device, table, rendererPane, table.convertColumnIndexToModel(c));

            device.print("</tr></thead>\n");
        }

        device.print("<tbody>\n");
        for (int r = startRow; r < endRow; r++) {
            device.print("<tr");
            if (selectionModel.isSelectedIndex(r))
                device.print(" selected=\"true\"");
            if (r % 2 != 0)
                device.print(" odd=\"true\">");
            else
                device.print(" even=\"true\">");

            if (numbering) {
                device.print("<td col=\"numbering\"");
                if (childSelectorWorkaround)
                    Utils.optAttribute(device, "class", "numbering");
                device.print(">");

                if (showAsFormComponent) {
                    writeButtonStart(device, table, table.getToggleSelectionParameter(r, -1));
                    device.print(" type=\"submit\" name=\"");
                    Utils.write(device, Utils.event(table));
                    device.print(SConstants.UID_DIVIDER);
                    Utils.write(device, table.getToggleSelectionParameter(r, -1));
                    device.print("\" value=\"\">");
                    device.print(r);
                    device.print("</button>");
                } else {
                    RequestURL selectionAddr = table.getRequestURL();
                    selectionAddr.addParameter(Utils.event(table),
                            table.getToggleSelectionParameter(r, -1));

                    device.print("<a href=\"");
                    Utils.write(device, selectionAddr.toString());
                    device.print("\">");
                    device.print(r);
                    device.print("</a>");
                }
                device.print("</td>");
            }

            for (int c = startCol; c < endCol; c++)
                writeCell(device, table, rendererPane, r, table.convertColumnIndexToModel(c));

            device.print("</tr>\n");
        }
        device.print("</tbody>\n");
        device.print("</table>\n");
    }

    public String getFixedTableBorderWidth() {
        return fixedTableBorderWidth;
    }

    public void setFixedTableBorderWidth(String fixedTableBorderWidth) {
        this.fixedTableBorderWidth = fixedTableBorderWidth;
    }
}
