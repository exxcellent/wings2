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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
        setFormatter( formatter );
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
        SAbstractFormatter oldFormatter = this.formatter;
        
        if ( oldFormatter != null ) {
            oldFormatter.uninstall();
        }
        
        this.formatter = formatter;
        
        if ( formatter != null ) {
            formatter.install( this );
        }
        
    }
}