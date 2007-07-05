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
package org.wings.table;

import javax.swing.*;
import java.io.Serializable;

/**
 * STableColumn
 */
public class STableColumn implements Serializable {
    protected int modelIndex;
    protected Object identifier;
    protected Object headerValue;
    protected String width;
    protected boolean hidden = false;
    protected STableCellRenderer headerRenderer;
    protected STableCellRenderer cellRenderer;
    protected STableCellEditor cellEditor;

    /**
     * Empty constructor of a table column. Assumes model index 0.
     */
    public STableColumn() {
        this(0);
    }


    /**
     * Constructs a new table column.
     *
     * @param modelIndex The index of this column inside the data model.
     */
    public STableColumn(int modelIndex) {
        this(modelIndex, null, null, null);
    }

    /**
     * Constructs a new table column.
     *
     * @param modelIndex The index of this column inside the data model.
     * @param width The desired width of this column as relative weight. (1 = default)
     */
    public STableColumn(int modelIndex, String width) {
        this(modelIndex, width, null, null);
    }

    /**
     * Constructs a new table column.
     *
     * @param modelIndex The index of this column inside the data model.
     * @param width The desired width of this column in px.
     * @param cellRenderer The renderer for cells in this column
     * @param cellEditor The editor for cells in this column
     */
    public STableColumn(int modelIndex, String width,
                        STableCellRenderer cellRenderer,
                        STableCellEditor cellEditor) {
        super();
        this.modelIndex = modelIndex;
        this.width = width;

        this.cellRenderer = cellRenderer;
        this.cellEditor = cellEditor;
        headerValue = null;
    }


    /**
     * Sets the cmp2 index for this column. The cmp2 index is the
     * index of the column in the cmp2 that will be displayed by this
     * <code>STableColumn</code>. As the <code>STableColumn</code>
     * is moved around in the view the cmp2 index remains constant.
     *
     * @param modelIndex the new modelIndex
     * bound: true
     * description: The cmp2 index.
     */
    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }

    /**
     * Returns the cmp2 index for this column.
     *
     * @return the <code>modelIndex</code> property
     */
    public int getModelIndex() {
        return modelIndex;
    }

    /**
     * Sets the <code>STableColumn</code>'s identifier to
     * <code>anIdentifier</code>. <p>
     * Note: identifiers are not used by the <code>JTable</code>,
     * they are purely a
     * convenience for the external tagging and location of columns.
     *
     * @param identifier an identifier for this column
     * @see #getIdentifier
     *      bound: true
     *      description: A unique identifier for this column.
     */
    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }


    /**
     * Returns the <code>identifier</code> object for this column.
     * Note identifiers are not used by <code>JTable</code>,
     * they are purely a convenience for external use.
     * If the <code>identifier</code> is <code>null</code>,
     * <code>getIdentifier()</code> returns <code>getHeaderValue</code>
     * as a default.
     *
     * @return the <code>identifier</code> property
     * @see #setIdentifier
     */
    public Object getIdentifier() {
        return (identifier != null) ? identifier : getHeaderValue();
    }

    /**
     * Returns the <code>Object</code> used as the value for the header
     * renderer.
     *
     * @return the <code>headerValue</code> property
     * @see #setHeaderValue
     */
    public Object getHeaderValue() {
        return headerValue;
    }

    /**
     * Sets the <code>Object</code> whose string representation will be
     * used as the value for the header for this column.
     */
    public void setHeaderValue(Object headerValue) {
        this.headerValue = headerValue;
    }


    //
    // Renderers and Editors
    //

    /**
     * Sets the <code>TableCellRenderer</code> used to draw the
     * <code>STableColumn</code>'s header to <code>headerRenderer</code>.
     *
     * @param headerRenderer the new headerRenderer
     * @see #getHeaderRenderer
     *      bound: true
     *      description: The header renderer.
     */
    public void setHeaderRenderer(STableCellRenderer headerRenderer) {
        this.headerRenderer = headerRenderer;
    }

    /**
     * Returns the <code>TableCellRenderer</code> used to draw the header of the
     * <code>STableColumn</code>. When the <code>headerRenderer</code> is
     * <code>null</code>, the <code>JTableHeader</code>
     * uses its <code>defaultRenderer</code>. The default value for a
     * <code>headerRenderer</code> is <code>null</code>.
     *
     * @return the <code>headerRenderer</code> property
     * @see #setHeaderRenderer
     * @see #setHeaderValue
     * @see javax.swing.table.JTableHeader#getDefaultRenderer()
     */
    public STableCellRenderer getHeaderRenderer() {
        return headerRenderer;
    }

    /**
     * Sets the <code>TableCellRenderer</code> used by <code>JTable</code>
     * to draw individual values for this column.
     *
     * @param cellRenderer the new cellRenderer
     * @see #getCellRenderer bound: true
     *      description: The renderer to use for cell values.
     */
    public void setCellRenderer(STableCellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    /**
     * Returns the <code>TableCellRenderer</code> used by the
     * <code>JTable</code> to draw
     * values for this column.  The <code>cellRenderer</code> of the column
     * not only controls the visual look for the column, but is also used to
     * interpret the value object supplied by the <code>TableModel</code>.
     * When the <code>cellRenderer</code> is <code>null</code>,
     * the <code>JTable</code> uses a default renderer based on the
     * class of the cells in that column. The default value for a
     * <code>cellRenderer</code> is <code>null</code>.
     *
     * @return the <code>cellRenderer</code> property
     * @see #setCellRenderer
     * @see JTable#setDefaultRenderer
     */
    public STableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    /**
     * Sets the editor to used by when a cell in this column is edited.
     *
     * @param cellEditor the new cellEditor
     * @see #getCellEditor bound: true
     *      description: The editor to use for cell values.
     */
    public void setCellEditor(STableCellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }

    /**
     * Returns the <code>TableCellEditor</code> used by the
     * <code>JTable</code> to edit values for this column.  When the
     * <code>cellEditor</code> is <code>null</code>, the <code>JTable</code>
     * uses a default editor based on the
     * class of the cells in that column. The default value for a
     * <code>cellEditor</code> is <code>null</code>.
     *
     * @return the <code>cellEditor</code> property
     * @see #setCellEditor
     * @see JTable#setDefaultEditor
     */
    public STableCellEditor getCellEditor() {
        return cellEditor;
    }

    /**
     * The widht for this column including the unit
     *
     * @param width The width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * @return The widht of this column including the unit.
     */
    public String getWidth() {
        return width;
    }

    /**
     * Indicates if this column is hidden
     *
     * @return <code>true</code> if this column is invisible
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Indicates if this column should be hidden
     *
     * @param hidden <code>true</code> if this column should be invisible
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
