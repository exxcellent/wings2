/*
 * SDefaultFormatter.java
 *
 * Created on 9. September 2003, 14:16
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

/**
 * SDefault Formatter simulates the normal behavior of a TextField.
 * (when an ActionListener is added, the Form will be submitted onchange)
 *
 * @author theresia
 */
public class SDefaultFormatter extends SAbstractFormatter {
    /**
     * Creates a new instance of SDefaultFormatter
     */
    public SDefaultFormatter() {
    }

    public Object stringToValue(String text) {
        return text;
    }

    public String valueToString(Object value) {
        return "";
    }

    /*
     * Creates the javaScriptFunction based on the given Format. Here isn't any
     * Format, so the SDefaultFormatter creates the code an ActionListener needs.
     */
    public JavaScriptListener generateJavaScript(SFormattedTextField field, boolean actionListener) {
        if (actionListener) {
            JavaScriptListener javaScriptListener = new JavaScriptListener("onchange", "submit()");
            return javaScriptListener;
        } else {
            return new JavaScriptListener("", "");
        }
    }

}
