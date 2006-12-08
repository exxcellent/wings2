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

import java.util.List;
import java.util.Set;
import java.io.Serializable;

import org.wings.plaf.Update;

/**
 * A reload manger is responsible for managing reloads and updates of components as
 * well as for invalidating the epoch of frames whose contained components changed.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author Stephan Schuster
 * @version $Revision$
 */
public interface ReloadManager extends Serializable {

    /**
     * Reloads the entire component.
     * @param component  the component that changed
     */
    public void reload(SComponent component);

    /**
     * Adds an update (for a specific component).
     * @param update  the update to add
     */
    public void addUpdate(Update update);

    /**
     * Returns a (filtered) list of all available updates.
     * @return a list of updates
     */
    public List getUpdates();

    /**
     * Returns a set of all components that were marked dirty.
     * @return a set of all dirty components
     */
    public Set getDirtyComponents();

    /**
     * Return a set of all frames that were marked dirty.
     * @return a set of all dirty frames
     */
    public Set getDirtyFrames();

    /**
     * Invalidates the frames containing dirty components.
     */
    public void invalidateFrames();

    /**
     * Notifies the CG's of dirty components about the change.
     */
    public void notifyCGs();

    /**
     * Clears all requested reloads and updates for cmponents.
     */
    public void clear();

    /**
     * Returns true if the given component requested a reload.
     * @return true if a reload was requested
     */
    public boolean componentRequestedReload(SComponent component);

    /**
     * Returns true if the given component requested an update.
     * @return true if an update was requested
     */
    public boolean componentRequestedUpdate(SComponent component);

    /**
     * Returns true if this ReloadManager is in update mode.
     * @return true if in update mode
     */
    public boolean isUpdateMode();

    /**
     * Enabled or disabled the update mode of this ReloadManager.
     * @param enabled  true to enable update mode
     */
    public void setUpdateMode(boolean enabled);

}