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

package org.wings.event;

import java.awt.event.*;

/**
 * The listener interface for receiving component events.
 * The class that is interested in processing a component event
 * either implements this interface (and all the methods it
 * contains) or extends the abstract ComponentAdapter class
 * (overriding only the methods of interest).
 * The listener object created from that class is then registered with a
 * component using the component's addComponentListener method. When the
 * component's size, location, or visibility
 * changes, the relevant method in the listener object is invoked,
 * and the ComponentEvent is passed to it.
 *
 * Component events are provided for notification purposes ONLY;
 * WingS will automatically handle component moves and resizes
 * internally so that GUI layout works properly regardless of
 * whether a program registers a ComponentListener or not.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$, $Date$
 * @see org.wings.event.SComponentAdapter
 * @see org.wings.event.SComponentEvent
 */
public interface SComponentListener
    extends java.util.EventListener
{
    /**
     * Invoked when the component has been made invisible.
     */
    public void componentHidden(SComponentEvent e);

    /**
     * Invoked when the component's position changes.
     */
    public void componentMoved(SComponentEvent e);

    /**
     * Invoked when the component's size changes.
     */
    public void componentResized(SComponentEvent e);

    /**
     * Invoked when the component has been made visible.
     */
    public void componentShown(SComponentEvent e);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
