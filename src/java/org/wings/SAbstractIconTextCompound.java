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


import org.wings.style.CSSSelector;
import org.wings.style.CSSStyleSheet;
import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSProperty;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Base class for elements with icon and text like {@link SAbstractButton} and {@link SClickable}.
 * 
 * It supports 7 different icon states.
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

    public static final CSSSelector SELECTOR_SELECTION = new CSSSelector.Pseudo("SELECTION");

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

    private int verticalTextPosition = SConstants.CENTER;

    private int horizontalTextPosition = SConstants.RIGHT;

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
    
    /**
     * Sets the icon for the compound.
     *
     * @param i the icon.
     */
    public void setIcon(SIcon i) {
        reloadIfChange(icon, i);
        icon = i;
    }
    
    /**
     * Returns the icon of the Compound.
     *
     * @see #setIcon(SIcon)
     */
    public SIcon getIcon() {
        return icon;
    }

    /**
     * Sets the icon that is displayed when the compound is pressed with the mouse.
     *
     * @param icon to be shown when mouse button is pressed.
     */
    public void setPressedIcon(SIcon icon) {
        reloadIfChange(pressedIcon, icon);
        pressedIcon = icon;
    }

    /**
     * Returns the icon that is displayed when a compound is pressed with the mouse.
     *
     * @see #setPressedIcon(SIcon)
     */
    public SIcon getPressedIcon() {
        return pressedIcon;
    }

    /**
     * Sets the icon that is displayed as rollOver effect (meaning the icon
     * shown when the mouse is just positioned over the compound).
     *
     * @param icon rollOver icon for unselected compound.
     */
    public void setRolloverIcon(SIcon icon) {
        reloadIfChange(rolloverIcon, icon);
        rolloverIcon = icon;
    }

    /**
     * Returns the icon that is displayed as rollOver effect (meaning the icon
     * shown when the mouse is just positioned over the compound).
     * 
     * @see #setRolloverIcon(SIcon)
     */
    public SIcon getRolloverIcon() {
        return rolloverIcon;
    }

    /**
     * Sets the icon that is displayed as rollover effect of a selected compound 
     * (meaning the icon shown when the mouse is just positioned over the selected compound).
     *
     * @param icon rollOver icon for selected compound.
     */
    public void setRolloverSelectedIcon(SIcon icon) {
        reloadIfChange(rolloverSelectedIcon, icon);
        rolloverSelectedIcon = icon;
    }

    /**
     * Returns the the rollOver icon of a selected compound.
     *
     * @see #setRolloverSelectedIcon(SIcon)
     */
    public SIcon getRolloverSelectedIcon() {
        return rolloverSelectedIcon;
    }

    /**
     * Sets the icon that is displayed when a compound is selected. 
     *
     * @param icon to be shown for a selected compound.
     */
    public void setSelectedIcon(SIcon icon) {
        reloadIfChange(selectedIcon, icon);
        selectedIcon = icon;
    }

    /**
     * Returns the icon of a selected compound.
     *
     * @see #setSelectedIcon(SIcon)
     */
    public SIcon getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * Sets the Icon that is displayed when a selected compound is disabled .
     *
     * @param icon to be shown for a selected compound that is disabled.
     */
    public void setDisabledSelectedIcon(SIcon icon) {
        reloadIfChange(disabledSelectedIcon, icon);
        disabledSelectedIcon = icon;
    }

    /**
     * Returns the icon of a selected compound that is disabled.
     *
     * @see #setDisabledSelectedIcon(SIcon)
     */
    public SIcon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
    }

    /**
     * Sets the icon that is displayed when a compound is disabled. 
     *
     * @param icon to be shown for a disabled compound.
     */
    public void setDisabledIcon(SIcon icon) {
        reloadIfChange(disabledIcon, icon);
        disabledIcon = icon;
    }

    /**
     * Returns the the icon of a compound that is disabled.
     *
     * @see #setDisabledIcon(SIcon)
     */
    public SIcon getDisabledIcon() {
       // Creates disabled icon only for SImageIcons not for SURLIcons
    	/*
         if(disabledIcon == null) {
           if(icon != null && icon instanceof SImageIcon)
             disabledIcon = new SImageIcon(GrayFilter.createDisabledImage(((SImageIcon)icon).getImage()));
         }
        */
        return disabledIcon;
    }

    /**
     * Return the background color.
     *
     * @return the background color
     */
    public Color getSelectionBackground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getBackground((CSSAttributeSet) dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Set the foreground color.
     *
     * @param color the new foreground color
     */
    public void setSelectionBackground(Color color) {
        setAttribute(SELECTOR_SELECTION, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Return the foreground color.
     *
     * @return the foreground color
     */
    public Color getSelectionForeground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getForeground((CSSAttributeSet) dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Set the foreground color.
     *
     * @param color the new foreground color
     */
    public void setSelectionForeground(Color color) {
        setAttribute(SELECTOR_SELECTION, CSSProperty.COLOR, CSSStyleSheet.getAttribute(color));
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
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTION) == null ? null : CSSStyleSheet.getFont((CSSAttributeSet) dynamicStyles.get(SELECTOR_SELECTION));
    }

    /**
     * Sets the label of the button.
     */
    public void setText(String t) {
        reloadIfChange(text, t);
        text = t;
    }

    /**
     * Return the text of the button.
     *
     * @return the text
     */
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
