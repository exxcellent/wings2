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
import org.wings.SConstants;
import org.wings.SToolbar;
import org.wings.io.Device;

import java.io.IOException;

public class ToolbarCG extends AbstractComponentCG implements
        org.wings.plaf.ToolbarCG {
    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        final SToolbar toolbar = (SToolbar) _c;

        device.print("<table><tr>");
        SComponent[] components = toolbar.getComponents();
        for (int i = 0; i < components.length; i++) {
            SComponent component = components[i];

            if (component.getHorizontalAlignment() == SConstants.RIGHT_ALIGN)
                device.print("<td width=\"100%\"></td>");

            device.print("<td>");
            component.write(device);
            device.print("</td>");
        }
        device.print("</tr></table>");
    }
}
