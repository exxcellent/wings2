/*
 * SAbstractFormatter.java
 *
 * Created on 11. September 2003, 11:29
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
package org.wings.text;

import org.wings.SFormattedTextField;
import org.wings.script.JavaScriptListener;

import java.util.Iterator;
import java.util.Vector;
import java.text.ParseException;

/**
 * @author theresia
 */
public abstract class SAbstractFormatter {

    public SAbstractFormatter() {
    }

    /*
     * Installs a SFormattedTextField to the Formatter. This is important, because the
     * Formatter must create JavaScript for the FormattedTextField everytime the Formatter
     * is changed.
     **/
    public void install(SFormattedTextField textField) {
    }

    /*
     * removes the Formatter
     */
    public void uninstall(SFormattedTextField textField) {
    }

    /*
     * Generates the new JavaScript for all registered SFormattedTextFields.
     * New code must be created if the Format of the formatter changes or if
     * a new ActionListener is registered.
     */
    public void updateFormatter() {
    }

    /**
     * @param text String to convert
     * @return Object representation of text
     */
    public abstract Object stringToValue(String text) throws ParseException;

    /**
     * @param value Value to convert
     * @return String representation of value
     */
    public abstract String valueToString(Object value) throws ParseException;

    public String validate(String text) {
        try {
            return valueToString(stringToValue(text));
        }
        catch (ParseException e) {
            return null;
        }
    }
}
