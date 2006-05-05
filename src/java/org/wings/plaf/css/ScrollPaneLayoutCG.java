/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;

import org.wings.*;
import org.wings.io.Device;
import org.wings.util.SStringBuilder;

import java.io.IOException;
import java.awt.Rectangle;
import java.util.*;

public class ScrollPaneLayoutCG extends AbstractLayoutCG {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void write(Device d, SLayoutManager l)
            throws IOException {
        SScrollPaneLayout layout = (SScrollPaneLayout) l;

        if (layout.isPaging()) {
            writePaging(d, layout);
        } else {
            writeNonePaging(d, layout);
        }
    }

    private void writeNonePaging(Device d, SScrollPaneLayout layout) throws IOException {
        openLayouterBody(d, layout);
        d.print("<tr><td>");
        Map components = layout.getComponents();
        SComponent center = (SComponent) components.get(SScrollPaneLayout.VIEWPORT);
        Scrollable scrollable = (Scrollable) center;
        Rectangle viewportSize = scrollable.getViewportSize();
        Rectangle scrollableViewportSize = scrollable.getScrollableViewportSize();
        scrollable.setViewportSize(scrollableViewportSize);
        writeComponent(d, center);
        scrollable.setViewportSize(viewportSize);

        d.print("</td></tr>");
        closeLayouterBody(d, layout);
    }

    private void writePaging(Device d, SScrollPaneLayout layout) throws IOException {
        Map components = layout.getComponents();
        SComponent north = (SComponent) components.get(SScrollPaneLayout.NORTH);
        SComponent east = (SComponent) components.get(SScrollPaneLayout.EAST);
        SComponent center = (SComponent) components.get(SScrollPaneLayout.VIEWPORT);
        SComponent west = (SComponent) components.get(SScrollPaneLayout.WEST);
        SComponent south = (SComponent) components.get(SScrollPaneLayout.SOUTH);

        openLayouterBody(d, layout);

        if (north != null) {
            d.print("<tr height=\"0%\">");
            if (west != null) {
                d.print("<td width=\"0%\"></td>");
            }

            d.print("<td width=\"100%\">");
            writeComponent(d, north);
            d.print("</td>");

            if (east != null) {
                d.print("<td width=\"0%\"></td>");
            }
            d.print("</tr>\n");
        }

        d.print("<tr height=\"100%\">");
        if (west != null) {
            d.print("<td width=\"0%\">");
            writeComponent(d, west);
            d.print("</td>");
        }
        if (center != null) {
            d.print("<td width=\"100%\"");
            Utils.printTableCellAlignment(d, center, SConstants.LEFT_ALIGN, SConstants.TOP_ALIGN);
            d.print(">");
            writeComponent(d, center);
            d.print("</td>");
        }
        if (east != null) {
            d.print("<td width=\"0%\">");
            writeComponent(d, east);
            d.print("</td>");
        }
        d.print("</tr>\n");

        if (south != null) {
            d.print("<tr height=\"0%\">");
            if (west != null) {
                d.print("<td width=\"0%\"></td>");
            }

            d.print("<td width=\"100%\">");
            writeComponent(d, south);
            d.print("</td>");

            if (east != null) {
                d.print("<td width=\"0%\"></td>");
            }
            d.print("</tr>\n");
        }

        closeLayouterBody(d, layout);
    }

    protected void writeComponent(Device d, SComponent c)
            throws IOException {
        c.write(d);
    }
}


