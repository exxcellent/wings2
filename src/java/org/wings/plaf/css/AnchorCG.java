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
package org.wings.plaf.css;


import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.io.Device;

import java.io.IOException;

public class AnchorCG
        extends AbstractComponentCG
        implements org.wings.plaf.AnchorCG {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see org.wings.plaf.css.AbstractComponentCG#writeContent(org.wings.io.Device, org.wings.SComponent)
     */
    public void write(final Device device,
                      final SComponent _c)
            throws IOException {
        final SAnchor component = (SAnchor) _c;
        if (hasDimension(component)) {
            writeTablePrefix(device, component);
        } else {
            Utils.printButtonStart(device, component, null, true, component.getShowAsFormComponent());
            writeAllAttributes(device, component);
            device.print(">");
        }
        

        Utils.printCSSInlineFullSize(device, _c.getPreferredSize());
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        Utils.optAttribute(device, "target", component.getTarget());
        Utils.optAttribute(device, "name", component.getName());
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        device.print(">");
        Utils.renderContainer(device, component);
        Utils.printButtonEnd(device, component, null, true);
    }

}
