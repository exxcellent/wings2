/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
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


import org.wings.*;
import org.wings.io.Device;
import org.wings.script.JavaScriptListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PopupMenuCG extends MenuItemCG implements SConstants, org.wings.plaf.MenuBarCG {

//--- byte array converted template snippets.
    private final static byte[] __table_cellpadd = "<table cellpadding=\"0\" cellspacing=\"0\" id=\"".getBytes();
    private final static byte[] __class_pdmenu_s = "\" class=\"pdmenu\" style=\"display:none\">".getBytes();
    private final static byte[] __tr_id = "<tr id=\"".getBytes();
    private final static byte[] __ = "\"".getBytes();
    private final static byte[] __onMouseDown_Me = " onMouseDown=\"Menu.prototype.toggle('".getBytes();
    private final static byte[] ___1 = "','".getBytes();
    private final static byte[] ___2 = "')\"".getBytes();
    private final static byte[] __class_menu = " class=\"menu\"".getBytes();
    private final static byte[] __class_disabled = " class=\"disabledmenu\"".getBytes();
    private final static byte[] __onClick_window = " onClick=\"window.location.href='".getBytes();
    private final static byte[] ___3 = "'\"".getBytes();
    private final static byte[] __class_menuitem = " class=\"menuitem\"".getBytes();
    private final static byte[] __class_disabled_1 = " class=\"disabledmenuitem\"".getBytes();
    private final static byte[] __td = "><td>".getBytes();
    private final static byte[] __td_td = "</td><td>".getBytes();
    private final static byte[] __img_border_0_a = "<img border=\"0\" align=\"middle\" src=\"".getBytes();
    private final static byte[] ___4 = "/>".getBytes();
    private final static byte[] __td_tr = "</td></tr>".getBytes();
    private final static byte[] __table = "</table>".getBytes();

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


        device.write(__table_cellpadd);
        org.wings.plaf.Utils.write(device, popupId);

        device.write(__class_pdmenu_s);
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            SComponent menuItem = menu.getMenuComponent(i);

            if (menuItem.isVisible()) {
                String itemComponentId = menu.getMenuComponent(i).getName();
                String itemHookId = itemComponentId + "_hook";


                device.write(__tr_id);
                org.wings.plaf.Utils.write(device, itemHookId);

                device.write(__);
                if (menu.getMenuComponent(i) instanceof SMenu) {
                    if (menuItem.isEnabled()) {
                        String itemParentId = popupId;
                        String itemPopupId = itemComponentId + "_pop";


                        device.write(__onMouseDown_Me);
                        org.wings.plaf.Utils.write(device, itemParentId);

                        device.write(___1);
                        org.wings.plaf.Utils.write(device, itemHookId);

                        device.write(___1);
                        org.wings.plaf.Utils.write(device, itemPopupId);

                        device.write(___2);

                        device.write(__class_menu);
                    } else {
                        device.write(__class_disabled);
                    }
                } else {
                    if (menuItem.isEnabled()) {
                        if (menuItem instanceof SMenuItem) {

                            device.write(__onClick_window);
                            writeAnchorAddress(device, (SMenuItem) menuItem);
                            device.write(___3);
                        }

                        device.write(__class_menuitem);
                    } else {
                        device.write(__class_disabled_1);
                    }
                }

                device.write(__td);
                menu.getMenuComponent(i).write(device);
                device.write(__td_td);

                if (menu.getMenuComponent(i) instanceof SMenu) {
                    device.write(__img_border_0_a);
                    org.wings.plaf.Utils.write(device, RIGHT_ARROW.getURL());

                    device.write(__);
                    org.wings.plaf.Utils.optAttribute(device, "width", RIGHT_ARROW.getIconWidth());
                    org.wings.plaf.Utils.optAttribute(device, "height", RIGHT_ARROW.getIconHeight());
                    device.write(___4);
                }

                device.write(__td_tr);
            }
        }

        device.write(__table);
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
