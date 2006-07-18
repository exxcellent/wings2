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
import java.io.Serializable;
import java.text.ParseException;

/**
 * @author theresia
 */
public abstract class SAbstractFormatter implements Serializable {

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
}
