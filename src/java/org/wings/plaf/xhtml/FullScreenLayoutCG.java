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
package org.wings.plaf.xhtml;

import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.io.IOException;

/**
 * CG for <a href="../../SFullScreenLayout.html">SFullScreenLayout</a>.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class FullScreenLayoutCG
        extends BorderLayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        d.print("\n<div style=\"position:absolute; z-index:1; left: 0; top: 0; width: 100%; height: 100%\">");
        super.write(d, l);
        System.out.println("Div Fullscreenlayout");
        d.print("</div>");
    }
}


