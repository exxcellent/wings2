package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SToolbar;
import org.wings.io.Device;

import java.io.IOException;

public class ToolbarCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.ToolbarCG {
    public void writeContent(final Device device, final SComponent _c)
        throws IOException {
        final SToolbar toolbar = (SToolbar)_c;

        device.print("<table><tr>");
        SComponent[] components = toolbar.getComponents();
        for (int i = 0; i < components.length; i++) {
            SComponent component = components[i];

            if (component.getHorizontalAlignment() == SConstants.RIGHT_ALIGN)
                device.print("<td width=\"100%\"></td>");

            device.print("<td>");
            component.write(device);
            device.print("</td>");
        }
        device.print("</tr></table>");
    }
}
