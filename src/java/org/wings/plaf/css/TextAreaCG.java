// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/TextArea.plaf'
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
import org.wings.STextArea;
import org.wings.io.Device;

import java.io.IOException;

public class TextAreaCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.TextAreaCG {

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final STextArea component = (STextArea) _c;

        device.print("<textarea");
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.optAttribute(device, "cols", component.getColumns());
        Utils.optAttribute(device, "rows", component.getRows());

        switch (component.getLineWrap()) {
            case STextArea.VIRTUAL_WRAP:
                device.print(" wrap=\"virtual\"");
                break;
            case STextArea.PHYSICAL_WRAP:
                device.print(" wrap=\"physical\"");
                break;
        }

        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());

        if (!component.isEditable() || !component.isEnabled()) {
            device.print(" readonly=\"1\"");
        }
        if (component.isEnabled()) {
            device.print(" name=\"");
            Utils.write(device, Utils.event(component));
            device.print("\"");
        } else {
            device.print(" disabled=\"1\"");
        }

        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        Utils.writeEvents(device, component);
        device.print(">");
        Utils.writeRaw(device, component.getText());
        device.print("</textarea>\n");
    }
}
