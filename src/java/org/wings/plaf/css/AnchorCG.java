/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;


import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.io.Device;

import java.io.IOException;

public class AnchorCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.AnchorCG {
    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SAnchor component = (SAnchor) _c;

        device.write("<a href=\"".getBytes());
        device.print(component.getURL());
        device.print("\"");

        Utils.innerPreferredSize(device, component.getPreferredSize());

        org.wings.plaf.Utils.optAttribute(device, "target", component.getTarget());
        org.wings.plaf.Utils.optAttribute(device, "name", component.getName());
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.writeEvents(device, component);
        device.print(">");
        Utils.renderContainer(device, component);
        device.print("</a>");
    }
}
