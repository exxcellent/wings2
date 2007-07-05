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
import org.wings.SDesktopPane;
import org.wings.SDimension;
import org.wings.SInternalFrame;
import org.wings.io.Device;

import java.io.IOException;

public final class DesktopPaneCG extends AbstractComponentCG implements org.wings.plaf.DesktopPaneCG {
    private static final long serialVersionUID = 1L;

    public void installCG(SComponent component) {
        super.installCG(component);
        component.setPreferredSize(SDimension.FULLWIDTH);
    }

    public void writeInternal(final Device device, final SComponent _c)
            throws IOException {
        SDesktopPane desktop = (SDesktopPane) _c;

        writeDivPrefix(device, desktop);
        // is one window maximized? if yes, skip rendering of other windows
        boolean maximized = false;

        device.print("<div class=\"spacer\"></div>");
        int componentCount = desktop.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
            if (!frame.isClosed() && frame.isMaximized()) {
                frame.write(device);
                maximized = true;
            }
        }

        if (!maximized) {
            for (int i = 0; i < componentCount; i++) {
                SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
                if (!frame.isClosed()) {
                    frame.write(device);
                }
            }
        }
        device.print("<div class=\"spacer\"></div>");

        writeDivSuffix(device, desktop);
    }
}
