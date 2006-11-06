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

import java.io.IOException;

import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SScrollPane;
import org.wings.io.Device;

public class ScrollPaneCG extends org.wings.plaf.css.AbstractComponentCG implements org.wings.plaf.ScrollPaneCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(Device device, SComponent component) throws IOException {
        SScrollPane scrollpane = (SScrollPane) component;

        if (scrollpane.getMode() == SScrollPane.MODE_COMPLETE) {
            SDimension preferredSize = scrollpane.getPreferredSize();
            if (preferredSize == null) {
                scrollpane.setPreferredSize(new SDimension(200, 400));
            } else {
                if (preferredSize.getWidthInt() < 0) preferredSize.setWidth(200);
                if (preferredSize.getHeightInt() < 0) preferredSize.setHeight(400);
            }

            writeContent(device, component);
            String function = "layoutScrollPane(\"" + component.getName() + "\")";
            device.print("<script type=\"text/javascript\">addWindowOnLoadFunction('" + function + "');</script>\n");
        } else {
            writeContent(device, component);
        }
    }

    public void writeContent(Device device, SComponent c) throws IOException {
        SScrollPane scrollPane = (SScrollPane) c;
        device.print("<table");
        writeAllAttributes(device, scrollPane);
        device.print(">");
        Utils.renderContainer(device, scrollPane);
        device.print("</table>");
    }
}
