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
        final SAnchor component = (SAnchor)_c;

        device.write("<a href=\"".getBytes());
        device.print(component.getURL());
        device.print("\"");

        Utils.innerPreferredSize(device, component.getPreferredSize());

        org.wings.plaf.Utils.optAttribute(device, "target", component.getTarget());
        org.wings.plaf.Utils.optAttribute(device, "name", component.getName());
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.writeEvents(device, component);
        device.print(">");
        Utils.renderContainer(device, component);
        device.print("</a>");
    }
}
