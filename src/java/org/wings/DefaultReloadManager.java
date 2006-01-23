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

import org.wings.resource.DynamicCodeResource;
import org.wings.resource.DynamicResource;
import org.wings.script.DynamicScriptResource;
import org.wings.style.DynamicStyleSheetResource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Default implementation of the reload manager that uses HashSets.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DefaultReloadManager
        implements ReloadManager
{
    private final transient static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog("org.wings");
    /**
     * a set of all components, manged by this ReloadManager, that are marked
     * dirty.
     */
    protected final Set dirtyComponents = new HashSet();
    /**
     * a set of all resources, manged by this ReloadManager, that are marked
     * dirty.
     */
    protected final Set dirtyResources = new HashSet();

    public DefaultReloadManager() {
    }

    public void reload(SComponent component, int aspect) {
        dirtyComponents.add(component);

        SFrame parent = component.getParentFrame();
        if (parent != null) {
            if ((aspect & STATE) != 0)
                markDirty(parent.getDynamicResource(DynamicCodeResource.class));
            if ((aspect & STYLE) != 0)
                markDirty(parent.getDynamicResource(DynamicStyleSheetResource.class));
            if ((aspect & SCRIPT) != 0)
                markDirty(parent.getDynamicResource(DynamicScriptResource.class));
        }
    }

    synchronized void markDirty(DynamicResource d) {
        if (d == null)
            log.warn("markDirty: null");
        else
            dirtyResources.add(d);
    }

    public Set getDirtyComponents() {
        return dirtyComponents;
    }

    public Set getDirtyResources() {
        return dirtyResources;
    }

    public synchronized void clear() {
        dirtyComponents.clear();
        dirtyResources.clear();
    }

    public synchronized void invalidateResources() {
        //Set frames = new HashSet();
        Iterator it = dirtyResources.iterator();
        while (it.hasNext()) {
            DynamicResource resource = (DynamicResource) it.next();
            resource.invalidate();
            it.remove();
        }
    }

    public void notifyCGs() {
        for (Iterator iterator = dirtyComponents.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.getCG().componentChanged(component);
        }
    }
}


