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


import org.wings.*;
import org.wings.io.Device;
import org.wings.script.JavaScriptListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PopupMenuCG extends MenuItemCG implements SConstants, org.wings.plaf.MenuBarCG {


    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addScriptListener(SCRIPT_LOADER);
    }

    public void uninstallCG(final SComponent comp) {
    }

//--- code from common area in template.
    public static final SIcon RIGHT_ARROW = new SResourceIcon("org/wings/icons/MenuArrowRight.gif");

    public static final JavaScriptListener SCRIPT_LOADER =
            new JavaScriptListener("", "", loadScript());

    public static String loadScript() {
        InputStream in = null;
        BufferedReader reader = null;

        try {
            in = PopupMenuCG.class.getClassLoader().getResourceAsStream("org/wings/plaf/css/Menu.js");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");
            buffer.append(line).append("\n");

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                in.close();
            } catch (Exception ign) {
            }
            try {
                reader.close();
            } catch (Exception ign1) {
            }
        }
    }

    protected void writePopup(final Device device, SPopupMenu menu)
            throws IOException {
        String componentId = menu.getName();
        String popupId = componentId + "_pop";


        device.print("<table cellpadding=\"0\" cellspacing=\"0\" id=\"");
        Utils.write(device, popupId);

        device.print("\" class=\"pdmenu\" style=\"display:none\">");
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            SComponent menuItem = menu.getMenuComponent(i);

            if (menuItem.isVisible()) {
                String itemComponentId = menu.getMenuComponent(i).getName();
                String itemHookId = itemComponentId + "_hook";


                device.print("<tr id=\"");
                Utils.write(device, itemHookId);

                device.print("\"");
                if (menu.getMenuComponent(i) instanceof SMenu) {
                    if (menuItem.isEnabled()) {
                        String itemParentId = popupId;
                        String itemPopupId = itemComponentId + "_pop";


                        device.print(" onMouseDown=\"Menu.prototype.toggle('");
                        Utils.write(device, itemParentId);

                        device.print("','");
                        Utils.write(device, itemHookId);

                        device.print("','");
                        Utils.write(device, itemPopupId);

                        device.print("')\"");

                        device.print(" class=\"menu\"");
                    } else {
                        device.print(" class=\"disabledmenu\"");
                    }
                } else {
                    if (menuItem.isEnabled()) {
                        if (menuItem instanceof SMenuItem) {

                            device.print(" onClick=\"window.location.href='");
                            writeAnchorAddress(device, (SMenuItem) menuItem);
                            device.print("'\"");
                        }

                        device.print(" class=\"menuitem\"");
                    } else {
                        device.print(" class=\"disabledmenuitem\"");
                    }
                }

                device.print("><td>");
                menu.getMenuComponent(i).write(device);
                device.print("</td><td>");

                if (menu.getMenuComponent(i) instanceof SMenu) {
                    device.print("<img border=\"0\" align=\"middle\" src=\"");
                    Utils.write(device, RIGHT_ARROW.getURL());

                    device.print("\"");
                    Utils.optAttribute(device, "width", RIGHT_ARROW.getIconWidth());
                    Utils.optAttribute(device, "height", RIGHT_ARROW.getIconHeight());
                    device.print("/>");
                }

                device.print("</td></tr>");
            }
        }

        device.print("</table>");
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            SComponent menuItem = menu.getMenuComponent(i);

            if (menuItem.isVisible() && menuItem.isEnabled() && menuItem instanceof SMenu) {
                writePopup(device, (SPopupMenu) menu.getMenuComponent(i));
            }
        }
    }

    protected void writeAnchorAddress(Device d, SAbstractButton abstractButton)
            throws IOException {
        RequestURL addr = abstractButton.getRequestURL();
        addr.addParameter(abstractButton,
                abstractButton.getToggleSelectionParameter());
        addr.write(d);
    }

    public void writeContent(final Device device, final SComponent _c)
            throws IOException {

        SPopupMenu menu = (SPopupMenu) _c;
        writePopup(device, menu);
    }
}
