/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SMenuItem;
import org.wings.SIcon;
import org.wings.io.Device;

import java.io.IOException;

public class MenuItemCG extends ButtonCG implements SConstants, org.wings.plaf.MenuBarCG {

//--- byte array converted template snippets.
    private final static byte[] __nobr = "<nobr>".getBytes();
    private final static byte[] __img_border_0_a = "<img border=\"0\" align=\"middle\" src=\"".getBytes();
    private final static byte[] __ = "\"".getBytes();
    private final static byte[] ___1 = "/>".getBytes();
    private final static byte[] __nobr_1 = "</nobr>".getBytes();
    private final static byte[] ___2 = "\n".getBytes();

    protected void writeItemContent(final Device device, SMenuItem menuItem)
        throws IOException {
        SIcon icon = getIcon(menuItem);


        device.write(__nobr);
        if (icon != null) {

            device.write(__img_border_0_a);
            org.wings.plaf.Utils.write(device, icon.getURL());

            device.write(__);
            org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
            org.wings.plaf.Utils.optAttribute(device, "height", icon.getIconHeight());

            device.write(___1);
        }

        String text = menuItem.getText();

        if (text != null) {
            org.wings.plaf.Utils.write(device, text);
        }

        device.write(__nobr_1);
    }

    protected void writePrefix(Device device, SComponent component) throws IOException {
    }

    protected void writePostfix(Device device, SComponent component) throws IOException {
    }

    public void writeContent(final Device device, final SComponent component)
        throws IOException {
        final SMenuItem menuItem = (SMenuItem)component;

        writeItemContent(device, menuItem);
    }
}
