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
package org.wings.plaf.xhtml;

import org.wings.SBorderLayout;
import org.wings.SComponent;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.Map;

public class BorderLayoutCG
        implements LayoutCG {

    public void write(Device d, SLayoutManager l)
            throws IOException {
        SBorderLayout layout = (SBorderLayout) l;
        Map components = layout.getComponents();

        SComponent north = (SComponent) components.get(SBorderLayout.NORTH);
        SComponent east = (SComponent) components.get(SBorderLayout.EAST);
        SComponent center = (SComponent) components.get(SBorderLayout.CENTER);
        SComponent west = (SComponent) components.get(SBorderLayout.WEST);
        SComponent south = (SComponent) components.get(SBorderLayout.SOUTH);

        int cols = 0;
        if (west != null) cols++;
        if (center != null) cols++;
        if (east != null) cols++;

        d.print("\n<table class=\"SBorderLayout\" cellpadding=\"0\" cellspacing=\"0\"><tbody>");

        if (north != null) {
            d.print("\n<tr style=\"height: 0%\"><td colspan=\"").print(cols).print("\">");
            writeComponent(d, north);
            d.print("</td></tr>");
        }
        d.print("\n<tr style=\"height: 100%\">");

        if (west != null) {
            d.print("<td style=\"width: 0%\">");
            writeComponent(d, west);
            d.print("</td>");
        }

        if (center != null) {
            d.print("<td style=\"width: 100%\">");
            writeComponent(d, center);
            d.print("</td>");
        }

        if (east != null) {
            d.print("<td style=\"width: 0%\">");
            writeComponent(d, east);
            d.print("</td>");
        }
        d.print("</tr>\n");

        if (south != null) {
            d.print("\n<tr style=\"height: 0%\"><td colspan=\"").print(cols).print("\">");
            writeComponent(d, south);
            d.print("</td></tr>");
        }
        d.print("\n</tbody></table>");
    }

    protected void writeComponent(Device d, SComponent c)
            throws IOException {
        c.write(d);
    }
}


