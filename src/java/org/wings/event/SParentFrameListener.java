package org.wings.event;

import java.util.EventListener;

/**
 * This Listener is called when a component's parent frame reference is
 * updated. It is used for registering things like headers or listeners.
 * @author ole
 *
 */
public interface SParentFrameListener extends EventListener {
    /**
     * called, whenever a parentFrame reference is added to the container.
     */
    void parentFrameAdded(SParentFrameEvent e);

    /**
     * called, whenever a parentFrame reference is removed from the container.
     */
    void parentFrameRemoved(SParentFrameEvent e);
}
