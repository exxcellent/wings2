package org.wings.event;

import java.util.EventListener;

/**
 * Defines an object which listens for SViewportChangeEvents.
 *
 * @author Stephan Schuster
 */
public interface SViewportChangeListener extends EventListener {
	/**
     * Invoked when the target of the listener has changed its viewport.
     *
     * @param e  a SViewportChangeEvent object
     */
    void viewportChanged(SViewportChangeEvent e);
}
