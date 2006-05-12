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
import org.wings.SPasswordField;
import org.wings.io.Device;

import java.io.IOException;

public final class PasswordFieldCG extends AbstractComponentCG implements
        org.wings.plaf.PasswordFieldCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device,
                      final SComponent _c)
            throws IOException {
        final SPasswordField component = (SPasswordField) _c;

        device.print("<input type=\"password\"");
        writeAllAttributes(device, component);
        Utils.optAttribute(device, "size", component.getColumns());
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.optAttribute(device, "maxlength", component.getMaxColumns());
        Utils.writeEvents(device, component, null);
        if (component.isFocusOwner())
            Utils.optAttribute(device, "foc", component.getName());


        if (!component.isEditable() || !component.isEnabled()) {
            device.print(" readonly=\"true\"");
        }
        if (component.isEnabled()) {
            device.print(" name=\"");
            Utils.write(device, Utils.event(component));
            device.print("\"");
        } else {
            device.print(" disabled=\"true\"");
        }

        Utils.optAttribute(device, "value", component.getText());
        device.print("/>");
    }
}
