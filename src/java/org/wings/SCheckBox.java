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

import org.wings.plaf.CheckBoxCG;

import java.util.Arrays;

/*
 * Checkboxen sind etwas besondere {@link SFormComponent}, denn
 * ihre eigentliche Identitaet ({@link #getUID}) steckt nicht im Name,
 * sondern im Value. Das ist deswegen so, weil in HTML Gruppen
 * durch den selben Namen generiert werden. Das ist natuerlich
 * problematisch. Eine anderes Problem, welches hier auftaucht ist,
 * das HTML immer nur rueckmeldet, wenn eine Checkbox markiert ist.
 * Deshalb wird hintenan immer ein Hidden Form Element gehaengt,
 * welches rueckmeldet, dass die Checkbox bearbeitet wurde.
 */

/**
 * Checkbox widget.
 * <p/>
 * May be displayed as a HTML form element or as a clickable icon.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class SCheckBox extends SAbstractButton {
	
	
	/**
     * Creates an initially unselected checkbox.
     */
    public SCheckBox() {
        this(false);
    }

    /**
     * Creates an initially unselected checkbox with a text-label.
     * 
     * @param label Text to display
     */
    public SCheckBox(String label) {
        this(false);
        setText(label);
    }
    
    /**
     * Creates a checkbox with a certain state.
     * 
     * @param selected Whether the checkbox is initially selected or not.
     */
    public SCheckBox(boolean selected) {
        setSelected(selected);

        setHorizontalTextPosition(SConstants.NO_ALIGN);
        setVerticalTextPosition(SConstants.NO_ALIGN);

        super.setType(CHECKBOX);
    }

    /**
     * Creates a checkbox with a text-label and a state.
     * 
     * @param label Text to display
     * @param selected Whether the checkbox is initially selected or not.
     */
    public SCheckBox(String label, boolean selected) {
        this(selected);
        setText(label);
    }

    

    protected void setGroup(SButtonGroup g) {
        if (g != null) {
            throw new IllegalArgumentException("SCheckBox don`t support button groups, use SRadioButton");
        } // end of if ()
    }

    /**
     * You cannot change type of checkbox. <p>
     * Accepts only {@link SAbstractButton#CHECKBOX}
     */
    public final void setType(String t) {
        if (!CHECKBOX.equals(t))
            throw new IllegalArgumentException("type change not supported, type is fix: checkbox");

        super.setType(t);
    }

    public void setCG(CheckBoxCG cg) {
        super.setCG(cg);
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        if (action.endsWith("_keystroke"))
            return;

        delayEvents(true);

        boolean requestSelection;
        if (Arrays.asList(values).contains("hidden_reset")) {
            // one hidden and one checked event from the form says select
            // it, else deselect it (typically only the hidden event)
            requestSelection = values.length == 2;
        } else {
            requestSelection = parseSelectionToggle(values[0]);
        }

        if (requestSelection != isSelected()) {
            setSelected(requestSelection);
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


