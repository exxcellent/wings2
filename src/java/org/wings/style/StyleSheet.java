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
import java.util.Set;
import java.io.Serializable;

/**
 * A StyleSheet is a collection of CSS {@link Style} definitions.
 * Known instance are of type {@link CSSStyleSheet}.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public interface StyleSheet extends Renderable, Serializable {
    /**
     * Register a {@link Style} in the style sheet.
     */
    void putStyle(Style style);

    /**
     * The {@link Style}s definitions contained in this style sheet.
     *
     * @return All set of {@link Style}s contained in this style sheet
     */
    Set styles();

    /**
     * Declares if this style sheet is final or may change during runtime.
     * @return <code>true</code> if the content of this style sheet will never change, <code>false</code> otherwise.
     */
    boolean isFinal();
}


