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

import java.io.Serializable;

/**
 * A CSS selector string.
 * Please refer to http://www.w3.org/TR/REC-CSS2/selector.html
 *
 * @author bschmid
 *
 * TODO introduce CSSPseudoSelector class
 */
public class CSSSelector implements Serializable{
    public final static CSSSelector GLOBAL = new CSSSelector("");

    private String selectorString;

    public CSSSelector(String selectorString) {
        this.selectorString = selectorString;
    }

    public String getSelectorString() {
        return selectorString;
    }

    // Some dirty hack? what about #id, etc.... TODO
    public String getName() {
        return selectorString.substring(selectorString.indexOf(".") + 1);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CSSSelector)) return false;

        final CSSSelector cssSelector = (CSSSelector) o;

        if (selectorString != null ? !selectorString.equals(cssSelector.selectorString) : cssSelector.selectorString != null) return false;

        return true;
    }

    public int hashCode() {
        return (selectorString != null ? selectorString.hashCode() : 0);
    }
}
