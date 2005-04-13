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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.*;
import org.wings.io.Device;
import org.wings.script.JavaScriptListener;

public class PopupMenuCG extends AbstractComponentCG implements SConstants, org.wings.plaf.MenuBarCG {

    private final transient static Log log = LogFactory.getLog(PopupMenuCG.class);

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addScriptListener(UTILS_SCRIPT_LOADER);
        comp.addScriptListener(MENU_SCRIPT_LOADER);
        SFrame parentFrame = comp.getParentFrame();
        if (parentFrame != null) {
            log.info("attached js listener to parent frame");
            parentFrame.addScriptListener(BODY_ONCLICK_SCRIPT);
        } else {
            log.error("No parent frame to attach the body click handler onto!");
        }
    }

    public void uninstallCG(final SComponent comp) {
    }

    public static final JavaScriptListener UTILS_SCRIPT_LOADER =
        new JavaScriptListener("", "", Utils.loadScript("org/wings/plaf/css/Utils.js"));
    public static final JavaScriptListener MENU_SCRIPT_LOADER =
        new JavaScriptListener("", "", Utils.loadScript("org/wings/plaf/css/Menu.js"));
    public static final JavaScriptListener BODY_ONCLICK_SCRIPT =
        new JavaScriptListener("onclick", "wpm_handleBodyClicks()");

    protected void writePopup(final Device device, SPopupMenu menu)
            throws IOException {
        if (menu.isEnabled()) {
            String componentId = menu.getName();
            device.print("<ul id=\"");
            device.print(componentId);
            device.print("_pop\">");
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
                                    device.print(" class=\"x\"");
                                } else {
                                    device.print(" class=\"y\"");
                                }
                            }
                            device.print(">");
                    }
                    menuItem.write(device);
                    if (menuItem instanceof SMenuItem) {
                        device.print("</a>");
                    }
                    if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                        ((SMenu)menuItem).writePopup(device);
                    }
                    device.print("</li>\n");
                }
            }
            device.print("</ul>");
        }
        device.print("\n");
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
