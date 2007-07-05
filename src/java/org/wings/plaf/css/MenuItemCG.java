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
import org.wings.SIcon;
import org.wings.SMenuItem;
import org.wings.io.Device;
import org.wings.plaf.Update;
import org.wings.util.KeystrokeUtil;

import javax.swing.*;
import java.io.IOException;

public class MenuItemCG extends ButtonCG implements org.wings.plaf.MenuItemCG {

    private static final long serialVersionUID = 1L;

    protected void writeItemContent(final Device device, SMenuItem menuItem)
            throws IOException {
        SIcon icon = getIcon(menuItem);
        if (icon != null) {
            device.print("<img align=\"middle\"");
            Utils.optAttribute(device, "src", icon.getURL());
            Utils.optAttribute(device, "width", icon.getIconWidth());
            Utils.optAttribute(device, "height", icon.getIconHeight());
            Utils.attribute(device, "alt", icon.getIconTitle());
            device.print("/>");
        }
        String text = menuItem.getText();
        if (text != null) {
            Utils.write(device, text);
        }
        KeyStroke acc = menuItem.getAccelerator();
        if (acc != null) {
            device.print(" <span class=\"accelerator\">");
            Utils.write(device, KeystrokeUtil.keyStroke2String(acc));
            device.print("</span>");
        }
    }

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SMenuItem menuItem = (SMenuItem) component;
        writeItemContent(device, menuItem);
    }

    public Update getComponentUpdate(SComponent component) {
        SComponent parentMenu = ((SMenuItem) component).getParentMenu();
        if (parentMenu != null)
            return parentMenu.getCG().getComponentUpdate(parentMenu);
        else
            return super.getComponentUpdate(component);
    }

}
