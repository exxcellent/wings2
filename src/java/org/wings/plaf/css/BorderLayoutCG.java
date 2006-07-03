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
import org.wings.SConstants;
import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.io.IOException;

public class BorderLayoutCG extends AbstractLayoutCG {

    private static final long serialVersionUID = 1L;

    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SBorderLayout layout = (SBorderLayout) l;
        final SComponent north = (SComponent) layout.getComponents().get(SBorderLayout.NORTH);
        final SComponent east = (SComponent) layout.getComponents().get(SBorderLayout.EAST);
        final SComponent center = (SComponent) layout.getComponents().get(SBorderLayout.CENTER);
        final SComponent west = (SComponent) layout.getComponents().get(SBorderLayout.WEST);
        final SComponent south = (SComponent) layout.getComponents().get(SBorderLayout.SOUTH);

        String styles = layoutStyles(layout);
        RenderHelper renderHelper = RenderHelper.getInstance(l.getContainer());
        renderHelper.setVerticalLayoutPadding(layout.getVgap());
        renderHelper.setHorizontalLayoutPadding(layout.getHgap());

        int cols = 1;
        if (west != null) {
            cols++;
        }
        if (east != null) {
            cols++;
        }

        openLayouterBody(d, layout);

        if (north != null) {
            openLayouterRow(d, "0%");
            Utils.printNewline(d, north);
            openLayouterCell(d, north, false, cols, -1, null, SConstants.LEFT, SConstants.TOP, styles);
            north.write(d);
            closeLayouterCell(d, north, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        openLayouterRow(d, "100%");

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
            openLayouterRow(d, "0%");
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

    protected int getLayoutHGap(SLayoutManager layout) {
        SBorderLayout borderLayout = (SBorderLayout) layout;
        return borderLayout.getHgap();
    }

    protected int getLayoutVGap(SLayoutManager layout) {
        SBorderLayout borderLayout = (SBorderLayout) layout;
        return borderLayout.getVgap();
    }

    protected int getLayoutBorder(SLayoutManager layout) {
        SBorderLayout borderLayout = (SBorderLayout) layout;
        return borderLayout.getBorder();
    }

    protected int layoutOversize(SLayoutManager layout) {
        return 0;
    }

    public int getDefaultLayoutCellHAlignment() {
        return SConstants.NO_ALIGN;  // Don't knoff.
    }

    public int getDefaultLayoutCellVAlignment() {
        return SConstants.NO_ALIGN;  // Don't knoff.
    }
    
}
