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

import java.io.IOException;

import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

public class CardLayoutCG implements LayoutCG {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        SCardLayout cardLayout = (SCardLayout) l;
        SComponent c = cardLayout.getVisibleComponent();
        // Just present visible component
        if (c != null) {
            c.write(d);
        }
    }
}


