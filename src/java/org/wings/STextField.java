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

import org.wings.plaf.TextFieldCG;

import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Single-lined input component for text input which requires a surrounding {@link SForm} element.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class STextField
        extends STextComponent {

    /**
     * maximum columns shown
     */
    protected int columns = 12;

    /**
     * maximum columns allowed
     */
    protected int maxColumns = -1;

    /**
     * default action command to fire
     */
    protected String actionCommand;

    // Flag to ensure that infinite loops do not occur with ActionEvents.
    private boolean firingActionEvent = false;

    public STextField() {
    }

    public STextField(String text) {
        super(text);
    }
    
    /**
     * Sets the number of columns of the TextField.
     * @param c the number of columns 
     */
    public void setColumns(int c) {
        int oldColumns = columns;
        columns = c;
        if (columns != oldColumns)
            reload();
    }

    /**
     * Returns the number of columns of the TextField.
     * @return the number of columns
     */
    public int getColumns() {
        return columns;
    }

    public void setMaxColumns(int mc) {
        int oldMaxColumns = maxColumns;
        maxColumns = mc;
        if (maxColumns != oldMaxColumns)
            reload();
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setCG(TextFieldCG cg) {
        super.setCG(cg);
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
        if (actionCommand == null)
            return getText();
        return actionCommand;
    }

    /**
     * Adds an ActionListener to the button.
     *
     * @param listener the ActionListener to be added
     */
    public void addActionListener(ActionListener listener) {
        addEventListener(ActionListener.class, listener);
    }

    /**
     * Removes the supplied Listener from teh listener list
     */
    public void removeActionListener(ActionListener listener) {
        removeEventListener(ActionListener.class, listener);
    }

    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this AbstractButton with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[]) (getListeners(ActionListener.class));
    }

    /**
     * Fire an ActionEvent at each registered listener.
     *
     * @param event the supplied ActionEvent
     */
    protected void fireActionPerformed(ActionEvent event) {
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    String actionCommand = event.getActionCommand();
                    if (actionCommand == null) {
                        actionCommand = getActionCommand();
                    }
                    e = new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED,
                            actionCommand,
                            event.getWhen(),
                            event.getModifiers());
                }
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
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

    public void fireFinalEvents() {
        super.fireFinalEvents();
        fireActionEvent();
    }
}


