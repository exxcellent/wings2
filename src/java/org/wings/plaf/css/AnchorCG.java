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

import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.io.Device;

public class AnchorCG
        extends AbstractComponentCG
        implements org.wings.plaf.AnchorCG {
    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SAnchor component = (SAnchor) _c;

        device.print("<a href=\"");
        device.print(component.getURL());
        device.print("\"");

        Utils.printCSSInlineFullSize(device, _c.getPreferredSize());
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        Utils.optAttribute(device, "target", component.getTarget());
        Utils.optAttribute(device, "name", component.getName());
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.writeEvents(device, component);
        device.print(">");
        Utils.renderContainer(device, component);
        device.print("</a>");
    }
}
