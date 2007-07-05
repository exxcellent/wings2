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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.io.Serializable;

/**
 * Default implementation of {@link SButtonModel}
 */
public class SDefaultButtonModel
        implements SButtonModel, Serializable {
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private boolean selected;
    private EventListenerList listeners = null;

    protected transient ChangeEvent changeEvent = null;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected == selected) {
            return;
        }
        this.selected = selected;
        fireStateChanged();
    }

    public Object[] getSelectedObjects() {
        return new Object[0];
    }

    public void addChangeListener(ChangeListener listener) {
        if (listeners == null)
            listeners = new EventListenerList();
        listeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if (listeners == null)
            return;
        listeners.remove(ChangeListener.class, listener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listeners.getListeners(ChangeListener.class);
    }

    protected final Object[] getListenerList() {
        if (listeners == null) {
            return EMPTY_OBJECT_ARRAY;
        } else {
            return listeners.getListenerList();
        } // end of else
    }

    protected void fireStateChanged() {
        Object[] listeners = getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
