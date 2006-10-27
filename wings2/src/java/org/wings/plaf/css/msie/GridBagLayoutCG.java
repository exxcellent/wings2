package org.wings.plaf.css.msie;

import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;
import org.wings.io.Device;
import org.wings.*;

import java.io.IOException;
import java.awt.*;

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
        final SGridBagLayout.Grid grid = layout.getGrid();
        String styles = layoutStyles(layout);
        int vertivalOversize = layoutOversize(layout);
        boolean useCellStyles = layout.getVgap() == -1 && layout.getHgap() == -1;

        RenderHelper renderHelper = RenderHelper.getInstance(l.getContainer());

        if (grid.cols == 0)
            return;

        openLayouterBody(d, layout);

        for (int row = grid.firstRow; row < grid.rows; row++) {
            Utils.printNewline(d, layout.getContainer());
            d.print("<tr");
            Utils.optAttribute(d, "yweight", determineRowHeight(layout, row));
            if (useCellStyles) {
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
                Utils.printNewline(d, layout.getContainer());
                final boolean headerCell = row == grid.firstRow && header;
                if (comp == null) {
                    openLayouterCell(d, null, headerCell, -1, -1, null, SConstants.CENTER, SConstants.CENTER, styles);
                    closeLayouterCell(d, null, headerCell);
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
                        if (gridwidth < 2)
                            gridwidth = -1;

                        int gridheight = c.gridheight;
                        if (gridheight == GridBagConstraints.RELATIVE) {
                            gridheight = grid.cols - col - 1;
                        } else if (gridheight == GridBagConstraints.REMAINDER) {
                            gridheight = grid.cols - col;
                        }
                        if (gridheight < 2)
                            gridheight = -1;

                        String width = null;
                        if (c.weightx > 0 && grid.colweight[row] > 0)
                            width = (int) (100 * c.weightx / grid.colweight[row]) + "%";

                        String cellStyles = useCellStyles ? cellStyles(layout,  c.insets) : styles;
                        renderHelper.setVerticalLayoutPadding(useCellStyles ? c.insets.top + c.insets.bottom : layout.getVgap());
                        renderHelper.setHorizontalLayoutPadding(useCellStyles ? c.insets.left + c.insets.right : layout.getHgap());
                        openLayouterCell(d, comp, headerCell, gridwidth, gridheight, width, SConstants.CENTER, SConstants.CENTER, cellStyles);

                        Utils.printNewline(d, comp);
                        comp.write(d); // Render component

                        closeLayouterCell(d, comp, headerCell);
                    }
                }
            }
            Utils.printNewline(d, layout.getContainer());
            closeLayouterRow(d);
        }
        closeLayouterBody(d, layout);

        renderHelper.setVerticalLayoutPadding(0);
        renderHelper.setHorizontalLayoutPadding(0);
    }
}
