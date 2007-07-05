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

import java.util.ArrayList;
import java.util.List;

/**
 * Arranges components in a left-to-right or top-to-bottom order.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SFlowLayout
        extends SAbstractLayoutManager {
    /**
     * List of layouted components.
     */
    protected final List components;

    /**
     * Alignment (left, center, right) of components.
     */
    protected int align;
    
    /**
     * The horizontal gap (in pixels) specifiying the space
     * between columns.  They can be changed at any time.
     * This should be a non-negative integer.
     */
    protected int hgap = 0;

    /**
     * The vertical gap (in pixels) which specifiying the space
     * between rows.  They can be changed at any time.
     * This should be a non negative integer.
     */
    protected int vgap = 0;
    
    /**
     * Creates a new <code>SFlowLayout</code> with horizontal orientation and
     * left alignment.
     */
    public SFlowLayout() {
        components = new ArrayList(2);
        setAlignment(SConstants.CENTER_ALIGN);
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
     * Creates a new <code>SFlowLayout</code> with horizontal orientation and the given alignment
     * and gaps
     * @param alignment the alignment
     * @param hgap the horizontal gap
     * @param vgap the vertical gap
     */
    public SFlowLayout(int alignment, int hgap, int vgap) {
        this(alignment);
        setHgap(hgap);
        setVgap(vgap);
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
     * Gets the horizontal gap between components in pixel. Rendered half as margin left and margin right
     * Some PLAFs might ignore this property.
     *
     * @return the horizontal gap between components
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * Sets the horizontal gap between components to the specified value in pixe. Rendered half as margin left and margin right
     * Some PLAFs might ignore this property.
     *
     * @param hgap the horizontal gap between components
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * Gets the vertical gap between components in pixel. Rendered half as margin top and margin bottom
     * Some PLAFs might ignore this property.
     *
     * @return the vertical gap between components
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * Sets the vertical gap between components to the specified value in pixel.
     * Rendered half as margin top and margin bottom. Some PLAFs might ignore this property.
     *
     * @param vgap the vertical gap between components
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
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


