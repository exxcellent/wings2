// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/PasswordField.plaf'
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
import org.wings.SPasswordField;
import org.wings.io.Device;

import java.io.IOException;

public class PasswordFieldCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.PasswordFieldCG {

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SPasswordField component = (SPasswordField) _c;

        device.print("<input type=\"password\"");
        org.wings.plaf.css.Utils.writeEvents(device, component);
        org.wings.plaf.Utils.optAttribute(device, "size", component.getColumns());
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute(device, "maxlength", component.getMaxColumns());
        org.wings.plaf.Utils.optAttribute(device, "focus", component.getName());

        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());

        if (!component.isEditable() || !component.isEnabled()) {
            device.print(" readonly=\"1\"");
        }
        if (component.isEnabled()) {
            device.print(" name=\"");
            org.wings.plaf.Utils.write(device, Utils.event(component));
            device.print("\"");
        } else {
            device.print(" disabled=\"1\"");
        }

        if (component.isFocusOwner())
            org.wings.plaf.Utils.optAttribute(device, "focus", component.getName());

        Utils.writeEvents(device, component);

        org.wings.plaf.Utils.optAttribute(device, "value", component.getText());
        device.print("/>");
    }
}
