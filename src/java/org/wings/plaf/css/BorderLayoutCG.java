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

import org.wings.SBorderLayout;
import org.wings.SComponent;
import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.io.IOException;

public class BorderLayoutCG extends AbstractLayoutCG {

    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SBorderLayout layout = (SBorderLayout) l;
        final SComponent north = (SComponent) layout.getComponents().get(SBorderLayout.NORTH);
        final SComponent east = (SComponent) layout.getComponents().get(SBorderLayout.EAST);
        final SComponent center = (SComponent) layout.getComponents().get(SBorderLayout.CENTER);
        final SComponent west = (SComponent) layout.getComponents().get(SBorderLayout.WEST);
        final SComponent south = (SComponent) layout.getComponents().get(SBorderLayout.SOUTH);

        int cols = 0;
        if (west != null) cols++;
        if (center != null) cols++;
        if (east != null) cols++;

        printLayouterTableHeader(d, "SBorderLayout",0,0,0,layout);

        if (north != null) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<tr style=\"height: 0%\"><td colspan=\"").print(cols).print("\">");
            north.write(d);
            d.print("</td></tr>");
        }

        Utils.printNewline(d, layout.getContainer());
        d.print("<tr style=\"height: 100%\">");

        if (west != null) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<td style=\"width: 0%\">");
            west.write(d);
            d.print("</td>");
        }

        if (center != null) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<td style=\"width: 100%\">");
            center.write(d);
            d.print("</td>");
        }

        if (east != null) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<td style=\"width: 0%\">");
            east.write(d);
            d.print("</td>");
        }
        d.print("</tr>");

        if (south != null) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<tr style=\"height: 0%\"><td colspan=\"").print(cols).print("\">");
            south.write(d);
            d.print("</td></tr>");
        }

        printLayouterTableFooter(d, "SBorderLayout", layout);
    }

}


