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
package org.wings;


import org.wings.style.AttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.Style;
import org.wings.style.CSSSelector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * An abstract class, which compounds icon and text. It is the base class for
 * {@link SAbstractButton} and {@link SClickable}. It supports 7 different icon
 * states.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class SAbstractIconTextCompound
        extends SComponent
        implements ItemSelectable {
    public static final int ICON_COUNT = 7;
    public static final int DISABLED_ICON = 0;
    public static final int DISABLED_SELECTED_ICON = 1;
    public static final int ENABLED_ICON = 2;
    public static final int SELECTED_ICON = 3;
    public static final int ROLLOVER_ICON = 4;
    public static final int ROLLOVER_SELECTED_ICON = 5;
    public static final int PRESSED_ICON = 6;

    public static final CSSSelector SELECTOR_SELECTION = new CSSSelector("SELECTION");

    private SButtonModel model;

    /**
     * The button model's <code>changeListener</code>.
     */
    protected ChangeListener changeListener = null;

    protected transient ChangeEvent changeEvent = null;

    /**
     * the text the button is showing
     */
    private String text;

    /**
     * The icon to be displayed
     */
    private SIcon icon;

    private SIcon disabledIcon;

    private SIcon selectedIcon;

    private SIcon pressedIcon;

    private SIcon disabledSelectedIcon;

    private SIcon rolloverIcon;

    private SIcon rolloverSelectedIcon;

    private int verticalTextPosition = CENTER;

    private int horizontalTextPosition = RIGHT;

    private int iconTextGap = 0;

    private boolean delayEvents = false;

    /**
     * Create a button with given text.
     *
     * @param text the button text
     */
    public SAbstractIconTextCompound(String text) {
        setText(text);
        model = new SDefaultButtonModel();
    }

    /**
     * Creates a new submit button
     */
    public SAbstractIconTextCompound() {
        this(null);
    }

    public SButtonModel getModel() {
        return model;
    }

    public void setModel(SButtonModel model) {
        if (model == null)
            throw new IllegalArgumentException("null not allowed");
        this.model = model;
        reloadIfChange(this.model, model);
    }

    /**
     * Returns the selected items or null if no items are selected.
     */
    public Object[] getSelectedObjects() {
        return model.isSelected() ? new Object[]{this} : null;
    }

    /**
     * Adds a ItemListener to the button.
     *
     * @see #removeItemListener(ItemListener)
     */
    public void addItemListener(ItemListener il) {
        addEventListener(ItemListener.class, il);
    }

    /**
     * Remove the given itemListener from list of
     * item listeners.
     *
     * @see #addItemListener(ItemListener)
     */
    public void removeItemListener(ItemListener il) {
        removeEventListener(ItemListener.class, il);
    }

    /**
     * Adds a <code>ChangeListener</code> to the button.
     *
     * @param l the listener to be added
     */
    public void addChangeListener(ChangeListener l) {
        addEventListener(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the button.
     *
     * @param l the listener to be removed
     */
    public void removeChangeListener(ChangeListener l) {
        removeEventListener(ChangeListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
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

    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
        reloadIfChange(this.horizontalTextPosition, textPosition);

    }

    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
        reloadIfChange(this.verticalTextPosition, textPosition);
    }

    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    public void setIconTextGap(int gap) {
        iconTextGap = gap;
        reloadIfChange(this.iconTextGap, gap);
    }

    public int getIconTextGap() {
        return iconTextGap;
    }

    public void setIcon(SIcon i) {
        reloadIfChange(icon, i);
        icon = i;
    }

    public SIcon getIcon() {
        return icon;
    }

    public void setPressedIcon(SIcon i) {
        reloadIfChange(pressedIcon, i);
        pressedIcon = i;
    }

    public SIcon getPressedIcon() {
        return pressedIcon;
    }

    public void setRolloverIcon(SIcon i) {
        reloadIfChange(rolloverIcon, i);
        rolloverIcon = i;
    }

    public SIcon getRolloverIcon() {
        return rolloverIcon;
    }

    public void setRolloverSelectedIcon(SIcon i) {
        reloadIfChange(rolloverSelectedIcon, i);
        rolloverSelectedIcon = i;
    }

    public SIcon getRolloverSelectedIcon() {
        return rolloverSelectedIcon;
    }

    public void setSelectedIcon(SIcon i) {
        reloadIfChange(selectedIcon, i);
        selectedIcon = i;
    }

    public SIcon getSelectedIcon() {
        return selectedIcon;
    }

    public void setDisabledSelectedIcon(SIcon i) {
        reloadIfChange(disabledSelectedIcon, i);
        disabledSelectedIcon = i;
    }

    public SIcon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
    }

    public void setDisabledIcon(SIcon i) {
        reloadIfChange(disabledIcon, i);
        disabledIcon = i;
    }

    public SIcon getDisabledIcon() {
        /**** TODO
         if(disabledIcon == null) {
         if(icon != null && icon instanceof ImageIcon)
         disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)icon).getImage()));
         }
         ***/
        return disabledIcon;
    }

    /**
     * Return the background color.
     *
     * @return the background color
     */
    public Color getSelectionBackground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getBackground((AttributeSet) dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Set the foreground color.
     *
     * @param color the new foreground color
     */
    public void setSelectionBackground(Color color) {
        setAttribute(SELECTOR_SELECTION, Style.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Return the foreground color.
     *
     * @return the foreground color
     */
    public Color getSelectionForeground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getForeground((AttributeSet) dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Set the foreground color.
     *
     * @param color the new foreground color
     */
    public void setSelectionForeground(Color color) {
        setAttribute(SELECTOR_SELECTION, Style.COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Set the font.
     *
     * @param font the new font
     */
    public void setSelectionFont(SFont font) {
        setAttributes(SELECTOR_SELECTION, CSSStyleSheet.getAttributes(font));
    }

    /**
     * Return the font.
     *
     * @return the font
     */
    public SFont getSelectionFont() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getFont((AttributeSet) dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Sets the label of the button.
     */
    public void setText(String t) {
        reloadIfChange(text, t);
        text = t;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return model.isSelected();
    }

    /**
     * Toggle the selection. If the new selection
     * is different to the old selection
     * an {@link ItemEvent} is raised.
     */
    public void setSelected(boolean selected) {
        if (model.isSelected() != selected) {
            model.setSelected(selected);
            fireStateChanged();
            reload();
        }
    }

    /**
     * Sets the proper icons for buttonstatus enabled resp. disabled.
     */
    public void setIcons(SIcon[] icons) {
        setIcon(icons[ENABLED_ICON]);
        setDisabledIcon(icons[DISABLED_ICON]);
        setDisabledSelectedIcon(icons[DISABLED_SELECTED_ICON]);
        setRolloverIcon(icons[ROLLOVER_ICON]);
        setRolloverSelectedIcon(icons[ROLLOVER_SELECTED_ICON]);
        setPressedIcon(icons[PRESSED_ICON]);
        setSelectedIcon(icons[SELECTED_ICON]);
    }


    private ItemEvent delayedItemEvent;

    protected final void delayEvents(boolean b) {
        delayEvents = b;
    }

    protected final boolean shouldDelayEvents() {
        return delayEvents;
    }

    /**
     * Reports a selection change.
     *
     * @param ie report this event to all listeners
     * @see java.awt.event.ItemListener
     * @see java.awt.ItemSelectable
     */
    protected void fireItemStateChanged(ItemEvent ie) {
        if (ie == null)
            return;

        if (delayEvents) {
            delayedItemEvent = ie;
            return;
        } // end of if ()
        
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(ie);
            }
        }
    }

    public void fireIntermediateEvents() {
        if (delayEvents && delayedItemEvent != null) {
            delayEvents = false;
            fireItemStateChanged(delayedItemEvent);
            delayEvents = true;
        } // end of if ()

    }

    public void fireFinalEvents() {
        super.fireFinalEvents();
        delayEvents = false;
    }
}
