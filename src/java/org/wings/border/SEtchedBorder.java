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
package org.wings.border;

import java.awt.*;

/**
 * Draw a etched border around a component.
 * <span style="border-style: ridge; border-width: 3px;">RAISED</span>
 * <span style="border-style: groove; border-width: 3px;">LOWERED</span>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class SEtchedBorder
        extends SAbstractBorder {
    public static final int RAISED = 0;
    public static final int LOWERED = 1;

    int etchedType = RAISED;

    public SEtchedBorder() {
        setEtchedType(RAISED);
    }

    public SEtchedBorder(int etchedType) {
        setEtchedType(etchedType);
    }

    public SEtchedBorder(int etchedType, Insets insets) {
        super(Color.black, 2, insets);
        setEtchedType(etchedType);
    }

    public void setEtchedType(int etchedType) {
        this.etchedType = etchedType;
        attributes.put("border-style", etchedType == RAISED ? "ridge" : "groove");
    }

    public int getEtchedType() { return etchedType; }
}


