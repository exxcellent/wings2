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
    private CSSSelector selector;
    private String name;

    public Style(CSSSelector selector, AttributeSet attributes) {
        super(attributes);
        this.selector = selector;
    }

    public Style(CSSSelector selector, String name, String value) {
        super(name, value);
        this.selector = selector;
    }

    public void setSelector(CSSSelector selector) {
        this.selector = selector;
        name = null;
    }

    public CSSSelector getSelector() { return selector; }

    public String getName() {
        if (name == null && selector != null)
            name = selector.getName();
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


