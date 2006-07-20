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
import org.wings.SScrollPane;
import org.wings.SScrollPaneLayout;
import org.wings.Scrollable;
import org.wings.io.Device;

import java.awt.*;
import java.io.IOException;

public class ScrollPaneCG extends org.wings.plaf.css.AbstractComponentCG implements org.wings.plaf.ScrollPaneCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(Device device, SComponent component) throws IOException {
        SScrollPane scrollPane = (SScrollPane) component;
        Scrollable scrollable = scrollPane.getScrollable();
        SScrollPaneLayout layout = (SScrollPaneLayout) scrollPane.getLayout();

        if (!layout.isPaging() && scrollable instanceof SComponent) {
            SComponent center = (SComponent) scrollable;
            Rectangle viewportSizeBackup = scrollable.getViewportSize();
            SDimension preferredSizeBackup = center.getPreferredSize();
            try {
                scrollable.setViewportSize(scrollable.getScrollableViewportSize());
                writeContent(device, component);
                device.print("<script type=\"text/javascript\">\n" +
                    "    var outer = document.getElementById(\"" + component.getName() + "\");\n" +
                    "    var div = outer.getElementsByTagName(\"div\")[0];\n" +
                    "    div.style.height = document.defaultView.getComputedStyle(outer, null).getPropertyValue(\"height\");\n" +
                    "    div.style.display = \"block\";" +
                    "</script>");
            } finally {
                //component.setPreferredSize(center.getPreferredSize());
                scrollable.setViewportSize(viewportSizeBackup);
                center.setPreferredSize(preferredSizeBackup);
            }
        }
        else {
            scrollPane.synchronizeAdjustables();
            writeContent(device, component);
        }
    }

    public void writeContent(Device device, SComponent c)
            throws IOException {
        SScrollPane scrollPane = (SScrollPane) c;
        device.print("<table");
        writeAllAttributes(device, scrollPane);
        device.print(">");
        Utils.renderContainer(device, scrollPane);
        device.print("</table>");
    }
}
