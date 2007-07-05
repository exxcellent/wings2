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
package org.wings.plaf;

import org.wings.SContainer;
import org.wings.SLayoutManager;
import org.wings.io.Device;

import java.io.IOException;

public class NullLayoutCG
        implements LayoutCG {
    public void write(Device d, SLayoutManager l) throws IOException {
        SContainer c = l.getContainer();
        for (int i = 0; i < c.getComponentCount(); i++)
            c.getComponent(i).write(d);
    }
}


