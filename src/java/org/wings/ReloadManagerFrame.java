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

import org.wings.externalizer.ExternalizeManager;
import org.wings.io.Device;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An invisible frame, that executes a javascript function <code>onload</code>,
 * that reloads all dirty frames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class ReloadManagerFrame
extends SFrame {
    private final static Logger logger = Logger.getLogger("org.wings");
    
    
    public ReloadManagerFrame() {}
    
    public final SContainer getContentPane() {
        return null; // heck :-)
    }
    
    /**
     * This frame stays invisible
     */
    public SComponent addComponent(SComponent c, Object constraint, int index) {
        throw new IllegalArgumentException("Adding Components is not allowed");
    }
    
    /**
     * This frame stays invisible
     * @deprecated use {@link #remove(SComponent)} instead for swing conformity
     */
    public void removeComponent(SComponent c) {
        throw new IllegalArgumentException("Does not have Components");
    }
    
    /**
     * Sets the parent FrameSet container.
     *
     * @param p the container
     */
    public void setParent(SContainer p) {
        if (!(p == null || p instanceof SFrameSet))
            throw new IllegalArgumentException("The ReloadManagerFrame can only be added to SFrameSets.");
        
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
     * No LayoutManager allowed.
     */
    public void setLayout(SLayoutManager l) {
        throw new IllegalArgumentException("No LayoutManager allowed");
    }
    
    private Set dirtyResources;
    public void setDirtyResources(Set dirtyResources) {
        this.dirtyResources = dirtyResources;
    }
    
    /**
     * Generate a minimal document with a javascript function, that reloads
     * all dirty frames. The list of dirty frames is obtained from the ReloadManager.
     * After the code has been generated, the dirty components list is cleared.
     *** create a PLAF for this ***
     */
    public void write(Device d) throws IOException {
        ExternalizeManager externalizer = getSession().getExternalizeManager();
        
        d.print("<head><title>ReloadManager</title>\n");
        d.print("<script language=\"javascript\">\n");
        d.print("function reload() {\n");
        
        if (dirtyResources != null) {
            boolean all = false;
            DynamicResource toplevel = null; {
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource)it.next();
                    if (!(resource.getFrame() instanceof ReloadManagerFrame) &&
                    resource.getFrame().getParent() == null) {
                        toplevel = resource;
                        all = true;
                    }
                }
            }
            
            if (all) {
                // reload the _whole_ document
                d.print("parent.location.href='");
                d.print(toplevel.getURL());
                d.print("';\n");
                
                if (logger.isLoggable(Level.FINER))
                    logger.finer("parent.location.href='" + toplevel.getURL() + "';\n");
                
                // invalidate resources
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource)it.next();
                    resource.invalidate();
                    
                }
            }
            else {
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource)it.next();
                    resource.invalidate();
                    
                    
                    d.print("parent.frame");
                    d.print(resource.getFrame().getComponentId());
                    d.print(".location.href='");
                    d.print(resource.getURL());
                    d.print("';\n");
                    
                    if (logger.isLoggable(Level.FINER))
                        logger.finer("parent.frame" +
                        resource.getFrame().getComponentId() +
                        ".location.href='" +
                        resource.getURL() +
                        "';\n");
                }
            }
        }
        
        d.print("}\n");
        d.print("</script>\n");
        d.print("</head>\n");
        d.print("<body onload=\"reload()\"></body>");
        
    }
    
    
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
