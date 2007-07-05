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

import org.wings.plaf.ToggleButtonCG;

import javax.swing.*;

/**
 * Knows and displays two different states.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SToggleButton extends SAbstractButton {

	 /**
     * Creates a toggle button with an initial text.
     * 
     * @param label Text to display
     */
    public SToggleButton(String label) {
        super(label);
    }

    /**
     * Creates a toggle button with an icon.
     * 
     * @param icon Icon to display
     */
    public SToggleButton(SIcon icon) {
        super();
        setIcon(icon);
    }

    /**
     * Creates a button where properties are taken from the
     * Action supplied.
     *
     * @param action the Action used to specify the new button
     */
    public SToggleButton(Action action) {
        super(action);
    }

    /**
     * Creates a toggle button.
     * 
     */
    public SToggleButton() {
    }

    public void setCG(ToggleButtonCG cg) {
        super.setCG(cg);
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        if (action.endsWith("_keystroke"))
            return;
        
        delayEvents(true);

        boolean origSelected = isSelected();

        if (getGroup() != null) {
            getGroup().setDelayEvents(true);
            setSelected(parseSelectionToggle(values[0]));
            getGroup().setDelayEvents(false);
        } else {
            setSelected(parseSelectionToggle(values[0]));
        } // end of else


        if (isSelected() != origSelected) {
            // got an event, that is a select...
            SForm.addArmedComponent(this);
        } // end of if ()
        
        delayEvents(false);
    }

    /**
     * in form components the parameter value of an button is the button
     * text. So just toggle selection, in process request, if it is a request
     * for me.
     */
    protected boolean parseSelectionToggle(String toggleParameter) {
        // a button/image in a form has no value, so just toggle selection...
        if (getShowAsFormComponent()) {
            return !isSelected();
        } // end of if ()

        if ("1".equals(toggleParameter))
            return true;
        else if ("0".equals(toggleParameter))
            return false;


        // don't change...
        return isSelected();
    }

}


