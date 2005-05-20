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
import org.wings.SIcon;
import org.wings.SSpacer;
import org.wings.io.Device;
import org.wings.session.SessionManager;

import java.io.IOException;

public class SpacerCG extends AbstractComponentCG implements org.wings.plaf.SpacerCG {
    /**
     * The default invisible icon.
     */
    private static final SIcon BLIND_ICON = (SIcon) SessionManager.getSession()
    .getCGManager().getObject("SpacerCG.blindIcon", SIcon.class);

    public void writeContent(final Device device, final SComponent c) throws IOException {
        final SSpacer component = (SSpacer) c;
        int height = component.getPreferredSize().getIntHeight();
        int width = component.getPreferredSize().getIntWidth();
        device.print("<img src=\"");
        device.print(BLIND_ICON.getURL());
        device.print("\" height=\"");
        device.print(height >= 0 ? height : 0);
        device.print("\" width=\"");
        device.print(width >= 0 ? width : 0);
        device.print("\"/>");
    }
}
