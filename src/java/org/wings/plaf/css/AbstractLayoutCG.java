/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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
import org.wings.SContainer;
import org.wings.SGridBagLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;
import org.wings.plaf.css.PaddingVoodoo;
import org.wings.session.BrowserType;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;
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
        if (pos != -1) {
            name = name.substring(pos + 1);
        }
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
     * @param renderedContainer       The (container) component rendered
     * @param cols                    Wrap after this amount of columns
     * @param components              The components to layout in this table
     * @param cellStyle               Style attributes for the table cells.
     */
    protected void printLayouterTableBody(final Device d, final SContainer renderedContainer,
                                          int cols, final List components, final TableCellStyle cellStyle)
            throws IOException {
        final int componentCount = components.size();
        final boolean isMSIE = SessionManager.getSession().getUserAgent().getBrowserType() == BrowserType.IE;
        final TableCellStyle origCellStyle = cellStyle.makeACopy();

        int col = 0;
        int row = 0;
        int idx = 0;

        for (Iterator iter = components.iterator(); iter.hasNext();) {
            final SComponent c = (SComponent) iter.next();

            if (col == 0) {
                Utils.printNewline(d, c.getParent());
                d.print("<tr>");
            } else if (col % cols == 0) {
                row += 1;
                col = 0;
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
                d.print("<tr>");
            }

            cellStyle.renderAsTH = row == 0 && cellStyle.renderAsTH;
            cellStyle.defaultLayoutCellHAlignment = getDefaultLayoutCellHAlignment();
            cellStyle.defaultLayoutCellVAlignment = getDefaultLayoutCellVAlignment();

            if (PaddingVoodoo.hasPaddingInsets(renderedContainer)) {
                final Insets patchedInsets = (Insets) origCellStyle.getInsets().clone();
                final boolean isFirstRow = row == 0;
                final boolean isLastRow = ((componentCount - (idx+1))+col < cols);
                final boolean isFirstCol = (col == 0);
                final boolean isLastCol = ((col % cols) == 0);
                PaddingVoodoo.doBorderPaddingsWorkaround(renderedContainer.getBorder(), patchedInsets,
                        isFirstRow, isFirstCol, isLastCol, isLastRow);
                cellStyle.setInsets(patchedInsets);
            }

            openLayouterCell(d, c, cellStyle);

            c.write(d); // Render component

            closeLayouterCell(d, c, row == 0 && cellStyle.renderAsTH);

            col++;
            idx++;

            if (!iter.hasNext()) {
                d.print("</tr>");
                Utils.printNewline(d, c.getParent());
            }
        }
    }

    /**
     * The default horizontal alignment for components inside a layout cell.
     */
    public abstract int getDefaultLayoutCellHAlignment();

    /**
     * The default vertical alignment for components inside a layout cell.
     */
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
     */
    public static void openLayouterCell(final Device d, final SComponent component, final TableCellStyle cellStyle)
            throws IOException {
        Utils.printNewline(d, component);
        if (cellStyle.renderAsTH) {
            d.print("<th");
        } else {
            d.print("<td");
        }

        int oversizePadding = Utils.calculateHorizontalOversize(component, true);
        oversizePadding += cellStyle.getInsets().left;
        oversizePadding += cellStyle.getInsets().right;
        Utils.optAttribute(d, "oversize", oversizePadding);

        Utils.printTableCellAlignment(d, component, cellStyle.defaultLayoutCellHAlignment, cellStyle.defaultLayoutCellVAlignment);
        Utils.optAttribute(d, "colspan", cellStyle.colspan);
        Utils.optAttribute(d, "rowspan", cellStyle.rowspan);
        Utils.optAttribute(d, "width", cellStyle.width);
        Utils.optAttribute(d, "class", cellStyle.optionalStyleClass);
        // render optional style attribute
        if (cellStyle.hasAdditionalCellStyles() || cellStyle.hasInsets()) {
            SStringBuilder styleString = new SStringBuilder();
            Utils.createInlineStylesForInsets(styleString, cellStyle.getInsets());
            styleString.append(cellStyle.getAdditionalCellStyles().toString());
            Utils.optAttribute(d, "style", styleString);
        }
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

    protected final TableCellStyle cellLayoutStyle(SLayoutManager layout) {
        final Insets insets = convertGapsToInset(getLayoutHGap(layout), getLayoutVGap(layout));
        final int layoutBorder = getLayoutBorder(layout);
        final TableCellStyle cellStyle = new TableCellStyle();

        cellStyle.setInsets(insets);
        if (layoutBorder > 0) {
            cellStyle.getAdditionalCellStyles().put(CSSProperty.BORDER, layoutBorder + "px solid black");
        }
        return cellStyle;
    }

    protected abstract int layoutOversize(SLayoutManager layout);

    protected final int cellOversize(SGridBagLayout layout, Insets insets) {
        return insets.top + insets.bottom + layout.getBorder();
    }

    /**
     * @return true if current browser is microsoft exploder
     */
    protected final boolean isMSIE(final SComponent component) {
        return component.getSession().getUserAgent().getBrowserType() == BrowserType.IE;
    }
}
