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

import org.wings.SComponent;
import org.wings.SFrame;

import java.io.Serializable;

/**
 * A CSS selector string. A CSS selector my address one of the following
 * <ul>
 * <li>One specific visual SComponent</li>
  * <li>A virtual area of an visual components. (Instances of {@link Pseudo} found as constants).</li>
 * <li>some free user-defined CSS selection string</li>
 * </ul>
 * On this selection you can define specific value fo CSS attributes.
 * <p/>
 * You use CSS selectors ({@link CSSSelector}) to define <b>which</b> elements you want to modify and define
 * with CSS properties {@link CSSProperty} <b>what</b> visual property you want to modify.
 * <p/>
 * Please refer to
 * <a href="http://www.w3.org/TR/REC-CSS2/selector.html">http://www.w3.org/TR/REC-CSS2/selector.html</a> for details
 *
 * @author bschmid
 */
public class CSSSelector implements Serializable {

    /**
     * We address a free CSS selector string.
     */
    protected String selectorString;
    /**
     * We address a specific component.
     */
    protected SComponent addressedComponent;

    /**
     * Constructs a CSS selector with an free CSS Selector expression.
     * In the dynamic style sheet these selectors are rendered exactly as the passed argument.
     *
     * @param selectorString A css selector expression. i.e. <code>DIV.SLabel &gt; table &gt; tr</code>
     */
    public CSSSelector(String selectorString) {
        this.selectorString = selectorString;
    }

    /**
     * Constructs a CSS selector which exactly addresses the passed component.
     * In the dynamic style sheet these selectors appear as <code>#id</code>
     *
     * @param addressedComponent The SComponent to address via a dynamically generated CSS selector expression
     */
    public CSSSelector(SComponent addressedComponent) {
        this.addressedComponent = addressedComponent;
    }
    
    /**
     * @return An valid CSS selection expression.
     */
    public String getSelectorString() {
        if (selectorString != null)
            return selectorString;
        else
            return getSelectorString(addressedComponent);
    }

    /**
     * Creates the CSS selector string to addressed the passed SComponent
     * @param addressedComponent Adress this component. SFrame are applied globally.
     * @return A valid CSS selector string
     */
    public static String getSelectorString(SComponent addressedComponent) {
        if (addressedComponent == null)
            return "";
        else if (addressedComponent instanceof SFrame)
            return "body";
        else
            return "#" + addressedComponent.getName();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CSSSelector)) return false;

        final CSSSelector cssSelector = (CSSSelector) o;
        final String selectorString = getSelectorString();
        return (selectorString.equals(cssSelector.getSelectorString()));
    }

    public int hashCode() {
        if (selectorString != null)
            return selectorString.hashCode();
        else if (addressedComponent != null)
            return addressedComponent.hashCode();
        else
            return 0;
    }

    /**
     * A CSS Pseudo-Selector. This refers to a specific area of a component.
     * This pseudo selecteor get mapped by the according CG to a real CSS selector.
     */
    public static class Pseudo extends CSSSelector {

        /**
         * Address an area of the specific component. Instances of this classes are converted to real
         * CSS selector by the according component CG's
         *
         * @param pseudoSelectorName A unique name for the area.
         */
        public Pseudo(String pseudoSelectorName) {
            super(pseudoSelectorName);
            this.selectorString = pseudoSelectorName;
        }
    }
}
