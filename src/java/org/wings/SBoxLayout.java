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
 * This is a box layout - a layout manager that allows multiple components
 * to be laid out either vertically {@link <code>SConstants#VERTICAL</code>}
 * or horizontally {@link <code>SConstants#HORIZONTAL</code>}.
 * <p/>
 * Nesting multiple panels with different combinations of horizontal
 * and vertical gives an effect similar to GridBagLayout, without the complexity.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SBoxLayout
        extends SAbstractLayoutManager {

    // Constants for swing compatibility
    public static final int X_AXIS = SConstants.HORIZONTAL;
    public static final int Y_AXIS = SConstants.VERTICAL;

    protected ArrayList components = new ArrayList(2);

    protected int orientation = SConstants.HORIZONTAL;
    protected int align = SConstants.LEFT_ALIGN;
    protected int cellSpacing = 0;
    protected int cellPadding = 0;
    protected int borderThickness = 0;

    /**
     * creates a new box layout with the given orientation
     *
     * @param orientation orientation
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
     * The paddding between the layout cells in pixel. Some PLAFs might ignore this property.
     * @param cellPadding cell padding in pixel
     */
    public void setCellPadding(int cellPadding) {
        this.cellPadding = cellPadding;
    }

    /**
     * The paddding between the layout cells in pixel. Some PLAFs might ignore this property.
     * @return cell padding in pixel
     */
    public int getCellPadding() {
        return cellPadding;
    }

    /**
     * The paddding between the layout cells in pixel. Some PLAFs might ignore this property.
     * @param cellSpacing The spacing between the layout cells. pixel
     */
    public void setCellSpacing(int cellSpacing) {
        this.cellSpacing = cellSpacing;
    }

    /**
     * The paddding between the layout cells in pixel. Some PLAFs might ignore this property.
     * @return The spacing between the layout cells. pixel
     */
    public int getCellSpacing() {
        return cellSpacing;
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
}


