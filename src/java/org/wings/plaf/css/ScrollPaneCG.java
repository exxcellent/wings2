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

import java.awt.Rectangle;
import java.io.IOException;

import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SScrollPane;
import org.wings.SScrollPaneLayout;
import org.wings.Scrollable;
import org.wings.io.Device;

public class ScrollPaneCG extends org.wings.plaf.css.AbstractComponentCG implements org.wings.plaf.ScrollPaneCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(Device device, SComponent component) throws IOException {
        SScrollPane scrollPane = (SScrollPane) component;
        Scrollable scrollable = scrollPane.getScrollable();
        SScrollPaneLayout layout = (SScrollPaneLayout) scrollPane.getLayout();

        if (!layout.isPaging() && scrollable instanceof SComponent) {
            SComponent center = (SComponent) scrollable;
            Rectangle viewportSize = scrollable.getViewportSize();
            SDimension preferredSize = center.getPreferredSize();
            try {
                scrollable.setViewportSize(scrollable.getScrollableViewportSize());
                center.setPreferredSize(component.getPreferredSize());
                component.setPreferredSize(null);
                writeContent(device, component);
            } finally {
                component.setPreferredSize(center.getPreferredSize());
                scrollable.setViewportSize(viewportSize);
                center.setPreferredSize(preferredSize);
            }
        } else
            writeContent(device, component);
    }

    public void writeContent(Device device, SComponent c)
            throws IOException {
        SScrollPane scrollPane = (SScrollPane) c;
        scrollPane.synchronizeAdjustables();
        device.print("<table");
        writeAllAttributes(device, scrollPane);
        device.print(">");
        Utils.renderContainer(device, scrollPane);
        device.print("</table>");
    }
}


