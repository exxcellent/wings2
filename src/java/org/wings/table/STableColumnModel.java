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
package org.wings.table;

import java.util.Collection;

/**
 * @see  javax.swing.table.TableColumnModel
 */
public interface STableColumnModel
{
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
     * @return The total width of this table. <code>-1</code> indicates a problem (mismatching width untits).
     */
    int getTotalColumnWidth();

    /**
     * @return The unit of the total column width. Only valid if getTotalColumnWidth() returns >= 0!
     */
    public String getTotalColumnWidthUnit();

}
