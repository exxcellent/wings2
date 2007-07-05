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
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SSeparator;
import org.wings.io.Device;

import java.io.IOException;

public final class SeparatorCG extends AbstractComponentCG implements
        org.wings.plaf.SeparatorCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device,
                      final SComponent _c)
            throws IOException {
        final SSeparator component = (SSeparator) _c;

        device.print("<hr");
        Utils.optAttribute(device, "class", component.getStyle());
        Utils.optAttribute(device, "width", component.getWidth());
        Utils.optAttribute(device, "size", component.getSize());

        switch (component.getAlignment()) {
            case SConstants.RIGHT_ALIGN:
                device.print(" align=\"right\"");
                break;
            case SConstants.CENTER_ALIGN:
                device.print(" align=\"center\"");
                break;
            case SConstants.BLOCK_ALIGN:
                device.print(" align=\"justify\"");
                break;
        }

        if (!component.getShade()) {
            device.print(" noshade=\"true\"");
        }
        device.print("/>\n");
    }
}
