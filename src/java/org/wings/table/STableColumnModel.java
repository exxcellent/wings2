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

import java.util.Collection;

import org.wings.event.STableColumnModelListener;

/**
 * @see  javax.swing.table.TableColumnModel
 */
public interface STableColumnModel {
    void addColumn( STableColumn aColumn );

    void removeColumn( STableColumn column );

    void moveColumn( int columnIndex, int newIndex );

    void setColumnMargin( int newMargin );

    int getColumnCount();

    Collection getColumns();

    int getColumnIndex( Object columnIdentifier );

    STableColumn getColumn( int columnIndex );

    int getColumnMargin();

    /**
     * @return The total width of this table including the unit
     */
    String getTotalColumnWidth();

    /**
     * Adds a listener for table column model events.
     *
     * @param x  a <code>STableColumnModelListener</code> object
     */
    void addColumnModelListener(STableColumnModelListener x);

    /**
     * Removes a listener for table column model events.
     *
     * @param x  a <code>STableColumnModelListener</code> object
     */
    void removeColumnModelListener(STableColumnModelListener x);
}
