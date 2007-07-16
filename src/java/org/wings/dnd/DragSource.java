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
package org.wings.dnd;

/**
 * The Interface for a source of drag and drop events. A component must implement this interface to be dragable.
 * @author ole
 */
public interface DragSource {

    /**
     * Getter for the property if dragging is enabled on this Component.
     * @return if dragging is enabled on this component
     */
     boolean isDragEnabled();
    /**
     * Setter for the property if dragging is enabled on this Component.
     * Don't forget to register the component with the @link{DragAndDropManager}
     * @param dragEnabled should the component be draggable?
     */
    void setDragEnabled(boolean dragEnabled);
}
