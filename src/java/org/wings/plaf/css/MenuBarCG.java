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
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.io.Device;

import java.io.IOException;

public class MenuBarCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.MenuBarCG {

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenuBar component = (SMenuBar) _c;

//--- code from write-template.
        SMenuBar mbar = (SMenuBar) component;
        int mcount = mbar.getComponentCount();

        device.print("<div width=\"100%\"");
        Utils.optAttribute(device, "class", component.getStyle());
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
            if (mbar.getComponent(i).isVisible()) {
                device.print("<span id=\"");
                Utils.write(device, mbar.getComponent(i).getName() + "_hook");
                device.print("\" class=\"menu\"");
                if (mbar.getComponent(i).isEnabled() &&
                        mbar.getComponent(i) instanceof SMenuItem) {
                    device.print(" onMouseOver=\"Menu.prototype.setMouseOverStyle(this)\"");
                    device.print(" onMouseOut=\"Menu.prototype.setMouseOutStyle(this)\"");
                }
                device.print(">");
                mbar.getComponent(i).write(device);
                device.print("</nobr></span>");
            }
        }
        device.print("</div>");
        device.print("\n");

//--- end code from write-template.
    }
}
