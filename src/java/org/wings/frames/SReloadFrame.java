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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.resource.ReloadResource;
import org.wings.resource.DynamicResource;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


/**
 * An invisible frame, that executes a javascript function <code>onload</code>,
 * that reloads all dirty frames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SReloadFrame extends SFrame {
    private final static Log logger = LogFactory.getLog("org.wings");
    private Set dirtyFrames;

    public SReloadFrame() {
    }

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
     *
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
            throw new IllegalArgumentException("The SReloadFrame can only be added to SFrameSets.");

        super.setParent(p);
    }

    /**
     * There is no parent frame.
     */
    protected void setParentFrame(SFrame f) {
    }

    /**
     * There is no parent frame.
     */
    public SFrame getParentFrame() {
        return null;
    }

    /**
     * No LayoutManager allowed.
     */
    public void setLayout(SLayoutManager l) {
    }

    public void setDirtyFrames(Set dirtyFrames) {
        this.dirtyFrames = dirtyFrames;
    }

    /**
     * Generate a minimal document with a javascript function, that reloads
     * all dirty frames. The list of dirty frames is obtained from the ReloadManager.
     * After the code has been generated, the dirty components list is cleared.
     * ** create a PLAF for this ***
     */
    public void write(Device d) throws IOException {
        d.print("<head><title>Reloading frameset</title>\n");
        d.print("<script language=\"javascript\">\n");
        d.print("function reload() {\n");

        if (dirtyFrames != null) {
            boolean all = false;
            SFrame toplevel = null;
            {
                Iterator it = dirtyFrames.iterator();
                while (it.hasNext()) {
                    SFrame frame = (SFrame) it.next();
                    if (!(frame instanceof SReloadFrame) &&
                            frame.getParent() == null) {
                        toplevel = frame;
                        all = true;
                    }
                }
            }

            if (all) {
                // reload the _whole_ document
                d.print("parent.location.href='");
                d.print(toplevel.getRequestURL());
                d.print("';\n");

                if (logger.isTraceEnabled())
                    logger.debug("parent.location.href='" + toplevel.getRequestURL() + "';\n");

                // invalidate frames
                Iterator it = dirtyFrames.iterator();
                while (it.hasNext()) {
                    SFrame frame = (SFrame) it.next();
                    frame.invalidate();

                }
            } else {
                Iterator it = dirtyFrames.iterator();
                while (it.hasNext()) {
                	SFrame frame = (SFrame) it.next();
                	frame.invalidate();

                    d.print("parent.frame");
                    d.print(frame.getName());
                    d.print(".location.href='");
                    d.print(frame.getRequestURL());
                    d.print("';\n");

                    if (logger.isTraceEnabled())
                        logger.debug("parent.frame" +
                                frame.getName() +
                                ".location.href='" +
                                frame.getRequestURL() +
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