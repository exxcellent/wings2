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
package org.wings;

import org.wings.io.Device;
import org.wings.plaf.LayoutCG;
import org.wings.session.SessionManager;

import java.io.IOException;

/**
 * Base class for layout managers.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class SAbstractLayoutManager
        implements SLayoutManager {
    /**
     * The code generation delegate, which is responsible for
     * the visual representation of a layout.
     */
    protected transient LayoutCG cg;

    /**
     * The container using this layout
     */
    protected SContainer container;

    /**
     * Preferred size of component in pixel.
     */
    protected SDimension preferredSize = null;


    protected SAbstractLayoutManager() {
        updateCG();
    }

    protected void setCG(LayoutCG newCG) {
        cg = newCG;
    }

    /**
     * Return the look and feel delegate.
     *
     * @return the componet's cg
     */
    public LayoutCG getCG() {
        return cg;
    }

    /**
     * Notification from the CGFactory that the L&F
     * has changed.
     *
     * @see SComponent#updateCG
     */
    public void updateCG() {
        setCG((LayoutCG) SessionManager.getSession().getCGManager().getCG(this));
    }

    public void write(Device d)
            throws IOException {
        cg.write(d, this);
    }

    public void setContainer(SContainer c) {
        container = c;
    }

    public SContainer getContainer() {
        return container;
    }

    /**
     * Set the preferred size of the receiving {@link SLayoutManager} in pixel.
     * It is not guaranteed that the {@link SLayoutManager} accepts this property because of
     * missing implementations in the {@link SLayoutManager } cg or html properties.
     * If <i>width</i> or <i>height</i> is zero, it is ignored and the browser
     * defines the size.
     *
     * @see #getPreferredSize
     */
    public final void setPreferredSize(SDimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    /**
     * Get the preferred size of this {@link SLayoutManager }.
     *
     * @see #setPreferredSize
     */
    public final SDimension getPreferredSize() {
        return this.preferredSize;
    }
}


