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


import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.SSpacer;
import org.wings.io.Device;

import java.io.IOException;

public class SpacerCG extends AbstractComponentCG implements org.wings.plaf.SpacerCG {
    /**
     * The default invisible icon.
     */
    public static final SIcon INVISIBLE_ICON = new SResourceIcon("org/wings/icons/blind.gif");

    public void writeContent(final Device device, final SComponent c) throws IOException {
        final SSpacer component = (SSpacer) c;
        int height = component.getPreferredSize().getIntHeight();
        int width = component.getPreferredSize().getIntWidth();
        device.print("<img src=\"");
        device.print(INVISIBLE_ICON.getURL());
        device.print("\" height=\"");
        device.print(height >= 0 ? height : 0);
        device.print("\" width=\"");
        device.print(width >= 0 ? width : 0);
        device.print("\"/>");
    }
}
