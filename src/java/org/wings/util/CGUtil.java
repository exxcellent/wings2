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
        SDimension dim = c.getPreferredSize();

        if (dim != null) {
            if (dim.width != null)
                d.print(" width=\"").print(dim.width).print("\"");
            if (dim.height != null)
                d.print(" height=\"").print(dim.height).print("\"");
        }
    }

    public static void writeSize(Device d, SAbstractLayoutManager lm)
            throws IOException {
        SDimension dim = lm.getPreferredSize();

        if (dim != null) {
            if (dim.width != null)
                d.print(" width=\"").print(dim.width).print("\"");
            if (dim.height != null)
                d.print(" height=\"").print(dim.height).print("\"");
        }
    }
}


