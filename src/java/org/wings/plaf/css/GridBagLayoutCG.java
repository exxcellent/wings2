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

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

import org.wings.SComponent;
import org.wings.SGridBagLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;

public class GridBagLayoutCG extends AbstractLayoutCG {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Renders a gridbag layout using invisible layouter tables.
     *
     * @param d the device to write the code to
     * @param l the layout manager
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SGridBagLayout layout = (SGridBagLayout) l;
        final boolean header = layout.getHeader();
        final int border = layout.getBorder() >= 0 ? layout.getBorder() : 0;
        final SGridBagLayout.Grid grid = layout.getGrid();
        final Insets layoutInsets = convertGapsToInset(layout.getHgap(), layout.getVgap());

        if (grid.cols == 0)
            return;

        printLayouterTableHeader(d, "SGridBagLayout", layoutInsets, border, layout);

        for (int row = grid.firstRow; row < grid.rows; row++) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<tr height=\"");
            d.print(determineRowHeight(layout, row));
            d.print("%\">");
            for (int col = grid.firstCol; col < grid.cols; col++) {
                final SComponent comp = grid.grid[col][row];
                Utils.printNewline(d, layout.getContainer());
                final boolean headerCell = row == grid.firstRow && header;
                if (comp == null) {
                    openLayouterCell(d, headerCell, layoutInsets, border, null);
                    d.print(">");
                    closeLayouterCell(d, headerCell);
                } else {
                    GridBagConstraints c = layout.getConstraints(comp);

                    if ((c.gridx == SGridBagLayout.LAST_CELL || c.gridx == col) &&
                            (c.gridy == SGridBagLayout.LAST_CELL || c.gridy == row)) {

                        final Insets cellInsets = addInsets(layoutInsets, c.insets);
                        openLayouterCell(d, headerCell, cellInsets, border, comp);

                        int gridwidth = c.gridwidth;
                        if (gridwidth == GridBagConstraints.RELATIVE) {
                            gridwidth = grid.cols - col - 1;
                        } else if (gridwidth == GridBagConstraints.REMAINDER) {
                            gridwidth = grid.cols - col;
                        }
                        if (gridwidth > 1) {
                            d.print(" colspan=" + gridwidth);
                        }

                        int gridheight = c.gridheight;
                        if (gridheight == GridBagConstraints.RELATIVE) {
                            gridheight = grid.rows - row - 1;
                        } else if (gridheight == GridBagConstraints.REMAINDER) {
                            gridheight = grid.rows - row;
                        }
                        if (gridheight > 1) {
                            d.print(" rowspan=" + gridheight);
                        }
                        if (c.weightx > 0 && grid.colweight[row] > 0) {
                            d.print(" width=\"" +
                                    (int) (100 * c.weightx / grid.colweight[row]) +
                                    "%\"");
                        }
                        /* Height needs be written on the tr element.
                           Replaced by new determineRowHeight() method as rendering bug occured
                           in FireFox. Refer to http://jira.j-wings.org/browse/WGS-120
                        if (c.weighty > 0 && grid.rowweight[col] > 0) {
                            d.print(" height=\"" +
                                    (int) (100 * c.weighty / grid.rowweight[col]) +
                                    "%\"");
                        }*/

                        d.print(">");

                        Utils.printNewline(d, comp);
                        comp.write(d); // Render component

                        closeLayouterCell(d, headerCell);
                    }
                }
            }
            Utils.printNewline(d, layout.getContainer());
            d.print("</tr>");
        }
        printLayouterTableFooter(d, "SGridBagLayout", layout);
    }

    /**
     * Copy and paste extracted method to determine an optional row height of the passed row.
     * Was necessary to avoid a rendering bug with Gecko engines leading to a messed up layout.
     * Refer to http://jira.j-wings.org/browse/WGS-120
     *
     * @param layout The gridbaglayout
     * @param row The row
     * @return Row height percentage as int or 0
     */
    private int determineRowHeight(SGridBagLayout layout, int row) {
        final SGridBagLayout.Grid grid = layout.getGrid();
        int rowHeight = 0;

        for (int col = grid.firstCol; col < grid.cols; col++) {
            SComponent comp = grid.grid[col][row];
            if (comp != null) {
                GridBagConstraints c = layout.getConstraints(comp);
                if ((c.gridx == SGridBagLayout.LAST_CELL || c.gridx == col)
                        && (c.gridy == SGridBagLayout.LAST_CELL || c.gridy == row)) {
                    if (c.weighty > 0 && grid.rowweight[col] > 0) {
                        int cellHeight = (int) (100 * c.weighty / grid.rowweight[col]);
                        if (cellHeight > rowHeight)
                            rowHeight = cellHeight;
                    }
                }
            }
        }
        return rowHeight;
    }

    /**
     * Adds both passed insets and accepts / ignores null and 0 values.
     *
     * @param inset1 Inset or <code>null</code>
     * @param inset2 Inset or <code>null</code>
     * @return The same inset if only one inset was != <code>null</code>, otherwise sum of both insets.
     */
    private Insets addInsets(final Insets inset1, final Insets inset2) {
        // If gridbag constraint defines additinal insets, add them to existing
        if (inset2 != null && (inset2.top > 0 || inset2.left > 0 || inset2.right > 0 || inset2.bottom > 0)) {
            if (inset1 == null || (inset1.top == 0 && inset1.left == 0 && inset1.right == 0 && inset1.bottom == 0))
                return inset2;
            else
                return new Insets(inset1.top + inset2.top, inset1.left + inset2.left,
                        inset1.bottom + inset2.bottom, inset1.right + inset2.right);
        } else
            return inset1;
    }


}


