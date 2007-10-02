package org.wings.event;

import java.util.EventObject;

import org.wings.Scrollable;

/**
 * SViewportChangeEvent is used to notify interested parties that the viewport
 * of a Scrollable (the source) has changed either horizontally or vertically.
 *
 * @author Stephan Schuster
 */
public class SViewportChangeEvent extends EventObject {

	private boolean horizontalChange;

	/**
     * Constructs a SViewportChangeEvent object.
     *
     * @param source  the Scrollable which is the source of the event
     * @param horizontalChange  the direction in of the viewport change
     */
    public SViewportChangeEvent(Scrollable source, boolean horizontalChange) {
        super(source);
        this.horizontalChange = horizontalChange;
    }

    /**
     * Returns true in case of a vertical change.
     *
     * @return  true, if the Scrollable's viewport changed horizontally
     *          - false if the Scrollable's viewport changed vertically
     */
    public boolean isHorizontalChange() {
    	return horizontalChange;
    }
}
