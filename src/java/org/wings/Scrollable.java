/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import java.awt.*;

/**
 * Interface for scrollable components, meaning components that may show only parts of it contents at once.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface Scrollable {

    /**
     * is the size of the component in respect to scrollable units. e.g. a
     * {@link STable}
     * has the scrollable viewport size:
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
     * set the visible part of a scrollable.
     */
    void setViewportSize(Rectangle d);

    /**
     * get the actual visible part of a scrollable. This may be valid only at
     * rendering time. In fact, inside a {@link SScrollPane}
     * synchronization with the adjustables is done
     * short before component it rendered...
     * You should never rely this values. This method is mainly needed to backup
     * viewports...
     */
    Rectangle getViewportSize();

    /**
     * if scrolling is activated, the component can suggest it's extent.
     *
     * @return null if no preference
     */
    Dimension getPreferredExtent();

}


