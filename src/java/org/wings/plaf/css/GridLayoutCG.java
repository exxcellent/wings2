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
import org.wings.SGridLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class GridLayoutCG extends AbstractLayoutCG
        implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SGridLayout layout = (SGridLayout) l;
        final List components = layout.getComponents();
        final int rows = layout.getRows();
        int cols = layout.getColumns();
        if (cols <= 0)
            cols = components.size() / rows;

        printLayouterTableHeader(d, "SGridLayout", layout.getCellSpacing(), layout.getCellPadding(), layout.getBorder(), layout);

        boolean firstRow = true;
        int col = 0;
        for (Iterator iter = components.iterator(); iter.hasNext();) {
            if (col == 0)
                d.print("<tr>");
            else if (col % cols == 0 && iter.hasNext()) {
                d.print("</tr>");
                Utils.printNewline(d, layout.getContainer());
                d.print("<tr>");
                firstRow = false;
            }

            SComponent c = (SComponent) iter.next();

            if (firstRow && layout.getRenderFirstLineAsHeader())
                d.print("<th");
            else
                d.print("<td");

            Utils.printTableCellAlignment(d, c);
            Utils.printCSSInlineStyleAttributes(d, c);
            d.print(">");

            c.write(d); // Render component

            if (firstRow && layout.getRenderFirstLineAsHeader())
                d.print("</th>");
            else
                d.print("</td>");

            col++;

            if (!iter.hasNext()){
                d.print("</tr>");
                Utils.printNewline(d, layout.getContainer());
            }
        }

        printLayouterTableFooter(d, "SGridLayout", layout);
    }
}
