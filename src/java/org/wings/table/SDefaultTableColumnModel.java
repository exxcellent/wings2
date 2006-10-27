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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

/*
 * @see STableColumnModel
 */
public class SDefaultTableColumnModel implements STableColumnModel, Serializable {
    /**
     * Apache jakarta commons logger
     */
    private final static Log log = LogFactory.getLog(SDefaultTableColumnModel.class);
    private List columns = new LinkedList();
    private int columnMargin;
    private String totalColumnWidth;

    public void addColumn(STableColumn column) {
        if (column == null)
            throw new IllegalArgumentException("Column is null");

        columns.add(column);
    }

    public void removeColumn(STableColumn column) {
        if (column == null)
            throw new IllegalArgumentException("Column is null");

        columns.remove(column);
    }

    public void moveColumn(int columnIndex, int newIndex) {
        if ((columnIndex < 0) || (columnIndex >= getColumnCount()) ||
                (newIndex < 0) || (newIndex >= getColumnCount()))
            throw new IllegalArgumentException("moveColumn() - Index out of range");

        STableColumn column = (STableColumn) columns.remove(columnIndex);
        columns.add(newIndex, column);
    }

    public void setColumnMargin(int newMargin) {
        this.columnMargin = newMargin;
    }

    public int getColumnCount() {
        return columns.size();
    }

    public Collection getColumns() {
        return columns;
    }

    public int getColumnIndex(Object columnIdentifier) {
        int index = 0;
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            STableColumn column = (STableColumn) iterator.next();
            if (columnIdentifier.equals(column.getIdentifier()))
                return index;

            index++;
        }

        return -1;
    }

    public STableColumn getColumn(int columnIndex) {
        if (columns == null || columnIndex >= columns.size() || columnIndex < 0)
            return null;
        else
            return (STableColumn) columns.get(columnIndex);
    }

    public int getColumnMargin() {
        return columnMargin;
    }

    public String getTotalColumnWidth() {
        return totalColumnWidth;
    }

    public void setTotalColumnWidth(String totalColumnWidth) {
        this.totalColumnWidth = totalColumnWidth;
    }
}
