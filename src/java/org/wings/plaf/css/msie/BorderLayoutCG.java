/*
 * $Id: BorderLayoutCG.java 2686 2006-05-17 11:59:07 +0200 (Wed, 17 May 2006) hengels $
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
package org.wings.plaf.css.msie;

import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;
import org.wings.io.Device;
import org.wings.*;

import java.io.IOException;

public final class BorderLayoutCG extends org.wings.plaf.css.BorderLayoutCG {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SBorderLayout layout = (SBorderLayout) l;

        SDimension preferredSize = layout.getContainer().getPreferredSize();
        if (preferredSize == null) {
            super.write(d, l);
            return;
        }
        String height = preferredSize.getHeight();
        if (height == null || "auto".equals(height)) {
            super.write(d, l);
            return;
        }

        // special implementation with expressions is only required, if the center component
        // shall consume the remaining height

        final SComponent north = (SComponent) layout.getComponents().get(SBorderLayout.NORTH);
        final SComponent east = (SComponent) layout.getComponents().get(SBorderLayout.EAST);
        final SComponent center = (SComponent) layout.getComponents().get(SBorderLayout.CENTER);
        final SComponent west = (SComponent) layout.getComponents().get(SBorderLayout.WEST);
        final SComponent south = (SComponent) layout.getComponents().get(SBorderLayout.SOUTH);

        String styles = layoutStyles(layout);
        int oversize = layoutOversize(layout);
        RenderHelper renderHelper = RenderHelper.getInstance(l.getContainer());
        renderHelper.setVerticalLayoutPadding(layout.getVgap());
        renderHelper.setHorizontalLayoutPadding(layout.getHgap());

        int cols = 1;
        if (west != null) cols++;
        if (east != null) cols++;

        openLayouterBody(d, layout);

        if (north != null) {
            d.print("<tr");
            Utils.optAttribute(d, "oversize", oversize);
            d.print(">");
            Utils.printNewline(d, north);
            openLayouterCell(d, north, false, cols, -1, null, SConstants.LEFT, SConstants.TOP, styles);
            north.write(d);
            closeLayouterCell(d, north, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        d.print("<tr yweight=\"100\"");
        Utils.optAttribute(d, "oversize", oversize);
        d.print(">");

        if (west != null) {
            Utils.printNewline(d, west);
            openLayouterCell(d, west, false, -1, -1, "0%", SConstants.LEFT, SConstants.CENTER, styles);
            west.write(d);
            closeLayouterCell(d, west, false);
        }

        if (center != null) {
            Utils.printNewline(d, center);
            openLayouterCell(d, center, false, -1, -1, "100%", SConstants.LEFT, SConstants.CENTER, styles);
            center.write(d);
            closeLayouterCell(d, center, false);
        } else {
            d.print("<td width=\"100%\"></td>");
        }

        if (east != null) {
            Utils.printNewline(d, east);
            openLayouterCell(d, east, false, -1, -1, "0%", SConstants.RIGHT, SConstants.CENTER, styles);
            east.write(d);
            closeLayouterCell(d, east, false);
        }

        Utils.printNewline(d, layout.getContainer());
        closeLayouterRow(d);

        if (south != null) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<tr");
            Utils.optAttribute(d, "oversize", oversize);
            d.print(">");
            Utils.printNewline(d, south);
            openLayouterCell(d, south, false, cols, -1, "0%", SConstants.LEFT, SConstants.BOTTOM, styles);
            south.write(d);
            closeLayouterCell(d, south, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        closeLayouterBody(d, layout);

        renderHelper.setVerticalLayoutPadding(0);
        renderHelper.setHorizontalLayoutPadding(0);
    }
}
