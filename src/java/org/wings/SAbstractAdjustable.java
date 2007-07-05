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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.Serializable;

/**
 * Base class for adjustable elements like {@link SScrollBar} and {@link SPageScroller} 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public abstract class SAbstractAdjustable extends SComponent implements Adjustable, LowLevelEventListener {
    public static final int UNIT = 0;

    public static final int BLOCK = 1;

    public static final int MARGIN = 2;

    /**
     * All changes from the model are treated as though the user moved
     * the scrollbar knob.
     */
    private final ChangeListener fwdAdjustmentEvents = new ModelListener();

    /**
     * The model that represents the scrollbar's minimum, maximum, extent
     * (aka "visibleAmount") and current value.
     *
     * @see #setModel
     */
    protected SBoundedRangeModel model;

    /**
     * @see #setUnitIncrement
     */
    protected int unitIncrement;

    /**
     * @see #setBlockIncrement
     */
    protected int blockIncrement;

    /**
     * @see #setBlockIncrement
     */
    protected int orientation;

    /**
     * Creates a scrollbar with the specified orientation,
     * value, extent, mimimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known
     * as the "visible amount".
     * <p/>
     * Note: Use <code>setBlockIncrement</code> to set the block
     * increment to a size slightly smaller than the view's extent.
     * That way, when the user jumps the knob to an adjacent position,
     * one or two lines of the original contents remain in view.
     *
     * @throws IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public SAbstractAdjustable(int value, int extent, int min, int max) {
        this(new SDefaultBoundedRangeModel(value, extent, min, max));
    }


    public SAbstractAdjustable(SBoundedRangeModel model) {
        this.model = model;
        this.model.addChangeListener(fwdAdjustmentEvents);
        this.unitIncrement = 1;
        this.blockIncrement = (model.getExtent() == 0) ? 1 : model.getExtent();
    }

    /**
     * Creates a scrollbar with the specified orientation
     * and the following initial values:
     * <pre>
     * minimum = 0
     * maximum = 100
     * value = 0
     * extent = 10
     * </pre>
     */
    public SAbstractAdjustable() {
        this(0, 10, 0, 100);
    }

    /**
     * Returns data model that handles the scrollbar's four
     * fundamental properties: minimum, maximum, value, extent.
     *
     * @see #setModel
     */
    public final SBoundedRangeModel getModel() {
        return model;
    }


    /**
     * Sets the model that handles the scrollbar's four
     * fundamental properties: minimum, maximum, value, extent.
     * @see #getModel
     */
    public void setModel(SBoundedRangeModel newModel) {
        reloadIfChange(this.model, newModel);
        if (model != null) {
            model.removeChangeListener(fwdAdjustmentEvents);
        }
        model = newModel;
        if (model != null) {
            model.addChangeListener(fwdAdjustmentEvents);
        }
    }


    /**
     * Returns the amount to change the scrollbar's value by,
     * given a unit up/down request.  A ScrollBarUI implementation
     * typically calls this method when the user clicks on a scrollbar
     * up/down arrow and uses the result to update the scrollbar's
     * value.   Subclasses my override this method to compute
     * a value, e.g. the change required to scroll up or down one
     * (variable height) line text or one row in a table.
     * <p/>
     * The JScrollPane component creates scrollbars (by default)
     * that override this method and delegate to the viewports
     * Scrollable view, if it has one.  The Scrollable interface
     * provides a more specialized version of this method.
     *
     * @param direction is -1 or 1 for up/down respectively
     * @return the value of the unitIncrement property
     * @see #setUnitIncrement
     * @see #setValue
     */
    public int getUnitIncrement(int direction) {
        return unitIncrement;
    }


    /**
     * Sets the unitIncrement property.
     *
     * @see #getUnitIncrement
     */
    public void setUnitIncrement(int unitIncrement) {
        reloadIfChange(this.unitIncrement, unitIncrement);
        this.unitIncrement = unitIncrement;
    }

    /**
     * Returns the amount to change the scrollbar's value by,
     * given a block (usually "page") up/down request.  A ScrollBarUI
     * implementation typically calls this method when the user clicks
     * above or below the scrollbar "knob" to change the value
     * up or down by large amount.  Subclasses my override this
     * method to compute a value, e.g. the change required to scroll
     * up or down one paragraph in a text document.
     * <p/>
     * The JScrollPane component creates scrollbars (by default)
     * that override this method and delegate to the viewports
     * Scrollable view, if it has one.  The Scrollable interface
     * provides a more specialized version of this method.
     *
     * @param direction is -1 or 1 for up/down respectively
     * @return the value of the blockIncrement property
     * @see #setBlockIncrement
     * @see #setValue
     */
    public int getBlockIncrement(int direction) {
        return blockIncrement;
    }


    /**
     * Sets the blockIncrement property.
     * The scrollbar's block increment.
     * @see #getBlockIncrement()
     */
    public void setBlockIncrement(int blockIncrement) {
        reloadIfChange(this.blockIncrement, blockIncrement);
        this.blockIncrement = blockIncrement;
    }


    /**
     * For backwards compatibility with java.awt.Scrollbar.
     *
     * @see Adjustable#getUnitIncrement
     * @see #getUnitIncrement(int)
     */
    public final int getUnitIncrement() {
        return unitIncrement;
    }


    /**
     * For backwards compatibility with java.awt.Scrollbar.
     *
     * @see Adjustable#getBlockIncrement
     * @see #getBlockIncrement(int)
     */
    public final int getBlockIncrement() {
        return blockIncrement;
    }


    /**
     * Returns the scrollbar's value.
     *
     * @return the model's value property
     * @see #setValue
     */
    public final int getValue() {
        return getModel().getValue();
    }


    /**
     * Sets the scrollbar's value.  This method just forwards the value
     * to the model.
     *
     * @see #getValue
     * @see BoundedRangeModel#setValue
     */
    public void setValue(int value) {
        getModel().setValue(value);
    }

    public final int getExtent() {
        return getModel().getExtent();
    }


    public void setExtent(int value) {
        getModel().setExtent(value);
    }


    /**
     * Returns the scrollbar's extent, aka its "visibleAmount".  In many
     * scrollbar look and feel implementations the size of the
     * scrollbar "knob" or "thumb" is proportional to the extent.
     *
     * @return the value of the model's extent property
     * @see #setVisibleAmount
     */
    public final int getVisibleAmount() {
        return getModel().getExtent();
    }


    /**
     * Set the model's extent property:
     * The amount of the view that is currently visible.
     *
     * @see #getVisibleAmount
     * @see BoundedRangeModel#setExtent
     */
    public void setVisibleAmount(int extent) {
        getModel().setExtent(extent);
    }


    /**
     * Returns the minimum value supported by the scrollbar
     * (usually zero).
     *
     * @return the value of the model's minimum property
     * @see #setMinimum
     */
    public final int getMinimum() {
        return getModel().getMinimum();
    }


    /**
     * Sets the scrollbar's minimum value..
     *
     *
     * @see #getMinimum
     * @see BoundedRangeModel#setMinimum
     */
    public void setMinimum(int minimum) {
        getModel().setMinimum(minimum);
    }


    /**
     * The maximum value of the scrollbar is maximum - extent.
     *
     * @return the value of the model's maximum property
     * @see #setMaximum
     */
    public final int getMaximum() {
        return getModel().getMaximum();
    }


    /**
     * Sets the model's maximum property.  Note that the scrollbar's value
     * can only be set to maximum - extent. The scrollbar's maximum value.
     *
     * @see #getMaximum
     * @see BoundedRangeModel#setMaximum
     */
    public void setMaximum(int maximum) {
        getModel().setMaximum(maximum);
    }

    /**
     * Returns the adjustable's orientation (horizontal or vertical).
     *
     * @return VERTICAL or HORIZONTAL
     * @see #setOrientation
     * @see java.awt.Adjustable#getOrientation
     */
    public final int getOrientation() {
        return orientation;
    }

    /**
     * Set the scrollbar's orientation to either VERTICAL or
     * HORIZONTAL.
     *
     * @throws IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     * @see #getOrientation
     */
    public void setOrientation(int orientation) {
        switch (orientation) {
            case SConstants.VERTICAL:
                this.orientation = orientation;
                setPreferredSize(SDimension.FULLHEIGHT);
                break;
            case SConstants.HORIZONTAL:
                this.orientation = orientation;
                setPreferredSize(SDimension.FULLWIDTH);
                break;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }


    /**
     * True if the scrollbar knob is being dragged.
     *
     * @return the value of the model's valueIsAdjusting property
     * @see #setValueIsAdjusting
     */
    public final boolean getValueIsAdjusting() {
        return getModel().getValueIsAdjusting();
    }


    /**
     * Sets the model's valueIsAdjusting property.  Scrollbar look and
     * feel implementations should set this property to true when
     * a knob drag begins, and to false when the drag ends.  The
     * scrollbar model will not generate ChangeEvents while
     * valueIsAdjusting is true. True if the scrollbar thumb is being dragged.
     *
     * @see #getValueIsAdjusting
     * @see BoundedRangeModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) {
        getModel().setValueIsAdjusting(b);
    }


    /**
     * Sets the four BoundedRangeModel properties after forcing
     * the arguments to obey the usual constraints:
     * <pre>
     * minimum <= value <= value+extent <= maximum
     * </pre>
     * <p/>
     *
     * @see BoundedRangeModel#setRangeProperties
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public void setValues(int newValue, int newExtent, int newMin, int newMax) {
        BoundedRangeModel m = getModel();
        m.setRangeProperties(newValue, newExtent, newMin, newMax, m.getValueIsAdjusting());
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        if (action.endsWith("_keystroke"))
            return;

        getModel().setDelayEvents(true);
        for (int i = 0; i < values.length; i++) {
            try {
                setValue(Integer.parseInt(values[i]));
            } catch (NumberFormatException ex) {
                // ignore
            }
        }

        SForm.addArmedComponent(this);
        getModel().setDelayEvents(false);
    }

    public void fireIntermediateEvents() {
        getModel().fireDelayedIntermediateEvents();
    }

    public void fireFinalEvents() {
        super.fireFinalEvents();
        getModel().fireDelayedFinalEvents();
    }

    /**
     * Adds an AdjustmentListener.  Adjustment listeners are notified
     * each time the scrollbar's model changes.  Adjustment events are
     * provided for backwards compatability with java.awt.Scrollbar.
     * <p/>
     * Note that the AdjustmentEvents type property will always have a
     * placeholder value of AdjustmentEvent.TRACK because all changes
     * to a BoundedRangeModels value are considered equivalent.  To change
     * the value of a BoundedRangeModel one just sets its value property,
     * i.e. model.setValue(123).  No information about the origin of the
     * change, e.g. it's a block decrement, is provided.  We don't try
     * fabricate the origin of the change here.
     *
     * @param l the AdjustmentLister to add
     * @see #removeAdjustmentListener
     * @see BoundedRangeModel#addChangeListener
     */
    public void addAdjustmentListener(AdjustmentListener l) {
        addEventListener(AdjustmentListener.class, l);
    }


    /**
     * Removes an AdjustmentEvent listener.
     *
     * @param l the AdjustmentLister to remove
     * @see #addAdjustmentListener
     */
    public void removeAdjustmentListener(AdjustmentListener l) {
        removeEventListener(AdjustmentListener.class, l);
    }


    /*
     * Notify listeners that the scrollbar's model has changed.
     *
     * @see #addAdjustmentListener
     * @see EventListenerList
     */
    protected void fireAdjustmentValueChanged(int id, int type, int value) {
        AdjustmentEvent e = null;

        Object[] listeners = getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == AdjustmentListener.class) {
                if (e == null) {
                    e = new AdjustmentEvent(this, id, type, value);
                }
                ((AdjustmentListener) listeners[i + 1]).adjustmentValueChanged(e);
            }
        }
    }

    /**
     * This class listens to ChangeEvents on the model and forwards
     * AdjustmentEvents for the sake of backwards compatibility.
     * Unfortunately there's no way to determine the proper
     * type of the AdjustmentEvent as all updates to the model's
     * value are considered equivalent.
     */
    private class ModelListener implements ChangeListener, Serializable {
        public void stateChanged(ChangeEvent e) {
            int id = AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED;
            int type = AdjustmentEvent.TRACK;
            fireAdjustmentValueChanged(id, type, getValue());
            reload();
        }
    }

    /** @see LowLevelEventListener#isEpochCheckEnabled() */
    private boolean epochCheckEnabled = true;

    /** @see LowLevelEventListener#isEpochCheckEnabled() */
    public boolean isEpochCheckEnabled() {
        return epochCheckEnabled;
    }

    /** @see LowLevelEventListener#isEpochCheckEnabled() */
    public void setEpochCheckEnabled(boolean epochCheckEnabled) {
        this.epochCheckEnabled = epochCheckEnabled;
    }

}
