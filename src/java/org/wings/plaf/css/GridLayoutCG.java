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

import org.wings.SGridLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GridLayoutCG extends AbstractLayoutCG {
    private static final long serialVersionUID = 1L;

    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        final SGridLayout layout = (SGridLayout) l;
        final List components = layout.getComponents();
        final int rows = layout.getRows();
        final Insets insets = convertGapsToInset(layout.getHgap(), layout.getVgap());

        int cols = layout.getColumns();
        if (cols <= 0)
            cols = components.size() / rows;
        final int border = layout.getBorder();

        openLayouterBody(d, layout);

        printLayouterTableBody(d, cols, layout.getRenderFirstLineAsHeader(), insets, border, components);

        closeLayouterBody(d, layout);
    }

}
