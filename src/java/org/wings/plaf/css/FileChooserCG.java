// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/FileChooser.plaf'
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
import org.wings.SFileChooser;
import org.wings.io.Device;

import java.io.IOException;

public class FileChooserCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.FileChooserCG {

//--- byte array converted template snippets.
    private final static byte[] __input_type_fil = "<input type=\"file\"".getBytes();
    private final static byte[] __name = " name=\"".getBytes();
    private final static byte[] __ = "\"".getBytes();
    private final static byte[] __id = " id=\"".getBytes();
    private final static byte[] __readonly_1 = " readonly=\"1\"".getBytes();
    private final static byte[] ___2 = "\n".getBytes();

    public void writeContent(final Device device,
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
        device.write(__input_type_fil);

        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());

        org.wings.plaf.Utils.optAttribute(device, "size", columns);
        org.wings.plaf.Utils.optAttribute(device, "accept", component.getFileNameFilter());

        if (component.isEnabled()) {
            device.write(__name);
            org.wings.plaf.Utils.write(device, Utils.event(component));
            device.write(__);
            device.write(__id);
            org.wings.plaf.Utils.write(device, component.getName());
            device.write(__);
        } else
            device.write(__readonly_1);

        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());

        Utils.writeEvents(device, component);
        device.write("/>".getBytes());
    }
}
