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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.wings.event.STableColumnModelEvent;
import org.wings.event.STableColumnModelListener;

/*
 * @see STableColumnModel
 */
public class SDefaultTableColumnModel implements STableColumnModel, Serializable {

    private List columns = new LinkedList();
    private int columnMargin;
    private String totalColumnWidth;

    /** Change event (only one instance needed) */
    transient protected ChangeEvent changeEvent = null;

    /** List of STableColumnModelListener */
    protected EventListenerList listenerList = new EventListenerList();

    public void addColumn(STableColumn column) {
        if (column == null)
            throw new IllegalArgumentException("Column is null");

        columns.add(column);

        fireColumnAdded(new STableColumnModelEvent(this, 0, getColumnCount() - 1));
    }

    public void removeColumn(STableColumn column) {
        if (column == null)
            throw new IllegalArgumentException("Column is null");

        int columnIndex = columns.indexOf(column);

    	if (columnIndex != -1) {
    		columns.remove(columnIndex);
        	fireColumnRemoved(new STableColumnModelEvent(this, columnIndex, 0));
    	}
    }

    public void moveColumn(int columnIndex, int newIndex) {
        if ((columnIndex < 0) || (columnIndex >= getColumnCount()) ||
                (newIndex < 0) || (newIndex >= getColumnCount()))
            throw new IllegalArgumentException("moveColumn() - Index out of range");

        STableColumn column = (STableColumn) columns.remove(columnIndex);
        columns.add(newIndex, column);

        fireColumnMoved(new STableColumnModelEvent(this, columnIndex, newIndex));
    }

    public void setColumnMargin(int newMargin) {
    	if (columnMargin != newMargin) {
    		columnMargin = newMargin;
    		fireColumnMarginChanged();
    	}
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

    /**
     * Indicates if the given column is hidden.
     *
     *
     * @return <code>true</code> if the given column is invisible
     */
    public boolean isColumnHidden(STableColumn column) {
        return column.isHidden();
    }

    /**
     * Indicates if the given column should be hidden.
     *
     * @param hidden <code>true</code> if the given column should be invisible
     */
    public void setColumnHidden(STableColumn column, boolean hidden) {
    	if (column.isHidden() != hidden) {
    		column.setHidden(hidden);
    		if (hidden) fireColumnHidden(column);
    		else fireColumnShown(column);
    	}
    }

    /**
     * Adds a listener for table column model events.
     * @param x  a <code>STableColumnModelListener</code> object
     */
    public void addColumnModelListener(STableColumnModelListener x) {
    	listenerList.add(STableColumnModelListener.class, x);
    }

    /**
     * Removes a listener for table column model events.
     * @param x  a <code>STableColumnModelListener</code> object
     */
    public void removeColumnModelListener(STableColumnModelListener x) {
    	listenerList.remove(STableColumnModelListener.class, x);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     * @param e  the event received
     * @see EventListenerList
     */
    protected void fireColumnAdded(STableColumnModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
		    if (listeners[i] == STableColumnModelListener.class) {
		    	((STableColumnModelListener) listeners[i+1]).columnAdded(e);
		    }
		}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     * @param e  the event received
     * @see EventListenerList
     */
    protected void fireColumnRemoved(STableColumnModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
		    if (listeners[i] == STableColumnModelListener.class) {
		    	((STableColumnModelListener) listeners[i+1]).columnRemoved(e);
		    }
		}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     * @param e  the event received
     * @see EventListenerList
     */
    protected void fireColumnMoved(STableColumnModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
		    if (listeners[i] == STableColumnModelListener.class) {
		    	((STableColumnModelListener) listeners[i+1]).columnMoved(e);
		    }
		}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     * @see EventListenerList
     */
    protected void fireColumnMarginChanged() {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i>=0; i-=2) {
    	    if (listeners[i] == STableColumnModelListener.class) {
	    		// Lazily create the event:
	    		if (changeEvent == null) changeEvent = new ChangeEvent(this);
	    		((STableColumnModelListener) listeners[i+1]).columnMarginChanged(changeEvent);
    	    }
    	}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     * @see EventListenerList
     */
    protected void fireColumnShown(STableColumn column) {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i>=0; i-=2) {
    	    if (listeners[i] == STableColumnModelListener.class) {
	    		((STableColumnModelListener) listeners[i+1]).columnShown(new ChangeEvent(column));
    	    }
    	}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     * @see EventListenerList
     */
    protected void fireColumnHidden(STableColumn column) {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i>=0; i-=2) {
    	    if (listeners[i] == STableColumnModelListener.class) {
	    		((STableColumnModelListener) listeners[i+1]).columnHidden(new ChangeEvent(column));
    	    }
    	}
    }
}
