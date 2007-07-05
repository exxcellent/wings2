/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;

import org.wings.*;
import org.wings.plaf.css.PaddingVoodoo;
import org.wings.io.Device;

import java.io.IOException;
import java.awt.*;

public class BorderLayoutCG extends AbstractLayoutCG {

    private static final long serialVersionUID = 1L;

    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SBorderLayout layout = (SBorderLayout) l;
        SContainer container = l.getContainer();

        final SComponent north = (SComponent) layout.getComponents().get(SBorderLayout.NORTH);
        final SComponent east = (SComponent) layout.getComponents().get(SBorderLayout.EAST);
        final SComponent center = (SComponent) layout.getComponents().get(SBorderLayout.CENTER);
        final SComponent west = (SComponent) layout.getComponents().get(SBorderLayout.WEST);
        final SComponent south = (SComponent) layout.getComponents().get(SBorderLayout.SOUTH);

        final TableCellStyle cellStyle = cellLayoutStyle(layout);
        final TableCellStyle origCellStyle = cellStyle.makeACopy();

        SDimension preferredSize = container.getPreferredSize();
        String height = preferredSize != null ? preferredSize.getHeight() : null;
        boolean clientLayout = isMSIE(container) && height != null && !"auto".equals(height);
        int oversize = layoutOversize(l);

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

            if (clientLayout) {
                d.print("<tr");
                Utils.optAttribute(d, "oversize", oversize);
                d.print(">");
            }
            else
                openLayouterRow(d, "0%");
            Utils.printNewline(d, north);

            if (PaddingVoodoo.hasPaddingInsets(container)) {
                final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                final boolean isFirstRow = true;
                final boolean isLastRow = west == null && center == null && east == null && south == null;
                final boolean isFirstCol = true;
                final boolean isLastCol = true;
                PaddingVoodoo.doBorderPaddingsWorkaround(container.getBorder(), patchedInsets, isFirstRow, isFirstCol, isLastCol, isLastRow);
                cellStyle.setInsets(patchedInsets);
            }

            openLayouterCell(d, north, cellStyle);
            north.write(d);
            closeLayouterCell(d, north, false);
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
            Utils.printNewline(d, layout.getContainer());
        }

        if (clientLayout) {
            d.print("<tr yweight=\"100\"");
            Utils.optAttribute(d, "oversize", oversize);
            d.print(">");
        }
        else
            openLayouterRow(d, "100%");

        if (west != null) {
            cellStyle.defaultLayoutCellHAlignment = SConstants.LEFT;
            cellStyle.defaultLayoutCellVAlignment = SConstants.CENTER;
            cellStyle.width = "0%";
            cellStyle.colspan = -1;
            cellStyle.rowspan = -1;

            if (PaddingVoodoo.hasPaddingInsets(container)) {
                final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                final boolean isFirstRow = north == null;
                final boolean isLastRow = south == null;
                final boolean isFirstCol = true;
                final boolean isLastCol = center == null && east == null;
                PaddingVoodoo.doBorderPaddingsWorkaround(container.getBorder(), patchedInsets, isFirstRow, isFirstCol, isLastCol, isLastRow);
                cellStyle.setInsets(patchedInsets);
            }

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

            if (PaddingVoodoo.hasPaddingInsets(container)) {
                final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                final boolean isFirstRow = north == null;
                final boolean isLastRow = south == null;
                final boolean isFirstCol = west == null;
                final boolean isLastCol = east == null;
                PaddingVoodoo.doBorderPaddingsWorkaround(container.getBorder(), patchedInsets, isFirstRow, isFirstCol, isLastCol, isLastRow);
                cellStyle.setInsets(patchedInsets);
            }

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

            if (PaddingVoodoo.hasPaddingInsets(container)) {
                final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                final boolean isFirstRow = north == null;
                final boolean isLastRow = south == null;
                final boolean isFirstCol = west == null && center == null;
                final boolean isLastCol = true;
                PaddingVoodoo.doBorderPaddingsWorkaround(container.getBorder(), patchedInsets, isFirstRow, isFirstCol, isLastCol, isLastRow);
                cellStyle.setInsets(patchedInsets);
            }

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

            if (PaddingVoodoo.hasPaddingInsets(container)) {
                final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                final boolean isFirstRow = north == null && west == null && center == null && east == null;
                final boolean isLastRow = true;
                final boolean isFirstCol = true;
                final boolean isLastCol = true;
                PaddingVoodoo.doBorderPaddingsWorkaround(container.getBorder(), patchedInsets, isFirstRow, isFirstCol, isLastCol, isLastRow);
                cellStyle.setInsets(patchedInsets);
            }

            Utils.printNewline(d, layout.getContainer());
            if (clientLayout) {
                d.print("<tr");
                Utils.optAttribute(d, "oversize", oversize);
                d.print(">");
            }
            else
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

    protected int layoutOversize(SLayoutManager layout) {
        SBorderLayout borderLayout = (SBorderLayout) layout;
        return borderLayout.getVgap() + borderLayout.getBorder();
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

    public int getDefaultLayoutCellHAlignment() {
        return SConstants.NO_ALIGN;  // Don't knoff.
    }

    public int getDefaultLayoutCellVAlignment() {
        return SConstants.NO_ALIGN;  // Don't knoff.
    }
    
}
