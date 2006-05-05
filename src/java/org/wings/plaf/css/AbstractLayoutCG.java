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

import org.wings.*;
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
    protected void openLayouterBody(Device d, SLayoutManager layout) throws IOException {
        Utils.printDebugNewline(d, layout.getContainer());
        Utils.printDebug(d, "<!-- START LAYOUT: " + name(layout) + " -->");
        d.print("<tbody>");
    }

    private String name(SLayoutManager layout) {
        String name = layout.getClass().getName();
        int pos = name.lastIndexOf('.');
        if (pos != -1)
            name = name.substring(pos + 1);
        return name;
    }

    /**
     * Counterpart to {@link #openLayouterBody}
     */
    protected void closeLayouterBody(Device d, SLayoutManager layout) throws IOException {
        d.print("</tbody>");
        Utils.printDebugNewline(d, layout.getContainer());
        Utils.printDebug(d, "<!-- END LAYOUT: " + name(layout) + " -->");
    }

    /**
     * Render passed list of components to a table body.
     * Use {@link #openLayouterBody(org.wings.io.Device,org.wings.SLayoutManager)}  in front
     * and {@link #closeLayouterBody(org.wings.io.Device,org.wings.SLayoutManager)} afterwards!
     *
     * @param d                       The device to write to
     * @param cols                    Wrap after this amount of columns
     * @param renderFirstLineAsHeader Render cells in first line as TH-Element or regular TD.
     * @param components              The components to layout
     * @param style
     */
    protected void printLayouterTableBody(Device d, int cols, final boolean renderFirstLineAsHeader,
                                          final List components, String style)
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

            openLayouterCell(d, c, firstRow && renderFirstLineAsHeader, -1, -1, null, SConstants.CENTER, SConstants.CENTER, style);

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


    public static void openLayouterRow(final Device d, String height) throws IOException {
        d.print("<tr");
        Utils.optAttribute(d, "height", height);
        d.print(">");
    }

    /**
     * Closes a TR.
     */
    public static void closeLayouterRow(final Device d) throws IOException {
        d.print("</tr>");
    }

    /**
     * Opens a TD or TH cell of an invisible layouter table. This method also does component alignment.
     * <b>Attention:</b> As you want to attach more attributes you need to close the tag with &gt; on your own!
     *


     @param renderAsHeader Print TH instead of TD
      * @param colspan
     * @param rowspan
     * @param width
     * @param defaultHorizontalAlignment
     * @param defaultVerticalAlignment
     * @param style
     */
    public static void openLayouterCell(final Device d, final SComponent component, final boolean renderAsHeader, int colspan, int rowspan, String width, int defaultHorizontalAlignment, int defaultVerticalAlignment, String style) throws IOException {
        if (renderAsHeader)
            d.print("<th");
        else
            d.print("<td");

        Utils.printTableCellAlignment(d, component, defaultHorizontalAlignment, defaultVerticalAlignment);
        Utils.optAttribute(d, "colspan", colspan);
        Utils.optAttribute(d, "rowspan", rowspan);
        Utils.optAttribute(d, "width", width);
        Utils.optAttribute(d, "style", style);
        d.print(">");
    }

    /**
     * Closes a TD or TH cell of an invisible layouter table.
     *
     * @param renderAsHeader Print TH instead of TD
     */
    public static void closeLayouterCell(final Device d, final boolean renderAsHeader) throws IOException {
        d.print(renderAsHeader ? "</th>" : "</td>");
    }

    protected static String cellStyles(SLayoutManager layout) {
        SStringBuilder styles = new SStringBuilder();
        if (layout instanceof SBorderLayout) {
            SBorderLayout borderLayout = (SBorderLayout)layout;
            Insets insets = convertGapsToInset(borderLayout.getHgap(), borderLayout.getVgap());
            createInlineStylesForInsets(styles, insets);
            if (borderLayout.getBorder() > 0)
                styles.append("border:").append(borderLayout.getBorder()).append("px solid black");
        }
        else if (layout instanceof SGridLayout) {
            SGridLayout gridLayout = (SGridLayout)layout;
            Insets insets = convertGapsToInset(gridLayout.getHgap(), gridLayout.getVgap());
            createInlineStylesForInsets(styles, insets);
            if (gridLayout.getBorder() > 0)
                styles.append("border:").append(gridLayout.getBorder()).append("px solid black");
        }
        else if (layout instanceof SGridBagLayout) {
            SGridBagLayout gridbagLayout = (SGridBagLayout)layout;
            Insets insets = convertGapsToInset(gridbagLayout.getHgap(), gridbagLayout.getVgap());
            createInlineStylesForInsets(styles, insets);
            if (gridbagLayout.getBorder() > 0)
                styles.append("border:").append(gridbagLayout.getBorder()).append("px solid black");
        }
        else if (layout instanceof SBoxLayout) {
            SBoxLayout boxLayout = (SBoxLayout)layout;
            Insets insets = convertGapsToInset(boxLayout.getHgap(), boxLayout.getVgap());
            createInlineStylesForInsets(styles, insets);
            if (boxLayout.getBorder() > 0)
                styles.append("border:").append(boxLayout.getBorder()).append("px solid black");
        }
        return styles.length() > 0 ? styles.toString() : null;
    }
}
