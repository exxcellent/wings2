// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/MenuBar.plaf'
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


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.io.Device;

import java.io.IOException;

public class MenuBarCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.MenuBarCG {

//--- byte array converted template snippets.
    private final static byte[] __table_class_me = "<table class=\"menubar\" cellspacing=\"0\" cellpadding=\"0\" vspace=\"0\" hspace=\"0\" width=\"100%\"".getBytes();
    private final static byte[] __ = ">".getBytes();
    private final static byte[] __tr_align_left = "<tr align=\"left\">".getBytes();
    private final static byte[] __td_width_100_t = "<td width=\"100%\"></td>".getBytes();
    private final static byte[] __td_id = "<td id=\"".getBytes();
    private final static byte[] __class_menu = "\" class=\"menu\"".getBytes();
    private final static byte[] __onMouseOver_Me = " onMouseOver=\"Menu.prototype.setMouseOverStyle(this)\"".getBytes();
    private final static byte[] __onMouseOut_Men = " onMouseOut=\"Menu.prototype.setMouseOutStyle(this)\"".getBytes();
    private final static byte[] __nobr_td = "</nobr></td>".getBytes();
    private final static byte[] __td_width_100_n = "<td width=\"100%\">&nbsp;</td>".getBytes();
    private final static byte[] __tr_table = "</tr></table>".getBytes();
    private final static byte[] ___1 = "\n".getBytes();

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenuBar component = (SMenuBar) _c;

//--- code from write-template.
        SMenuBar mbar = (SMenuBar) component;
        int mcount = mbar.getComponentCount();

        device.write(__table_class_me);
        org.wings.plaf.Utils.optAttribute(device, "class", org.wings.plaf.css.Utils.style(component));
        device.write(__);
        device.write(__tr_align_left);
        /***
         * Due to the current Opera problems we are switching to the older Menue style
         * in all other cases we do a normal job
         ***/
        boolean rightAligned = false;
        for (int i = 0; i < mcount; i++) {
            if (mbar.getComponent(i).isVisible()) {
                if (mbar.getComponent(i).getHorizontalAlignment() == SConstants.RIGHT_ALIGN &&
                        !rightAligned) {
                    device.write(__td_width_100_t);
                    rightAligned = true;
                }
                device.write(__td_id);
                org.wings.plaf.Utils.write(device, mbar.getComponent(i).getName() + "_hook");
                device.write(__class_menu);
                if (mbar.getComponent(i).isEnabled() &&
                        mbar.getComponent(i) instanceof SMenuItem) {
                    device.write(__onMouseOver_Me);
                    device.write(__onMouseOut_Men);
                }
                device.write(__);
                mbar.getComponent(i).write(device);
                device.write(__nobr_td);
            }
        }
        if (!rightAligned) {
            device.write(__td_width_100_n);
        }
        device.write(__tr_table);
        device.write(___1);

//--- end code from write-template.
    }
}
