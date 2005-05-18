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


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SSeparator;
import org.wings.io.Device;

import java.io.IOException;

public class SeparatorCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.SeparatorCG {


    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SSeparator component = (SSeparator) _c;

        device.print("<hr");
        Utils.optAttribute(device, "class", component.getStyle());
        Utils.optAttribute(device, "width", component.getWidth());
        Utils.optAttribute(device, "size", component.getSize());

        switch (component.getAlignment()) {
            case SSeparator.RIGHT_ALIGN:
                device.print(" align=\"right\"");
                break;
            case SSeparator.CENTER_ALIGN:
                device.print(" align=\"center\"");
                break;
            case SSeparator.BLOCK_ALIGN:
                device.print(" align=\"justify\"");
                break;
        }
        ;
        if (!component.getShade()) {
            device.print(" noshade=\"true\"");
        }
        device.print("/>\n");
    }
}
