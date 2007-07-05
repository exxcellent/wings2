/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;


import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.io.Device;
import org.wings.script.JavaScriptEvent;

import java.io.IOException;

public final class AnchorCG extends AbstractComponentCG implements org.wings.plaf.AnchorCG {
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see org.wings.plaf.css.AbstractComponentCG#writeContent(org.wings.io.Device, org.wings.SComponent)
     */
    public void writeInternal(final Device device,
                      final SComponent _c)
            throws IOException {
        final SAnchor component = (SAnchor) _c;
        /*final boolean useTable = hasDimension(component);
        if (useTable) {
            writeTablePrefix(device, component);
            // render javascript event handlers
            Utils.writeEvents(device, component, null);
        }
        Utils.printButtonStart(device, component, null, true, component.getShowAsFormComponent());
        if (!useTable) {
            writeAllAttributes(device, component);
            // render javascript event handlers
            Utils.writeEvents(device, component, null);
        }
        */

        final String target = component.getTarget() != null ? "'" + component.getTarget() + "'" : "null";
        device.print("<table onclick=\"wingS.util.openLink(" + target + ",'" + component.getURL() + "'" +
                Utils.collectJavaScriptListenerCode(component, JavaScriptEvent.ON_CLICK) + "); return false;\"");
        writeAllAttributes(device, component);
        if (component.isFocusOwner())
            Utils.optAttribute(device, "foc", component.getName());
        //Utils.optAttribute(device, "target", component.getTarget());
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        device.print(">");
        Utils.renderContainer(device, component);
        device.print("</table>");
    }
}
