// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/ComboBox.plaf'
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
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.io.IOException;

public class ComboBoxCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.ComboBoxCG {

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

//--- code from common area in template.
    private static final JavaScriptListener submitListener = new JavaScriptListener(JavaScriptEvent.ON_CHANGE, "submit()");

    protected void writeFormComboBox(Device device, SComboBox component) throws IOException {

        device.print("<select size=\"1\"");
        org.wings.plaf.Utils.optAttribute(device, "name", Utils.event(component));
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());

        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());

        if (!component.isEnabled())
            device.print(" disabled=\"true\"");
        if (component.isFocusOwner())
            org.wings.plaf.Utils.optAttribute(device, "focus", component.getName());


        component.removeScriptListener(submitListener);
        if (component.getActionListeners().length > 0 || component.getItemListener().length > 0 ) {
            component.addScriptListener(submitListener);
        }

        Utils.writeEvents(device, component);

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


            device.print("\n<option");
            org.wings.plaf.Utils.optAttribute(device, "value", component.getSelectionParameter(i));
            if (selected == i) {

                device.print(" selected=\"selected\"");
            }

            if (cellRenderer != null) {
                if (cellRenderer.getToolTipText() != null) {

                    device.print(" title=\"");
                    org.wings.plaf.Utils.write(device, cellRenderer.getToolTipText());

                    device.print("\"");
                }

                org.wings.io.StringBufferDevice stringBufferDevice = getStringBufferDevice();
                Utils.printCSSInlineStyleAttributes(stringBufferDevice, cellRenderer);
                String styleString = stringBufferDevice.toString();
                if (styleString != null && styleString.length() > 0) {

                    device.print(" style=\"");
                    org.wings.plaf.Utils.write(device, styleString);

                    device.print("\"");
                }
            }

            device.print(">");
            if (cellRenderer != null) {
                // Hack: remove all tags, because in form selections, looks ugly.
                org.wings.io.StringBufferDevice string = getStringBufferDevice();
                cellRenderer.write(string);
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

                device.print("<!--cellrenderer==null, use toString-->");
                device.print(model.getElementAt(i).toString());
            }

            device.print("</option>");
        }


        device.print("\n</select>");
        // util method

        device.print("<input type=\"hidden\"");
        org.wings.plaf.Utils.optAttribute(device, "name", Utils.event(component));
        org.wings.plaf.Utils.optAttribute(device, "value", -1);

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


//--- end code from common area in template.


    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SComboBox component = (SComboBox) _c;

//--- code from write-template.
        SComboBox comboBox = (SComboBox) component;
        // TODO: implement anchor combobox
        //if (comboBox.getShowAsFormComponent()) {
        writeFormComboBox(device, comboBox);
        //} else {
        //    writeAnchorComboBox(device, comboBox);
        // }
        device.print("\n");

//--- end code from write-template.
    }
}
