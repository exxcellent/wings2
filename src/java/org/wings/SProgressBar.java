/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.plaf.ProgressBarCG;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.Serializable;
import java.text.Format;
import java.text.NumberFormat;


/**
 * <!--
 * SProgressBar.java
 * Created: Mon Oct 28 18:55:02 2002
 * -->
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SProgressBar extends SComponent {

    public static final String STRING_PROPERTY = "_String_Property";
    public static final String STRING_PAINTED_PROPERTY = "_String_Painted_Property";
    public static final String BORDER_PAINTED_PROPERTY = "_Border_Painted_Property";
    public static final String ORIENTATION_PROPERTY = "_Orientation_Property";


    /**
     * Whether the progress bar is horizontal or vertical.
     * The default is <code>HORIZONTAL</code>.
     *
     * @see #setOrientation
     */
    protected int orientation;

    /**
     * Whether to display a border around the progress bar.
     * The default is <code>true</code>.
     *
     * @see #setBorderPainted
     */
    protected boolean paintBorder;

    /**
     * The object that holds the data for the progress bar.
     *
     * @see #setModel
     */
    protected BoundedRangeModel model;

    /**
     * An optional string that can be displayed on the progress bar.
     * The default is <code>null</code>. Setting this to a non-<code>null</code>
     * value does not imply that the string will be displayed.
     *
     * @see #setString
     */
    protected String progressString;

    /**
     * Whether to textually display a string on the progress bar.
     * The default is <code>false</code>.
     * Setting this to <code>true</code> causes a textual
     * display of the progress to be rendered on the progress bar. If
     * the <code>progressString</code> is <code>null</code>,
     * the percentage of completion is displayed on the progress bar.
     * Otherwise, the <code>progressString</code> is
     * rendered on the progress bar.
     *
     * @see #setStringPainted
     */
    protected boolean paintString;

    /**
     * The default minimum for a progress bar is 0.
     */
    static final private int defaultMinimum = 0;
    /**
     * The default maximum for a progress bar is 100.
     */
    static final private int defaultMaximum = 100;
    /**
     * The default orientation for a progress bar is <code>HORIZONTAL</code>.
     */
    static final private int defaultOrientation = HORIZONTAL;

    /**
     * Only one <code>ChangeEvent</code> is needed per instance since the
     * event's only interesting property is the immutable source, which
     * is the progress bar.
     */
    protected transient ChangeEvent changeEvent = null;

    /**
     * Listens for change events sent by the progress bar's model,
     * redispatching them
     * to change-event listeners registered upon
     * this progress bar.
     *
     * @see #createChangeListener
     */
    protected ChangeListener changeListener = null;

    /**
     * Format used when displaying percent complete.
     */
    private transient Format format;

    /**
     * Whether the progress bar is indeterminate (<code>true</code>) or
     * normal (<code>false</code>); the default is <code>false</code>.
     *
     * @see #setIndeterminate
     * @since 1.4
     */
    private boolean indeterminate;

    /**
     * The color in which the border is painted
     */
    private Color borderColor;

    /**
     * The color in which the filled region is painted
     */
    private Color filledColor;

    /**
     * The color in which the unfilled region is painted
     */
    private Color unfilledColor;

    /**
     * Creates a horizontal progress bar
     * that displays a border but no progress string.
     * The initial and minimum values are 0,
     * and the maximum is 100.
     *
     * @see #setOrientation
     * @see #setBorderPainted
     * @see #setStringPainted
     * @see #setString
     * @see #setIndeterminate
     */
    public SProgressBar() {
        this(defaultOrientation);
    }

    /**
     * Creates a progress bar with the specified orientation,
     * which can be
     * either <code>SProgressBar.VERTICAL</code> or
     * <code>SProgressBar.HORIZONTAL</code>.
     * By default, a border is painted but a progress string is not.
     * The initial and minimum values are 0,
     * and the maximum is 100.
     *
     * @param orient the desired orientation of the progress bar
     * @see #setOrientation
     * @see #setBorderPainted
     * @see #setStringPainted
     * @see #setString
     * @see #setIndeterminate
     */
    public SProgressBar(int orient) {
        this(orient, defaultMinimum, defaultMaximum);
    }


    /**
     * Creates a horizontal progress bar
     * with the specified minimum and maximum.
     * Sets the initial value of the progress bar to the specified minimum.
     * By default, a border is painted but a progress string is not.
     * The <code>BoundedRangeModel</code> that holds the progress bar's data
     * handles any issues that may arise from improperly setting the
     * minimum, initial, and maximum values on the progress bar.
     *
     * @param min the minimum value of the progress bar
     * @param max the maximum value of the progress bar
     * @see BoundedRangeModel
     * @see #setOrientation
     * @see #setBorderPainted
     * @see #setStringPainted
     * @see #setString
     * @see #setIndeterminate
     */
    public SProgressBar(int min, int max) {
        this(defaultOrientation, min, max);
    }


    /**
     * Creates a progress bar using the specified orientation,
     * minimum, and maximum.
     * By default, a border is painted but a progress string is not.
     * Sets the initial value of the progress bar to the specified minimum.
     * The <code>BoundedRangeModel</code> that holds the progress bar's data
     * handles any issues that may arise from improperly setting the
     * minimum, initial, and maximum values on the progress bar.
     *
     * @param orient the desired orientation of the progress bar
     * @param min    the minimum value of the progress bar
     * @param max    the maximum value of the progress bar
     * @see BoundedRangeModel
     * @see #setOrientation
     * @see #setBorderPainted
     * @see #setStringPainted
     * @see #setString
     * @see #setIndeterminate
     */
    public SProgressBar(int orient, int min, int max) {
        // Creating the model this way is a bit simplistic, but
        //  I believe that it is the the most common usage of this
        //  component - it's what people will expect.
        setModel(new DefaultBoundedRangeModel(min, 0, min, max));

        setOrientation(orient);      // documented with set/getOrientation()
        setBorderPainted(true);      // documented with is/setBorderPainted()
        setStringPainted(false);     // see setStringPainted
        setString(null);             // see getString
        setIndeterminate(false);     // see setIndeterminate
    }


    /**
     * Creates a horizontal progress bar
     * that uses the specified model
     * to hold the progress bar's data.
     * By default, a border is painted but a progress string is not.
     *
     * @param newModel the data model for the progress bar
     * @see #setOrientation
     * @see #setBorderPainted
     * @see #setStringPainted
     * @see #setString
     * @see #setIndeterminate
     */
    public SProgressBar(BoundedRangeModel newModel) {
        setModel(newModel);

        setOrientation(defaultOrientation);  // see setOrientation()
        setBorderPainted(true);              // see setBorderPainted()
        setStringPainted(false);             // see setStringPainted
        setString(null);                     // see getString
        setIndeterminate(false);             // see setIndeterminate
    }


    /**
     * Returns <code>SProgressBar.VERTICAL</code> or
     * <code>SProgressBar.HORIZONTAL</code>, depending on the orientation
     * of the progress bar. The default orientation is
     * <code>HORIZONTAL</code>.
     *
     * @return <code>HORIZONTAL</code> or <code>VERTICAL</code>
     * @see #setOrientation
     */
    public int getOrientation() {
        return orientation;
    }


    /**
     * Sets the progress bar's orientation to <code>newOrientation</code>,
     * which must be <code>SProgressBar.VERTICAL</code> or
     * <code>SProgressBar.HORIZONTAL</code>. The default orientation
     * is <code>HORIZONTAL</code>.
     *
     * @param newOrientation <code>HORIZONTAL</code> or <code>VERTICAL</code>
     * @throws IllegalArgumentException if <code>newOrientation</code>
     *                                  is an illegal value
     * @beaninfo preferred: true
     * bound: true
     * attribute: visualUpdate true
     * description: Set the progress bar's orientation.
     * @see #getOrientation
     */
    public void setOrientation(int newOrientation) {
        if (orientation != newOrientation) {
            switch (newOrientation) {
                case VERTICAL:
                case HORIZONTAL:
                    int oldOrientation = orientation;
                    orientation = newOrientation;
                    reloadIfChange(oldOrientation, newOrientation);
                    break;
                default:
                    throw new IllegalArgumentException(newOrientation +
                            " is not a legal orientation");
            }
        }
    }


    /**
     * Returns the value of the <code>stringPainted</code> property.
     *
     * @return the value of the <code>stringPainted</code> property
     * @see #setStringPainted
     * @see #setString
     */
    public boolean isStringPainted() {
        return paintString;
    }


    /**
     * Sets the value of the <code>stringPainted</code> property,
     * which determines whether the progress bar
     * should render a progress string.
     * The default is <code>false</code>:
     * no string is painted.
     * Some look and feels might not support progress strings
     * or might support them only when the progress bar is in determinate mode.
     *
     * @param b <code>true</code> if the progress bar should render a string
     * @beaninfo bound: true
     * attribute: visualUpdate true
     * description: Whether the progress bar should render a string.
     * @see #isStringPainted
     * @see #setString
     */
    public void setStringPainted(boolean b) {
        //PENDING: specify that string not painted when in indeterminate mode?
        //         or just leave that to the L&F?
        boolean oldValue = paintString;
        paintString = b;
        reloadIfChange(oldValue, paintString);
    }


    /**
     * Returns the current value of the progress string.
     * If you are providing a custom progress string
     * by overriding this method,
     * make sure your implementation calls <code>setString</code> before
     * calling <code>super.getString</code>.
     *
     * @return the value of the percent string
     * @see #setString
     */
    public String getString() {
        if (progressString != null) {
            return progressString;
        } else {
            if (format == null) {
                format = NumberFormat.getPercentInstance();
            }
            return format.format(new Double(getPercentComplete()));
        }
    }

    /**
     * Sets the value of the progress string. By default,
     * this string is <code>null</code>.
     * If you have provided a custom progress string and want to revert to
     * the built-in behavior, set the string back to <code>null</code>.
     * If you are providing a custom progress string
     * by overriding this method,
     * make sure that you call <code>setString</code> before
     * calling <code>getString</code>.
     * The progress string is painted only if
     * the <code>isStringPainted</code> method returns <code>true</code>.
     *
     * @param s the value of the percent string
     * @beaninfo bound: true
     * attribute: visualUpdate true
     * description: Specifies the progress string to paint
     * @see #getString
     * @see #setStringPainted
     * @see #isStringPainted
     */
    public void setString(String s) {
        String oldValue = progressString;
        progressString = s;
        reloadIfChange(oldValue, progressString);
    }

    /**
     * Returns the percent complete for the progress bar.
     * Note that this number is between 0.0 and 1.0.
     *
     * @return the percent complete for this progress bar
     */
    public double getPercentComplete() {
        long span = model.getMaximum() - model.getMinimum();
        double currentValue = model.getValue();
        double pc = (currentValue - model.getMinimum()) / span;
        return pc;
    }

    /**
     * Returns the <code>borderPainted</code> property.
     *
     * @return the value of the <code>borderPainted</code> property
     * @beaninfo description: Does the progress bar paint its border
     * @see #setBorderPainted
     */
    public boolean isBorderPainted() {
        return paintBorder;
    }

    /**
     * Sets the <code>borderPainted</code> property, which is
     * <code>true</code> if the progress bar should paint its border.
     * The default value for this property is <code>true</code>.
     * Some look and feels might not implement painted borders;
     * they will ignore this property.
     *
     * @param b <code>true</code> if the progress bar
     *          should paint its border;
     *          otherwise, <code>false</code>
     * @beaninfo bound: true
     * attribute: visualUpdate true
     * description: Whether the progress bar should paint its border.
     * @see #isBorderPainted
     */
    public void setBorderPainted(boolean b) {
        boolean oldValue = paintBorder;
        paintBorder = b;
        reloadIfChange(oldValue, paintBorder);
    }

    /**
     * Set the color in which the border is painted, if the border is painted
     *
     * @param c a <code>Color</code> value
     */
    public void setBorderColor(Color c) {
        borderColor = c;
    }

    /**
     * Returns the color in which the border is painted, if the border is painted
     *
     * @return a <code>Color</code> value
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the color in which the fille region is painted
     *
     * @param c a <code>Color</code> value
     */
    public void setFilledColor(Color c) {
        filledColor = c;
    }

    /**
     * Returns the color in which the fille region is painted
     *
     * @param c a <code>Color</code> value
     */
    public Color getFilledColor() {
        return filledColor;
    }

    /**
     * Sets the color in which the unfilled region is painted
     *
     * @param c a <code>Color</code> value
     */
    public void setUnfilledColor(Color c) {
        unfilledColor = c;
    }

    /**
     * Returns the color in which the unfilled region is painted
     *
     * @param c a <code>Color</code> value
     */
    public Color getUnfilledColor() {
        return unfilledColor;
    }

    /**
     * Sets the look-and-feel object that renders this component.
     *
     * @param ui a <code>ProgressBarCG</code> object
     */
    public void setCG(ProgressBarCG cg) {
        super.setCG(cg);
    }

    /* We pass each Change event to the listeners with the
     * the progress bar as the event source.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
    private class ModelListener implements ChangeListener, Serializable {
        public void stateChanged(ChangeEvent e) {
            reload();
            fireStateChanged();
        }
    }

    /**
     * Subclasses that want to handle change events
     * from the model differently
     * can override this to return
     * an instance of a custom <code>ChangeListener</code> implementation.
     *
     * @see #changeListener
     * @see javax.swing.event.ChangeListener
     * @see javax.swing.BoundedRangeModel
     */
    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    /**
     * Adds the specified <code>ChangeListener</code> to the progress bar.
     *
     * @param l the <code>ChangeListener</code> to add
     */
    public void addChangeListener(ChangeListener l) {
        addEventListener(ChangeListener.class, l);
    }

    /**
     * Removes a <code>ChangeListener</code> from the progress bar.
     *
     * @param l the <code>ChangeListener</code> to remove
     */
    public void removeChangeListener(ChangeListener l) {
        removeEventListener(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this progress bar with <code>addChangeListener</code>.
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) getListeners(ChangeListener.class);
    }

    /**
     * Notifies all listeners that have registered interest in
     * <code>ChangeEvent</code>s.
     * The event instance
     * is created if necessary.
     *
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Returns the data model used by this progress bar.
     *
     * @return the <code>BoundedRangeModel</code> currently in use
     * @see BoundedRangeModel
     */
    public BoundedRangeModel getModel() {
        return model;
    }

    /**
     * Sets the data model used by the <code>SProgressBar</code>.
     *
     * @param newModel the <code>BoundedRangeModel</code> to use
     * @beaninfo expert: true
     * description: The data model used by the SProgressBar.
     */
    public void setModel(BoundedRangeModel newModel) {
        // PENDING(???) setting the same model to multiple bars is broken; listeners
        BoundedRangeModel oldModel = getModel();

        if (newModel != oldModel) {
            if (oldModel != null) {
                oldModel.removeChangeListener(changeListener);
                changeListener = null;
            }

            model = newModel;

            if (newModel != null) {
                changeListener = createChangeListener();
                newModel.addChangeListener(changeListener);
            }

            if (model != null) {
                model.setExtent(0);
            }
            reload();
        }
    }


    /* All of the model methods are implemented by delegation. */

    /**
     * Returns the progress bar's current value,
     * which is stored in the progress bar's <code>BoundedRangeModel</code>.
     * The value is always between the
     * minimum and maximum values, inclusive. By default, the
     * value is initialized to be equal to the minimum value.
     *
     * @return the current value of the progress bar
     * @see #setValue
     * @see BoundedRangeModel#getValue
     */
    public int getValue() { return getModel().getValue(); }

    /**
     * Returns the progress bar's minimum value,
     * which is stored in the progress bar's <code>BoundedRangeModel</code>.
     * By default, the minimum value is <code>0</code>.
     *
     * @return the progress bar's minimum value
     * @see #setMinimum
     * @see BoundedRangeModel#getMinimum
     */
    public int getMinimum() { return getModel().getMinimum(); }

    /**
     * Returns the progress bar's maximum value,
     * which is stored in the progress bar's <code>BoundedRangeModel</code>.
     * By default, the maximum value is <code>100</code>.
     *
     * @return the progress bar's maximum value
     * @see #setMaximum
     * @see BoundedRangeModel#getMaximum
     */
    public int getMaximum() { return getModel().getMaximum(); }

    /**
     * Sets the progress bar's current value
     * (stored in the progress bar's data model) to <code>n</code>.
     * The data model (a <code>BoundedRangeModel</code> instance)
     * handles any mathematical
     * issues arising from assigning faulty values.
     * <p/>
     * If the new value is different from the previous value,
     * all change listeners are notified.
     *
     * @param n the new value
     * @beaninfo preferred: true
     * description: The progress bar's current value.
     * @see #getValue
     * @see BoundedRangeModel#setValue
     */
    public void setValue(int n) {
        BoundedRangeModel brm = getModel();
        int oldValue = brm.getValue();
        brm.setValue(n);
    }

    /**
     * Sets the progress bar's minimum value
     * (stored in the progress bar's data model) to <code>n</code>.
     * The data model (a <code>BoundedRangeModel</code> instance)
     * handles any mathematical
     * issues arising from assigning faulty values.
     * <p/>
     * If the minimum value is different from the previous minimum,
     * all change listeners are notified.
     *
     * @param n the new minimum
     * @beaninfo preferred: true
     * description: The progress bar's minimum value.
     * @see #getMinimum
     * @see #addChangeListener
     * @see BoundedRangeModel#setMinimum
     */
    public void setMinimum(int n) { getModel().setMinimum(n); }

    /**
     * Sets the progress bar's maximum value
     * (stored in the progress bar's data model) to <code>n</code>.
     * The underlying <code>BoundedRangeModel</code> handles any mathematical
     * issues arising from assigning faulty values.
     * <p/>
     * If the maximum value is different from the previous maximum,
     * all change listeners are notified.
     *
     * @param n the new maximum
     * @beaninfo preferred: true
     * description: The progress bar's maximum value.
     * @see #getMaximum
     * @see #addChangeListener
     * @see BoundedRangeModel#setMaximum
     */
    public void setMaximum(int n) { getModel().setMaximum(n); }

    /**
     * Sets the <code>indeterminate</code> property of the progress bar,
     * which determines whether the progress bar is in determinate
     * or indeterminate mode.
     * By default, the progress bar is determinate
     * and this method returns <code>false</code>.
     * An indeterminate progress bar continuously displays animation
     * indicating that an operation of unknown length is occurring.
     * By default, this property is <code>false</code>.
     * Some look and feels might not support indeterminate progress bars;
     * they will ignore this property.
     * <p/>
     * <p/>
     * <p/>
     * See
     * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/progress.html" target="_top">How to Monitor Progress</a>
     * for examples of using indeterminate progress bars.
     *
     * @param newValue <code>true</code> if the progress bar
     *                 should change to indeterminate mode;
     *                 <code>false</code> if it should revert to normal.
     * @beaninfo bound: true
     * attribute: visualUpdate true
     * description: Set whether the progress bar is indeterminate (true)
     * or normal (false).
     * @see #isIndeterminate
     * @see javax.swing.plaf.basic.BasicProgressBarUI
     * @since 1.4
     */
    public void setIndeterminate(boolean newValue) {
        boolean oldValue = indeterminate;
        indeterminate = newValue;
    }

    /**
     * Returns the value of the <code>indeterminate</code> property.
     *
     * @return the value of the <code>indeterminate</code> property
     * @beaninfo description: Is the progress bar indeterminate (true)
     * or normal (false)?
     * @see #setIndeterminate
     * @since 1.4
     */
    public boolean isIndeterminate() {
        return indeterminate;
    }


}// SProgressBar

/*
   $Log$
   Revision 1.3  2004/11/24 18:13:21  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.2  2004/11/20 17:04:48  hengels
   o remove property change support

   Revision 1.1.1.1  2004/10/04 16:13:09  hengels
   o start development of wings 2

   Revision 1.3  2002/11/18 14:50:48  ahaaf
   o reload code on model change

   Revision 1.2  2002/10/29 19:00:59  ahaaf
   o use property constants
   o remove tabs

   Revision 1.1  2002/10/28 19:56:54  ahaaf
   o add ProgressBar component

*/
