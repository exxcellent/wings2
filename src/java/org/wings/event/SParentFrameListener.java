package org.wings.event;

import java.util.EventListener;

/**
 * This Listener is called when a component's parent frame reference is
 * updated i.e. via {@link org.wings.SComponent#setParentFrame(org.wings.SFrame)}
 * or {@link org.wings.SComponent#setParent(org.wings.SContainer)}
 *
 * It is used for lazy registering things like headers or listeners at the application frame as typically
 * {@link org.wings.SComponent#getParentFrame()} will return <code>null</code> during construction time.

 * @author ole
 */
public interface SParentFrameListener extends EventListener {
    /**
     * Called, whenever a parentFrame reference is added to the container.
     * This i.e. happens on <code>frame.add(component)</code>.
     */
    void parentFrameAdded(SParentFrameEvent e);

    /**
     * Called, whenever a parentFrame reference is removed from the container.
     */
    void parentFrameRemoved(SParentFrameEvent e);
}
