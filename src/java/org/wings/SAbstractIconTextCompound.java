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


import org.wings.plaf.ButtonCG;
import org.wings.plaf.CheckBoxCG;
import org.wings.plaf.ClickableCG;
import org.wings.plaf.RadioButtonCG;
import org.wings.plaf.ToggleButtonCG;
import org.wings.style.Selector;
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

    /**
     * A Pseudo selector for buttons in selected state
     * Refer to {@link SComponent#setAttribute(org.wings.style.Selector, org.wings.style.CSSProperty, String)}
     */
    public static final Selector SELECTOR_SELECTED = new Selector("selected");

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

    private int iconTextGap = 4;

    private boolean delayEvents = false;
    private boolean delayedEvent = false;

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
        reloadIfChange(this.model, model);
        this.model = model;
    }

    /**
     * Returns the selected items or null if no items are selected.
     */
    public Object[] getSelectedObjects() {
        return model.isSelected() ? new Object[]{this} : null;
    }

    /**
     * Returns an array of all the <code>ItemListener</code>s added
     * to this SAbstractIconTextCompound with addItemListener().
     *
     * @return all of the <code>ItemListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ItemListener[] getItemListeners() {
        return (ItemListener[]) (getListeners(ItemListener.class));
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
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this SAbstractIconTextCompound with addChangeListener().
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) (getListeners(ChangeListener.class));
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
        reloadIfChange(this.horizontalTextPosition, textPosition);
        horizontalTextPosition = textPosition;

    }

    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    public void setVerticalTextPosition(int textPosition) {
        reloadIfChange(this.verticalTextPosition, textPosition);
        verticalTextPosition = textPosition;
    }

    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    public void setIconTextGap(int gap) {
        reloadIfChange(this.iconTextGap, gap);
        iconTextGap = gap;
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
        if (isDifferent(icon, i)) {
            // do reload if previous icon was null
            if (isUpdatePossible() && icon != null) {
                if (SButton.class.isAssignableFrom(getClass()))
                    update(((ButtonCG) getCG()).getIconUpdate((SButton) this, i));
                else if (SCheckBox.class.isAssignableFrom(getClass()))
                    update(((CheckBoxCG) getCG()).getIconUpdate((SCheckBox) this, i));
                else if (SRadioButton.class.isAssignableFrom(getClass()))
                    update(((RadioButtonCG) getCG()).getIconUpdate((SRadioButton) this, i));
                else if (SToggleButton.class.isAssignableFrom(getClass()))
                    update(((ToggleButtonCG) getCG()).getIconUpdate((SToggleButton) this, i));
                else if (SClickable.class.isAssignableFrom(getClass()))
                    update(((ClickableCG) getCG()).getIconUpdate((SClickable) this, i));
                else
                    reload();
            }
            else {
                reload();
            }
            icon = i;
        }
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
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTED) == null ? null : CSSStyleSheet.getBackground((CSSAttributeSet) dynamicStyles.get(SELECTOR_SELECTED));
    }

    /**
     * Set the foreground color.
     *
     * @param color the new foreground color
     */
    public void setSelectionBackground(Color color) {
        setAttribute(SELECTOR_SELECTED, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Return the foreground color.
     *
     * @return the foreground color
     */
    public Color getSelectionForeground() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTED) == null ? null : CSSStyleSheet.getForeground((CSSAttributeSet) dynamicStyles.get(SELECTOR_SELECTED));
    }

    /**
     * Set the foreground color.
     *
     * @param color the new foreground color
     */
    public void setSelectionForeground(Color color) {
        setAttribute(SELECTOR_SELECTED, CSSProperty.COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Set the font.
     *
     * @param font the new font
     */
    public void setSelectionFont(SFont font) {
        setAttributes(SELECTOR_SELECTED, CSSStyleSheet.getAttributes(font));
    }

    /**
     * Return the font.
     *
     * @return the font
     */
    public SFont getSelectionFont() {
        return dynamicStyles == null || dynamicStyles.get(SELECTOR_SELECTED) == null ? null : CSSStyleSheet.getFont((CSSAttributeSet) dynamicStyles.get(SELECTOR_SELECTED));
    }

    /**
     * Sets the label of the button.
     */
    public void setText(String t) {
        if (isDifferent(text, t)) {
            // do reload if previous text was null
            if (isUpdatePossible() && text != null) {
                if (SButton.class.isAssignableFrom(getClass()))
                    update(((ButtonCG) getCG()).getTextUpdate((SButton) this, t));
                else if (SCheckBox.class.isAssignableFrom(getClass()))
                    update(((CheckBoxCG) getCG()).getTextUpdate((SCheckBox) this, t));
                else if (SRadioButton.class.isAssignableFrom(getClass()))
                    update(((RadioButtonCG) getCG()).getTextUpdate((SRadioButton) this, t));
                else if (SToggleButton.class.isAssignableFrom(getClass()))
                    update(((ToggleButtonCG) getCG()).getTextUpdate((SToggleButton) this, t));
                else if (SClickable.class.isAssignableFrom(getClass()))
                    update(((ClickableCG) getCG()).getTextUpdate((SClickable) this, t));
                else
                    reload();
            }
            else {
                reload();
            }
            text = t;
        }
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

            if (!delayEvents) {
                fireStateChanged();
                fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this,
                        model.isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
                delayedEvent = false;
            }
            else
                delayedEvent = true;

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

    public void fireIntermediateEvents() {}

    public void fireFinalEvents() {
        super.fireFinalEvents();
        if (delayedEvent) {
            fireStateChanged();
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this,
                    model.isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
            delayedEvent = false;
        }
    }
}
