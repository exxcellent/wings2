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
 * Empty default implementation of {@link SInternalFrameListener}.
 *
 * @author Holger Engels
 * @see SInternalFrameEvent
 * @see SInternalFrameListener
 */
public abstract class SInternalFrameAdapter
        implements SInternalFrameListener {
    /**
     * Invoked when an internal frame has been opened.
     */
    public void internalFrameOpened(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame has been closed.
     */
    public void internalFrameClosed(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is iconified.
     */
    public void internalFrameIconified(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is de-iconified.
     */
    public void internalFrameDeiconified(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is maximized.
     */
    public void internalFrameMaximized(SInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is unmaximized.
     */
    public void internalFrameUnmaximized(SInternalFrameEvent e) {}
}


