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
import org.wings.SIcon;
import org.wings.SMenuItem;
import org.wings.io.Device;

public class MenuItemCG extends ButtonCG implements org.wings.plaf.MenuItemCG {

    protected void writeItemContent(final Device device, SMenuItem menuItem)
            throws IOException {
        SIcon icon = getIcon(menuItem);
        if (icon != null) {
            device.print("<img align=\"middle\"");
            Utils.optAttribute(device, "src", icon.getURL());
            Utils.optAttribute(device, "width", icon.getIconWidth());
            Utils.optAttribute(device, "height", icon.getIconHeight());
            device.print(" alt=\"");
            device.print(icon.getIconTitle());
            device.print("\"/>");
        }
        String text = menuItem.getText();
        if (text != null) {
            Utils.write(device, text);
        }
    }

    protected void writePrefix(Device device, SComponent component) throws IOException {
    }

    protected void writeSuffix(Device device, SComponent component) throws IOException {
    }

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SMenuItem menuItem = (SMenuItem) component;
        writeItemContent(device, menuItem);
    }
}
