/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.style;

import org.wings.io.Device;

import java.io.IOException;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public final class Style
        extends AttributeSet implements StyleConstants {
    private StyleSheet sheet;
    private String selector;
    private String name;

    public Style(String selector, AttributeSet attributes) {
        super(attributes);
        this.selector = selector;
    }

    public Style(String selector, String name, String value) {
        super(name, value);
        this.selector = selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
        name = null;
    }

    public String getSelector() { return selector; }

    public String getName() {
        if (name == null && selector != null)
            name = selector.substring(selector.indexOf(".") + 1);
        return name;
    }

    public void setSheet(StyleSheet sheet) {
        this.sheet = sheet;
    }

    public StyleSheet getSheet() { return sheet; }

    public void write(Device d)
            throws IOException {
        d.print(selector).print("{");
        super.write(d);
        d.print("}\n");
    }

    public String toString() {
        return selector + " { " + super.toString() + "}";
    }
}


