//DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Menu.plaf'
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

import org.wings.*;
import org.wings.io.Device;
import org.wings.script.JavaScriptListener;

public class MenuJsCG extends org.wings.plaf.css.MenuItemCG implements SConstants, org.wings.plaf.MenuCG {

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addScriptListener(SCRIPT_LOADER);
    }

    public void uninstallCG(final SComponent comp) {
    }

//--- code from common area in template.
    public static final SIcon RIGHT_ARROW = new SResourceIcon("org/wings/icons/MenuArrowRight.gif");

    public static final JavaScriptListener SCRIPT_LOADER =
            new JavaScriptListener("", "", Utils.loadScript("org/wings/plaf/css/Menu.js"));

    protected void writePopup(final Device device, SMenu menu)
            throws IOException {
        String componentId = menu.getName();
        String popupId = componentId + "_pop";


        device.print("<div id=\"");
        Utils.write(device, popupId);

        device.print("\" class=\"SMenuPopup\" style=\"display:none\">");
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            SComponent menuItem = menu.getMenuComponent(i);

            if (menuItem.isVisible()) {
                String itemComponentId = menuItem.getName();
                String itemHookId = itemComponentId + "_hook";


                device.print("<div id=\"");
                Utils.write(device, itemHookId);

                device.print("\"");
                if (menuItem instanceof SMenu) {
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

                        device.print(" class=\"SMenuDisabledMenu\"");
                    }

                } else {
                    if (menuItem.isEnabled()) {
                        if (menuItem instanceof SMenuItem) {

                            device.print(" onClick=\"window.location.href='");
                            writeAnchorAddress(device, (SMenuItem) menuItem);
                            device.print("'\"");
                        }

                        device.print(" class=\"SMenuPopupItem\"");
                    } else {

                        device.print(" class=\"SMenuDisabledMenuitem\"");
                    }
                }

                device.print(">");
                menuItem.write(device);

                //device.print("</td><td>");
                if (menuItem instanceof SMenu) {

                    device.print("<img border=\"0\" align=\"middle\" src=\"");
                    Utils.write(device, RIGHT_ARROW.getURL());

                    device.print("\"");
                    Utils.optAttribute(device, "width", RIGHT_ARROW.getIconWidth());
                    Utils.optAttribute(device, "height", RIGHT_ARROW.getIconHeight());

                    device.print("/>");
                }

                device.print("</div>\n");
            }
        }

        device.print("</div>");
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            SComponent menuItem = menu.getMenuComponent(i);

            if (menuItem.isVisible() && menuItem.isEnabled() && menuItem instanceof SMenu) {
                writePopup(device, (SMenu) menuItem);
            }
        }
    }

    protected void writeItem(final Device device, SMenuItem menu)
            throws IOException {
        boolean hasParent = menu.getParentMenu() != null;

        String parentId = hasParent ? "'" + menu.getParentMenu().getName() + "_pop'" : "null";
        String componentId = menu.getName();
        String popupId = componentId + "_pop";
        String hookId = componentId + "_hook";



        // parent, hook, menu
        if (!hasParent) {
            device.print("<span");
            if (menu.isEnabled()) {
                device.print(" onClick=\"Menu.prototype.toggle(");
                Utils.write(device, parentId);
    
                device.print(",'");
                Utils.write(device, hookId);
    
                device.print("','");
                Utils.write(device, popupId);
    
                device.print("')\" id=\"");
                Utils.write(device, componentId);
    
                device.print("\"");
            } else {
                device.print(" style=\"color:#666\"");
            }
            device.print(">");
        }

        writeItemContent(device, menu);

        if (!hasParent) {
            device.print("</span>");
        }
    }

    protected void writeAnchorAddress(Device d, SAbstractButton abstractButton)
            throws IOException {
        RequestURL addr = abstractButton.getRequestURL();
        addr.addParameter(abstractButton,
                abstractButton.getToggleSelectionParameter());
        addr.write(d);
    }

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenu component = (SMenu) _c;

        SMenu menu = component;
        writeItem(device, menu);
        if (menu.getParentMenu() == null)
            writePopup(device, menu);
    }
}
