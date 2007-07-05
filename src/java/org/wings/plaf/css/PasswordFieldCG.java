/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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

import java.io.IOException;

public final class PasswordFieldCG extends AbstractComponentCG implements
        org.wings.plaf.PasswordFieldCG {

    private static final long serialVersionUID = 1L;

    int horizontalOversize = 4;

    public int getHorizontalOversize() {
        return horizontalOversize;
    }

    public void setHorizontalOversize(int horizontalOversize) {
        this.horizontalOversize = horizontalOversize;
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        if (isMSIE(component))
            component.putClientProperty("horizontalOversize", new Integer(horizontalOversize));
    }

    public void writeInternal(final Device device,
                      final SComponent _c)
            throws IOException {
        final SPasswordField component = (SPasswordField) _c;
        
        SDimension preferredSize = component.getPreferredSize();
        boolean tableWrapping = Utils.isMSIE(component) && preferredSize != null && "%".equals(preferredSize.getWidthUnit());
        String actualWidth = null;
        if (tableWrapping) {
            actualWidth = preferredSize.getWidth();
            preferredSize.setWidth("100%");
            device.print("<table style=\"table-layout: fixed; width: " + actualWidth + "\"><tr>");
            device.print("<td style=\"padding-right: " + Utils.calculateHorizontalOversize(component, true) + "px\">");
        }

        device.print("<input type=\"password\"");
        writeAllAttributes(device, component);
        if (tableWrapping)
            device.print(" wrapping=\"4\"");

        Utils.optAttribute(device, "size", component.getColumns());
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.optAttribute(device, "maxlength", component.getMaxColumns());
        Utils.writeEvents(device, component, null);
        if (component.isFocusOwner())
            Utils.optAttribute(device, "foc", component.getName());


        if (!component.isEditable() || !component.isEnabled()) {
            device.print(" readonly=\"true\"");
        }
        if (component.isEnabled()) {
            device.print(" name=\"");
            Utils.write(device, Utils.event(component));
            device.print("\"");
        } else {
            device.print(" disabled=\"true\"");
        }

        Utils.optAttribute(device, "value", component.getText());
        device.print("/>");
        if (tableWrapping) {
            preferredSize.setWidth(actualWidth);
            device.print("</td></tr></table>");
        }
    }
}
