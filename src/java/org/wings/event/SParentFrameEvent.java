package org.wings.event;

import org.wings.SComponent;
import org.wings.SFrame;

public class SParentFrameEvent extends SComponentEvent {
    /**
     * The first number of used IDs for parentFrame events.
     */
    public static final int PARENTFRAME_FIRST = 11100;

    /**
     * An event with this ID indicates, that a component was added to
     * the container.
     */
    public static final int PARENTFRAME_ADDED = PARENTFRAME_FIRST;

    /**
     * An event with this ID indicates, that a component was removed from
     * the container.
     */
    public static final int PARENTFRAME_REMOVED = PARENTFRAME_FIRST + 1;

    /**
     * The last number of used IDs for container events.
     */
    public static final int PARENTFRAME_LAST = PARENTFRAME_REMOVED;

    /**
     * the parent frame that has been added or removed.
     */
    private SFrame parentFrame;


    public SParentFrameEvent(SComponent aSource, int anId, SFrame parentFrame) {
        super(aSource, anId);
        this.parentFrame = parentFrame;
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @return the parentFrame that has been added or removed.
     */
    public SFrame getParentFrame() {
        return parentFrame;
    }

}
