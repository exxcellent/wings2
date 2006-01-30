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

import java.util.Set;
import java.io.Serializable;

/**
 * Responsible for registering and invalidating modified components.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface ReloadManager extends Serializable {
    /**
     * HTML represenation aspect
     */
    public static final int STATE = 1;
    /**
     * CSS StyleSheet represenation aspect
     */
    public static final int STYLE = 2;
    /**
     * Dynamic (Java)Script represenation aspect 
     */
    public static final int SCRIPT = 4;

    /**
     * Mark an aspect of an component as dirty. Valid aspects are {@link #STATE},
     * {@link #STYLE}, {@link #SCRIPT}
     *
     * @param component Component whoose representation modified.
     * @param aspect The respresentation aspect that changed.
     */
    void reload(SComponent component, int aspect);

    /**
     * Return a set of all components that are marked dirty.
     * @return a set of all components that have been marked dirty.
     */
    Set getDirtyComponents();

    /**
     * Return a set of all dynamic resources that are marked dirty.
     * @return a set of all dynamic resource that have been marked dirty.
     */
    Set getDirtyResources();

    /**
     * Clear dirty components collection.
     */
    void clear();

    /**
     * Invalidates the resources containining / depending on dirty components.
     */
    void invalidateResources();

    /**
     * Notify the CG's of the dirty components that those components were updated.
     */
    void notifyCGs();
}
