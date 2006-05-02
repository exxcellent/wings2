/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.Utils;
import org.wings.io.Device;

import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public class BirdsNestCG
        extends AbstractComponentCG
{
    public void installCG(SComponent component) {
        component.setStyle("BirdsNest");
    }

    public void write(final Device device, final SComponent _c)
            throws IOException
    {
        final BirdsNest component = (BirdsNest) _c;
        BirdsNest desktop = (BirdsNest) component;
        // is one window maximized? if yes, skip rendering of other windows
        boolean maximized = false;

        device.print("<div class=\"spacer\">&nbsp;</div>");
        int componentCount = desktop.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            Bird frame = (Bird) desktop.getComponent(i);
            if (!frame.isClosed() && frame.isMaximized()) {
                frame.write(device);
                maximized = true;
            }
        }

        if (!maximized) {
            for (int i = 0; i < componentCount; i++) {
                Bird frame = (Bird) desktop.getComponent(i);
                if (!frame.isClosed()) {
                    frame.write(device);
                }
            }
        }
        device.print("<div class=\"spacer\">&nbsp;</div>");
    }
}
