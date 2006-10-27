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

public final class MenuCG extends org.wings.plaf.css.MenuItemCG implements
        org.wings.plaf.MenuCG {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SResourceIcon arrowIcon = new SResourceIcon("org/wings/icons/MenuArrowRight.gif");
    {
        arrowIcon.getId();
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);
    }

    public void uninstallCG(final SComponent comp) {
    }

    public void writePopup(final Device device, SMenu menu)
            throws IOException {
        if (menu.isEnabled()) {
            device.print("<ul");
            writeListAttributes(device, menu);
            device.print(" class=\"SMenu\">");
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                SComponent menuItem = menu.getMenuComponent(i);
    
                if (menuItem.isVisible()) {
                    device.print("<li");
                    if (menuItem instanceof SMenu) {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenu\"");
                        } else {
                            device.print(" class=\"SMenu_Disabled\"");
                        }
                    } else {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenuItem\"");
                        } else {
                            device.print(" class=\"SMenuItem_Disabled\"");
                        }
                    }
                    printScriptHandlers(device, menuItem);
                    device.print(">");
                    if (menuItem instanceof SMenuItem) {
                            device.print("<a");
                            if (menuItem.isEnabled()) {
                                device.print(" href=\"");
                                writeAnchorAddress(device, (SMenuItem) menuItem);
                                device.print("\"");
                            }
                            if (menuItem instanceof SMenu) {
                                if (menuItem.isEnabled()) {
                                    device.print(" class=\"x sub\"");
                                } else {
                                    device.print(" class=\"y sub\"");
                                }
                            }
                            device.print(">");
                    }
                    menuItem.write(device);
                    if (menuItem instanceof SMenuItem) {
                        device.print("</a>");
                    }
                    if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                        menuItem.putClientProperty("popup", Boolean.TRUE);
                        menuItem.write(device);
                        menuItem.putClientProperty("popup", null);
                    }
                    device.print("</li>\n");
                }
            }
            device.print("</ul>");
        }
        device.print("\n");
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.MenuCG#printScriptHandlers(org.wings.io.Device, org.wings.SComponent)
     */
    protected void printScriptHandlers(Device device, SComponent menuItem) throws IOException {
        // print the script handlers, if it is a SMenu or if the parent has items and menus as childs
        SMenuItem tMenuItem = (SMenuItem) menuItem;
        if (!(tMenuItem instanceof SMenu)) {
            if (tMenuItem.getParentMenu() != null && tMenuItem.getParentMenu() instanceof SMenu) {
                SMenu tParentMenu = (SMenu) tMenuItem.getParentMenu();
                boolean tHasMenuChild = false;
                boolean tHasMenuItemChild = false;
                for (int tChildIndex = 0; tChildIndex < tParentMenu.getMenuComponentCount(); tChildIndex++) {
                    SComponent tChild = tParentMenu.getChild(tChildIndex);
                    if (tChild instanceof SMenu) {
                        tHasMenuChild = true;
                    } else {
                        tHasMenuItemChild = true;
                    }
                }

                // only print, if has both types
                if (!(tHasMenuChild && tHasMenuItemChild)) {
                    return;
                }
            }
        }

        device.print(" onmouseover=\"wpm_openMenu(event, '");
        device.print(tMenuItem.getName());
        device.print("_pop','");
        device.print(tMenuItem.getParentMenu().getName()

        );
        device.print("_pop');\"");
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.MenuCG#writeListAttributes(org.wings.io.Device, org.wings.SMenu)
     */
    protected void writeListAttributes(Device device, SMenu menu) throws IOException {
        // calculate max length of children texts for sizing of layer
        int maxLength = 0;
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            if (!(menu.getMenuComponent(i) instanceof SMenuItem))
                continue;
            String text = ((SMenuItem) menu.getMenuComponent(i)).getText();
            if (text != null && text.length() > maxLength) {
                maxLength = text.length();
                if (menu.getMenuComponent(i) instanceof SMenu) {
                    maxLength = maxLength + 2; //graphics
                }
            }
        }
        device.print(" style=\"width:");
        String stringLength = String.valueOf(maxLength * menu.getWidthScaleFactor());
        device.print(stringLength.substring(0, stringLength.lastIndexOf('.') + 2));
        device.print("em;\"");
        device.print(" id=\"");
        device.print(menu.getName());
        device.print("_pop\"");
    }

    protected void writeAnchorAddress(Device d, SAbstractButton abstractButton)
            throws IOException {
        RequestURL addr = abstractButton.getRequestURL();
        addr.addParameter(abstractButton,
                abstractButton.getToggleSelectionParameter());
        addr.write(d);
    }

    public void writeInternal(final Device device, final SComponent _c)
        throws IOException {
        SMenu menu = (SMenu) _c;
        if (menu.getClientProperty("popup") == null)
            writeItemContent(device, menu);
        else
            writePopup(device, menu);
    }
}
