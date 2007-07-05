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

import org.wings.Renderable;

import java.io.Serializable;
import java.util.*;

/**
 * A CSS Style definition.
 *
 * <p>A Style is collection of CSS property/value pairs that are applied to a specified component or component
 * element. This target is defined by the CSS selector.
 * <p>So this object can be understood as the OO equivalent of a CSS style definition likw i.e.<br/>
 * <code>     A.myStyle { color: red; background: #fff; }</code><br/>
 * which consists of a CSS selector before the braces and the CSS property/value list inside the braces.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public interface Style extends Renderable, Serializable, Cloneable {

    /**
     * A object defining on what this styles applies to.
     * @return The CSS selector which defines to which elements this style applies to.
     */
    Selector getSelector();

    /**
     * Defines a CSS property/value pair in this style.
     * @return The previous style property value.
     */
    String put(CSSProperty styleProperty, String styleValue);

    /**
     * Adds a set of CSS property/value pairs to this style definition.
     *
     * @param attributes Set of CSS property/value pairs to add to this style definition
     * @return <code>true</code> if the style was changed, <code>false</code> if no modification was necessary
     */
    boolean putAll(CSSAttributeSet attributes);

    /**
     * Removes an attribute from the list.
     *
     * @param name the attribute name
     * @return The previous value for this CSS property
     */
    String remove(CSSProperty name);

    /**
     * Gets the Map of defined CSS properties.
     *
     * @return the map of CSS properties
     */
    Map properties();

}

// We don't need the backlink.
/* *
 * The style sheet owning this style.
 * @param sheet The style sheet owning this style.
 * /
void setSheet(StyleSheet sheet);

 /* *
 * @return  The style sheet owning this style.
 * /
StyleSheet getSheet();
*/
