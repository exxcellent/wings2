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

//--- byte array converted template snippets.
    private final static byte[] __input_type_tex = "<input type=\"text\"".getBytes();
    private final static byte[] __readonly_1 = " readonly=\"1\"".getBytes();
    private final static byte[] __name = " name=\"".getBytes();
    private final static byte[] __ = "\"".getBytes();
    private final static byte[] __id = " id=\"".getBytes();
    private final static byte[] __disabled_1 = " disabled=\"1\"".getBytes();
    private final static byte[] ___1 = "/>".getBytes();

//--- code from common area in template.
    private static final JavaScriptListener submitListener = new JavaScriptListener(JavaScriptEvent.ON_CHANGE, "submit()");


//--- end code from common area in template.


    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final STextField component = (STextField) _c;

        device.write(__input_type_tex);
        org.wings.plaf.Utils.optAttribute(device, "size", component.getColumns());
        org.wings.plaf.Utils.optAttribute(device, "maxlength", component.getMaxColumns());
        org.wings.plaf.Utils.optAttribute(device, "focus", component.getName());

        if (!component.isEditable() || !component.isEnabled()) {
            device.write(__readonly_1);
        }
        if (component.isEnabled()) {
            device.write(__name);
            org.wings.plaf.Utils.write(device, Utils.event(component));
            device.write(__);
        } else {
            device.write(__disabled_1);
        }
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());

        if (!(component instanceof SFormattedTextField)) {
            component.removeScriptListener(submitListener);
            if (component.getActionListeners().length > 0) {
                component.addScriptListener(submitListener);
            }
        }
        Utils.innerPreferredSize(device, component.getPreferredSize());
        Utils.writeEvents(device, component);

        org.wings.plaf.Utils.optAttribute(device, "value", component.getText());
        device.write(___1);
    }
}
