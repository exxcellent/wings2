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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a border layout. You can add up to 5 components to a
 * container with this layout at the following positions:
 * <code>NORTH</code>, <code>SOUTH</code>, <code>EAST</code>,
 * <code>WEST</code> and <code>CENTER</code>.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SBorderLayout
        extends SAbstractLayoutManager {

    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String CENTER = "Center";

    protected Map components = new HashMap(5);
    protected int border = 0;
    protected int cellspacing = 0;

    /**
     * creates a new border layout
     */
    public SBorderLayout() {}

    /**
     * creates a new border layout
     */
    public SBorderLayout(int spacing) {
        this.cellspacing = spacing;
    }

    /**
     * creates a new border layout
     */
    public SBorderLayout(int spacing, int border) {
        this.cellspacing = spacing;
        this.border = border;
    }

    public void addComponent(SComponent c, Object constraint, int index) {
        if (constraint == null)
            constraint = CENTER;

        components.put(constraint, c);
    }

    public void removeComponent(SComponent c) {
        if (c == null)
            return;

        Iterator iterator = components.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (c.equals(entry.getValue())) {
                iterator.remove();
                break;
            }
        }
    }

    public Map getComponents() {
        return components;
    }

    /**
     * Set the thickness of the border.
     * Default is 0, which means no border.
     *
     * @param pixel thickness of the border
     */
    public void setBorder(int pixel) {
        border = pixel;
    }

    /**
     * Returns the thickness of the border.
     *
     * @return thickness of the border
     */
    public int getBorder() {
        return border;
    }
    /**
     * Set the spacing of the parts.
     * Default is 0, which means no spacing.
     *
     * @param pixel thickness of the spacing
     */
    public void setSpacing(int pixel) {
        cellspacing = pixel;
    }

    /**
     * Returns the thickness of the spacing.
     *
     * @return thickness of the spacing
     */
    public int getSpacing() {
        return cellspacing;
    }
}


