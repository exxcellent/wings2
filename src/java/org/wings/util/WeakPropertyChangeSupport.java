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
package org.wings.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.io.Serializable;

/**
 * This is a utility class that can be used by beans that support bound
 * properties.  You can use an instance of this class as a member field
 * of your bean and delegate various work to it.
 * <p/>
 * This class is serializable.  When it is serialized it will save
 * (and restore) any listeners that are themselves serializable.  Any
 * non-serializable listeners will be skipped during serialization.
 *
 * @author <a href="mailto:haaf@mercatis.de">Holger Engels</a>
 */
public class WeakPropertyChangeSupport implements Serializable {
    /**
     * Constructs a <code>WeakPropertyChangeSupport</code> object.
     *
     * @param sourceBean The bean to be given as the source for any events.
     */
    public WeakPropertyChangeSupport(Object sourceBean) {
        if (sourceBean == null) {
            throw new NullPointerException();
        }
        source = sourceBean;
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener The PropertyChangeListener to be added
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listeners == null) {
            listeners = new java.util.LinkedList();
        } else
            processQueue();
        listeners.add(WeakEntry.create(listener, queue));
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listeners == null) {
            return;
        } else
            processQueue();
        listeners.remove(WeakEntry.create(listener));
    }

    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be invoked only when a call on firePropertyChange names that
     * specific property.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener     The PropertyChangeListener to be added
     */
    public synchronized void addPropertyChangeListener(String propertyName,
                                                       PropertyChangeListener listener) {
        if (children == null) {
            children = new java.util.WeakHashMap();
        }
        WeakPropertyChangeSupport child = (WeakPropertyChangeSupport) children.get(propertyName);
        if (child == null) {
            child = new WeakPropertyChangeSupport(source);
            children.put(propertyName, child);
        }
        child.addPropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     *
     * @param propertyName The name of the property that was listened on.
     * @param listener     The PropertyChangeListener to be removed
     */
    public synchronized void removePropertyChangeListener(String propertyName,
                                                          PropertyChangeListener listener) {
        if (children == null) {
            return;
        }
        WeakPropertyChangeSupport child = (WeakPropertyChangeSupport) children.get(propertyName);
        if (child == null) {
            return;
        }
        child.removePropertyChangeListener(listener);
    }

    /**
     * Report a bound property update to any registered listeners.
     * No event is fired if old and new are equal and non-null.
     *
     * @param propertyName The programmatic name of the property
     *                     that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            return;
        }

        java.util.LinkedList targets = null;
        WeakPropertyChangeSupport child = null;
        synchronized (this) {
            if (listeners != null) {
                targets = (java.util.LinkedList) listeners.clone();
            }
            if (children != null && propertyName != null) {
                child = (WeakPropertyChangeSupport) children.get(propertyName);
            }
        }

        PropertyChangeEvent evt = new PropertyChangeEvent(source, propertyName, oldValue, newValue);

        if (targets != null) {
            for (int i = 0; i < targets.size(); i++) {
                WeakEntry entry = (WeakEntry) targets.get(i);
                PropertyChangeListener target = (PropertyChangeListener) entry.get();
                if (target != null)
                    target.propertyChange(evt);
            }
        }

        if (child != null) {
            child.firePropertyChange(evt);
        }
    }

    /**
     * Report an int bound property update to any registered listeners.
     * No event is fired if old and new are equal and non-null.
     * <p/>
     * This is merely a convenience wrapper around the more general
     * firePropertyChange method that takes Object values.
     *
     * @param propertyName The programmatic name of the property
     *                     that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        if (oldValue == newValue) {
            return;
        }
        firePropertyChange(propertyName, new Integer(oldValue), new Integer(newValue));
    }


    /**
     * Report a boolean bound property update to any registered listeners.
     * No event is fired if old and new are equal and non-null.
     * <p/>
     * This is merely a convenience wrapper around the more general
     * firePropertyChange method that takes Object values.
     *
     * @param propertyName The programmatic name of the property
     *                     that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     */
    public void firePropertyChange(String propertyName,
                                   boolean oldValue, boolean newValue) {
        if (oldValue == newValue) {
            return;
        }
        firePropertyChange(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }

    /**
     * Fire an existing PropertyChangeEvent to any registered listeners.
     * No event is fired if the given event's old and new values are
     * equal and non-null.
     *
     * @param evt The PropertyChangeEvent object.
     */
    public void firePropertyChange(PropertyChangeEvent evt) {
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        String propertyName = evt.getPropertyName();
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            return;
        }

        java.util.LinkedList targets = null;
        WeakPropertyChangeSupport child = null;
        synchronized (this) {
            if (listeners != null) {
                targets = (java.util.LinkedList) listeners.clone();
            }
            if (children != null && propertyName != null) {
                child = (WeakPropertyChangeSupport) children.get(propertyName);
            }
        }

        if (targets != null) {
            for (int i = 0; i < targets.size(); i++) {
                WeakEntry entry = (WeakEntry) targets.get(i);
                PropertyChangeListener target = (PropertyChangeListener) entry.get();
                if (target != null)
                    target.propertyChange(evt);
            }
        }
        if (child != null) {
            child.firePropertyChange(evt);
        }
    }

    /**
     * Check if there are any listeners for a specific property.
     *
     * @param propertyName the property name.
     * @return true if there are ore or more listeners for the given property
     */
    public synchronized boolean hasListeners(String propertyName) {
        if (listeners != null && !listeners.isEmpty()) {
            // there is a generic listener
            return true;
        }
        if (children != null) {
            WeakPropertyChangeSupport child = (WeakPropertyChangeSupport) children.get(propertyName);
            if (child != null && child.listeners != null) {
                return !child.listeners.isEmpty();
            }
        }
        return false;
    }

    /**
     * "listeners" lists all the generic listeners.
     * <p/>
     * This is transient - its state is written in the writeObject method.
     */
    transient private java.util.LinkedList listeners;

    /**
     * Hashtable for managing listeners for specific properties.
     * Maps property names to WeakPropertyChangeSupport objects.
     */
    private transient java.util.WeakHashMap children;

    /**
     * The object to be provided as the "source" for any generated events.
     */
    private Object source;


    private transient ReferenceQueue queue = new ReferenceQueue();

    /**
     * Remove all invalidated entries from the map, that is, remove all entries
     * whose keys have been discarded.  This method should be invoked once by
     * each public mutator in this class.  We don't invoke this method in
     * public accessors because that can lead to surprising
     * ConcurrentModificationExceptions.
     */
    private void processQueue() {
        WeakEntry wk;
        while ((wk = (WeakEntry) queue.poll()) != null) {
            listeners.remove(wk);
        }
    }

    static private class WeakEntry extends WeakReference {
        private int hash;	/* Hashcode of key, stored here since the key
        may be tossed by the GC */

        private WeakEntry(Object k) {
            super(k);
            hash = k.hashCode();
        }

        private static WeakEntry create(Object k) {
            if (k == null)
                return null;
            else
                return new WeakEntry(k);
        }

        private WeakEntry(Object k, ReferenceQueue q) {
            super(k, q);
            hash = k.hashCode();
        }

        private static WeakEntry create(Object k, ReferenceQueue q) {
            if (k == null)
                return null;
            else
                return new WeakEntry(k, q);
        }

        /* A WeakEntry is equal to another WeakEntry iff they both refer to objects
         * that are, in turn, equal according to their own equals methods */
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WeakEntry)) return false;
            Object t = this.get();
            Object u = ((WeakEntry) o).get();
            if ((t == null) || (u == null)) return false;
            if (t == u) return true;
            return t.equals(u);
        }

        public int hashCode() {
            return hash;
        }
    }
}


