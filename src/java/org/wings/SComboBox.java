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

import org.wings.plaf.ComboBoxCG;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * Combobox widget to be used inside {@link SForm} elements.
 *
 * @author Holger Engels
 * @see javax.swing.ComboBoxModel
 * @see SListCellRenderer
 */
public class SComboBox
        extends SComponent
        implements LowLevelEventListener, ListDataListener, ItemSelectable {
    /**
     * The model.
     *
     * @see javax.swing.ComboBoxModel
     */
    protected ComboBoxModel dataModel;

    /**
     * The renderer used for cell rendering each cell.
     *
     * @see SListCellRenderer
     */
    protected SListCellRenderer renderer;

    /**
     * how many rows are displayed in the popup window
     */
    protected int maximumRowCount = 8;

    /**
     * action command to fire
     */
    protected String actionCommand = "comboBoxChanged";


    // do not initalize with null!
    private final SCellRendererPane cellRendererPane = new SCellRendererPane();

    /**
     * This protected field is implementation specific. Do not access directly
     * or override.
     */
    protected Object selectedItemReminder;

    // Flag to ensure that infinite loops do not occur with ActionEvents.
    private boolean firingActionEvent = false;

    // Flag to ensure the we don't get multiple ActionEvents on item selection.
    private boolean selectingItem = false;

    private boolean delayEvent = false;
    private boolean delayedEvent = false;

    /**
     * Creates a SComboBox that takes its items from an existing ComboBoxModel.
     *
     * @param model the ComboBoxModel that provides the displayed list of items
     */
    public SComboBox(ComboBoxModel model) {
        setModel(model);
    }

    /**
     * Creates a SComboBox that contains the elements in the specified array.
     */
    public SComboBox(final Object items[]) {
        setModel(new DefaultComboBoxModel(items));
    }

    /**
     * Creates a SComboBox that contains the elements in the specified Vector.
     */
    public SComboBox(Vector items) {
        setModel(new DefaultComboBoxModel(items));
    }

    /**
     * Creates a SComboBox with a default data model.
     * The default data model is an empty list of objects.
     * Use <code>addItem</code> to add items.
     */
    public SComboBox() {
        setModel(new DefaultComboBoxModel());
    }

    /**
     * Sets the data model that the SComboBox uses to obtain the list of items.
     *
     * @param model the ComboBoxModel that provides the displayed list of items
     */
    public void setModel(ComboBoxModel model) {
        if (isDifferent(dataModel, model)) {
            if (dataModel != null)
                dataModel.removeListDataListener(this);

            dataModel = model;

            dataModel.addListDataListener(this);

            // set the current selected item.
            selectedItemReminder = dataModel.getSelectedItem();

            reload();
        }
    }

    /**
     * Returns the data model currently used by the SComboBox.
     *
     * @return the ComboBoxModel that provides the displayed list of items
     */
    public final ComboBoxModel getModel() {
        return dataModel;
    }

    /**
     * Sets the maximum number of rows the SComboBox displays.
     * If shown as a formComponent, this value is used for the <em>size</em>-attribute.
     *
     * @param count <em>size</em>-attribute
     */
    public void setMaximumRowCount(int count) {
        if (maximumRowCount != count) {
            maximumRowCount = count;

            reload();
        }
    }

    /**
     * Returns the <em>size</em>-attribute
     *
     * @return the value used for the <em>size</em>-attribute
     */
    public int getMaximumRowCount() {
        return maximumRowCount;
    }

    /**
     * @param newRenderer the SListCellRenderer that displays the selected item.
     */
    public void setRenderer(SListCellRenderer newRenderer) {
        if (isDifferent(renderer, newRenderer)) {
            renderer = newRenderer;
            reload();
        }
    }

    /**
     * @return the ListCellRenderer that displays the selected item.
     */
    public SListCellRenderer getRenderer() {
        return renderer;
    }

    /**
     * Sets the selected item in the SComboBox.
     *
     * @param object the list object to select
     */
    public void setSelectedItem(Object object) {
        if (isDifferent(object, dataModel.getSelectedItem())) {
            // Must toggle the state of this flag since this method
            // call may result in ListDataEvents being fired.

            selectingItem = true;
            dataModel.setSelectedItem(object);
            selectingItem = false;

            if (!delayEvent) {
                if (selectedItemReminder != dataModel.getSelectedItem()) {
                    // in case a users implementation of ComboBoxModel
                    // doesn't fire a ListDataEvent when the selection
                    // changes.
                    selectedItemChanged();
                }
                fireActionEvent();

                delayedEvent = false;
            }
            else
                delayedEvent = true;

            if (isUpdatePossible() && SComboBox.class.isAssignableFrom(getClass()))
                update(((ComboBoxCG) getCG()).getSelectionUpdate(this, getSelectedIndex()));
            else
                reload();
        }
    }

    /**
     * Returns the currently selected item.
     *
     * @return the currently selected list object from the data model
     */
    public Object getSelectedItem() {
        return dataModel.getSelectedItem();
    }

    /**
     * Selects the item at index <code>index</code>.
     *
     * @param index the item to be selected
     */
    public void setSelectedIndex(int index) {
        int size = dataModel.getSize();

        if (index == -1)
            setSelectedItem(null);
        else if (index < -1 || index >= size)
            throw new IllegalArgumentException("setSelectedIndex: " + index +
                    " out of bounds");
        else
            setSelectedItem(dataModel.getElementAt(index));
    }

    /**
     * Returns the index of the currently selected item in the list.
     *
     * @return the selected item in the list or -1 if no item is selected or if
     *         the currently selected item (text field) is not in the list
     */
    public int getSelectedIndex() {
        Object selected = dataModel.getSelectedItem();

        if (selected == null) return -1;

        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (selected.equals(getItemAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds an item to the item list.
     *
     * @param object the Object to add to the list
     */
    public void addItem(Object object) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) dataModel).addElement(object);
    }

    /**
     * Inserts an item into the item list at a given index.
     *
     * @param object the Object to add to the list
     * @param index  an int specifying the position at which to add the item
     */
    public void insertItemAt(Object object, int index) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) dataModel).insertElementAt(object, index);
    }

    /**
     * Removes an item from the item list.
     * This method works only if the SComboBox uses the default data model.
     * SComboBox uses the default data model when created with the empty constructor
     * and no other model has been set.
     *
     * @param object the object to remove from the item list
     */
    public void removeItem(Object object) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) dataModel).removeElement(object);
    }

    /**
     * Removes the item at <code>index</code>
     *
     * @param index an int specifying the idex of the item to remove, where 0
     *              indicates the first item in the list
     */
    public void removeItemAt(int index) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) dataModel).removeElementAt(index);
    }

    /**
     * Removes all items from the item list.
     */
    public void removeAllItems() {
        checkMutableComboBoxModel();
        MutableComboBoxModel model = (MutableComboBoxModel) dataModel;
        int size = model.getSize();

        if (model instanceof DefaultComboBoxModel) {
            ((DefaultComboBoxModel) model).removeAllElements();
        } else {
            for (int i = 0; i < size; ++i) {
                Object element = model.getElementAt(0);
                model.removeElement(element);
            }
        }
    }

    void checkMutableComboBoxModel() {
        if (!(dataModel instanceof MutableComboBoxModel))
            throw new RuntimeException("Cannot use this method with a non-Mutable data model.");
    }

    /**
     * Adds an ItemListener. The listener will receive an action event
     * when the user changed the selection.
     *
     * @param listener the ItemListener that is to be notified
     */
    public void addItemListener(ItemListener listener) {
        addEventListener(ItemListener.class, listener);
    }

    /**
     * Removes an ItemListener
     *
     * @param listener the ItemListener to remove
     */
    public void removeItemListener(ItemListener listener) {
        removeEventListener(ItemListener.class, listener);
    }

    /**
     * Returns an array of all the ItemListeners added to this SComboBox
     *
     * @return all ItemListeners added or an empty array if there are no such listeners
     */
    public ItemListener[] getItemListeners() {
        return (ItemListener[]) getListeners(ItemListener.class);
    }

    /**
     * Adds an ActionListener. The listener will receive an action event
     * when the user changed the selection.
     *
     * @param listener the ActionListener that is to be notified
     */
    public void addActionListener(ActionListener listener) {
        addEventListener(ActionListener.class, listener);
        reload();
    }

    /**
     * Removes an ActionListener
     *
     * @param listener the ActionListener to remove
     */
    public void removeActionListener(ActionListener listener) {
        removeEventListener(ActionListener.class, listener);
        reload();
    }

    /**
     * Returns an array of all the ActionListeners added to this SComboBox
     *
     * @return all ActionListeners added or an empty array if there are no such listeners
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[]) getListeners(ActionListener.class);
    }

    /**
     * Sets the action commnand that should be included in the event
     * sent to action listeners.
     *
     * @param command a string containing the "command" that is sent
     *                to action listeners. The same listener can then
     *                do different things depending on the command it
     *                receives.
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /**
     * Returns the action commnand that is included in the event sent to
     * action listeners.
     *
     * @return the string containing the "command" that is sent
     *         to action listeners.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Notify all listeners that have registered as ItemListeners.
     *
     * @see EventListenerList
     */
    protected void fireItemStateChanged(ItemEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(e);
            }
        }
    }

    /**
     * Notify all listeners that have registered as ActionListeners if the
     * selected item has changed
     *
     * @see EventListenerList
     */
    protected void fireActionEvent() {
        if (!firingActionEvent) {
            // Set flag to ensure that an infinite loop is not created
            firingActionEvent = true;

            ActionEvent e = null;

// Guaranteed to return a non-null array
            Object[] listeners = getListenerList();
// Process the listeners last to first, notifying
// those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ActionListener.class) {
                    if (e == null)
                        e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                getActionCommand());
                    ((ActionListener) listeners[i + 1]).actionPerformed(e);
                }
            }
            firingActionEvent = false;
        }
    }

    /**
     * Returns an array containing the selected item.
     *
     * @return an array of Objects containing the selected item
     */
    public Object[] getSelectedObjects() {
        Object selectedObject = getSelectedItem();
        if (selectedObject == null)
            return new Object[0];
        else
            return new Object[]{selectedObject};
    }

    /**
     * This method is public as an implementation side effect.
     * do not call or override.
     *
     * @see javax.swing.event.ListDataListener
     */
    public void contentsChanged(ListDataEvent e) {
        if (isDifferent(selectedItemReminder, dataModel.getSelectedItem())) {
            if (!delayEvent) {
                selectedItemChanged();
                if (!selectingItem)
                    fireActionEvent();

                delayedEvent = false;
            }

            if (e.getIndex0() != -1 && e.getIndex1() != -1)
                reload();
        }
    }

    /**
     * Invoked when items have been added to the internal data model.
     * The "interval" includes the first and last values added.
     *
     * @see javax.swing.event.ListDataListener
     */
    public void intervalAdded(ListDataEvent e) {
        reload();
    }

    /**
     * Invoked when values have been removed from the data model.
     * The"interval" includes the first and last values removed.
     *
     * @see javax.swing.event.ListDataListener
     */
    public void intervalRemoved(ListDataEvent e) {
        reload();
    }


    /* Accessing the model */
    /**
     * Returns the number of items in the list.
     *
     * @return an int equal to the number of items in the list
     */
    public int getItemCount() {
        return dataModel.getSize();
    }

    /**
     * Returns the list item at the specified index.
     *
     * @param index an int indicating the list position
     * @return the Object at that list position
     */
    public Object getItemAt(int index) {
        return dataModel.getElementAt(index);
    }

    /**
     * This protected method is implementation specific. Do not access directly
     * or override.
     */
    protected void selectedItemChanged() {
        if (selectedItemReminder != null) {
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                    selectedItemReminder,
                    ItemEvent.DESELECTED));
        }

        // set the new selected item.
        selectedItemReminder = dataModel.getSelectedItem();

        if (selectedItemReminder != null) {
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                    selectedItemReminder,
                    ItemEvent.SELECTED));
        }
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        if (action.endsWith("_keystroke"))
            return;

        delayEvent = true;

        int selectedIndex = -1;
        // last will win!!
        for (int i = 0; i < values.length; i++) {
            try {
                if (values[i].length() > 0) selectedIndex = Integer.parseInt(values[i]);
            } catch (Exception ex) {
                // ignore, some illegal request.. (maybe log it)
            }
        }

        if (selectedIndex >= 0 && getSelectedIndex() != selectedIndex) {
            setSelectedIndex(selectedIndex);
            SForm.addArmedComponent(this);
        }

        delayEvent = false;
    }


    public void setParent(SContainer p) {
        super.setParent(p);
        if (getCellRendererPane() != null)
            getCellRendererPane().setParent(p);
    }

    protected void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        if (getCellRendererPane() != null)
            getCellRendererPane().setParentFrame(f);
    }


    public void fireIntermediateEvents() {
    }

    public void fireFinalEvents() {
        super.fireFinalEvents();
        if (delayedEvent) {
            if (selectedItemReminder != dataModel.getSelectedItem()) {
                // in case a users implementation of ComboBoxModel
                // doesn't fire a ListDataEvent when the selection
                // changes.
                selectedItemChanged();
            }
            fireActionEvent();

            delayedEvent = false;
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

    public final SCellRendererPane getCellRendererPane() {
        return cellRendererPane;
    }

    public void setCG(ComboBoxCG cg) {
        super.setCG(cg);
    }

    public String getSelectionParameter(int index) {
        return Integer.toString(index);
    }
}


