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

import org.wings.SBoxLayout;
import org.wings.SComponent;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class BoxLayoutCG extends AbstractLayoutCG implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        SBoxLayout layout = (SBoxLayout) l;
        List components = layout.getComponents();

        printLayouterTableHeader(d, "SBoxLayout",0,0,0,layout);
        //Utils.printTableHorizontalAlignment(d, layout.getHorizontalAlignment());
        //Utils.printTableVerticalAlignment(d, layout.getVerticalAlignment());

        if (layout.getOrientation() == SBoxLayout.X_AXIS) {
            d.print("<tr>");
            for (Iterator iter = components.iterator(); iter.hasNext();) {

                SComponent c = (SComponent) iter.next();

                d.print("<td");
                Utils.printTableCellAlignment(d, c);
                Utils.printCSSInlineStyleAttributes(d, c);
                d.print(">");

                c.write(d);


                d.print("</td>");

            }
            d.print("</tr>\n");

        } else {   /* This Should be the Y-Axis :) */
            for (Iterator iter = components.iterator(); iter.hasNext();) {
                d.print("<tr>");
                SComponent c = (SComponent) iter.next();

                d.print("<td");

                Utils.printTableCellAlignment(d, c);
                Utils.printCSSInlineStyleAttributes(d, c);
                d.print(">");

                c.write(d);

                d.print("</td>");

                d.print("</tr>\n");
            }
        }
        d.print("</table>");
    }
}


