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
package org.wings.plaf.css.msie;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

import java.io.IOException;
import java.util.List;

/**
 * A special CG for the IE handling some special case the IE cannot hande via CSS.
 *
 * @author bschmid
 */
public class FlowLayoutCG extends org.wings.plaf.css.FlowLayoutCG {
   /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws java.io.IOException
     */
    public void write(Device d, SLayoutManager l) throws IOException {
       final SFlowLayout layout = (SFlowLayout) l;
       final int alignment = layout.getAlignment();
       final int orientation = layout.getOrientation();

       if (alignment == SConstants.CENTER && orientation == SConstants.HORIZONTAL) {
           final List components = layout.getComponents();
           final SContainer container = layout.getContainer();

           Utils.printNewline(d, container);
           d.print("<table");
           Utils.printDivHorizontalAlignment(d, alignment);
           d.print(" class=\"SFlowLayout\">");

            int count = 0;
            for (int i = 0; i < components.size(); i++) {
                SComponent comp = (SComponent) components.get(i);
                if (comp.isVisible()) {
                    if (count == 0) {
                        printLayouterTableHeader(d, null, 0, 0, 0, layout);
                        d.print("<tr><td");
                    } /*else if (orientation == SConstants.VERTICAL)
                        d.print("</td></tr>\n<tr><td"); */
                    else
                        d.print("</td><td");

                    Utils.printTableCellAlignment(d, comp);
                    d.print(">");
                    comp.write(d); // Render component itself
                    count++;
                }
            }
            if (count > 0) {
                d.print("</td></tr>");
                printLayouterTableFooter(d, "SFlowLayout", layout);
            }

           Utils.printNewline(d, container);
           d.print("</table>");
       } else {
           // For all other cases the default implementation should work. 
           super.write(d, l);
       }
    }
}
