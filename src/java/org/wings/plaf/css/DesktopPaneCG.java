// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/DesktopPane.plaf'
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

import java.io.IOException;

public class DesktopPaneCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.DesktopPaneCG
{
    public void installCG(SComponent component) {
        super.installCG(component);
        component.setPreferredSize(new SDimension("100%", null));
    }

    public void writeContent(final Device device, final SComponent _c)
            throws IOException
    {
        final SDesktopPane component = (SDesktopPane) _c;
        SDesktopPane desktop = (SDesktopPane) component;

        device.print("<table");
        Utils.printInnerPreferredSize(device, component.getPreferredSize());
        device.print(">\n");

        int componentCount = desktop.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
            if (!frame.isClosed() && frame.isMaximized()) {
                device.print("<tr><td>");
                frame.write(device);
                device.print("</td></tr></table>");
                return;
            }
        }

        for (int i = 0; i < componentCount; i++) {
            SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
            if (!frame.isClosed()) {
                device.print("<tr><td>");
                frame.write(device);
                device.print("</td></tr>");
            }
        }

        device.print("</table>\n");
    }
}
