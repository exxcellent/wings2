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

/**
 * A
 *
 * @author bschmid
 */
public class CSSStyle extends CSSAttributeSet implements Style {
    /*private StyleSheet sheet; */
    private CSSSelector selector;

    public CSSStyle(CSSSelector selector, CSSAttributeSet attributes) {
        super(attributes);
        this.selector = selector;
    }

    public CSSStyle(CSSSelector selector, CSSProperty property, String value) {
        super(property, value);
        this.selector = selector;
    }

    // BSC: We don't need the backling to the style.
    /* *
     * The style sheet owning this style.
     * @param sheet The style sheet owning this style.
     * /
    public void setSheet(StyleSheet sheet) {
        this.sheet = sheet;
    }*/

    /* *
     * @return  The style sheet owning this style.
     * /
    public StyleSheet getSheet() { return sheet; }
    */

    /**
     * @return Rendered Style: css selector { attribute/values }
     */
    public String toString() {
        return selector.getSelectorString() + " { " + super.toString() + "}";
    }

    /* @see Style */
    public CSSSelector getSelector() {
        return selector;
    }

    /* @see Style */
    public void setSelector(CSSSelector selector) {
        this.selector = selector;
    }
}
