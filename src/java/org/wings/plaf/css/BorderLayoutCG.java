/*
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

        final TableCellStyle cellStyle = cellLayoutStyle(layout);

        int cols = 1;
        if (west != null) {
            cols++;
        }
        if (east != null) {
            cols++;
        }

        openLayouterBody(d, layout);

        if (north != null) {
            cellStyle.defaultLayoutCellHAlignment = SConstants.LEFT;
            cellStyle.defaultLayoutCellVAlignment = SConstants.TOP;
            cellStyle.width = null;
            cellStyle.colspan = cols;
            cellStyle.rowspan = -1;

            openLayouterRow(d, "0%");
            Utils.printNewline(d, north);
            openLayouterCell(d, north, cellStyle);
            north.write(d);
            closeLayouterCell(d, north, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        openLayouterRow(d, "100%");

        if (west != null) {
            cellStyle.defaultLayoutCellHAlignment = SConstants.LEFT;
            cellStyle.defaultLayoutCellVAlignment = SConstants.CENTER;
            cellStyle.width = "0%";
            cellStyle.colspan = -1;
            cellStyle.rowspan = -1;

            Utils.printNewline(d, west);
            openLayouterCell(d, west, cellStyle);
            west.write(d);
            closeLayouterCell(d, west, false);
        }

        if (center != null) {
            cellStyle.defaultLayoutCellHAlignment = SConstants.LEFT;
            cellStyle.defaultLayoutCellVAlignment = SConstants.CENTER;
            cellStyle.width = "100%";
            cellStyle.colspan = -1;
            cellStyle.rowspan = -1;

            Utils.printNewline(d, center);
            openLayouterCell(d, center, cellStyle);
            center.write(d);
            closeLayouterCell(d, center, false);
        } else {
            d.print("<td width=\"100%\"></td>");
        }

        if (east != null) {
            cellStyle.defaultLayoutCellHAlignment = SConstants.RIGHT;
            cellStyle.defaultLayoutCellVAlignment = SConstants.CENTER;
            cellStyle.width = "0%";
            cellStyle.colspan = -1;
            cellStyle.rowspan = -1;

            Utils.printNewline(d, east);
            openLayouterCell(d, east, cellStyle);
            east.write(d);
            closeLayouterCell(d, east, false);
        }

        Utils.printNewline(d, layout.getContainer());
        closeLayouterRow(d);

        if (south != null) {
            cellStyle.defaultLayoutCellHAlignment = SConstants.LEFT;
            cellStyle.defaultLayoutCellVAlignment = SConstants.BOTTOM;
            cellStyle.width = "0%";
            cellStyle.colspan = cols;
            cellStyle.rowspan = -1;

            Utils.printNewline(d, layout.getContainer());
            openLayouterRow(d, "0%");
            Utils.printNewline(d, south);
            openLayouterCell(d, south, cellStyle);
            south.write(d);
            closeLayouterCell(d, south, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        closeLayouterBody(d, layout);
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
