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
package org.wings.event;

/**
 * An abstract adapter class for receiving component events.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * <p/>
 * Extend this class to create a ComponentEvent listener
 * and override the methods for the events of interest. (If you implement the
 * ComponentListener interface, you have to define all of
 * the methods in it. This abstract class defines null methods for them
 * all, so you can only have to define methods for events you care about.)
 * <p/>
 * Create a listener object using your class and then register it with a
 * component using the component's addComponentListener method. When the component's size, location, or visibility
 * changes, the relevant method in the listener object is invoked,
 * and the ComponentEvent is passed to it.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @see org.wings.event.SComponentEvent
 * @see org.wings.event.SComponentListener
 */
public abstract class SComponentAdapter
        implements SComponentListener {
    /**
     * Invoked when the component has been made invisible.
     */
    public void componentHidden(SComponentEvent e) {}

    /**
     * Invoked when the component's position changes.
     */
    public void componentMoved(SComponentEvent e) {}

    /**
     * Invoked when the component's size changes.
     */
    public void componentResized(SComponentEvent e) {}

    /**
     * Invoked when the component has been made visible.
     */
    public void componentShown(SComponentEvent e) {}
}


