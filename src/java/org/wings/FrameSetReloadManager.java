/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.util.HashSet;
import java.util.Set;

/**
 * This implementation assumes, that the granularity of separately reloadable
 * components is the frame. May be, there will be better possibilities in the
 * future - DHTML, updating the DOM with JavaScript ???
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FrameSetReloadManager
{
    private final Set dirtyFrames = new HashSet();
    private ReloadManagerFrame reloadManagerFrame = null;

    /**
     * Marking a component dirty effectively marks the component's
     * parent frame dirty.
     */
    public void markDirty(SComponent component, int aspect) {
	if (component instanceof SFrameSet) {
	    SFrameSet frameSet = (SFrameSet)component;
	    while (frameSet.getParent() != null)
		frameSet = (SFrameSet)frameSet.getParent();
	    dirtyFrames.add(frameSet);
	}
	else if (component.getParentFrame() != null)
	    dirtyFrames.add(component.getParentFrame());
    }

    public SComponent[] getDirtyComponents(int aspect) {
	return (SComponent[])dirtyFrames.toArray(new SFrame[dirtyFrames.size()]);
    }

    public void clearDirtyComponents(int aspect) {
	dirtyFrames.clear();
    }

    /**
     * The manager component is the invisible target frame.
     */
    public SComponent getManagerComponent() {
	if (reloadManagerFrame == null)
	    reloadManagerFrame = new ReloadManagerFrame();
	return reloadManagerFrame;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
