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

import org.wings.SBoxLayout;
import org.wings.SLayoutManager;
import org.wings.SConstants;
import org.wings.io.Device;

import java.io.IOException;
import java.util.List;

public class BoxLayoutCG extends AbstractLayoutCG {
    private static final long serialVersionUID = 1L;

    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {

        final SBoxLayout layout = (SBoxLayout) l;
        final List components = layout.getComponents();
        final int cols = layout.getOrientation() == SBoxLayout.HORIZONTAL ? components.size() : 1;
        final TableCellStyle styles = cellLayoutStyle(layout);

        openLayouterBody(d, layout);

        printLayouterTableBody(d, layout.getContainer(), cols, components, styles);

        closeLayouterBody(d, layout);
    }

    protected int getLayoutHGap(SLayoutManager layout) {
        SBoxLayout boxLayout = (SBoxLayout) layout;
        return boxLayout.getHgap();
    }

    protected int getLayoutVGap(SLayoutManager layout) {
        SBoxLayout boxLayout = (SBoxLayout) layout;
        return boxLayout.getVgap();
    }

    protected int getLayoutBorder(SLayoutManager layout) {
        SBoxLayout boxLayout = (SBoxLayout) layout;
        return boxLayout.getBorder();
    }

    protected int layoutOversize(SLayoutManager layout) {
        SBoxLayout boxLayout = (SBoxLayout) layout;
        return boxLayout.getHgap() + boxLayout.getBorder();
    }

    public int getDefaultLayoutCellHAlignment() {
        return SConstants.CENTER;
    }

    public int getDefaultLayoutCellVAlignment() {
        return SConstants.CENTER;
    }
}


