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

import org.wings.Renderable;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A special root container forming up an frameset.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SFrameSet extends SFrame {

    private boolean frameborderVisible = true;

    public SFrameSet(SFrameSetLayout layout) {
        setLayout(layout);
    }

    public final SContainer getContentPane() {
        return null; // heck :-)
    }

    /**
     * Removes the given component from the container.
     *
     * @param c the component to remove
     * @see #removeComponent(org.wings.SComponent)
     */
    public void remove(SComponent c) {
        if ( c==null )  return;

        if ( getLayout()!=null )
            getLayout().removeComponent(c);

        int index = getComponentList().indexOf(c);
        if ( getComponentList().remove(c) ) {
            getConstraintList().remove(index);
            // fireContainerEvent(org.wings.event.SContainerEvent.COMPONENT_REMOVED, c);

            c.setParent(null);
            reload();
        }
    }

    /**
     * Removes the component at the given position from the container.
     *
     * @param index remove the component at position <i>index</i>
     * 	from this container
     */
    public void remove(int index) {
        SComponent c = getComponent(index);
        remove(c);
    }

    /**
     * Removes all components from the container.
     */
    public void removeAll() {
        while ( getComponentCount() > 0 ) {
            remove(0);
        }
    }

    /**
     * Only SFrameSets or SFrames are allowed.
     */
    public SComponent addComponent(SComponent c, Object constraint, int index) {
        if (c == null)
            return null;

        if (!(c instanceof SFrame))
            throw new IllegalArgumentException("Only SFrameSets or SFrames are allowed.");

        if (getLayout() != null)
            getLayout().addComponent(c, constraint, index);

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

        if (getLayout() != null)
            getLayout().removeComponent(c);

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
     * @param p A {@link SFrameSet}
     */
    public void setParent(SContainer p) {
        if (!(p instanceof SFrameSet))
            throw new IllegalArgumentException("SFrameSets can only be added to SFrameSets.");

        super.setParent(p);
    }

    /**
     * Disabling parent frame.
     * @param f ignored paramter
     */
    protected void setParentFrame(SFrame f) {}

    /**
     * There is no parent frame.
     */
    public SFrame getParentFrame() {
        return null;
    }

    /**
     * Set the base target and propagate it to all frames
     */
    /**ING removed
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
    ***/

    /**
     * Set the base target and propagate it to all frames
     */
    public static void assignBaseTarget(SFrame frame, String baseTarget) {
        //The baseTarget attribute was removed in wings2. It was used as a way
        // to set the base target in html head, i.e. <base target="xxx" />
        // where xxx is the name of a frame (eg in a frameset)
        // Hence reimplement this by using the standard frame headers.
        // (eg. see org.wings.header.Meta)
        // If someone would like to move this method down into SFrame
        // it could be come a non-static method again

        // There can only be one such header so if existing overwrite
        List headers = frame.getHeaders();
        boolean found = false;
        Iterator it = headers.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof BaseTarget) {
                ((BaseTarget)next).setTarget(baseTarget);
                found = true;
                break;
            }
        }
        if(!found)
            frame.addHeader(new BaseTarget(baseTarget));
        // propagate it to all frame(set)s (getComponentList was protected)
        SComponent[] components = frame.getComponents();
        for(int i = 0; i < components.length; ++i) {
            SComponent c = components[i];
            if (c instanceof SFrame)
                assignBaseTarget((SFrame)c, baseTarget);
        }
    }

    /**
     * Creates the base target name for passed frame.
     * @param forInnerFrame Frame to use as inner frame
     * @return The base target name for passed frame.
     */
    public static String createBaseTargetName(SFrame forInnerFrame) {
         return "frame" + forInnerFrame.getName();
    }

    /**
     * Retrieve attached <code>BaseTarget</code> of a frame.
     * @return The found base target header element or <code>null</code>
     */
    public static BaseTarget retrieveBaseTarget(SFrame frame) {
        List headers = frame.getHeaders();
        Iterator it = headers.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof BaseTarget) {
                return (BaseTarget) next;
            }
        }
        return(null);
    }

    public void setLayout(SLayoutManager l) {
        if (!(l instanceof SFrameSetLayout)) {
            // ING unfortunately we do not have a constructor in base classes
            // that allows us to set this directly at construction time so
            // we have to be lenient since base class creates a different default
            // layout
            if (getLayout() == null) // still in constructor
                return;
            throw new IllegalArgumentException("Only SFrameSetLayout is allowed.");
        }
        super.setLayout(l);
    }

    public void write(Device s) throws IOException {
        getLayout().write(s);
    }

    public void setFrameborderVisible ( boolean bool ) {
        this.frameborderVisible = bool;
    }

    /**
     *
     * @see #setFrameborderVisible(boolean)
     * @return true if the FrameBorder is visible otherwise false
     */
    public boolean isFrameBorderVisible () {
        return frameborderVisible;
    }


    /**
     * A HTML header element
     */
    public static class BaseTarget implements Renderable {
        protected String target;

        public BaseTarget(String target) {
            this.target = target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getTarget() {
            return target;
        }

        public void write(Device d)
                throws IOException {
            if (target != null)
                d.print("<base target=\"" + target + "\"/>");
        }
    } // ...BaseTarget
}
