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

import org.wings.SLayoutManager;
import org.wings.SComponent;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * Abstract super class for layout CGs using invisible tables to arrange their contained components.
 *
 * @author bschmid
 */
public abstract class AbstractLayoutCG implements LayoutCG {

    /**
     * Print HTML table element declaration of a typical invisible layouter table.
     */
    protected void printLayouterTableHeader(Device d, String styleClass, int cellSpacing, int cellPadding,
                                            int border, SLayoutManager layout)
            throws IOException {
        Utils.printDebugNewline(d, layout.getContainer());
        Utils.printDebug(d, "<!-- START LAYOUT: ").print(styleClass).print(" -->");

        Utils.printNewline(d, layout.getContainer());
        d.print("<table ");
        d.print(" cellspacing=\"").print(cellSpacing < 0 ? 0 : cellSpacing).print("\"");
        d.print(" cellpadding=\"").print(cellPadding < 0 ? 0 : cellPadding).print("\"");
        d.print(" border=\"").print(border < 0 ? 0 : border).print("\"");
        Utils.optAttribute(d, "class", styleClass);
        // we don't need to do that here once we have set all layoutmanagers
        // to 100% width/height 
        Utils.printCSSInlinePreferredSize(d, layout.getContainer().getPreferredSize());
        d.print("><tbody>");
        Utils.printNewline(d, layout.getContainer());
    }

    /**
     * Counterpart to {@link #printLayouterTableHeader}
     */
    protected void printLayouterTableFooter(Device d, String styleClass, SLayoutManager layout) throws IOException {
        Utils.printNewline(d, layout.getContainer());
        d.print("</tbody></table>");

        Utils.printDebugNewline(d, layout.getContainer());
        Utils.printDebug(d, "<!-- END LAYOUT: ").print(styleClass).print(" -->");
    }

    /**
     * Render passed list of components to a table body.
     * Use {@link #printLayouterTableHeader(org.wings.io.Device, String, int, int, int, org.wings.SLayoutManager)} in front
     * and {@link #printLayouterTableFooter(org.wings.io.Device, String, org.wings.SLayoutManager)} afterwards!
     * @param d The device to write to
     * @param cols Wrap after this amount of columns
     * @param renderFirstLineAsHeader Render cells in first line as TH-Element or regular TD.
     * @param components The components to layout
     */
    protected void printLayouterTableBody(Device d, int cols, final boolean renderFirstLineAsHeader, final List components)
            throws IOException {
        boolean firstRow = true;
        int col = 0;
        for (Iterator iter = components.iterator(); iter.hasNext();) {
            final SComponent c = (SComponent) iter.next();

            if (col == 0)
                d.print("<tr>");
            else if (col % cols == 0) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
                d.print("<tr>");
                firstRow = false;
            }


            if (firstRow && renderFirstLineAsHeader)
                d.print("<th");
            else
                d.print("<td");

            Utils.printTableCellAlignment(d, c);
            Utils.printCSSInlineStyleAttributes(d, c);
            d.print(">");

            Utils.printNewline(d, c);            
            c.write(d); // Render component

            if (firstRow && renderFirstLineAsHeader)
                d.print("</th>");
            else
                d.print("</td>");

            col++;

            if (!iter.hasNext()){
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
            }
        }
    }
}
