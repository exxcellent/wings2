// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Menu.plaf'
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

import java.io.IOException;

public class MenuCssCG extends org.wings.plaf.css.MenuItemCG implements SConstants, org.wings.plaf.MenuCG {

    public void installCG(final SComponent comp) {
        super.installCG(comp);
    }

    public void uninstallCG(final SComponent comp) {
    }

//--- code from common area in template.
    public static final SIcon RIGHT_ARROW = new SResourceIcon("org/wings/icons/MenuArrowRight.gif");

    protected void writePopup(final Device device, SMenu menu)
            throws IOException {
        String componentId = menu.getName();
        //String popupId = componentId + "_pop";

        // calculate max length of children texts for sizing of layer
        int maxLength = 0;
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            String text = ((SMenuItem)menu.getMenuComponent(i)).getText();
            if (text != null && text.length() > maxLength) {
                maxLength = ((SMenuItem)menu.getMenuComponent(i)).getText().length();
                if (menu.getMenuComponent(i) instanceof SMenu) {
                        maxLength = maxLength + 2; //graphics
                }
            }
        }
        device.print("<ul");
        device.print(" style=\"width:");
        String stringLength = String.valueOf(maxLength * menu.getWidthScaleFactor());
        device.print(stringLength.substring(0,stringLength.lastIndexOf('.')+2));
        device.print("em;\" class=\"SMenu\">");
//        device.print(" class=\"SMenu\">");
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            SComponent menuItem = menu.getMenuComponent(i);

            if (menuItem.isVisible()) {
//                String itemComponentId = menu.getMenuComponent(i).getName();
//                String itemHookId = itemComponentId + "_hook";


                device.print("<li");
//                Utils.write(device, itemHookId);

//                device.print("\"");
                if (menu.getMenuComponent(i) instanceof SMenu) {
                    if (menuItem.isEnabled()) {
//                        String itemParentId = popupId;
//                        String itemPopupId = itemComponentId + "_pop";
//
//
//                        device.print(" onMouseDown=\"Menu.prototype.toggle('");
//                        Utils.write(device, itemParentId);
//
//                        device.print("','");
//                        Utils.write(device, itemHookId);
//
//                        device.print("','");
//                        Utils.write(device, itemPopupId);
//
//                        device.print("')\"");

                        device.print(" class=\"SMenu\"");
                    } else {

                        device.print(" class=\"SMenu_Disabled\"");
                    }

                } else {
                    if (menuItem.isEnabled()) {
//                        if (menuItem instanceof SMenuItem) {
//
//                            device.print(" onClick=\"window.location.href='");
//                            writeAnchorAddress(device, (SMenuItem) menuItem);
//                            device.print("'\"");
//                        }
//
                        device.print(" class=\"SMenuItem\"");
                    } else {

                        device.print(" class=\"SMenuItem_Disabled\"");
                    }
                }

                device.print(">");
                if (menuItem instanceof SMenuItem) {
                    device.print("<a href=\"");
                    writeAnchorAddress(device, (SMenuItem) menuItem);
                    if (menuItem instanceof SMenu) {
                        device.print("\" class=\"x\"");
                    }
                    device.print("\">");
                }
                menu.getMenuComponent(i).write(device);
                
//                if (menu.getMenuComponent(i) instanceof SMenu) {
//
//                    device.print("<img border=\"0\" align=\"middle\" src=\"");
//                    Utils.write(device, RIGHT_ARROW.getURL());
//
//                    device.print("\"");
//                    Utils.optAttribute(device, "width", RIGHT_ARROW.getIconWidth());
//                    Utils.optAttribute(device, "height", RIGHT_ARROW.getIconHeight());
//
//                    device.print("/>");
//                }

                if (menuItem instanceof SMenuItem) {
                    device.print("</a>");
                }
                if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                    writePopup(device, (SMenu) menu.getMenuComponent(i));
                }

                device.print("</li>\n");

            }
        }

        device.print("</ul>\n");
//        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
//            SComponent menuItem = menu.getMenuComponent(i);
//
//            if (menuItem.isVisible() && menuItem.isEnabled() && menuItem instanceof SMenu) {
//                writePopup(device, (SMenu) menu.getMenuComponent(i));
//            }
//        }
    }

    protected void writeItem(final Device device, SMenuItem menu)
            throws IOException {
        boolean hasParent = menu.getParentMenu() != null;

//        String parentId = hasParent ? "'" + menu.getParentMenu().getName() + "_pop'" : "null";
//        String componentId = menu.getName();
//        String popupId = componentId + "_pop";
//        String hookId = componentId + "_hook";



        // parent, hook, menu
        if (menu.isEnabled() && !hasParent) {

            device.print("<span>");
//            device.print("<span onClick=\"Menu.prototype.toggle(");
//            Utils.write(device, parentId);
//
//            device.print(",'");
//            Utils.write(device, hookId);
//
//            device.print("','");
//            Utils.write(device, popupId);
//
//            device.print("')\" id=\"");
//            Utils.write(device, componentId);
//
//            device.print("\">");
        }

        writeItemContent(device, menu);

        if (menu.isEnabled() && !hasParent) {

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
