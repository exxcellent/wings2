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
import org.wings.SSpacer;
import org.wings.io.Device;
import java.io.IOException;

public class SpacerCG extends AbstractComponentCG implements org.wings.plaf.SpacerCG {

    public void writeContent(final Device device, final SComponent c) throws IOException {
        final SSpacer component = (SSpacer) c;
        int height = component.getPreferredSize().getHeightInt();
        int width = component.getPreferredSize().getWidthInt();
        device.print("<img");
        Utils.optAttribute(device, "src", getBlindIcon().getURL());
        Utils.optAttribute(device, "width", width);
        Utils.optAttribute(device, "height", height);
        device.print(" alt=\"");
        device.print(getBlindIcon().getIconTitle());
        device.print("\"/>");
    }
}
