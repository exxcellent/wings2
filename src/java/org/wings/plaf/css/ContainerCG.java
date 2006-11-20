/*
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


import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SLayoutManager;
import org.wings.STemplateLayout;
import org.wings.io.Device;

public class ContainerCG extends AbstractComponentCG implements org.wings.plaf.PanelCG {
    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component) throws java.io.IOException {
        final SContainer container = (SContainer) component;
        final SLayoutManager manager = container.getLayout();

        BorderCG.writeComponentBorderPrefix(device, component);

        device.print("<table");
        writeAllAttributes(device, component);
        Utils.writeEvents(device, component, null);
        device.print(">");

        // special case templateLayout and card layout. We open TABLE cell for them.
        final boolean writeTableData = manager instanceof STemplateLayout || manager instanceof SCardLayout;
        if (writeTableData) {
            device.print("<tr><td>");
        }

        Utils.renderContainer(device, container);

        if (writeTableData) {
            device.print("</td></tr>");
        }

        device.print("</table>");

        BorderCG.writeComponentBorderSufix(device, component);
    }
}
