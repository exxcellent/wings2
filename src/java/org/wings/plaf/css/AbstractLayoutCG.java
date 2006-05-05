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
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;
import org.wings.util.SStringBuilder;

import java.awt.*;
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
    protected void printLayouterTableHeader(Device d, String styleClass, Insets insets,
                                            int border, SLayoutManager layout)
            throws IOException {
        Utils.printDebugNewline(d, layout.getContainer());
        Utils.printDebug(d, "<!-- START LAYOUT: ").print(styleClass).print(" -->");

        // Generate CSS Inline Style
        // we don't need to do that here once we have set all layoutmanagers
        // to 100% width/height

        Utils.printNewline(d, layout.getContainer());
        d.print("<table ");
        /* This won't work any longer as we override padding/spacing with default SLayout styles class
        d.print(" cellspacing=\"").print(cellSpacing < 0 ? 0 : cellSpacing).print("\"");
        d.print(" cellpadding=\"").print(cellPadding < 0 ? 0 : cellPadding).print("\""); */
        Utils.optAttribute(d, "class", styleClass);
        Utils.optAttribute(d, "style", getInlineStyles(layout, border, insets));
        d.print("><tbody>");
        Utils.printNewline(d, layout.getContainer());
    }

    /**
     * Write the inline style attribute
     * @throws IOException
     */
    protected SStringBuilder getInlineStyles(SLayoutManager layout, int border, Insets insets) throws IOException {
        final SStringBuilder styles = new SStringBuilder();
        // write inline styles
        Utils.generateCSSInlinePreferredSize(styles,layout.getContainer().getPreferredSize());
        Utils.generateCSSInlineBorder(styles, border);
        return styles;
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
     * Use {@link #printLayouterTableHeader(org.wings.io.Device, String, java.awt.Insets, int, org.wings.SLayoutManager)}  in front
     * and {@link #printLayouterTableFooter(org.wings.io.Device, String, org.wings.SLayoutManager)} afterwards!
     *
     * @param d                       The device to write to
     * @param cols                    Wrap after this amount of columns
     * @param renderFirstLineAsHeader Render cells in first line as TH-Element or regular TD.
     * @param components              The components to layout
     * @param insets                  Layouter cell insets in px
     * @param border                  Border width to draw.
     */
    protected void printLayouterTableBody(Device d, int cols, final boolean renderFirstLineAsHeader,
                                          Insets insets, int border, final List components)
            throws IOException {
        boolean firstRow = true;
        int col = 0;
        for (Iterator iter = components.iterator(); iter.hasNext();) {
            final SComponent c = (SComponent) iter.next();

            if (col == 0) {
                d.print("<tr>");
            } else if (col % cols == 0) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
                d.print("<tr>");
                firstRow = false;
            }

            openLayouterCell(d, firstRow && renderFirstLineAsHeader, insets, border, c);
            d.print(">");

            //MSIEUtils.printNewline(d, c);
            c.write(d); // Render component

            closeLayouterCell(d, c, firstRow && renderFirstLineAsHeader);

            col++;

            if (!iter.hasNext()) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
            }
        }
    }

    /**
     * Converts a hgap/vgap in according inline css padding style.
     * @param styles
     *
     * @param insets The insets to generate CSS padding declaration
     * @return Empty or fille stringbuffer with padding declaration
     */
    protected static SStringBuilder createInlineStylesForInsets(SStringBuilder styles, Insets insets) {
        if (insets != null && (insets.top > 0 || insets.left > 0 || insets.right > 0 || insets.bottom > 0)) {
            if (insets.top == insets.left && insets.left == insets.right && insets.right == insets.bottom) {
                // Trivial style: all the same
                styles.append("padding:").append(insets.top).append("px;");
            } else {
                styles.append("padding:").append(insets.top).append("px ").append(insets.right).append("px ")
                        .append(insets.bottom).append("px ").append(insets.left).append("px;");
            }
        }
        return styles;
    }

    /**
     * Converts a hgap/vgap in according inset declaration.
     * If a gapp is odd, the overlapping additonal pixel is added to the right/bottom inset.
     *
     * @param hgap Horizontal gap between components in px
     * @param vgap Vertical gap between components in px
     * @return An inset equal to the gap declarations
     */
    protected static Insets convertGapsToInset(int hgap, int vgap) {
        Insets insets = null;
        if (hgap > 0 || vgap > 0) {
            final int hPaddingTop = (int) Math.round((vgap < 0 ? 0 : vgap) / 2.0);
            final int hPaddingBottom = (int) Math.round((vgap < 0 ? 0 : vgap) / 2.0 + 0.1); // round up
            final int vPaddingLeft = (int) Math.round((hgap < 0 ? 0 : hgap) / 2.0);
            final int vPaddingRight = (int) Math.round((hgap < 0 ? 0 : hgap) / 2.0 + 0.1); // round up
            insets = new Insets(hPaddingTop, vPaddingLeft, hPaddingBottom, vPaddingRight);
        }
        return insets;
    }


    /**
     * Opens a TD or TH cell of an invisible layouter table. This method also does component alignment.
     * <b>Attention:</b> As you want to attach more attributes you need to close the tag with &gt; on your own!
     *
     * @param renderAsHeader Print TH instead of TD
     */
    public static void openLayouterCell(final Device d, final boolean renderAsHeader, final Insets insets,
                                        final int border, final SComponent containedComponent) throws IOException {
        if (renderAsHeader) {
            d.print("<th");
        } else {
            d.print("<td");
        }

        Utils.printTableCellAlignment(d, containedComponent, SConstants.TOP, SConstants.LEFT);

        // CSS inline attributes
        SStringBuilder inlineAttributes = new SStringBuilder();
            Utils.generateCSSInlineBorder(inlineAttributes, border);
        createInlineStylesForInsets(inlineAttributes, insets);
        Utils.optAttribute(d, "style", inlineAttributes.toString());
    }

    /**
     * Closes a TD or TH cell of an invisible layouter table.
     *
     * @param renderAsHeader Print TH instead of TD
     */
    public static void closeLayouterCell(final Device d, final SComponent c, final boolean renderAsHeader) throws IOException {
        d.print(renderAsHeader ? "</th>" : "</td>");
        Utils.printNewline(d, c);
    }
}
