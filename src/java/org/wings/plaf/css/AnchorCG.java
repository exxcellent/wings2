/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.io.Device;

import java.io.IOException;

public class AnchorCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.AnchorCG {
    public void writeContent(final Device device,
                             final SComponent _c)
        throws IOException {
        final SAnchor anchor = (SAnchor)_c;

        device.write("<a href=\"".getBytes());
        device.print(anchor.getURL());
        device.print("\"");

        if (anchor.getPreferredSize() != null)
            device.print(" style=\"width:100%; height: 100%\"");

        org.wings.plaf.Utils.optAttribute(device, "target", anchor.getTarget());
        org.wings.plaf.Utils.optAttribute(device, "name", anchor.getName());
        org.wings.plaf.Utils.optAttribute(device, "tabindex", anchor.getFocusTraversalIndex());
        Utils.writeEvents(device, anchor);
        device.print(">");
        Utils.renderContainer(device, anchor);
        device.print("</a>");
    }
}
