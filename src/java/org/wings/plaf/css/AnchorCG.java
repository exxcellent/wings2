/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;
import org.wings.util.AnchorRenderStack;

import java.io.IOException;

public class AnchorCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.AnchorCG {

//--- byte array converted template snippets.
    private final static byte[] __span          = "<span".getBytes();
    private final static byte[] __              = ">".getBytes();
    private final static byte[] __span_1        = "</span>".getBytes();

    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SAnchor component = (SAnchor) _c;

//--- code from write-template.
        String style = org.wings.plaf.css.Utils.style(component);
        if (style != null || component.getScriptListeners().length != 0) {
            device.write(__span);            org.wings.plaf.Utils.optAttribute( device, "class", style);            Utils.writeEvents(device, component);
            device.write(__);
        }

        try {
            AnchorRenderStack.push(component.getURL(), component.getTarget());
            Utils.renderContainer(device, component);
        }
        finally {
            AnchorRenderStack.pop();
        }

        if (style != null || component.getScriptListeners().length != 0) {
            device.write(__span_1);
        }
//--- end code from write-template.
    }
}
