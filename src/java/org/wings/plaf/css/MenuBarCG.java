// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/MenuBar.plaf'
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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.io.Device;
import org.wings.script.JavaScriptListener;

import java.io.IOException;

public class MenuBarCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.MenuBarCG {

    private final transient static Log log = LogFactory.getLog(MenuBarCG.class);

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

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenuBar component = (SMenuBar) _c;

//--- code from write-template.
        SMenuBar mbar = (SMenuBar) component;
        int mcount = mbar.getComponentCount();

        device.print("<div class=\"spacer\"></div>");
        for (int i = 0; i < mcount; i++) {
            SComponent menu = mbar.getComponent(i);
            if (menu.isVisible()) {
                if (menu.isEnabled()) {
                    device.print("<div class=\"SMenu\" onMouseDown=\"javascript:wpm_menu(event,'");
                    device.print(menu.getName());
                    device.print("_pop');\">");
                } else {
                    device.print("<div class=\"SMenu_Disabled\">");
                }
                device.print(((SMenu)menu).getText());
                device.print("</div>");
            }
        }
        device.print("<div class=\"spacer\"></div>");

//--- end code from write-template.
    }
}
