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
        Object value = manager.getObject("SProgressBar.borderColor", java.awt.Color.class);
        if (value != null) {
            component.setBorderColor((java.awt.Color) value);
        }
        value = manager.getObject("SProgressBar.filledColor", java.awt.Color.class);
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

        /* FIXME: The problem here is that the component size is used as the
         * size for the progressbar. If text is rendered below
         * (isStringPainted), then that text is out of the component box. So
         * either create a distinct ProgressBar size or subtract some height.
         * OL: created distinct height. other solution is removing string 
         * completely.
         */
        final SDimension size = component.getProgressBarDimension();
        final int minSize = component.isBorderPainted() ? 3 : 1;
        int width = (size != null && size.getWidthInt() >= minSize) ? size.getWidthInt() : 200;
        int height = (size != null && size.getHeightInt() >= minSize) ? size.getHeightInt() : 10;

        if (component.isBorderPainted()) {
            if (isMSIE(component)) {
                // compensation for whole component as MSIE uses the correct CSS
                // border mox model. Hence border is rendered AROUND the content size
                width -= 2;
                height -= 2;
            }
            device.print("<div style=\"width:").print(width).print("px;height:");
            device.print(height).print("px;border:1px solid ");
            Utils.write(device, component.getBorderColor());
            device.print(";\">");
            if (isMSIE(component) == false) {
                // compensation for inner component to border only
                // for border  box mode used by gecko (and formerly MSIE).
                width -= 2;
                height -= 2;
            }
        }

        device.print("<table><tr style=\"height:").print(height).print("px\"\">");

        // Part with completed bar
        final int completedWidth = (int) Math.round(width * component.getPercentComplete());
        device.print("<td");
        if (component.getFilledColor() != null) {
            device.print(" style=\"background-color: ");
            Utils.write(device, component.getFilledColor());
            device.print(";\"");
        }
        device.print(">");
        printSpacerIcon(device, completedWidth, height);
        device.print("</td>");

        // Part with remaining, incompleted bar
        final int incompleteWidth = width - completedWidth;
        device.print("<td");
        if (component.getUnfilledColor() != null) {
            device.print(" style=\"background-color: ");
            Utils.write(device, component.getUnfilledColor());
           device.print(";\"");
        }
        device.print(">");
        printSpacerIcon(device, incompleteWidth, height);
        device.print("</td></tr></table>");
        if (component.isBorderPainted()) {
            device.print("</div>");
        }

        if (component.isStringPainted()) {
            device.print("<div style=\"width: 100%; text-align: center;\"");
            Utils.optAttribute(device, "class", Utils.appendSuffixesToWords(component.getStyle(), "_string"));
            device.print(">");
            Utils.write(device, component.getString());
            device.print("</div>");
        }
    }

    private void printSpacerIcon(final Device device, final int width, int height) throws IOException {
        device.print("<img");
        Utils.optAttribute(device, "src", getBlindIcon().getURL());
        Utils.optAttribute(device, "width", width);
        Utils.optAttribute(device, "height", String.valueOf(height));
        Utils.optAttribute(device, "class", "spacer");
        Utils.emptyAttribute(device, "alt");
        device.print("/>");
    }
}
