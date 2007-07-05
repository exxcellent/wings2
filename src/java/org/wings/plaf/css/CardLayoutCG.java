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

import org.wings.*;
import org.wings.session.BrowserType;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;

public class CardLayoutCG implements LayoutCG {
    private static final long serialVersionUID = 1L;

    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException
    {
        SCardLayout cardLayout = (SCardLayout) l;
        SContainer container = l.getContainer();
        SComponent c = cardLayout.getVisibleComponent();

        SDimension preferredSize = container.getPreferredSize();
        String height = preferredSize != null ? preferredSize.getHeight() : null;
        boolean clientLayout = isMSIE(container) && height != null && !"auto".equals(height);

        if (clientLayout)
            d.print("<tr yweight=\"100\">");
        else
            openLayouterRow(d, "100%");

        openLayouterCell(d, c);
        // Just present visible component
        if (c != null) {
            c.write(d);
        }

        closeLayouterCell(d);
        closeLayouterRow(d);
    }

    public static void openLayouterRow(final Device d, String height) throws IOException {
        d.print("<tr");
        Utils.optAttribute(d, "height", height);
        d.print(">");
    }

    public static void openLayouterCell(final Device d, SComponent component) throws IOException {
        d.print("<td");
        Utils.printTableCellAlignment(d, component, SConstants.CENTER_ALIGN, SConstants.CENTER_ALIGN);
        d.print(">");
    }

    public static void closeLayouterCell(final Device d) throws IOException {
        d.print("</td>");
    }

    public static void closeLayouterRow(final Device d) throws IOException {
        d.print("</tr>");
    }

    /**
     * @return true if current browser is microsoft exploder
     */
    protected final boolean isMSIE(final SComponent component) {
        return component.getSession().getUserAgent().getBrowserType() == BrowserType.IE;
    }
}


