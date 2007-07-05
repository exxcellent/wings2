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
package org.wings.plaf;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Properties;

/**
 * A Property table that stores default. This table overrides the
 * mappings of its <code>parent</code> table.
 */
public class ResourceDefaults
        extends HashMap
{
    private PropertyChangeSupport changeSupport;
    private ResourceDefaults parent;
    private ResourceFactory factory;

    /**
     * @param parent the parent defaults table that backs this defaults table
     */
    public ResourceDefaults(ResourceDefaults parent, Properties properties) {
        this.parent = parent;
        this.factory = new ResourceFactory(properties);
    }

    /**
     * Set the value of <code>key</code> to <code>value</code>.
     * If <code>key</code> is a string and the new value isn't
     * equal to the old one, fire a PropertyChangeEvent.  If value
     * is null, the key is removed from the table.
     *
     * @param key   the unique Object who's value will be used to
     *              retreive the data value associated with it
     * @param value the new Object to store as data under that key
     * @return the previous Object value, or null
     * @see java.util.Map#put
     */
    public Object put(Object key, Object value) {
        Object oldValue = (value == null) ? super.remove(key) : super.put(key, value);
        if (key instanceof String) {
            firePropertyChange((String) key, oldValue, value);
        }
        return oldValue;
    }

    /**
     * Get a value from the defaults table.
     * If the <code>key</code> is not associated with a value,
     * the request is delegated to the parent defaults table
     *
     * @param key  the key
     * @param type the class of the value in question
     * @return the associated value or <code>null</code>
     */
    public Object get(Object key, Class type) {
        Object value = super.get(key);
        if (value == null) {
            // cached with a null value
            if (super.containsKey(key))
                return null;

            // ask the parent
            if (parent != null)
                value = parent.get(key, type);

            // ask the factory
            if (value == null && factory != null)
                value = factory.get(key, type);
        }

        super.put(key, value);
        return value;
    }


    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * <p/>
     * A PropertyChangeEvent will get fired whenever a default
     * is changed.
     *
     * @param listener The PropertyChangeListener to be added
     * @see java.beans.PropertyChangeSupport
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }


    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     *
     * @param listener The PropertyChangeListener to be removed
     * @see java.beans.PropertyChangeSupport
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }


    /**
     * Support for reporting bound property changes.  If oldValue and
     * newValue are not equal and the PropertyChangeEvent listener list
     * isn't empty, then fire a PropertyChange event to each listener.
     *
     * @param propertyName The programmatic name of the property that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     * @see java.beans.PropertyChangeSupport
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}
