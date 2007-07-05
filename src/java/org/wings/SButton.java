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
import java.awt.event.ActionEvent;

/**
 * Simple Button widget.
 * <p/>
 * This is also a button for usage in a renderer (e.g {@link org.wings.table.STableCellRenderer}).
 * This button implementation encodes its action command into the low level
 * event and fires the encoded action command and not the actual action command,
 * if an low level event triggers a button press.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class SButton extends SAbstractButton {

    /**
     * Creates a button with text.
     *
     * @param text the text of the button
     */
    public SButton(String text) {
        super(text);
    }

    /**
     * Creates a button where properties are taken from the
     * Action supplied.
     *
     * @param action the Action used to specify the new button
     */
    public SButton(Action action) {
        super(action);
    }

    /**
     * Creates a button with no set text or icon.
     */
    public SButton() {
        super();
    }

    /**
     * Creates a button with a icon
     *
     * @param i the Icon image to display on the button
     */
    public SButton(SIcon i) {
        super();
        setIcon(i);
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text the text of the button
     * @param i    the Icon image to display on the button
     */

    public SButton(String text, SIcon i) {
        super(text);
        setIcon(i);
    }

    protected void setGroup(SButtonGroup g) {
        if (g != null) {
            throw new IllegalArgumentException("SButton doesn't support button groups, use SToggleButton");
        } // end of if ()
    }

    /**
     * Returns the state of the button. This can be true if the button is selected, otherwise false.
     * 
     * @return true if the button is selected, false if not
     */
    public boolean isSelected() {
        return false;
    }

    private String actionCommandToFire;

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        if (action.endsWith("_keystroke"))
            return;

        // got an event, that is a select...
        SForm.addArmedComponent(this);

        if (getShowAsFormComponent() &&
                getActionCommand() != null) {
            actionCommandToFire = getActionCommand();
        } else {
            actionCommandToFire = values[0];
        }
    }

    public void fireFinalEvents() {
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommandToFire));
        if (getGroup() != null) {
            getGroup().fireDelayedFinalEvents();
        }
    }

    public String getSelectionParameter() {
        return getActionCommand() != null ? getActionCommand() : "1";
    }
}
