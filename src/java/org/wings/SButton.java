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


import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * A button implementation.
 * This is also a button for usage in a renderer (e.g {@link org.wings.table.STableCellRenderer}).
 * This button implementation encodes its action command into the low level
 * event and fires the encoded action command and not the actual action command,
 * if an low level event triggers a button press.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
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
            throw new IllegalArgumentException("SButton don`t support button groups, use SToggleButton");
        } // end of if ()
    }

    public boolean isSelected() {
        return false;
    }

    public String getSelectionParameter() {
        return getActionCommand() != null ? getActionCommand() : "1";
    }
}


