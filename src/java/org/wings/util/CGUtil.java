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
package org.wings.util;

import org.wings.SAbstractLayoutManager;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.io.Device;

import java.io.IOException;

public class CGUtil {
    /**
     * Write <i>width</i> and <i>height</i> argument to component tag.
     */
    public static void writeSize(Device d, SComponent c)
            throws IOException {
        writeDimension(d, c.getPreferredSize());
    }

    /**
     * Write <i>width</i> and <i>height</i> argument to LayoutManager tag.
     */
    public static void writeSize(Device d, SAbstractLayoutManager lm)
            throws IOException {
        writeDimension(d, lm.getPreferredSize());
    }

    private static void writeDimension(Device d, SDimension dim) throws IOException {
        if (dim != null) {
            if (dim.isWidthDefined())
                d.print(" width=\"").print(dim.getWidth()).print("\"");
            if (dim.isHeigthDefined())
                d.print(" height=\"").print(dim.getHeight()).print("\"");
        }
    }

}


