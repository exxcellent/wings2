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

import java.util.ArrayList;
import java.util.List;

/**
 * The flow layout arranges components in a left-to-right or top-to-bottom order.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFlowLayout
        extends SAbstractLayoutManager {
    /**
     * List of layouted components.
     */
    protected final List components;

    /**
     * Orientation (horizontally or vertically) of components.
     */
    protected int orientation;

    /**
     * Alignment (left, center, right) of components.
     */
    protected int align;

    /**
     * Creates a new <code>SFlowLayout</code> with horizontal orientation and
     * left alignment.
     */
    public SFlowLayout() {
        components = new ArrayList(2);
        setOrientation(SConstants.HORIZONTAL);
        setAlignment(SConstants.LEFT_ALIGN);
    }

    /**
     * Creates a new <code>SFlowLayout</code> with horizonal orientation and the given alignment.
     *
     * @param alignment the alignment
     */
    public SFlowLayout(int alignment) {
        this();
        setAlignment(alignment);
    }

    /**
     * Adds the given component at given index.
     *
     * @param c          component to add
     * @param constraint is ignored in this layout manager!
     * @param index      position to add component to
     */
    public void addComponent(SComponent c, Object constraint, int index) {
        components.add(index, c);
    }

    public void removeComponent(SComponent c) {
        components.remove(c);
    }

    /**
     * returns a list of all components
     *
     * @return all components
     */
    public List getComponents() {
        return components;
    }

    /**
     * returns the component at the given position
     *
     * @param i position
     * @return component
     */
    public SComponent getComponentAt(int i) {
        return (SComponent) components.get(i);
    }

    /**
     * Sets the orientation for this layout. Possible values are
     * <ul>
     * <li>{@link org.wings.SConstants#HORIZONTAL}
     * <li>{@link org.wings.SConstants#VERTICAL}
     * </ul>
     *
     * @param o one of the orientation values shown above
     */
    public void setOrientation(int o) {
        orientation = o;
    }

    /**
     * returns the orientation
     *
     * @return orientation
     */
    public int getOrientation() { return orientation; }

    /**
     * Sets the alignment for this layout. Possible values are
     * <ul>
     * <li>{@link org.wings.SConstants#LEFT_ALIGN}
     * <li>{@link org.wings.SConstants#CENTER_ALIGN}
     * <li>{@link org.wings.SConstants#RIGHT_ALIGN}
     * </ul>
     *
     * @param a one of the allignment values shown above
     */
    public void setAlignment(int a) {
        align = a;
    }

    /**
     * returns the alignment
     *
     * @return alignment
     */
    public int getAlignment() { return align; }
}


