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
package org.wings.plaf.xhtml;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class BoxLayoutCG implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        SBoxLayout layout = (SBoxLayout) l;
        SContainer container = layout.getContainer();
        List components = layout.getComponents();
        SDimension dim = layout.getPreferredSize();


        d.print("\n<table ");
        if (Utils.hasSpanAttributes(container)) {
            d.print("style=\"");
            Utils.writeSpanAttributes(d, (SComponent) container);
            d.print("\" ");
        }

        org.wings.plaf.css.Utils.printTableHorizontalAlignment(d, layout.getHorizontalAlignment());
        org.wings.plaf.css.Utils.printTableVerticalAlignment(d, layout.getVerticalAlignment());
        d.print(" cellspacing=\"0\" cellpadding=\"0\">\n");


        if (layout.getOrientation() == SBoxLayout.X_AXIS) {
            d.print("<tr>");
            for (Iterator iter = components.iterator(); iter.hasNext();) {

                SComponent c = (SComponent) iter.next();

                d.print("<td");

                Utils.printTableCellAlignment(d, c);
                if (c instanceof SContainer && c.isVisible() && Utils.hasSpanAttributes(c)) {
                    // Adapt inner styles (esp. width of containers)
                    // maybe better restrict to dimension styles only?
                    d.print(" style=\"");
                    Utils.writeAttributes(d, c);
                    d.print("\"");

                }
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
                if (c instanceof SContainer && c.isVisible() && Utils.hasSpanAttributes(c)) {
                    // Adapt inner styles (esp. width of containers)
                    // maybe better restrict to dimension styles only?
                    d.print(" style=\"");
                    Utils.writeAttributes(d, c);
                    d.print("\"");

                }
                d.print(">");

                c.write(d);

                d.print("</td>");

                d.print("</tr>\n");
            }
        }
        d.print("</table>");
    }
}


