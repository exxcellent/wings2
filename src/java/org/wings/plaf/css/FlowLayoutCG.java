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
import org.wings.SConstants;
import org.wings.SFlowLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.List;

public class FlowLayoutCG extends AbstractLayoutCG implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l) throws IOException {
        SFlowLayout layout = (SFlowLayout) l;
        List components = layout.getComponents();
        int orientation = layout.getOrientation();

        if (components.size() > 0) {
            // Horizontal alignment
            d.print("\n<div");
            org.wings.plaf.css.Utils.printDivHorizontalAlignment(d, layout.getAlignment());
            d.print(">\n");

            int count = 0;
            for (int i = 0; i < components.size(); i++) {
                SComponent comp = (SComponent) components.get(i);
                if (comp.isVisible()) {
                    if (count == 0) {
                        printLayouterTableHeader(d, "SFlowLayout", 0, 0, 0, layout);
                        d.print("<tr><td");
                    } else if (orientation == SConstants.VERTICAL)
                        d.print("</td></tr>\n<tr><td");
                    else
                        d.print("</td><td");

                    org.wings.plaf.css.Utils.printTableCellAlignment(d, comp);
                    //org.wings.plaf.css.Utils.printCSSInlineStyleAttributes(d, comp);

                    d.print(">");
                    comp.write(d); // Render component itself
                    count++;
                }
            }
            if (count > 0) {
                d.print("</td></tr>");
                printLayouterTableFooter(d, "SFlowLayout", layout);
            }

            d.print("\n</div>\n"); // close hozrontal alignment
        }
    }
}


