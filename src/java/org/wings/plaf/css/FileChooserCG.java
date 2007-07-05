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


import org.wings.SComponent;
import org.wings.SFileChooser;
import org.wings.io.Device;

import java.io.IOException;

public final class FileChooserCG extends AbstractComponentCG implements
        org.wings.plaf.FileChooserCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device,
                      final SComponent _c)
            throws IOException {
        final SFileChooser component = (SFileChooser) _c;

        int columns = component.getColumns();
        /*
        * for some wierd reason, the 'maxlength' column contains the
        * maximum content length .. see RFC1867.
        * .. anyway, all browsers seem to ignore it or worse, render the
        * file upload unusable (konqueror 2.2.2).
        */
        //int maxContent = component.getSession().getMaxContentLength()*1024;

        // maxLength = maxContent removed, since it does not work.
        writeTablePrefix(device, component);
        device.print("<input type=\"file\"");
        //writeAllAttributes(device, component);
        Utils.optAttribute(device, "size", columns);
        Utils.optAttribute(device, "accept", component.getFileNameFilter());
        Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        Utils.optFullSize(device, component);
        Utils.writeEvents(device, component, null);
        if (component.isFocusOwner())
            Utils.optAttribute(device, "foc", component.getName());

        if (component.isEnabled()) {
            device.print(" name=\"");
            Utils.write(device, Utils.event(component));
            device.print("\"");
        }
        else
            device.print(" readonly=\"true\"");

        device.print("/>");
        writeTableSuffix(device, component);
        
    }
}
