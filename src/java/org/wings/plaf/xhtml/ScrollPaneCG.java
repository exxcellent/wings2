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

import org.wings.SComponent;
import org.wings.SScrollPane;
import org.wings.io.Device;

import java.io.IOException;

public class ScrollPaneCG
        extends org.wings.plaf.css.AbstractComponentCG
        implements org.wings.plaf.ScrollPaneCG {
    public void installCG(SComponent component) {
    }

    public void uninstallCG(SComponent component) {
    }

    public void writeContent(Device d, SComponent c)
            throws IOException {
        SScrollPane scrollPane = (SScrollPane) c;
        SComponent scrollable = (SComponent) scrollPane.getScrollable();

        if (scrollPane.getPreferredSize() == null &&
                scrollable.getPreferredSize() != null) {
            scrollPane.setPreferredSize(scrollable.getPreferredSize());
        }

        scrollPane.synchronizeAdjustables();

        Utils.writeContainerContents(d, scrollPane);
    }

}


