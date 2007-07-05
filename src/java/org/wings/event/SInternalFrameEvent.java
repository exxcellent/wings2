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

import org.wings.SInternalFrame;

import java.awt.*;

/**
 * SInternalFrameEvent:  an AWTEvent which adds support for
 * SInternalFrame objects as the event source.
 *
 * @author Holger Engels
 */
public class SInternalFrameEvent extends AWTEvent {
    /**
     * The first number in the range of ids used for window events.
     */
    public static final int INTERNAL_FRAME_FIRST = 12000;

    /**
     * The window opened event.  This event is delivered only
     * the first time a window is made visible.
     */
    public static final int INTERNAL_FRAME_OPENED = INTERNAL_FRAME_FIRST;

    /**
     * The window closed event. This event is delivered after
     * the window has been closed as the result of a call to hide or
     * destroy.
     */
    public static final int INTERNAL_FRAME_CLOSED = 1 + INTERNAL_FRAME_FIRST;

    /**
     * The window iconified event. This event indicates that the window
     * was shrunk down to a small icon.
     */
    public static final int INTERNAL_FRAME_ICONIFIED = 2 + INTERNAL_FRAME_FIRST;

    /**
     * The window deiconified event type. This event indicates that the
     * window has been restored to its normal size.
     */
    public static final int INTERNAL_FRAME_DEICONIFIED = 3 + INTERNAL_FRAME_FIRST;

    /**
     * The window maximized event type. This event indicates that keystrokes
     * and mouse clicks are directed towards this window.
     */
    public static final int INTERNAL_FRAME_MAXIMIZED = 4 + INTERNAL_FRAME_FIRST;

    /**
     * The window unmaximized event type. This event indicates that keystrokes
     * and mouse clicks are no longer directed to the window.
     */
    public static final int INTERNAL_FRAME_UNMAXIMIZED = 5 + INTERNAL_FRAME_FIRST;

    /**
     * The last number in the range of ids used for window events.
     */
    public static final int INTERNAL_FRAME_LAST = INTERNAL_FRAME_UNMAXIMIZED;

    /**
     * Constructs a SInternalFrameEvent object.
     *
     * @param source the SInternalFrame object that originated the event
     * @param id     an integer indicating the type of event
     */
    public SInternalFrameEvent(SInternalFrame source, int id) {
        super(source, id);
    }

    /**
     * Returns a parameter string identifying this event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;
        switch (id) {
            case INTERNAL_FRAME_OPENED:
                typeStr = "INTERNAL_FRAME_OPENED";
                break;
            case INTERNAL_FRAME_CLOSED:
                typeStr = "INTERNAL_FRAME_CLOSED";
                break;
            case INTERNAL_FRAME_ICONIFIED:
                typeStr = "INTERNAL_FRAME_ICONIFIED";
                break;
            case INTERNAL_FRAME_DEICONIFIED:
                typeStr = "INTERNAL_FRAME_DEICONIFIED";
                break;
            case INTERNAL_FRAME_MAXIMIZED:
                typeStr = "INTERNAL_FRAME_MAXIMIZED";
                break;
            case INTERNAL_FRAME_UNMAXIMIZED:
                typeStr = "INTERNAL_FRAME_UNMAXIMIZED";
                break;
            default:
                typeStr = "unknown type";
        }
        return typeStr;
    }


    /**
     * Returns the originator of the event.
     *
     * @return the SInternalFrame object that originated the event
     * @since 1.3
     */
    public SInternalFrame getInternalFrame() {
        return (source instanceof SInternalFrame) ? (SInternalFrame) source : null;
    }

}


