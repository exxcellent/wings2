/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
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
import java.util.List;

public class FlowLayoutCG
    implements LayoutCG {
    /**
     * TODO: documentation
     *
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
        throws IOException {
        SFlowLayout layout = (SFlowLayout)l;
        List components = layout.getComponents();
        int orientation = layout.getOrientation();
        int alignment = layout.getAlignment();
        SComponent container = (SComponent)layout.getContainer();

        if (components.size() > 0) {
            switch (alignment) {
                case SConstants.RIGHT_ALIGN:
                    d.print("\n<div align=\"right\">");
                    break;
                case SConstants.CENTER_ALIGN:
                    d.print("\n<div align=\"center\">");
                    break;
            }

            int count = 0;
            for (int i = 0; i < components.size(); i++) {
                SComponent comp = (SComponent)components.get(i);
                if (comp.isVisible()) {
                    if (count == 0) {
                        d.print("<table cellpadding=\"0\" cellspacing=\"0\"");
                        // CGUtil.writeSize( d, container );
                        if (Utils.hasSpanAttributes(container)) {
                            d.print(" style=\"");
                            Utils.writeSpanAttributes(d, (SComponent)container);
                            d.print("\" ");
                        }

                        d.print("><tr><td");
                    }
                    else if (orientation == SConstants.VERTICAL)
                        d.print("</td></tr>\n<tr><td");
                    else
                        d.print("</td><td");

                    SComponent c = ((SComponent)components.get(i));
                    Utils.printTableCellAlignment(d, c);
                    if (c instanceof SContainer && Utils.hasSpanAttributes(c)) {
                        // Adapt inner styles (esp. width of containers)
                        // maybe better restrict to dimension styles only?
                        d.print(" style=\"");
                        Utils.writeAttributes(d, c);
                        d.print("\"");

                        // Some containers (like SPanel) do not support
                        // background colors, hence we render the background
                        // of them using this surrounding gridlayout cell
                        // Utils.printTableCellColors(d, c);

                    }
                    d.print(">");

                    c.write(d);
                    count++;
                }
            }
            if (count > 0)
                d.print("</td></tr></table>\n");

            switch (alignment) {
                case SConstants.RIGHT_ALIGN:
                case SConstants.CENTER_ALIGN:
                    System.out.println("Divv FlowLayout");
                    d.print("\n</div>");
                    break;
            }

        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
