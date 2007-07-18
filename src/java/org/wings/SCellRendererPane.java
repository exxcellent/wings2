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
package org.wings;

import org.wings.io.Device;

import java.io.IOException;

/**
 * Internal component (re)used during the rendering process of cell based components
 * like {@link STree} and {@link STable}s.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SCellRendererPane
        extends SContainer {
    private final transient static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog("org.wings");

    /**
     * Construct a CellRendererPane object.
     */
    public SCellRendererPane() {
        super();
        setLayout(null);
        setVisible(false);
    }

    /**
     * Shouldn't be called.
     */
    public void write(Device d) {
    }

    /**
     * If the specified component is already a child of this then we don't
     * bother doing anything - stacking order doesn't matter for cell
     * renderer components (CellRendererPane doesn't paint anyway).
     */
    public SComponent addComponent(SComponent c, Object constraints, int index) {
        if (c.getParent() == this) {
            return null;
        } else {
            return super.addComponent(c, constraints, index);
        }
    }

    /**
     * Write a cell renderer component c to device d. Before the component
     * is drawn it's reparented to this (if that's neccessary).
     * The Component p is the component we're actually drawing on.
     */
    public void writeComponent(Device d, SComponent c, SComponent p)
            throws IOException {
        if (getParent() == null)
            log.warn("SCellRendererPane: parent == null!");

        if (getParentFrame() == null)
            log.warn("SCellRendererPane: parentFrame == null!");

        if (c == null || !c.isVisible()) {
            return;
        }

        addComponent(c);

        c.write(d);

        remove(c);
    }
}


