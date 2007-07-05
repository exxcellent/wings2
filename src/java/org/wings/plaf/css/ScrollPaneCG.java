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

import java.io.IOException;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.css.script.LayoutFillScript;
import org.wings.plaf.css.script.LayoutFixScript;
import org.wings.plaf.css.script.LayoutScrollPaneScript;
import org.wings.session.ScriptManager;

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

            ScriptManager.getInstance().addScriptListener(new LayoutScrollPaneScript(component.getName()));
            writeContent(device, component);
        } else {
            writeContent(device, component);
        }
    }

    public void writeContent(Device device, SComponent c) throws IOException {
        SScrollPane scrollPane = (SScrollPane) c;

        SDimension preferredSize = scrollPane.getPreferredSize();
        String height = preferredSize != null ? preferredSize.getHeight() : null;
        boolean clientLayout = isMSIE(scrollPane) && height != null && !"auto".equals(height)
            && scrollPane.getMode() != SScrollPane.MODE_COMPLETE;
        boolean clientFix = isMSIE(scrollPane) && (height == null || "auto".equals(height))
            && scrollPane.getMode() != SScrollPane.MODE_COMPLETE;

        device.print("<table");

        if (clientLayout) {
            Utils.optAttribute(device, "layoutHeight", height);
            preferredSize.setHeight(null);
        }

        writeAllAttributes(device, scrollPane);

        if (clientLayout) {
            preferredSize.setHeight(height);
            scrollPane.getSession().getScriptManager().addScriptListener(new LayoutFillScript(scrollPane.getName()));
        }
        else if (clientFix)
            scrollPane.getSession().getScriptManager().addScriptListener(new LayoutFixScript(scrollPane.getName()));

        device.print(">");

        Utils.renderContainer(device, scrollPane);
        device.print("</table>");
    }
}
