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
package org.wings;


import org.wings.plaf.DesktopPaneCG;
import org.wings.event.*;

/**
 * Container that holds SInternalFrames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SDesktopPane
    extends SContainer
{
    SInternalFrameListener listener = new SInternalFrameAdapter() {
        public void internalFrameMaximized(SInternalFrameEvent e) {
            reload();
        }

        public void internalFrameUnmaximized(SInternalFrameEvent e) {
            reload();
        }
    };

    public void setLayout(SLayoutManager l) {}

    /**
     * @param component   The internal frame to be added.
     * @param constraints nothing
     */
    public SComponent addComponent(SComponent component,
                                   Object constraints, int index) {
        if (constraints == null)
            constraints = component.getName();

        ((SInternalFrame)component).addInternalFrameListener(listener);

        return super.addComponent(component, constraints, index);
    }


    public void remove(SComponent c) {
        super.remove(c);

        ((SInternalFrame)c).removeInternalFrameListener(listener);
    }

    /**
     * Sets the position for the specified component.
     *
     * @param c        the Component to set the layer for
     * @param position an int specifying the position, where
     *                 0 is the topmost position and
     *                 -1 is the bottommost position
     */
    public void setPosition(SComponent c, int position) {
        if (position != getComponentList().indexOf(c)) {
            getComponentList().remove(c);
            getComponentList().add(position, c);
            reload();
        }
    }

    /**
     * Returns the index of the specified Component.
     * This is the absolute index, ignoring layers.
     * Index numbers, like position numbers, have the topmost component
     * at index zero. Larger numbers are closer to the bottom.
     *
     * @param c the Component to check
     * @return an int specifying the component's index
     */
    public int getIndexOf(SComponent c) {
        int i, count;

        count = getComponentCount();
        for (i = 0; i < count; i++) {
            if (c == getComponent(i))
                return i;
        }
        return -1;
    }

    /**
     * Get the position of the component.
     *
     * @param c the Component to check
     * @return an int giving the component's position, where 0 is the
     *         topmost position and the highest index value = the count
     *         count of components minus 1
     * @see #getIndexOf
     */
    public int getPosition(SComponent c) {
        return getIndexOf(c);
    }

    public void setCG(DesktopPaneCG cg) {
        super.setCG(cg);
    }
}


