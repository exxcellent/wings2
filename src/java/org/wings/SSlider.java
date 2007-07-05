/*
 * $Id: SProgressBar.java 2419 2006-01-23 10:20:41Z hengels $
 * Copyright 2000,2006 wingS development team.
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

import java.io.Serializable;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A component that lets the user graphically select a value by sliding
 * a knob within a bounded interval.
 * The displayed slider is highly dependent on the graphics used,
 * see http://developer.yahoo.com/yui/slider/.
 *
 * @author Christian Schyma
 */
public class SSlider extends SComponent implements SConstants, LowLevelEventListener  {

    /**
     * The data model that handles the numeric maximum value,
     * minimum value, and current-position value for the slider.
     */
    protected BoundedRangeModel sliderModel;

    /**
     * The number of values between the major tick marks.
     */
    protected int majorTickSpacing = 1;

    /**
     * If true, the knob (and the data value it represents)
     * resolve to the closest tick mark next to where the user
     * positioned the knob.  The default is false.
     * @see #setSnapToTicks
     */
    protected boolean snapToTicks = false;

    /**
     * @see #setOrientation
     */
    protected int orientation;

    /**
     * The changeListener (no suffix) is the listener we add to the
     * Sliders model.  By default this listener just forwards events
     * to ChangeListeners (if any) added directly to the slider.
     *
     * @see #addChangeListener
     * @see #createChangeListener
     */
    protected ChangeListener changeListener = createChangeListener();

    /**
     * Creates a horizontal slider with the range 0 to 100 and
     * an initial value of 50.
     */
    public SSlider() {
        this(HORIZONTAL, 0, 100, 50);
    }

    /**
     * Creates a slider using the specified orientation with the
     * range 0 to 100 and an initial value of 50.
     */
    public SSlider(int orientation) {
        this(orientation, 0, 100, 50);
    }

    /**
     * Creates a horizontal slider using the specified min and max
     * with an initial value equal to the average of the min plus max.
     */
    public SSlider(int min, int max) {
        this(HORIZONTAL, min, max, (min + max) / 2);
    }

    /**
     * Creates a horizontal slider using the specified min, max and value.
     */
    public SSlider(int min, int max, int value) {
        this(HORIZONTAL, min, max, value);
    }

    /**
     * Creates a slider with the specified orientation and the
     * specified minimum, maximum, and initial values.
     *
     * @exception IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     *
     * @see #setOrientation
     * @see #setMinimum
     * @see #setMaximum
     * @see #setValue
     */
    public SSlider(int orientation, int min, int max, int value) {
        checkOrientation(orientation);
        this.orientation = orientation;
        sliderModel = new DefaultBoundedRangeModel(value, 0, min, max);
        sliderModel.addChangeListener(changeListener);
    }

