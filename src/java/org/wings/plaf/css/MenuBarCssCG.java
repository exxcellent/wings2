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


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.io.Device;

import java.io.IOException;

public class MenuBarCssCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.MenuBarCG {

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenuBar component = (SMenuBar) _c;

//--- code from write-template.
        SMenuBar mbar = (SMenuBar) component;
        int mcount = mbar.getComponentCount();

        device.print("<div class=\"spacer\"></div>");
        device.print("<div width=\"100%\"");
        Utils.optAttribute(device, "class", component.getStyle() + "_css");
        if ( mcount > 0 &&  mbar.getComponent(0).getHorizontalAlignment() == SConstants.RIGHT_ALIGN ) {
            // align right
            device.print(" style=\"text-align: right;\"");
        }
        device.print(">");
        /***
         * Due to the current Opera problems we are switching to the older Menue style
         * in all other cases we do a normal job
         ***/
        boolean rightAligned = false;
        for (int i = 0; i < mcount; i++) {
            SComponent menu = mbar.getComponent(i);
            if (menu.isVisible()) {
                String stringLength = "0";
                String text = ((SMenu)mbar.getComponent(i)).getText();
                if (text != null) {
                    stringLength = (String.valueOf((text.length() * component.getWidthScaleFactor())));
                }
                stringLength = stringLength.substring(0,stringLength.lastIndexOf('.')+2);
                device.print("<ul class=\"SMenu_Main\" style=\"width:");
                device.print(stringLength);
                device.print("em;\"><li class=\"SMenu\" style=\"width:");
                device.print(stringLength);
                device.print("em;\">");
                menu.write(device);
                device.print("</li></ul>\n");
            }
        }
        device.print("</ul></div>");
        device.print("\n");
        device.print("<div class=\"spacer\"></div>");

//--- end code from write-template.
    }
}
