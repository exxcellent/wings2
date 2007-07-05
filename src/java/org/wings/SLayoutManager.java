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
import java.io.Serializable;

/**
 * For layout managers.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public interface SLayoutManager extends SConstants, Serializable, Renderable {
    /**
     * Adds a component to the layout manager
     *
     * @param c          The new component
     * @param constraint A (sometimes optional) constraint object
     */
    void addComponent(SComponent c, Object constraint, int index);

    /**
     * Removes a component from the layout manager
     *
     * @param c The new component
     */
    void removeComponent(SComponent c);

    /**
     * Sets the corresponding container
     *
     * @param c The container
     */
    void setContainer(SContainer c);

    /**
     * Returns the corresponding container
     *
     * @return The container
     */
    SContainer getContainer();

    /**
     * Writes the layouted container to the given device.
     *
     * @param s The output device
     */
    void write(Device s) throws IOException;

    /**
     * Notification from the CGFactory that the L&F has changed.
     *
     * @see SLayoutManager#updateCG
     */
    void updateCG();

    void setBorder(int pixel);

    int getBorder();
}


