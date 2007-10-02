package org.wings.event;

import org.wings.SComponent;
import org.wings.SFrame;

/**
 * This event is fired if the parent frame of a {@link SComponent} is modified.
 * To receive these events register via {@link SComponent#addParentFrameListener(SParentFrameListener)}
 *
 * @author ole
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
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


    /**
     * Event fired if parent frame of a component changes.
     *
     * @param modifiedComponent The {@link SComponent} whoose parent frame changed.
     * @param eventType The type of modifcation (i.e. {@link #PARENTFRAME_ADDED} or {@link #PARENTFRAME_REMOVED}
     * @param parentFrame
     */
    public SParentFrameEvent(SComponent modifiedComponent, int eventType, SFrame parentFrame) {
        super(modifiedComponent, eventType);
        this.parentFrame = parentFrame;
    }
    
    /**
     * @return the parentFrame that has been added or removed.
     */
    public SFrame getParentFrame() {
        return parentFrame;
    }

}
