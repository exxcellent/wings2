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
import org.wings.SContainer;
import org.wings.SFlowLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.awt.*;

public class FlowLayoutCG extends AbstractLayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l) throws IOException {
        final SFlowLayout layout = (SFlowLayout) l;
        final Insets insets = convertGapsToInset(layout.getHgap(), layout.getVgap());        
        final List components = layout.getComponents();
        final int alignment = layout.getAlignment();
        final SContainer container = layout.getContainer();

        Utils.printNewline(d, container);
        d.print("<div");
        Utils.printDivHorizontalAlignment(d, alignment != SConstants.NO_ALIGN ? alignment : SConstants.LEFT_ALIGN);
        if (alignment == SConstants.CENTER) {
            // Cheat -- margin left/right to simulate center float. Will not wrap
            d.print(" style=\"display:table; margin-left:auto; margin-right:auto;\"");
        } else {
            d.print(" style=\"display:table; width:100%;\""); // gecko bug workaround: inherit surrounding panel bg color.
        }
        d.print(" class=\"SFlowLayout\">");

        final String alignmentStyle;
        if (alignment == SConstants.LEFT) {
            alignmentStyle = "float:left;";
        } else if (alignment == SConstants.RIGHT) {
            alignmentStyle = "float:right;";
        } else if (alignment == SConstants.CENTER) {
            alignmentStyle = "float:left; "; // Floating does not work with center :-(
        } else {
            alignmentStyle = "";
        }

        if (components.size() > 1) {
            /* We need two spacer divs (end/beginning) so that the sourrounding flow layout takes up
               the whole space instead of 0px heigth. See http://www.alistapart.com/articles/practicalcss/. 
               The nbsp's are only needed for firefox... */
            d.print("<div class=\"spacer\"></div>");

            for (Iterator componentIterator = components.iterator(); componentIterator.hasNext();) {
                final SComponent component = (SComponent) componentIterator.next();
                if (component.isVisible()) {
                    Utils.printNewline(d, component);
                    d.print("<div style=\"");
                    d.print(alignmentStyle);
                    d.print( createInlineStylesForInsets(insets).toString() );
                    d.print("\">");
                    component.write(d); // Render contained component
                    Utils.printNewline(d, component);
                    d.print("</div>");
                }
            }

            /* Second spacer. See upper. */
            d.print("<div class=\"spacer\"></div>");
        } else if (components.size() == 1) {
            // Special handling of trivial case
            //
            // Why: FlowLayout may be default for many containers.
            // BUT: The effect is that in MSIE oversized, floating componentns DO NOT lead to a scrollbar
            //
            // Try this:
            //    <TABLE><TR style="background-color:yellow; border:5px solid red" ><TD>
            //        <DIV style="WIDTH: 100%" align=left >
            //          <DIV class=spacer></DIV>
            //      <DIV style="FLOAT: left; border:2px solid blue;">
            //     <table width="1000" heigth=10 bgcolor="#ff000"><tr>
            //     <td align=center>1000px widht</td></tr></table>
            //      </div>
            //      <DIV class=spacer></DIV>
            //     </div>
            //    </td></tr></table>
            final SComponent component = (SComponent) components.get(0);
            if (component.isVisible()) {
                Utils.printNewline(d, component);
                d.print("<div style=\"").print(createInlineStylesForInsets(insets).toString()).print("\">");
                component.write(d); // Render contained component
                Utils.printNewline(d, component);
                d.print("</div>");
            }
        }
        Utils.printNewline(d, container);
        d.print("</div>");
    }
}


