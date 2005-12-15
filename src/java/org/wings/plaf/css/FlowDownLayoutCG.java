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

import org.wings.SFlowDownLayout;
import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.io.IOException;
import java.awt.*;

/**
 * @author bschmid
 */
public class FlowDownLayoutCG extends AbstractLayoutCG {
    /**
     * Render FlowDownLayout as table based layout
     * @param d the device to write the code to
     * @param l the layout manager
     */
    public void write(Device d, SLayoutManager l) throws IOException {
        final SFlowDownLayout layout = (SFlowDownLayout) l;
        final Insets insets = convertGapsToInset(layout.getHgap(), layout.getVgap());

        printLayouterTableHeader(d, "SFlowDownLayout", insets, 0, layout);

        printLayouterTableBody(d, 1, false, insets, 0, layout.getComponents());

        printLayouterTableFooter(d, "SFlowDownLayout", layout);
    }
}
