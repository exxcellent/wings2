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

import org.wings.SComponent;
import org.wings.SGridBagLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.awt.*;
import java.io.IOException;

public class GridBagLayoutCG extends AbstractLayoutCG
        implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        SGridBagLayout layout = (SGridBagLayout) l;
        boolean header = layout.getHeader();
        int cellSpacing = layout.getCellSpacing() >= 0 ? layout.getCellSpacing() : 0;
        int cellPadding = layout.getCellPadding() >= 0 ? layout.getCellPadding() : 0;
        int border = layout.getBorder() >= 0 ? layout.getBorder() : 0;
        SGridBagLayout.Grid grid = layout.getGrid();

        if (grid.cols == 0) {
            return;
        }

        printLayouterTableHeader(d, "SGridBagLayout", cellSpacing, cellPadding, border, layout);

        for (int row = grid.firstRow; row < grid.rows; row++) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<tr>");
            for (int col = grid.firstCol; col < grid.cols; col++) {
                SComponent comp = grid.grid[col][row];
                Utils.printNewline(d, layout.getContainer());
                if (comp == null) {
                    if (row == grid.firstRow && header) {
                        d.print("<th></th>");
                    } else {
                        d.print("<td></td>");
                    }
                } else {
                    GridBagConstraints c = layout.getConstraints(comp);
                    if ((c.gridx == SGridBagLayout.LAST_CELL || c.gridx == col) &&
                            (c.gridy == SGridBagLayout.LAST_CELL || c.gridy == row)) {
                        if (row == grid.firstRow && header) {
                            d.print("<th");
                        } else {
                            d.print("<td");
                        }

                        Utils.printTableCellAlignment(d, comp);
                        Utils.printCSSInlineStyleAttributes(d, comp);

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
                        if (c.weighty > 0 && grid.rowweight[col] > 0) {
                            d.print(" height=\"" +
                                    (int) (100 * c.weighty / grid.rowweight[col]) +
                                    "%\"");
                        }

                        d.print(">");
                        comp.write(d);

                        if (row == grid.firstRow && header) {
                            d.print("</th>");
                        } else {
                            d.print("</td>");
                        }
                    }
                }
            }
            Utils.printNewline(d, layout.getContainer());
            d.print("</tr>");
        }
        printLayouterTableFooter(d, "SGridBagLayout", layout);
    }



}


