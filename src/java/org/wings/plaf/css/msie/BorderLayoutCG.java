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
package org.wings.plaf.css.msie;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.css.TableCellStyle;
import org.wings.plaf.css.Utils;

import java.io.IOException;
import java.awt.*;

public final class BorderLayoutCG extends org.wings.plaf.css.BorderLayoutCG {

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

        final SContainer container = layout.getContainer();
        final SComponent north = (SComponent) layout.getComponents().get(SBorderLayout.NORTH);
        final SComponent east = (SComponent) layout.getComponents().get(SBorderLayout.EAST);
        final SComponent center = (SComponent) layout.getComponents().get(SBorderLayout.CENTER);
        final SComponent west = (SComponent) layout.getComponents().get(SBorderLayout.WEST);
        final SComponent south = (SComponent) layout.getComponents().get(SBorderLayout.SOUTH);

        final TableCellStyle cellStyle = cellLayoutStyle(layout);
        final TableCellStyle origCellStyle = cellStyle.makeACopy();
        int oversize = layoutOversize(layout);

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

            d.print("<tr");
            Utils.optAttribute(d, "oversize", oversize);
            d.print(">");
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

        d.print("<tr yweight=\"100\"");
        Utils.optAttribute(d, "oversize", oversize);
        d.print(">");

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
            d.print("<tr");
            Utils.optAttribute(d, "oversize", oversize);
            d.print(">");
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
}
