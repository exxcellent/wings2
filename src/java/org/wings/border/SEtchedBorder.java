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
 * Draw a etched border around a component.
 * <span style="border-style: ridge; border-width: 3px;">RAISED</span>
 * <span style="border-style: groove; border-width: 3px;">LOWERED</span>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class SEtchedBorder
    extends SAbstractBorder
{
    public static final int RAISED = 0;
    public static final int LOWERED = 1;

    int etchedType = RAISED;

    /**
     * TODO: documentation
     *
     */
    public SEtchedBorder() {}

    /**
     * TODO: documentation
     *
     * @param etchedType
     */
    public SEtchedBorder(int etchedType) {
        setEtchedType(etchedType);
    }

    /**
     * TODO: documentation
     *
     * @param etchedType
     * @param insets
     */
    public SEtchedBorder(int etchedType, Insets insets) {
        super(insets);
        setEtchedType(etchedType);
    }

    /**
     * TODO: documentation
     *
     * @param etchedType
     */
    public void setEtchedType(int etchedType) {
        this.etchedType = etchedType;
    }

    public int getEtchedType() { return etchedType; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
