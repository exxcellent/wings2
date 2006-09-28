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


import org.wings.plaf.RadioButtonCG;

/**
 * Can be selected or deselected, and displays that state to the user.
 * 
 * <p/>
 * Example:
 * <form>
 * <b>Radiobuttons:</b>
 * <p><input type="radio" name="1" value="1" checked>One</p>
 * <p><input type="radio" name="1" value="2">Two</p>
 * <p><input type="radio" name="1" value="3">Three</p>
 * </form>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SRadioButton
        extends SAbstractButton {
    public SRadioButton() {
        setType(RADIOBUTTON);
    }

    public SRadioButton(String label) {
        super(label, RADIOBUTTON);
    }

    public SRadioButton(boolean selected) {
        this();
        setSelected(selected);
    }

    public String getLowLevelEventId() {
        if (getGroup() != null && getShowAsFormComponent()) {
            return getGroup().getComponentId();
        } else {
            return super.getLowLevelEventId();
        } // end of if ()
    }

    public void setType(String t) {
        if (!RADIOBUTTON.equals(t))
            throw new IllegalArgumentException("type change not supported, type is fix: radiobutton");

        super.setType(t);
    }

    public void setCG(RadioButtonCG cg) {
        super.setCG(cg);
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        
        delayEvents(true);

        boolean origSelected = isSelected();

        if (getShowAsFormComponent()) {
            if (getGroup() == null) {
                // one hidden and one checked event from the form says select
                // it, else deselect it (typically only the hidden event)
                setSelected(values.length == 2);
            } else {
                int eventCount = 0;
                for (int i = 0; i < values.length; i++) {
                    // check the count of events, which are for me - with a
                    // button group, the value is my component id, if a event is
                    // for me   
                    if (getName().equals(values[i])) {
                        eventCount++;
                    } // end of if ()
                } // end of for (int i=0; i<; i++)
                // one hidden and one checked event from the form says select
                // it, else deselect it (typically only the hidden event)
                setSelected(eventCount == 2);
            } // end of if ()
        } else {
            if (getGroup() != null) {
                getGroup().setDelayEvents(true);
                setSelected(parseSelectionToggle(values[0]));
                getGroup().setDelayEvents(false);
            } else {
                setSelected(parseSelectionToggle(values[0]));
            } // end of else
        }

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

    public String getSelectionParameter() {
        if (getGroup() != null && getShowAsFormComponent()) {
            return getName();
        } else {
            return "1";
        }
    }

    public String getDeselectionParameter() {
        if (getGroup() != null && getShowAsFormComponent()) {
            return getName();
        } else {
            return "0";
        }
    }
}
