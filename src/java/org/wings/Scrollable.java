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
package org.wings;

import java.awt.Rectangle;

import org.wings.event.SViewportChangeListener;

/**
 * For scrollable components, i.e. components that can show only a part of it's content.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public interface Scrollable {

    /**
     * The size of the component in respect to scrollable units.
     * E.g. a {@link STable} has the scrollable viewport size:
     * <pre>
     * new Dimension(table.getColumnCount(), table.getRowCount())
     * </pre>
     * a {@link SList}:
     * <pre>
     * new Dimension(1, list.getModel().getSize())
     * </pre>
     */
    Rectangle getScrollableViewportSize();

    /**
     * Returns the actual visible part of a scrollable.
     */
    Rectangle getViewportSize();

    /**
     * Sets the actual visible part of a scrollable.
     */
    void setViewportSize(Rectangle d);

    /**
     * Adds the given <code>SViewportChangeListener</code> to the scrollable.
     *
     * @param l the listener to be added
     */
    void addViewportChangeListener(SViewportChangeListener l);

    /**
     * Removes the given <code>SViewportChangeListener</code> from the scrollable.
     *
     * @param l the listener to be removed
     */
    void removeViewportChangeListener(SViewportChangeListener l);
}