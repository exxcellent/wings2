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
import org.wings.io.SStringBuilder;
import org.wings.plaf.CGManager;

import java.io.IOException;

public class ListCG extends AbstractComponentCG implements
        org.wings.plaf.ListCG {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final SList component = (SList) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SList.cellRenderer", SDefaultListCellRenderer.class);
        if (value != null) {
            component.setCellRenderer((SDefaultListCellRenderer) value);
        }
    }

    protected void writeFormList(Device device, SList list) throws IOException {

        device.print("<select");
        Utils.optAttribute(device, "name", Utils.event(list));
        Utils.optAttribute(device, "tabindex", list.getFocusTraversalIndex());
        Utils.optAttribute(device, "size", list.getVisibleRowCount());
        Utils.optAttribute(device, "multiple", (list.getSelectionMode() == SList.MULTIPLE_SELECTION) ? "multiple" : null);

        if (!list.isEnabled())
            device.print(" disabled=\"true\"");
        if (list.isFocusOwner())
            Utils.optAttribute(device, "focus", list.getName());

        Utils.printCSSInlineFullSize(device, list.getPreferredSize());
        Utils.writeEvents(device, list);

        device.print(">");
        javax.swing.ListModel model = list.getModel();
        int size = model.getSize();

        SListCellRenderer cellRenderer = list.getCellRenderer();

        for (int i = 0; i < size; i++) {
            SComponent renderer = null;
            if (cellRenderer != null) {
                renderer = cellRenderer.getListCellRendererComponent(list, model.getElementAt(i), false, i);
            }

            device.print("\n<option");
            Utils.optAttribute(device, "value", list.getSelectionParameter(i));
            if (list.isSelectedIndex(i)) {
                device.print(" selected=\"selected\"");
            }

            org.wings.io.StringBufferDevice stringBufferDevice = getStringBufferDevice();

            SStringBuilder buffer = Utils.generateCSSComponentInlineStyle(renderer);
            Utils.optAttribute(device, "style", buffer.toString());
            device.print(">");

            if (renderer != null) {
                // Hack: remove all tags, because in form selections, looks ugly.
                org.wings.io.StringBufferDevice string = getStringBufferDevice();
                renderer.write(string);
                char[] chars = string.toString().toCharArray();
                int pos = 0;
                for (int c = 0; c < chars.length; c++) {
                    switch (chars[c]) {
                        case '<':
                            device.print(chars, pos, c - pos);
                            break;
                        case '>':
                            pos = c + 1;
                    }
                }
                device.print(chars, pos, chars.length - pos);
            } else {
                device.print(model.getElementAt(i).toString());
            }

            device.print("</option>");
        }

        device.print("</select>");
        device.print("<input type=\"hidden\"");
        Utils.optAttribute(device, "name", Utils.event(list));
        Utils.optAttribute(device, "value", -1);
        device.print("/>");
    }

    private org.wings.io.StringBufferDevice stringBufferDevice = null;

    protected org.wings.io.StringBufferDevice getStringBufferDevice() {
        if (stringBufferDevice == null) {
            stringBufferDevice = new org.wings.io.StringBufferDevice();
        }
        stringBufferDevice.reset();
        return stringBufferDevice;
    }

    public void writeAnchorList(Device device, SList list)
            throws IOException {
        boolean showAsFormComponent = list.getShowAsFormComponent();
        boolean renderSelection = list.getSelectionMode() != SList.NO_SELECTION;

        device.print("<");
        device.print(list.getType());
        Utils.optAttribute(device, "type", list.getOrderType());
        Utils.optAttribute(device, "start", list.getStart());

        Utils.printCSSInlineFullSize(device, list.getPreferredSize());
        device.print(">");

        javax.swing.ListModel model = list.getModel();
        SListCellRenderer cellRenderer = list.getCellRenderer();
        SCellRendererPane rendererPane = list.getCellRendererPane();

        int start = 0;
        int end = model.getSize();

        java.awt.Rectangle viewport = list.getViewportSize();
        if (viewport != null) {
            start = viewport.y;
            end = start + viewport.height;
        }

        for (int i = start; i < end; i++) {
            boolean selected = list.isSelectedIndex(i);

            if (renderSelection && selected)
                device.print("<li class=\"SList_selected\"");
            else
                device.print("<li>");

            SComponent renderer = cellRenderer.getListCellRendererComponent(list, model.getElementAt(i), selected, i);

            if (renderSelection) {
                Utils.printButtonStart(device, list, list.getToggleSelectionParameter(i));
                Utils.optAttribute(device, "focus", renderer.getName());
                device.print(">");
            } else
                device.print("<span>");

            rendererPane.writeComponent(device, renderer, list);

            if (renderSelection) {
                Utils.printButtonEnd(device);
            } else
                device.print("</span>");

            device.print("</li>\n");
        }

        device.print("</");
        Utils.write(device, list.getType());
        device.print(">");
    }

    public void write(final Device device,
                      final SComponent _c)
            throws IOException {
        final SList component = (SList) _c;

        SList list = (SList) component;
        if (list.getShowAsFormComponent()) {
            writeFormList(device, list);
        } else {
            writeAnchorList(device, list);
        }
    }
}
