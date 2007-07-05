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
 * Allows multiple components to be laid out either vertically or horizontally.
 * <p/>
 * Nesting multiple panels with different combinations of horizontal
 * and vertical gives an effect similar to GridBagLayout, without the complexity.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SBoxLayout
        extends SAbstractLayoutManager {

    // Constants for swing compatibility
    public static final int X_AXIS = SConstants.HORIZONTAL;
    public static final int Y_AXIS = SConstants.VERTICAL;

    protected ArrayList components = new ArrayList(2);

    protected int orientation = SConstants.HORIZONTAL;
    protected int align = SConstants.LEFT_ALIGN;
    protected int borderThickness = 0;

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
     * creates a new box layout with the given orientation
     * 
     * @param orientation either {@link <code>SConstants#VERTICAL</code>} or {@link <code>SConstants#HORIZONTAL</code>}
     */
    public SBoxLayout(int orientation) {
        setOrientation(orientation);
    }

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
     * Sets the orientation. Use one of the following types:
     *
     * @param o One of the following constants:
     *          {@link <code>SConstants#HORIZONTAL</code>} or
     *          {@link <code>SConstants#VERTICAL</code>}
     */
    public void setOrientation(int o) {
        orientation = o;
    }

    /**
     * returns the orientation
     *
     * @return orientation
     */
    public int getOrientation() {
        return orientation;
    }


    /**
     * Typical PLAFs will render this layout as invisible table (border = 0). Use this property to make it visible
     * @param borderThickness The rendered border with in pixel
     */
    public void setBorder(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    /**
     * Typical PLAFs will render this layout as invisible table (border = 0). Use this property to make it visible
     * @return The rendered border with in pixel
     */
    public int getBorder() {
        return borderThickness;
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
}


