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

import org.wings.resource.DynamicResource;

import java.util.Set;

/**
 * The reload manager interface.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface ReloadManager {
    void reload(SComponent component);

    /**
     * Mark a <code>component</code> dirty.
     * Frames that contain dirty components have to be reloaded.
     *
     * @param component the dirty component
     */
    void markDirty(DynamicResource component);

    /**
     * Return a set of all dynamic resources that are marked dirty.
     *
     * @return a set all dynamic resource that have been marked dirty.
     */
    Set getDirtyResources();

    /**
     * Clear dirty components collection.
     */
    void clear();

    void invalidateResources();
}
