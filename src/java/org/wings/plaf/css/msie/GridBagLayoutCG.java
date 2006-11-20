package org.wings.plaf.css.msie;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.css.TableCellStyle;
import org.wings.plaf.css.Utils;

import java.awt.*;
import java.io.IOException;

public class GridBagLayoutCG extends org.wings.plaf.css.GridBagLayoutCG {
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
        final SContainer container = layout.getContainer();

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


        final boolean header = layout.getHeader();
        final boolean useCellInsets = layout.getVgap() == -1 && layout.getHgap() == -1;
        final SGridBagLayout.Grid grid = layout.getGrid();
        final TableCellStyle cellStyle = cellLayoutStyle(layout);
        final TableCellStyle origCellStyle = cellStyle.makeACopy();
        int vertivalOversize = layoutOversize(layout);

        if (grid.cols == 0) {
            return;
        }

        openLayouterBody(d, layout);

        for (int row = grid.firstRow; row < grid.rows; row++) {
            Utils.printNewline(d, container);
            d.print("<tr");
            Utils.optAttribute(d, "yweight", determineRowHeight(layout, row));
            if (useCellInsets) {
                vertivalOversize = 0;
                for (int col = grid.firstCol; col < grid.cols; col++) {
                    final SComponent comp = grid.grid[col][row];
                    if (comp != null) {
                        GridBagConstraints c = layout.getConstraints(comp);
                        Insets insets = c.insets;
                        if (insets != null)
                            vertivalOversize = Math.max(vertivalOversize, cellOversize(layout, insets));
                    }
                }
            }
            Utils.optAttribute(d, "oversize", vertivalOversize);
            d.print(">");
            for (int col = grid.firstCol; col < grid.cols; col++) {
                final SComponent comp = grid.grid[col][row];
                Utils.printNewline(d, container);

                cellStyle.renderAsTH = row == grid.firstRow && header;
                cellStyle.defaultLayoutCellHAlignment = SConstants.CENTER;
                cellStyle.defaultLayoutCellVAlignment = SConstants.CENTER;

                if (comp == null) {
                    cellStyle.colspan = -1;
                    cellStyle.rowspan = -1;
                    cellStyle.width = null;

                    openLayouterCell(d, null, cellStyle);
                    closeLayouterCell(d, null, cellStyle.renderAsTH);
                } else {
                    GridBagConstraints c = layout.getConstraints(comp);

                    if ((c.gridx == SGridBagLayout.LAST_CELL || c.gridx == col) &&
                            (c.gridy == SGridBagLayout.LAST_CELL || c.gridy == row)) {

                        int gridwidth = c.gridwidth;
                        if (gridwidth == GridBagConstraints.RELATIVE) {
                            gridwidth = grid.cols - col - 1;
                        } else if (gridwidth == GridBagConstraints.REMAINDER) {
                            gridwidth = grid.cols - col;
                        }
                        if (gridwidth < 2) {
                            gridwidth = -1;
                        }

                        int gridheight = c.gridheight;
                        if (gridheight == GridBagConstraints.RELATIVE) {
                            gridheight = grid.cols - col - 1;
                        } else if (gridheight == GridBagConstraints.REMAINDER) {
                            gridheight = grid.cols - col;
                        }
                        if (gridheight < 2) {
                            gridheight = -1;
                        }

                        cellStyle.width = null;
                        if (c.weightx > 0 && grid.colweight[row] > 0) {
                            cellStyle.width = (int) (100 * c.weightx / grid.colweight[row]) + "%";
                        }

                        if (useCellInsets) {
                            cellStyle.setInsets((Insets) c.insets.clone());
                        }
                        cellStyle.colspan = gridwidth;
                        cellStyle.rowspan = gridheight;

                        if (PaddingVoodoo.hasPaddingInsets(container)) {
                            final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                            final boolean isFirstRow = row == grid.firstRow;
                            final boolean isLastRow = row == grid.rows - 1;
                            final boolean isFirstCol = col == grid.firstCol;
                            final boolean isLastCol = col == grid.cols - 1;
                            PaddingVoodoo.doBorderPaddingsWorkaround(container.getBorder(), patchedInsets, isFirstRow, isFirstCol, isLastCol, isLastRow);
                            cellStyle.setInsets(patchedInsets);
                        }

                        openLayouterCell(d, comp, cellStyle);

                        Utils.printNewline(d, comp);
                        comp.write(d); // Render component

                        closeLayouterCell(d, comp, cellStyle.renderAsTH);
                    }
                }
            }
            Utils.printNewline(d, container);
            closeLayouterRow(d);
        }
        closeLayouterBody(d, layout);
    }
}
