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

import java.awt.Insets;
import java.io.IOException;

import org.wings.SBorderLayout;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SLayoutManager;
import org.wings.io.Device;

public final class BorderLayoutCG extends AbstractLayoutCG {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SBorderLayout layout = (SBorderLayout) l;
        final SComponent north = (SComponent) layout.getComponents().get(SBorderLayout.NORTH);
        final SComponent east = (SComponent) layout.getComponents().get(SBorderLayout.EAST);
        final SComponent center = (SComponent) layout.getComponents().get(SBorderLayout.CENTER);
        final SComponent west = (SComponent) layout.getComponents().get(SBorderLayout.WEST);
        final SComponent south = (SComponent) layout.getComponents().get(SBorderLayout.SOUTH);

        int cols = 1;
        if (west != null) cols++;
        if (east != null) cols++;

        openLayouterBody(d, layout);

        if (north != null) {
            openLayouterRow(d, "0%");
            Utils.printNewline(d, north);
            openLayouterCell(d, north, false, cols, -1, null, SConstants.LEFT, SConstants.TOP);
            north.write(d);
            closeLayouterCell(d, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        openLayouterRow(d, "100%");

        if (west != null) {
            Utils.printNewline(d, west);
            openLayouterCell(d, west, false, -1, -1, "0%", SConstants.LEFT, SConstants.CENTER);
            west.write(d);
            closeLayouterCell(d, false);
        }

        if (center != null) {
            Utils.printNewline(d, center);
            openLayouterCell(d, center, false, -1, -1, "100%", SConstants.LEFT, SConstants.CENTER);
            center.write(d);
            closeLayouterCell(d, false);
        } else {
            d.print("<td width=\"100%\"></td>");
        }

        if (east != null) {
            Utils.printNewline(d, east);
            openLayouterCell(d, east, false, -1, -1, "0%", SConstants.RIGHT, SConstants.CENTER);
            east.write(d);
            closeLayouterCell(d, false);
        }

        Utils.printNewline(d, layout.getContainer());
        closeLayouterRow(d);

        if (south != null) {
            Utils.printNewline(d, layout.getContainer());
            openLayouterRow(d, "0%");
            Utils.printNewline(d, south);
            openLayouterCell(d, south, false, cols, -1, "0%", SConstants.LEFT, SConstants.BOTTOM);
            south.write(d);
            closeLayouterCell(d, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        closeLayouterBody(d, layout);
    }
}
