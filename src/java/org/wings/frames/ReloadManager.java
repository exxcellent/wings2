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

package org.wings.frames;

import java.util.Set;
import java.util.HashSet;

import org.wings.SComponent;
import org.wings.SFrame;

/*
 * This implementation assumes, that the granularity of separately reloadable
 * components is the frame. May be, there will be better possibilities in the
 * future - DHTML, updating the DOM with JavaScript ???
 * Update as of 2007: The future is already here! :-) 
 */

/**
 * Reload manager for frame based wingS applications.
 * Frameset support is currently experimental.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class ReloadManager {
    private final Set<SFrame> dirtyFrames = new HashSet<SFrame>();
    private SReloadFrame reloadFrame = null;

    /**
     * Marking a component dirty effectively marks the component's
     * parent frame dirty.
     */
    public void markDirty(SComponent component, int aspect) {
        if (component instanceof SFrameSet) {
            SFrameSet frameSet = (SFrameSet) component;
            while (frameSet.getParent() != null)
                frameSet = (SFrameSet) frameSet.getParent();
            dirtyFrames.add(frameSet);
        } else if (component.getParentFrame() != null)
            dirtyFrames.add(component.getParentFrame());
    }

    public SComponent[] getDirtyComponents(int aspect) {
        return dirtyFrames.toArray(new SFrame[dirtyFrames.size()]);
    }

    public void clearDirtyComponents(int aspect) {
        dirtyFrames.clear();
    }

    /**
     * The manager component is the invisible target frame.
     */
    public SComponent getManagerComponent() {
        if (reloadFrame == null)
            reloadFrame = new SReloadFrame();
        return reloadFrame;
    }
}
