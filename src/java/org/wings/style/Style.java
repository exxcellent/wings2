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

import org.wings.Renderable;

import java.io.Serializable;

/**
 * A (CSS) Style definition.
 * <p>A Style is typically a CSS property/value pair that is applied to a element specified by it's selector.
 * <p>Hence this object is i.e. the OO equivalent of <br/><code>    A.myStyle { color: red; }</code>
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface Style extends Renderable, Serializable, Cloneable {

    /**
     * A object defining on what this styles applies to.
     * @return The CSS selector which defines to which elements this style applies to.
     */
    CSSSelector getSelector();

    /**
     * The style sheet owning this style.
     * @param sheet The style sheet owning this style.
     */
    void setSheet(StyleSheet sheet);

     /**
     * @return  The style sheet owning this style.
     */
    StyleSheet getSheet();

    /**
     * Defines a value for the given style propery.
     * @return The previous style property value.
     */
    String put(CSSProperty styleProperty, String styleValue);

    /**
     * Adds a set of attributes to the style.
     *
     * @param attributes the set of attributes to add
     * @return <code>true</code> if the style was changed
     */
    boolean putAll(CSSAttributeSet attributes);

}


