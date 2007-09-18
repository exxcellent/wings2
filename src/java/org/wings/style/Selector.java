/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.style;

import org.wings.SComponent;
import org.wings.SFrame;

import java.io.Serializable;

/**
 * A selector string. A selector address some part of a component.
 * For example the content of a tabbed pane ..
 */
public class Selector implements Serializable {

    /**
     * We address a specific area of the component. For example the content of a tabbed pane ..
     */
    protected String selector;


    /**
     * @param selectorString
     */
    public Selector(String selectorString) {
        this.selector = selectorString;
    }

    /**
     * @return An valid CSS selection expression.
     */
    public String getSelector() {
        return selector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Selector selector1 = (Selector)o;

        if (selector != null ? !selector.equals(selector1.selector) : selector1.selector != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (selector != null ? selector.hashCode() : 0);
    }
}
