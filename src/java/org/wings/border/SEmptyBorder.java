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
package org.wings.border;

import java.awt.*;

/**
 * An invisible border around a component used for spacing.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SEmptyBorder extends SAbstractBorder {

    /**
     * Constructs a new empty border
     * @param insets The desired insets / paddings.
     * @see #setInsets(java.awt.Insets)
     */
    public SEmptyBorder(Insets insets) {
        super(insets);
    }

    /**
     * Constructs a new empty border
     * @param top top padding in px
     * @param left left padding in px
     * @param bottom bottom padding in px
     * @param right right padding in px
     */
    public SEmptyBorder(int top, int left, int bottom, int right) {
        this(new Insets(top, left, bottom, right));
    }
}
