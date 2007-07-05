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

import java.util.EventListener;


/**
 * This listener is called on modifications on a Container. It can
 * be registered on an {@link org.wings.SContainer}.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @see org.wings.SContainer#addContainerListener(SContainerListener)
 */
public interface SContainerListener extends EventListener {
    /**
     * called, whenever a component is added to the container.
     */
    void componentAdded(SContainerEvent e);

    /**
     * called, whenever a component is removed from the container.
     */
    void componentRemoved(SContainerEvent e);
}


