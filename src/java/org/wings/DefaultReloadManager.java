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

import org.wings.resource.CompleteUpdateResource;
import org.wings.resource.DynamicResource;
import org.wings.resource.IncrementalUpdateResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Default implementation of the reload manager that uses HashSets.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 * @version $Revision$
 */
public class DefaultReloadManager
        implements ReloadManager {

    private final transient static Log log = LogFactory.getLog(DefaultReloadManager.class);

    /**
     * a set of all components, manged by this ReloadManager, that are marked
     * dirty.
     */
    protected final Set dirtyComponents = new HashSet();

    private boolean deliveryPhase = false;

    public DefaultReloadManager() {
    }

    public synchronized void reload(SComponent component, int aspect) {
        if (deliveryPhase && log.isDebugEnabled())
            log.debug("Component " + component.getName() + " changed during delivery phase");

        if (component != null)
            dirtyComponents.add(component);
    }

    public Set getDirtyComponents() {
        return dirtyComponents;
    }

    public synchronized Set getDirtyFrames() {
        final HashSet dirtyFrames = new HashSet(5);

        SFrame parentFrame;
        // collect dirty frames
        for (Iterator iterator = dirtyComponents.iterator(); iterator.hasNext();) {
            parentFrame = ((SComponent) iterator.next()).getParentFrame();
            if (parentFrame != null) {
                dirtyFrames.add(parentFrame);
            }
        }

        return dirtyFrames;
    }

    public synchronized void clear() {
        deliveryPhase = false;
        dirtyComponents.clear();
    }

    public synchronized void invalidateFrames() {
        //Set frames = new HashSet();
        Iterator it = getDirtyFrames().iterator();
        while (it.hasNext()) {
            ((SFrame) it.next()).invalidate();
            it.remove();
        }
        deliveryPhase = true;
    }

    public void notifyCGs() {
        for (Iterator iterator = dirtyComponents.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.getCG().componentChanged(component);
        }
    }
        
    public boolean hasDirtyComponents() {
        return (dirtyComponents.size() > 0) ? true : false;
    }
}
