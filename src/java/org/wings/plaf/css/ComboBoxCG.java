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


import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SDefaultListCellRenderer;
import org.wings.SListCellRenderer;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import java.io.IOException;

public final class ComboBoxCG extends AbstractComponentCG implements org.wings.plaf.ComboBoxCG {

    private static final long serialVersionUID = 1L;

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final SComboBox component = (SComboBox) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SComboBox.renderer", SDefaultListCellRenderer.class);
        if (value != null) {
            component.setRenderer((SDefaultListCellRenderer) value);
        }
    }


    protected void writeFormComboBox(Device device, SComboBox component) throws IOException {
        device.print("<select size=\"1\"");
        writeAllAttributes(device, component);
        Utils.optAttribute(device, "name", Utils.event(component));
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.writeEvents(device, component, null);
        if (!component.isEnabled())
            device.print(" disabled=\"true\"");
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        device.print(">");

        javax.swing.ComboBoxModel model = component.getModel();
        int size = model.getSize();
        int selected = component.getSelectedIndex();

        SListCellRenderer renderer = component.getRenderer();

        for (int i = 0; i < size; i++) {
            SComponent cellRenderer = null;
            if (renderer != null) {
                cellRenderer = renderer.getListCellRendererComponent(component, model.getElementAt(i), false, i);
            } else {
                device.print("<!--renderer==null-->");
            }


            Utils.printNewline(device, component);
            device.print("<option");
            Utils.optAttribute(device, "value", component.getSelectionParameter(i));
            if (selected == i) {
                device.print(" selected=\"selected\"");
            }

            if (cellRenderer != null) {
                Utils.optAttribute(device, "title", cellRenderer.getToolTipText());
                SStringBuilder buffer = Utils.generateCSSComponentInlineStyle(cellRenderer);
                Utils.optAttribute(device, "style", buffer.toString());
            }

            device.print(">"); //option

            if (cellRenderer != null) {
                // Hack: remove all tags, because in form selections, looks ugly.
                org.wings.io.StringBuilderDevice string = getStringBuilderDevice();
                cellRenderer.write(string);
                char[] chars = string.toString().replace('\n',' ').trim().toCharArray();
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
                device.print("<!--cellrenderer==null, use toString-->");
                device.print(model.getElementAt(i).toString());
            }

            device.print("</option>");
        }

        Utils.printNewline(device, component);
        device.print("</select>");
        // util method

        device.print("<input type=\"hidden\"");
        Utils.optAttribute(device, "name", Utils.event(component));
        Utils.optAttribute(device, "value", -1);
        device.print("/>");
    }

    private org.wings.io.StringBuilderDevice
            stringBuilderDevice = null;

    protected org.wings.io.StringBuilderDevice getStringBuilderDevice() {
        if (stringBuilderDevice == null) {
            stringBuilderDevice = new org.wings.io.StringBuilderDevice();
        }
        stringBuilderDevice.reset();
        return stringBuilderDevice;
    }



    public void writeInternal(final Device device, final SComponent _c) throws IOException {
        Utils.getRenderHelper(_c).forbidCaching();

        try {
            final SComboBox component = (SComboBox) _c;

            final SComboBox comboBox = (SComboBox) component;
            // TODO: implement anchor combobox
            //if (comboBox.getShowAsFormComponent()) {
            writeFormComboBox(device, comboBox);
            //} else {
            //    writeAnchorComboBox(device, comboBox);
            // }
        }
        finally {
            Utils.getRenderHelper(_c).allowCaching();
        }
    }
}
