// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/TextField.plaf'
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
import org.wings.SFormattedTextField;
import org.wings.STextField;
import org.wings.io.Device;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.io.IOException;

public class TextFieldCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.TextFieldCG {

    private static final JavaScriptListener submitListener = new JavaScriptListener(JavaScriptEvent.ON_CHANGE, "submit()");

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final STextField component = (STextField) _c;

        device.print("<input type=\"text\"");
        Utils.optAttribute(device, "size", component.getColumns());
        Utils.optAttribute(device, "maxlength", component.getMaxColumns());

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
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());

        if (!(component instanceof SFormattedTextField)) {
            component.removeScriptListener(submitListener);
            if (component.getActionListeners().length > 0) {
                component.addScriptListener(submitListener);
            }
        }
        
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        Utils.writeEvents(device, component);

        Utils.optAttribute(device, "value", component.getText());
        device.print("/>");
    }
}
