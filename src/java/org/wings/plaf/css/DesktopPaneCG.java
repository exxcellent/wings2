// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/DesktopPane.plaf'
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
import org.wings.SDesktopPane;
import org.wings.SInternalFrame;
import org.wings.io.Device;
import org.wings.plaf.CGManager;

import java.io.IOException;

public class DesktopPaneCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.DesktopPaneCG {

//--- byte array converted template snippets.
    private final static byte[] __table_cellpadd = "<table cellpadding=\"0\" cellspacing=\"7\" border=\"0\" width=\"100%\"".getBytes();
    private final static byte[] __ = ">".getBytes();
    private final static byte[] __tr_td = "<tr><td>".getBytes();
    private final static byte[] __td_tr_table = "</td></tr></table>".getBytes();
    private final static byte[] __td_tr = "</td></tr>".getBytes();
    private final static byte[] __table = "</table>".getBytes();
    private final static byte[] ___1 = "\n".getBytes();

    public void installCG(final SComponent comp) {
        final SDesktopPane component = (SDesktopPane) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SDesktopPane.style", String.class);
        if (value != null) {
            component.setStyle((String) value);
        }
    }

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SDesktopPane component = (SDesktopPane) _c;

//--- code from write-template.
        SDesktopPane desktop = (SDesktopPane) component;
        device.write(__table_cellpadd);
        org.wings.plaf.Utils.optAttribute(device, "class", component.getStyle());
        device.write(__);
        int componentCount = desktop.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
            if (!frame.isClosed() && frame.isMaximized()) {
                device.write(__tr_td);
                frame.write(device);
                device.write(__td_tr_table);
                return;
            }
        }

        for (int i = 0; i < componentCount; i++) {
            SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
            if (!frame.isClosed()) {
                device.write(__tr_td);
                frame.write(device);
                device.write(__td_tr);
            }
        }
        device.write(__table);
        device.write(___1);

//--- end code from write-template.
    }
}
