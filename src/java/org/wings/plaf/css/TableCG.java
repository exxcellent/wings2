// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Table.plaf'
/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
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

import java.awt.*;
import java.io.IOException;

public class TableCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.TableCG {

//--- byte array converted template snippets.
    private final static byte[] __table_1 = "</table>".getBytes();
    private final static byte[] ___3 = "\n".getBytes();
    private final static byte[] __align_left = " align=\"left\"".getBytes();
    private final static byte[] __align_center = " align=\"center\"".getBytes();
    private final static byte[] __align_right = " align=\"right\"".getBytes();
    private final static byte[] __valign_top = " valign=\"top\"".getBytes();
    private final static byte[] __valign_center = " valign=\"center\"".getBytes();
    private final static byte[] __align_bottom = " align=\"bottom\"".getBytes();
    private final static byte[] __th_1 = "</th>\n".getBytes();

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
            device.write("></td>".getBytes());
            return;
        }

        switch (component.getHorizontalAlignment()) {
            case LEFT_ALIGN:
                device.write(__align_left);
                break;
            case CENTER_ALIGN:
                device.write(__align_center);
                break;
            case RIGHT_ALIGN:
                device.write(__align_right);
                break;
        }

        switch (component.getVerticalAlignment()) {
            case TOP_ALIGN:
                device.write(__valign_top);
                break;
            case CENTER_ALIGN:
                device.write(__valign_center);
                break;
            case BOTTOM_ALIGN:
                device.write(__align_bottom);
                break;
        }
        device.write(">".getBytes());

        String parameter = null;
        if (table.isEditable() && !isEditingCell && table.isCellEditable(row, col))
            parameter = table.getEditParameter(row, col);
        else if (selectable)
            parameter = table.getToggleSelectionParameter(row, col);

        if (parameter != null && !isEditingCell) {
            if (showAsFormComponent) {
                device.print("<button type=\"submit\" name=\"");
                org.wings.plaf.Utils.write(device, Utils.event(table));
                device.print("\" value=\"");
                org.wings.plaf.Utils.write(device, parameter);
                device.print("\">");
            } else {
                RequestURL selectionAddr = table.getRequestURL();
                selectionAddr.addParameter(Utils.event(table), parameter);

                device.write("<a href=\"".getBytes());
                org.wings.plaf.Utils.write(device, selectionAddr.toString());
                device.print("\">");
            }
        } else
            device.write("<span>".getBytes());

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


    protected void writeHeaderCell(Device device, STable table,
                                   SCellRendererPane rendererPane,
                                   int c)
            throws IOException {
        SComponent comp = table.prepareHeaderRenderer(c);

        device.write("<th>".getBytes());
        rendererPane.writeComponent(device, comp, table);
        device.write(__th_1);
    }


    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        final STable component = (STable) _c;

        STable table = (STable) component;
        boolean childSelectorWorkaround = !component.getSession().getUserAgent().supportsChildSelector();

        SDimension intercellPadding = table.getIntercellPadding();
        SDimension intercellSpacing = table.getIntercellSpacing();

        device.write("<table".getBytes());
        Utils.innerPreferredSize(device, component.getPreferredSize());

        /* Tweaking: CG configured to have a fixed border="xy" width */
        org.wings.plaf.Utils.optAttribute(device, "border", fixedTableBorderWidth);
        org.wings.plaf.Utils.optAttribute(device, "cellspacing", ((intercellSpacing != null) ? intercellSpacing.width : null));
        org.wings.plaf.Utils.optAttribute(device, "cellpadding", ((intercellPadding != null) ? intercellPadding.width : null));
        device.write(">\n".getBytes());
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
            device.write("<tr>".getBytes());

            if (numbering)
                device.write("<th></th>".getBytes());

            for (int c = startCol; c < endCol; c++)
                writeHeaderCell(device, table, rendererPane, table.convertColumnIndexToModel(c));

            device.write("</tr>\n".getBytes());
        }

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
                    Utils.childSelectorWorkaround(device, "numbering");
                device.print(">");

                if (showAsFormComponent) {
                    device.print("<button type=\"submit\" name=\"");
                    org.wings.plaf.Utils.write(device, Utils.event(table));
                    device.print("\" value=\"");
                    org.wings.plaf.Utils.write(device, table.getToggleSelectionParameter(r, -1));
                    device.print("\">");
                    device.print(r);
                    device.print("</button>");
                } else {
                    RequestURL selectionAddr = table.getRequestURL();
                    selectionAddr.addParameter(org.wings.plaf.css.Utils.event(table),
                            table.getToggleSelectionParameter(r, -1));

                    device.write("<a href=\"".getBytes());
                    org.wings.plaf.Utils.write(device, selectionAddr.toString());
                    device.print("\">");
                    device.print(r);
                    device.print("</a>");
                }
                device.print("</td>");
            }

            for (int c = startCol; c < endCol; c++)
                writeCell(device, table, rendererPane, r, table.convertColumnIndexToModel(c));

            device.write("</tr>\n".getBytes());
        }
        device.write(__table_1);
        device.write(___3);
    }

    public String getFixedTableBorderWidth() {
        return fixedTableBorderWidth;
    }

    public void setFixedTableBorderWidth(String fixedTableBorderWidth) {
        this.fixedTableBorderWidth = fixedTableBorderWidth;
    }
}
