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


import java.io.IOException;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDesktopPane;
import org.wings.SDimension;
import org.wings.SInternalFrame;
import org.wings.io.Device;

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
        // is one window maximized? if yes, skip rendering of other windows
        boolean maximized = false;
        
        device.print("<div class=\"spacer\">&nbsp;</div>");
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
                if (!frame.isClosed() && !frame.isIconified()) {
                    frame.write(device);
                }
            }
        }
        device.print("<div class=\"spacer\">&nbsp;</div>");
        if (!maximized) {
            for (int i = 0; i < componentCount; i++) {
                SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
                if (!frame.isClosed() && frame.isIconified()) {
                    frame.write(device);
                }
            }
        }
        device.print("<div class=\"spacer\">&nbsp;</div>");
        
    }
}
