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


import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SProgressBar;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import java.io.IOException;

public final class ProgressBarCG extends AbstractComponentCG implements  org.wings.plaf.ProgressBarCG {

    private static final long serialVersionUID = 1L;

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final SProgressBar component = (SProgressBar) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value = manager.getObject("SProgressBar.filledColor", java.awt.Color.class);
        if (value != null) {
            component.setFilledColor((java.awt.Color) value);
        }
        value = manager.getObject("SProgressBar.foreground", java.awt.Color.class);
        if (value != null) {
            component.setForeground((java.awt.Color) value);
        }
        value = manager.getObject("SProgressBar.preferredSize", SDimension.class);
        if (value != null) {
            component.setPreferredSize((SDimension) value);
        }
        value = manager.getObject("SProgressBar.unfilledColor", java.awt.Color.class);
        if (value != null) {
            component.setUnfilledColor((java.awt.Color) value);
        }
    }

    public void writeInternal(final Device device, final SComponent pComp) throws IOException {
        final SProgressBar component = (SProgressBar) pComp;

        final SDimension size = component.getProgressBarDimension();
        int height = size != null ? size.getHeightInt() : 10;

        device.print("<table");
        writeAllAttributes(device, component);
        device.print("><tr class=\"bar\"");
        Utils.optAttribute(device, "height", height);
        device.print(">");

        final int filledWidth = (int) Math.round(100 * component.getPercentComplete());
        if (filledWidth > 0) {
            device.print("<td");
            Utils.optAttribute(device, "width", filledWidth + "%");
            Utils.optAttribute(device, "bgcolor", component.getFilledColor());
            device.print("></td>");
        }

        final int unfilledWidth = 100 - filledWidth;
        if (unfilledWidth > 0) {
            device.print("<td");
            Utils.optAttribute(device, "width", unfilledWidth + "%");
            Utils.optAttribute(device, "bgcolor", component.getUnfilledColor());
            device.print("></td>");
        }

        device.print("</tr>");

        if (component.isStringPainted()) {
            if (filledWidth != 0 && unfilledWidth != 0)
                device.print("<tr class=\"text\"><td colspan=\"2\">");
            else
                device.print("<tr class=\"text\"><td>");

            Utils.write(device, component.getString());
            device.print("</td></tr>");
        }
        device.print("</table>");
    }
}
