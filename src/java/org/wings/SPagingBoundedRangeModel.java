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
import java.util.EventListener;


/**
 * Paging implementation of SBoundedRangeModel
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class SPagingBoundedRangeModel implements SBoundedRangeModel {

    /**
     * Only one <code>ChangeEvent</code> is needed per model instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    protected transient ChangeEvent changeEvent = null;

    /**
     * The listeners waiting for model changes.
     */
    protected EventListenerList listenerList = new EventListenerList();

    protected int value = 0;
    protected int extent = 0;
    protected int min = 0;
    protected int max = 100;
    protected boolean isAdjusting = false;

    /**
     * indicates if we should fire event immediately when they arise, or if we
     * should collect them for a later delivery
     */
    private boolean delayEvents = false;

    /**
     * got a delayed Event?
     */
    protected boolean gotDelayedEvent = false;


    public SPagingBoundedRangeModel() {
        super();
    }

    public SPagingBoundedRangeModel(int value, int extent, int min, int max) {
        setRangeProperties(value, extent, min, max, false);
    }

    /**
     * Returns the model's current value.
     *
     * @return the model's current value
     * @see #setValue
     * @see javax.swing.BoundedRangeModel#getValue
     */
    public int getValue() {
        return value;
    }


    /**
     * Returns the model's extent.
     *
     * @return the model's extent
     * @see #setExtent
     * @see javax.swing.BoundedRangeModel#getExtent
     */
    public int getExtent() {
        return extent;
    }


    /**
     * Returns the model's minimum.
     *
     * @return the model's minimum
     * @see #setMinimum
     * @see javax.swing.BoundedRangeModel#getMinimum
     */
    public int getMinimum() {
        return min;
    }


    /**
     * Returns the model's maximum.
     *
     * @return the model's maximum
     * @see #setMaximum
     * @see javax.swing.BoundedRangeModel#getMaximum
     */
    public int getMaximum() {
        return max;
    }

    /**
     * Sets the current value of the model. For a slider, that
     * determines where the knob appears. Ensures that the new
     * value, <I>n</I> falls within the model's constraints:
     * <pre>
     *     minimum <= value <= maximum
     * </pre>
     *
     * @see javax.swing.BoundedRangeModel#setValue
     */
    public void setValue(int n) {
        n = Math.min(n, Integer.MAX_VALUE - extent);

        int newValue = Math.max(n, min);
        if (newValue + extent > max) {
            newValue = max - extent;
        }
        setRangeProperties(newValue, extent, min, max, isAdjusting);
    }


    /**
     * Sets the extent to <I>n</I> after ensuring that <I>n</I>
     * is greater than or equal to zero and falls within the model's
     * constraints:
     * <pre>
     *     minimum <= value <= maximum
     * </pre>
     *
     * @see javax.swing.BoundedRangeModel#setExtent
     */
    public void setExtent(int n) {
        int newExtent = Math.max(0, n);
        if(value + newExtent > max) {
            newExtent = max - value;
        }
        setRangeProperties(value, newExtent, min, max, isAdjusting);
    }


    /**
     * Sets the minimum to <I>n</I> after ensuring that <I>n</I>
     * that the other three properties obey the model's constraints:
     * <pre>
     *     minimum <= value <= maximum
     * </pre>
     *
     * @see #getMinimum
     * @see javax.swing.BoundedRangeModel#setMinimum
     */
    public void setMinimum(int n) {
        int newMax = Math.max(n, max);
        int newValue = Math.max(n, value);
        int newExtent = Math.min(newMax - newValue, extent);
        setRangeProperties(newValue, newExtent, n, newMax, isAdjusting);
    }


    /**
     * Sets the maximum to <I>n</I> after ensuring that <I>n</I>
     * that the other three properties obey the model's constraints:
     * <pre>
     *     minimum <= value <= maximum
     * </pre>
     *
     * @see javax.swing.BoundedRangeModel#setMaximum
     */
    public void setMaximum(int n) {
        int newMin = Math.min(n, min);
        int newExtent = Math.min(n - newMin, extent);
        int newValue = Math.min(n - newExtent, value);
        setRangeProperties(newValue, newExtent, newMin, n, isAdjusting);
    }


    /**
     * Sets the <code>valueIsAdjusting</code> property.
     *
     * @see #getValueIsAdjusting
     * @see #setValue
     * @see javax.swing.BoundedRangeModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) {
        setRangeProperties(value, extent, min, max, b);
    }

    /**
     * Returns true if the value is in the process of changing
     * as a result of actions being taken by the user.
     *
     * @return the value of the <code>valueIsAdjusting</code> property
     * @see #setValue
     * @see javax.swing.BoundedRangeModel#getValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    /**
     * Sets all of the <code>BoundedRangeModel</code> properties after forcing
     * the arguments to obey the usual constraints:
     * <pre>
     *     minimum <= value <= maximum
     * </pre>
     * <p/>
     * At most, one <code>ChangeEvent</code> is generated.
     *
     * @see javax.swing.BoundedRangeModel#setRangeProperties
     * @see #setValue
     * @see #setExtent
     * @see #setMinimum
     * @see #setMaximum
     * @see #setValueIsAdjusting
     */
    public void setRangeProperties(int newValue,
                                   int newExtent,
                                   int newMin,
                                   int newMax,
                                   boolean adjusting) {
        if (newMin > newMax) {
            newMin = newMax;
        }
        if (newValue > newMax) {
            newMax = newValue;
        }
        if (newValue < newMin) {
            newMin = newValue;
        }
        if (newExtent < 0) {
            newExtent = 0;
        }
        if ((newExtent + newValue) > newMax) {
            newExtent = newMax - newValue;
    	}

        boolean isChange =
                (newValue != value) ||
                (newExtent != extent) ||
                (newMin != min) ||
                (newMax != max) ||
                (adjusting != isAdjusting);

        if (isChange) {
            value = newValue;
            extent = newExtent;
            min = newMin;
            max = newMax;
            isAdjusting = adjusting;

            fireStateChanged();
        }
    }

    public boolean getDelayEvents() {
        return delayEvents;
    }

    public void setDelayEvents(boolean b) {
        delayEvents = b;
    }


    /**
     * Adds a <code>ChangeListener</code>.  The change listeners are run each
     * time any one of the Bounded Range model properties changes.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener
     * @see javax.swing.BoundedRangeModel#addChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }


    /**
     * Removes a <code>ChangeListener</code>.
     *
     * @param l the <code>ChangeListener</code> to remove
     * @see #addChangeListener
     * @see javax.swing.BoundedRangeModel#removeChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    /**
     * Returns an array of all the change listeners
     * registered on this <code>DefaultBoundedRangeModel</code>.
     *
     * @return all of this model's <code>ChangeListener</code>s
     *         or an empty
     *         array if no change listeners are currently registered
     * @see #addChangeListener
     * @see #removeChangeListener
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(ChangeListener.class);
    }


    /**
     * Runs each <code>ChangeListener</code>'s <code>componentChanged</code> method.
     *
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        if (delayEvents) {
            gotDelayedEvent = true;
        } else {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ChangeListener.class) {
                    if (changeEvent == null) {
                        changeEvent = new ChangeEvent(this);
                    }
                    ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
                }
            }
        }
    }


    /**
     * Returns a string that displays all of the
     * <code>BoundedRangeModel</code> properties.
     */
    public String toString() {
        String modelString =
                "value=" + getValue() + ", " +
                "extent=" + getExtent() + ", " +
                "min=" + getMinimum() + ", " +
                "max=" + getMaximum() + ", " +
                "adj=" + getValueIsAdjusting();

        return getClass().getName() + "[" + modelString + "]";
    }

    /**
     * Returns an array of all the objects currently registered as
     * <code><em>Foo</em>Listener</code>s
     * upon this model.
     * <code><em>Foo</em>Listener</code>s
     * are registered using the <code>add<em>Foo</em>Listener</code> method.
     * <p/>
     * You can specify the <code>listenerType</code> argument
     * with a class literal, such as <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a <code>DefaultBoundedRangeModel</code>
     * instance <code>m</code>
     * for its change listeners
     * with the following code:
     * <p/>
     * <pre>ChangeListener[] cls = (ChangeListener[])(m.getListeners(ChangeListener.class));</pre>
     * <p/>
     * If no such listeners exist,
     * this method returns an empty array.
     *
     * @param listenerType the type of listeners requested;
     *                     this parameter should specify an interface
     *                     that descends from <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *         <code><em>Foo</em>Listener</code>s
     *         on this model,
     *         or an empty array if no such
     *         listeners have been added
     * @throws ClassCastException if <code>listenerType</code> doesn't
     *                            specify a class or interface that implements
     *                            <code>java.util.EventListener</code>
     * @see #getChangeListeners
     * @since 1.3
     */
    public EventListener[] getListeners(Class listenerType) {
        return listenerList.getListeners(listenerType);
    }


    /**
     * fire event with isValueIsAdjusting true
     */
    public void fireDelayedIntermediateEvents() {
    }

    public void fireDelayedFinalEvents() {
        if (!delayEvents && gotDelayedEvent) {
            fireStateChanged();
            gotDelayedEvent = false;
        }
    }

}// SPagingBoundedRangeModel
