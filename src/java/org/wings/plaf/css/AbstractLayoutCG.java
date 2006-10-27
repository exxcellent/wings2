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

import java.awt.Insets;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.wings.*;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

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
                Utils.printNewline(d, c.getParent());
                d.print("<tr>");
            } else if (col % cols == 0) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
                d.print("<tr>");
                firstRow = false;
            }

            openLayouterCell(d, c, firstRow && renderFirstLineAsHeader, -1, -1, null,
                    getDefaultLayoutCellHAlignment(), getDefaultLayoutCellVAlignment(), style);

            c.write(d); // Render component

            closeLayouterCell(d, c, firstRow && renderFirstLineAsHeader);

            col++;

            if (!iter.hasNext()) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
            }
        }
    }

    /** The default horizontal alignment for components inside a layout cell. */
    public abstract int getDefaultLayoutCellHAlignment();
    /** The default vertical alignment for components inside a layout cell. */
    public abstract int getDefaultLayoutCellVAlignment();

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
        if (hgap > -1 || vgap > -1) {
            final int paddingTop = (int) Math.round((vgap < 0 ? 0 : vgap) / 2.0);
            final int paddingBottom = (int) Math.round((vgap < 0 ? 0 : vgap) / 2.0 + 0.1); // round up
            final int paddingLeft = (int) Math.round((hgap < 0 ? 0 : hgap) / 2.0);
            final int paddingRight = (int) Math.round((hgap < 0 ? 0 : hgap) / 2.0 + 0.1); // round up
            insets = new Insets(paddingTop, paddingLeft, paddingBottom, paddingRight);
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
     * @param renderAsHeader Print TH instead of TD
     */
    public static void openLayouterCell(final Device d, final SComponent component, final boolean renderAsHeader, int colspan, int rowspan, String width, int defaultHorizontalAlignment, int defaultVerticalAlignment, String style) throws IOException {
        Utils.printNewline(d, component);
        if (renderAsHeader)
            d.print("<th");
        else
            d.print("<td");

        int oversizePadding = Utils.calculateHorizontalOversize(component, true);
        oversizePadding += (component != null ? RenderHelper.getInstance(component).getHorizontalLayoutPadding() : 0);

        if (oversizePadding != 0)
            Utils.optAttribute(d, "oversize", oversizePadding);

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
    public static void closeLayouterCell(final Device d, final SComponent component, final boolean renderAsHeader) throws IOException {
        Utils.printNewline(d, component);
        d.print(renderAsHeader ? "</th>" : "</td>");
    }

    protected abstract int getLayoutHGap(SLayoutManager layout);
    protected abstract int getLayoutVGap(SLayoutManager layout);
    protected abstract int getLayoutBorder(SLayoutManager layout);

    protected String layoutStyles(SLayoutManager layout) {
        SStringBuilder styles = new SStringBuilder();
        Insets insets = convertGapsToInset(getLayoutHGap(layout), getLayoutVGap(layout));
        Utils.createInlineStylesForInsets(styles, insets);
        if (getLayoutBorder(layout) > 0)
            styles.append("border:").append(getLayoutBorder(layout)).append("px solid black");

        return styles.length() > 0 ? styles.toString() : null;
    }

    protected static String cellStyles(SGridBagLayout layout, Insets insets) {
        SStringBuilder styles = new SStringBuilder();
        Utils.createInlineStylesForInsets(styles, insets);
        if (layout.getBorder() > 0)
            styles.append("border:").append(layout.getBorder()).append("px solid black");
        return styles.length() > 0 ? styles.toString() : null;
    }

    protected abstract int layoutOversize(SLayoutManager layout);

    protected static int cellOversize(SGridBagLayout layout, Insets insets) {
        return insets.top + insets.bottom + layout.getBorder();
    }
}
