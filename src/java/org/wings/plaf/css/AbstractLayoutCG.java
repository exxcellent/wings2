/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;

import org.wings.SContainer;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;

/**
 * Abstract super class for layout CGs using invisible tables to arrange their contained components.
 *
 * @author bschmid
 */
public abstract class AbstractLayoutCG implements LayoutCG {

    /** Print HTML table element declaration of a typical invisible layouter table. */
    protected void printLayouterTableHeader(Device d, int cellSpacing, int cellPadding, int border, SContainer container)
            throws IOException {
        d.print("\n<table ");
        d.print(" cellspacing=\"").print(cellSpacing < 0 ? 0: cellSpacing).print("\"");
        d.print(" cellpadding=\"").print(cellPadding < 0 ? 0 : cellPadding).print("\"");
        d.print(" border=\"").print(border < 0 ? 0 : border).print("\"");
        Utils.printCSSInlinePreferredSize(d,container.getPreferredSize());
        d.print(">\n");
    }
    /** Counterpart to {@link #printLayouterTableHeader} */
    protected void printLayouterTableFooter(Device d) throws IOException {
        d.print("</table>\n");
    }

}