    private void checkOrientation(int orientation) {
        switch (orientation) {
            case VERTICAL:
            case HORIZONTAL:
                break;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    /**
     * Return this slider's vertical or horizontal orientation.
     * @return VERTICAL or HORIZONTAL
     * @see #setOrientation
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Set the scrollbars orientation to either VERTICAL or HORIZONTAL.
     *
     * @exception IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     * @see #getOrientation
     */
    public void setOrientation(int orientation) {
        checkOrientation(orientation);
        int oldValue = this.orientation;
        this.orientation = orientation;
        reloadIfChange(orientation, oldValue);
    }

    /**
     * We pass Change events along to the listeners with the
     * the slider (instead of the model itself) as the event source.
     */
    private class ModelListener implements ChangeListener, Serializable {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }

    /**
     * Subclasses that want to handle model ChangeEvents differently
     * can override this method to return their own ChangeListener
     * implementation.  The default ChangeListener just forwards
     * ChangeEvents to the ChangeListeners added directly to the slider.
     *
     * @see #fireStateChanged
     */
    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    /**
     * Adds a ChangeListener to the slider.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        addEventListener(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the slider.
     *
     * @param l the ChangeListener to remove
     * @see #addChangeListener
     *
     */
    public void removeChangeListener(ChangeListener l) {
        removeEventListener(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this slider with <code>addChangeListener</code>.
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) getListeners(ChangeListener.class);
    }

    /**
     * Notifies all listeners that have registered interest in
     * <code>ChangeEvent</code>s.
     */
    protected void fireStateChanged() {
        ChangeListener[] listeners = (ChangeListener[]) getListeners(ChangeListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ChangeListener listener = listeners[i];
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Returns data model that handles the sliders three
     * fundamental properties: minimum, maximum, value.
     *
     * @see #setModel
     */
    public BoundedRangeModel getModel() {
        return sliderModel;
    }

    /**
     * Sets the model that handles the sliders three
     * fundamental properties: minimum, maximum, value.
     *
     * @see #getModel
     */
    public void setModel(BoundedRangeModel newModel) {
        BoundedRangeModel oldModel = getModel();

        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
        }

        sliderModel = newModel;

        if (newModel != null) {
            newModel.addChangeListener(changeListener);


        }

        reloadIfChange(newModel, oldModel);
    }

    /**
     * Returns the sliders value.
     * @return the models value property
     * @see #setValue
     */
    public int getValue() {
        return getModel().getValue();
    }

    /**
     * Sets the sliders current value.  This method just forwards
     * the value to the model.
     *
     * @see #getValue
     */
    public void setValue(int n) {
        BoundedRangeModel m = getModel();
        int oldValue = m.getValue();
        m.setValue(n);
        reloadIfChange(n, oldValue);
    }


    /**
     * Returns the minimum value supported by the slider.
     *
     * @return the value of the models minimum property
     * @see #setMinimum
     */
    public int getMinimum() {
        return getModel().getMinimum();
    }


    /**
     * Sets the models minimum property.
     *
     * @see #getMinimum
     * @see BoundedRangeModel#setMinimum
     */
    public void setMinimum(int minimum) {
        int oldMin = getModel().getMinimum();
        getModel().setMinimum(minimum);
        reloadIfChange(minimum, oldMin);
    }


    /**
     * Returns the maximum value supported by the slider.
     *
     * @return the value of the models maximum property
     * @see #setMaximum
     */
    public int getMaximum() {
        return getModel().getMaximum();
    }

    /**
     * Sets the models maximum property.
     *
     * @see #getMaximum
     * @see BoundedRangeModel#setMaximum
     */
    public void setMaximum(int maximum) {
        int oldMax = getModel().getMaximum();
        getModel().setMaximum(maximum);
        reloadIfChange(maximum, oldMax);
    }

    public void fireFinalEvents() {
        fireStateChanged();
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        if (action.endsWith("_keystroke"))
            return;

        if (action.compareTo(getName()+"_val") == 0) {
            setValue(Integer.parseInt(values[0]));
            SForm.addArmedComponent(this);
        }
    }

    public boolean isEpochCheckEnabled() {
        return true;
    }

    public void fireIntermediateEvents() {
        // nothing to do
    }

    /**
     * This method returns the major tick spacing.  The number that is returned
     * represents the distance, measured in values, between each major tick mark.
     * If you have a slider with a range from 0 to 50 and the major tick spacing
     * is set to 10, you will get major ticks next to the following values:
     * 0, 10, 20, 30, 40, 50.
     *
     * @return the number of values between major ticks
     * @see #setMajorTickSpacing
     */
    public int getMajorTickSpacing() {
        return majorTickSpacing;
    }


    /**
     * This method sets the major tick spacing.  The number that is passed-in
     * represents the distance, measured in values, between each major tick mark.
     * If you have a slider with a range from 0 to 50 and the major tick spacing
     * is set to 10, you will get major ticks next to the following values:
     * 0, 10, 20, 30, 40, 50.
     *
     * @see #getMajorTickSpacing
     */
    public void setMajorTickSpacing(int n) {
        int oldValue = majorTickSpacing;
        majorTickSpacing = n;
        reloadIfChange(n, oldValue);
    }

    /**
     * Returns true if the knob (and the data value it represents)
     * resolve to the closest tick mark next to where the user
     * positioned the knob.
     *
     * @return true if the value snaps to the nearest tick mark, else false
     * @see #setSnapToTicks
     */
    public boolean getSnapToTicks() {
        return snapToTicks;
    }

    /**
     * Specifying true makes the knob (and the data value it represents)
     * resolve to the closest tick mark next to where the user
     * positioned the knob.
     *
     * @param b  true to snap the knob to the nearest tick mark
     * @see #getSnapToTicks
     */
    public void setSnapToTicks(boolean b) {
        boolean oldValue = snapToTicks;
        snapToTicks = b;
        reloadIfChange(b, oldValue);
    }

    /**
     * The maximum number of pixels the slider thumb can be moved. Usefull when
     * using different slider thumb or bar graphics.
     * @param max pixels
     */
    public void setMaxPixelConstraint(int max) {
        this.putClientProperty("maxPixelConstraint", new Integer(max));
    }

}