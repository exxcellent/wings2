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

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * A StyleSheet is a set of {@link Style}s. Proably a instance of {@link CSSStyleSheet}.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface StyleSheet extends Renderable {
    /**
     * Register a {@link Style} in the style sheet.
     * @param style
     */
    void putStyle(Style style);

    /**
     * The {@link Style}s contained in this style sheet.
     * @return
     */
    Set styles();

    /**
     * May this style sheet change during runtime?
     */
    boolean isFinal();

    /**
     * Creates styles by parsing an input stream.
     * @param inStream Stream containing style sheet source
     */
    void read(InputStream inStream) throws IOException;
}


