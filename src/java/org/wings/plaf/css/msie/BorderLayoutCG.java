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

package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.plaf.css.Utils;

/**
 * A special variant of the border layout to handle some specialities of the MSIE browser.
 * <ul>
 * <li>Component background colour does not stretch to full cell dimensions.</li>
 * </ul>
 *
 * @author bschmid
 */
public class BorderLayoutCG extends org.wings.plaf.css.BorderLayoutCG {
    protected String decorateLayoutCell(SComponent containedComponent) {

        // In CSS2 capable browsers a panel inside a border layout expands to full width
        // In MSIE we have to simulate this esp. the background colour aspect.
        
        // Benjamin: It doesn't always...this totally borked some examples...I'm commenting this out.
        // might be needed for some things, but we need to do this another way.
        // look at border example to see the mess. (OL)

//        if (containedComponent != null && containedComponent.getBackground() != null) {
//            return "background: " + Utils.toColorString(containedComponent.getBackground()) + ";";
//        } else {
            return null;
//        }
    }
}
