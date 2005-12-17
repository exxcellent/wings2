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
 * STableColumnModel
 */
public interface STableColumnModel
{
    public void addColumn( STableColumn aColumn );

    public void removeColumn( STableColumn column );

    public void moveColumn( int columnIndex, int newIndex );

    public void setColumnMargin( int newMargin );

    public int getColumnCount();

    public Collection getColumns();

    public int getColumnIndex( Object columnIdentifier );

    public STableColumn getColumn( int columnIndex );

    public int getColumnMargin();

    public int getTotalColumnWidth();
}
