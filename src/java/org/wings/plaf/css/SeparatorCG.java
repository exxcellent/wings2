// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Separator.plaf'
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
        org.wings.plaf.Utils.optAttribute(device, "class", component.getStyle());
        org.wings.plaf.Utils.optAttribute(device, "width", component.getWidth());
        org.wings.plaf.Utils.optAttribute(device, "size", component.getSize());

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
            device.print(" noshade=\"1\"");
        }
        device.print("/>\n");
    }
}
