package org.wings.plaf.css.msie;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;

import java.io.IOException;
import java.util.Map;

public class ScrollPaneLayoutCG extends org.wings.plaf.css.ScrollPaneLayoutCG {

    private static final long serialVersionUID = 1L;

    protected void writeNonePaging(Device d, SScrollPaneLayout layout) throws IOException {
        openLayouterBody(d, layout);
        d.print("<tr><td valign=\"top\"><div style=\"display: none;\">");

        Map components = layout.getComponents();
        SComponent center = (SComponent) components.get(SScrollPaneLayout.VIEWPORT);

        writeComponent(d, center);

        d.print("</div></td></tr>");
        closeLayouterBody(d, layout);

        RenderHelper.getInstance(center).addScript("layoutScrollPaneIE('" + layout.getContainer().getName() + "');");
    }

    protected void writePaging(Device d, SScrollPaneLayout layout) throws IOException {
        SDimension preferredSize = layout.getContainer().getPreferredSize();
        if (preferredSize == null || preferredSize.getHeight() == null ||
                SDimension.AUTO.equals(preferredSize.getHeight())) {
            super.writePaging(d, layout);
            return;
        }

        // special implementation with expressions is only required, if the center
        // component shall consume the remaining height

        Map components = layout.getComponents();
        SComponent north = (SComponent) components.get(SScrollPaneLayout.NORTH);
        SComponent east = (SComponent) components.get(SScrollPaneLayout.EAST);
        SComponent center = (SComponent) components.get(SScrollPaneLayout.VIEWPORT);
        SComponent west = (SComponent) components.get(SScrollPaneLayout.WEST);
        SComponent south = (SComponent) components.get(SScrollPaneLayout.SOUTH);

        openLayouterBody(d, layout);

        if (north != null) {
            d.print("<tr>");
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

        d.print("<tr yweight=\"100\">");
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
            d.print("<tr>");
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
}
