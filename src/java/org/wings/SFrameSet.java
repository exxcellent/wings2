/*
 * $Id$
 * (c) Copyright 2001 wingS development team.
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

import org.wings.io.Device;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SFrameSet
extends SFrame {
    public SFrameSet() {}
    
    public SFrameSet(SFrameSetLayout layout) {
        setLayout(layout);
    }
    
    public final SContainer getContentPane() {
        return null; // heck :-)
    }
    
    /**
     * Only SFrameSets or SFrames are allowed.
     */
    public SComponent addComponent(SComponent c, Object constraint, int index) {
        if (c == null)
            return null;
        
        if (!(c instanceof SFrame))
            throw new IllegalArgumentException("Only SFrameSets or SFrames are allowed.");
        
        if (layout != null)
            layout.addComponent(c, constraint, index);
        
        getComponentList().add(index, c);
        getConstraintList().add(index, constraint);
        c.setParent(this);
        
        return c;
        //return super.addComponent(c, constraint);
    }
    
    /**
     * Only SFrameSets or SFrames are allowed.
     */
    public void removeComponent(SComponent c) {
        if (c == null) return;
        
        if (!(c instanceof SFrame))
            throw new IllegalArgumentException("Only SFrameSets or SFrames are allowed.");
        
        if (layout != null)
            layout.removeComponent(c);
        
        c.setParent(null);
        
        int index = getComponentList().indexOf(c);
        
        if (getComponentList().remove(c)) {
            getConstraintList().remove(index);
        }
        //return super.removeComponent(c);
    }
    
    /**
     * Sets the parent frameset container.
     *
     * @param p the container
     */
    public void setParent(SContainer p) {
        if (!(p instanceof SFrameSet))
            throw new IllegalArgumentException("SFrameSets can only be added to SFrameSets.");
        
        parent = p;
    }
    
    /**
     * There is no parent frame.
     *
     * @param f the frame
     */
    protected void setParentFrame(SFrame f) {}
    
    /**
     * There is no parent frame.
     *
     * @return
     */
    public SFrame getParentFrame() {
        return null;
    }
    
    /**
     * Set the base target and propagate it to all frames
     */
    public void setBaseTarget(String baseTarget) {
        this.baseTarget = baseTarget;
        
        // propagate it to all frame(set)s
        Iterator iterator = getComponentList().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof SFrame)
                ((SFrame)object).setBaseTarget(baseTarget);
        }
    }
    
    public void setLayout(SLayoutManager l) {
        if (!(l instanceof SFrameSetLayout))
            throw new IllegalArgumentException("Only SFrameSetLayout is allowed.");
        
        super.setLayout(l);
    }
    
    public void write(Device s) throws IOException {
        layout.write(s);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
