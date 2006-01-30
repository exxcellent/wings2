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
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 * @version $Revision$
 */
public class DefaultReloadManager
        implements ReloadManager {
    /**
     * a set of all components, manged by this ReloadManager, that are marked
     * dirty.
     */
    protected final Set dirtyComponents = new HashSet();

    /**
     * Set of SComponents with dirty HTML representation
     */
    protected final Set dirtyCodeResourceComponents = new HashSet(1024);
    /**
     * Set of SComponents with dirty CSS representation
     */
    protected final Set dirtyStyleResourceComponents = new HashSet(1024);
    /**
     * Set of SComponents with dirty JavaScript representation
     */
    protected final Set dirtyScriptResourceComponents = new HashSet(1024);

    public DefaultReloadManager() {
    }

    public synchronized void reload(SComponent component, int aspect) {
        if (component != null) {
            dirtyComponents.add(component);

            if ((aspect & STATE) != 0)
                dirtyCodeResourceComponents.add(component);
            if ((aspect & STYLE) != 0)
                dirtyStyleResourceComponents.add(component);
            if ((aspect & SCRIPT) != 0)
                dirtyScriptResourceComponents.add(component);
        }
    }

    public Set getDirtyComponents() {
        return dirtyComponents;
    }

    public synchronized Set getDirtyResources() {
        final HashSet dirtyDynamicResources = new HashSet(5);

        SComponent component;
        SFrame parentFrame;
        // collect dirty HTML resources
        for (Iterator iterator = dirtyCodeResourceComponents.iterator(); iterator.hasNext();) {
            component = (SComponent) iterator.next();
            parentFrame = component.getParentFrame();
            if (parentFrame != null)
                dirtyDynamicResources.add(parentFrame.getDynamicResource(DynamicCodeResource.class));
        }
        // collect dirty CSS resources
        for (Iterator iterator = dirtyStyleResourceComponents.iterator(); iterator.hasNext();) {
            component = (SComponent) iterator.next();
            parentFrame = component.getParentFrame();
            if (parentFrame != null)
                dirtyDynamicResources.add(parentFrame.getDynamicResource(DynamicStyleSheetResource.class));
        }
        // collect dirty Script resources
        for (Iterator iterator = dirtyScriptResourceComponents.iterator(); iterator.hasNext();) {
            component = (SComponent) iterator.next();
            parentFrame = component.getParentFrame();
            if (parentFrame != null)
                dirtyDynamicResources.add(parentFrame.getDynamicResource(DynamicScriptResource.class));
        }


        return dirtyDynamicResources;
    }

    public synchronized void clear() {
        dirtyComponents.clear();
        dirtyCodeResourceComponents.clear();
        dirtyScriptResourceComponents.clear();
        dirtyStyleResourceComponents.clear();
    }

    public synchronized void invalidateResources() {
        //Set frames = new HashSet();
        Iterator it = getDirtyResources().iterator();
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


