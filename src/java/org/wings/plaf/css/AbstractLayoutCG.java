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
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract super class for layout CGs using invisible tables to arrange their contained components.
 *
 * @author bschmid
 */
public abstract class AbstractLayoutCG implements LayoutCG {

    /**
     * Print HTML table element declaration of a typical invisible layouter table.
     */
    protected void printLayouterTableHeader(Device d, String styleClass, int hgap, int vgap,
                                            int border, SLayoutManager layout)
            throws IOException {
        Utils.printDebugNewline(d, layout.getContainer());
        Utils.printDebug(d, "<!-- START LAYOUT: ").print(styleClass).print(" -->");

        // Generate CSS Inline Style
        // we don't need to do that here once we have set all layoutmanagers
        // to 100% width/height
        StringBuffer styleString = Utils.generateCSSInlinePreferredSize(layout.getContainer().getPreferredSize());
        styleString.append(Utils.generateCSSInlineBorder(border));
        styleString.append(createInlineStylesForGaps(hgap, vgap));

        Utils.printNewline(d, layout.getContainer());
        d.print("<table ");
        /* This won't work any longer as we override padding/spacing with default SLayout styles class
        d.print(" cellspacing=\"").print(cellSpacing < 0 ? 0 : cellSpacing).print("\"");
        d.print(" cellpadding=\"").print(cellPadding < 0 ? 0 : cellPadding).print("\""); */
        Utils.optAttribute(d, "class", styleClass != null ? styleClass + " SLayout" : "SLayout");
        Utils.optAttribute(d, "style", styleString.toString());
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
     *
     * @param d                       The device to write to
     * @param cols                    Wrap after this amount of columns
     * @param renderFirstLineAsHeader Render cells in first line as TH-Element or regular TD.
     * @param components              The components to layout
     * @param hgap                    Horizontal gap between components in px
     * @param vgap                    Vertical gap between components in px
     * @param border                  Border width to draw.
     */
    protected void printLayouterTableBody(Device d, int cols, final boolean renderFirstLineAsHeader,
                                          int hgap, int vgap, int border, final List components)
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

            openLayouterCell(d, firstRow && renderFirstLineAsHeader, hgap, vgap, border, c);
            d.print(">");

            Utils.printNewline(d, c);
            c.write(d); // Render component

            closeLayouterCell(d, firstRow && renderFirstLineAsHeader);

            col++;

            if (!iter.hasNext()) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
            }
        }
    }

    /**
     * Converts a hgap/vgap in according inline css padding style.
     *
     * @param hgap Horizontal gap between components in px
     * @param vgap Vertical gap between components in px
     */
    protected static StringBuffer createInlineStylesForGaps(int hgap, int vgap) {
        StringBuffer inlineStyle = new StringBuffer();
        if (hgap > 0 || vgap > 0) {
            int hPaddingTop = (int) Math.round((vgap < 0 ? 0 : vgap) / 2.0);
            int hPaddingBottom = (int) Math.round((vgap < 0 ? 0 : vgap) / 2.0 + 0.1); // round up
            int vPaddingLeft = (int) Math.round((hgap < 0 ? 0 : hgap) / 2.0);
            int vPaddingRight = (int) Math.round((hgap < 0 ? 0 : hgap) / 2.0 + 0.1); // round up
            if (hPaddingBottom == hPaddingTop && hPaddingTop == vPaddingRight && vPaddingRight == vPaddingLeft)
                inlineStyle.append("padding:").append(hPaddingTop).append("px;");
            else
                inlineStyle.append("padding:").append(hPaddingTop).append("px ").append(vPaddingRight).append("px ")
                        .append(hPaddingBottom).append("px ").append(vPaddingLeft).append("px;");
        }
        return inlineStyle;
    }

    /**
     * Opens a TD or TH cell of an invisible layouter table. This method also does component alignment.
     * <b>Attention:</b> As you want to attach more attributes you need to close the tag with &gt; on your own!
     *
     * @param renderAsHeader Print TH instead of TD
     */
    public static void openLayouterCell(Device d, boolean renderAsHeader, int hgap, int vgap, int border,
                                        SComponent containedComponent) throws IOException {
        if (renderAsHeader)
            d.print("<th");
        else
            d.print("<td");

        d.print(" class=\"SLayout\"");
        Utils.printTableCellAlignment(d, containedComponent);

        // CSS inline attributes
        StringBuffer inlineAttributes = Utils.generateCSSInlineBorder(border);
        inlineAttributes.append(createInlineStylesForGaps(hgap, vgap));
        Utils.optAttribute(d, "style", inlineAttributes.toString());
    }

    /**
     * Closes a TD or TH cell of an invisible layouter table.
     *
     * @param renderAsHeader Print TH instead of TD
     */
    public static void closeLayouterCell(Device d, boolean renderAsHeader) throws IOException {
        d.print(renderAsHeader ? "</th>" : "</td>");
    }
}
