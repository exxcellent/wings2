/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
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
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SEmptyBorder
    extends SAbstractBorder
{
    public SEmptyBorder(Insets insets) {
        super(Color.black, 2, insets);
   }

    public SEmptyBorder(int top, int left, int bottom, int right) {
        this(new Insets(top, left, bottom, right));
    }
}
