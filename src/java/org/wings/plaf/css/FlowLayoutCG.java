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

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FlowLayoutCG extends AbstractLayoutCG implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l) throws IOException {
        final SFlowLayout layout = (SFlowLayout) l;
        final List components = layout.getComponents();
        final int alignment = layout.getAlignment();
        final int orientation = layout.getOrientation();
        final SContainer container = layout.getContainer();

        Utils.printNewline(d, container);
        d.print("<div");
        Utils.printDivHorizontalAlignment(d, alignment);
        if (alignment == SConstants.CENTER)
            d.print(" style=\"display:table; margin-left:auto; margin-right:auto;\"");
        d.print(" class=\"SFlowLayout\">");

        final String alignmentStyle;
        if (orientation == SConstants.HORIZONTAL) {
            if (alignment == SConstants.LEFT)
                alignmentStyle = "float:left;";
            else if (alignment == SConstants.RIGHT)
                alignmentStyle = "float:right;";
            else if (alignment == SConstants.CENTER)
                alignmentStyle = "float:left; "; // Floating does not work with center :-(
            else
                alignmentStyle = "";
        } else {
            alignmentStyle = "display:block;"; // Vertical align does not support floating!
        }

        if (components.size() > 0) {
            /* We need two spacer divs (end/beginning) so that the sourrounding flow layout takes up
               the whole space instead of 0px heigth. See http://www.alistapart.com/articles/practicalcss/ */
            d.print("<div style=\"clear:both;\"></div>");

            for (Iterator componentIterator = components.iterator(); componentIterator.hasNext();) {
                SComponent component = (SComponent) componentIterator.next();
                if (component.isVisible()) {
                    Utils.printNewline(d, component);
                    d.print("<div style=\"");
                    d.print(alignmentStyle);
                    d.print("\">");
                    component.write(d); // Render contained component
                    Utils.printNewline(d, component);
                    d.print("</div>");
                }
            }

            /* Second spacer. See upper. */
            d.print("<div style=\"clear:both;\"></div>");
        }
        Utils.printNewline(d, container);
        d.print("</div>");

        // TODO Move this to a special IE implementation for CENTER case
        /*  int count = 0;
         for (int i = 0; i < components.size(); i++) {
             SComponent comp = (SComponent) components.get(i);
             if (comp.isVisible()) {
                 if (count == 0) {
                     printLayouterTableHeader(d, "", 0, 0, 0, layout);
                     d.print("<tr><td");
                 } else if (orientation == SConstants.VERTICAL)
                     d.print("</td></tr>\n<tr><td");
                 else
                     d.print("</td><td");

                 Utils.printTableCellAlignment(d, comp);
                 //Utils.printCSSInlineStyleAttributes(d, comp);

                 d.print(">");
                 comp.write(d); // Render component itself
                 count++;
             }
         }
         if (count > 0) {
             d.print("</td></tr>");
             printLayouterTableFooter(d, "SFlowLayout", layout);
         } */
    }
}


