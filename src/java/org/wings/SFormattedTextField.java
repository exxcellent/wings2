/*
 * SFormattedTextField.java
 *
 * Created on 9. September 2003, 09:05
 */

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

import org.wings.text.SAbstractFormatter;
import org.wings.text.SDefaultFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.event.ActionListener;
import java.text.ParseException;


/**
 * Formats it content interactivly on the server side via DWR/AJAX.
 *
 * @author theresia
 */
public class SFormattedTextField extends STextField {
    private final static Log log = LogFactory.getLog(SFormattedTextField.class);
    private SAbstractFormatter formatter = null;
    private static final SAbstractFormatter NO_FORMATTER = new SAbstractFormatter() {
        public Object stringToValue(String text) throws ParseException {
            return null;
        }

        public String valueToString(Object value) throws ParseException {
            return null;
        }
    };

    public SFormattedTextField() {
        this(NO_FORMATTER);
    }

    public SFormattedTextField(SAbstractFormatter formatter) {
        this.formatter = formatter;
    }

    public void setValue(Object object) {
        String string = null;
        if (formatter != null)
            try {
                string = this.formatter.valueToString(object);
            } catch (ParseException e) {
                log.info("Unable to parse object" + e);
            }
        super.setText(string);
    }

    public Object getValue() {
        Object returnValue = null;
        try {
            returnValue = this.formatter.stringToValue(this.getText());
        } catch (ParseException e) {
            log.debug("Unable to parse string" + e);
        }
        return returnValue;
    }

    // brauch man das?? So wohl nicht...
    public SFormattedTextField(Object value) {
        setValue(value);
    }

    public SAbstractFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(SAbstractFormatter formatter) {
        if (this.formatter != null) {
            this.formatter.uninstall(this);
        }
        this.formatter = formatter;
        formatter.install(this);
    }

    /*
     * When an ActionListener is added, the Formatter must be updated.
     */
    public void addActionListener(ActionListener listener) {
        super.addActionListener(listener);
        if (formatter == null) {
            formatter = new SDefaultFormatter();
            formatter.install(this);
        } else {
            formatter.updateFormatter();
        }
    }

    protected void setParentFrame(SFrame parentFrame) {
        if (super.getParentFrame() != null)
            formatter.uninstall(this);
        super.setParentFrame(parentFrame);
        if (super.getParentFrame() != null)
            formatter.install(this);
    }
}