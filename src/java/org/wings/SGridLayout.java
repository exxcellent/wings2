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
 * This is a grid layout
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SGridLayout
        extends SAbstractLayoutManager {
    protected ArrayList components = new ArrayList(2);
    protected int rows = 1;
    protected int cols = 1;
    protected int border = 0;
    protected boolean renderFirstLineAsHeader = false;
    protected int cellPadding = -1;
    protected int cellSpacing = -1;

    /**
     * creates a new grid layout with 1 row and 1 column extent
     */
    public SGridLayout() {
        this.setPreferredSize(new SDimension("100%", "100%"));
    }

    /**
     * creats a new grid layout with the given number of columns
     *
     * @param cols number of columns
     */
    public SGridLayout(int cols) {
        this();
        setColumns(cols);
    }

    /**
     * creats a new grid layout with the given number of columns and rows
     *
     * @param rows number of rows
     * @param cols number of columns
     */
    public SGridLayout(int rows, int cols) {
        this(cols);
        setRows(rows);
    }

    /**
     * sets the number of columns
     *
     * @param c number of columns
     */
    public void setColumns(int c) {
        cols = c;
    }

    /**
     * returns the number of columns
     *
     * @return number of columns
     */
    public int getColumns() {
        return cols;
    }

    /**
     * sets the number of rows
     *
     * @param r number of rows
     */
    public void setRows(int r) {
        rows = r;
    }

    /**
     * returns the number of rows
     *
     * @return number of rows
     */
    public int getRows() {
        return rows;
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


    public void setCellPadding(int p) {
        cellPadding = p;
    }

    public int getCellPadding() {
        return cellPadding;
    }


    public void setCellSpacing(int s) {
        cellSpacing = s;
    }

    public int getCellSpacing() {
        return cellSpacing;
    }


    public void setBorder(int pixel) {
        border = pixel;
    }

    public int getBorder() {
        return border;
    }

    /**
     * Renders the first line as HTML <code>&lt;th&gt;</code> instead regular <code>&lt;tr&gt;</code>.
     * @param b true if first line should be rendered as header
     */
    public void setRenderFirstLineAsHeader(boolean b) {
        renderFirstLineAsHeader = b;
    }

    /**
     * {@link #setRenderFirstLineAsHeader(boolean)}
     */
    public boolean getRenderFirstLineAsHeader() {
        return renderFirstLineAsHeader;
    }
}


