// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/ProgressBar.plaf'
/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.CGManager;

import java.io.IOException;

public class ProgressBarCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.ProgressBarCG {

//--- byte array converted template snippets.
    private final static byte[] __table_border_0 = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>".getBytes();
    private final static byte[] __table_border_0_1 = "<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\"><tr><td".getBytes();
    private final static byte[] __ = ">".getBytes();
    private final static byte[] __table_border_0_2 = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">".getBytes();
    private final static byte[] __tr = "<tr>".getBytes();
    private final static byte[] __td = "<td".getBytes();
    private final static byte[] __img = "<img".getBytes();
    private final static byte[] __width = " width=\"".getBytes();
    private final static byte[] ___1 = "\"".getBytes();
    private final static byte[] __height = " height=\"".getBytes();
    private final static byte[] __td_1 = "\"></td>".getBytes();
    private final static byte[] ___2 = "\">".getBytes();
    private final static byte[] __td_2 = "</td>".getBytes();
    private final static byte[] __tr_1 = "</tr>".getBytes();
    private final static byte[] __table = "</table>".getBytes();
    private final static byte[] __td_tr_table = "</td></tr></table>".getBytes();
    private final static byte[] __td_tr_tr_td_al = "</td></tr><tr><td align=\"center\">".getBytes();
    private final static byte[] __span_class = "<span class=\"".getBytes();
    private final static byte[] __span = "</span>".getBytes();

    public void installCG(final SComponent comp) {
        final SProgressBar component = (SProgressBar) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        Object previous;
        value = manager.getObject("SProgressBar.borderColor", java.awt.Color.class);
        if (value != null) {
            component.setBorderColor((java.awt.Color) value);
        }
        value = manager.getObject("SProgressBar.filledColor", java.awt.Color.class);
        if (value != null) {
            component.setFilledColor((java.awt.Color) value);
        }
        value = manager.getObject("SProgressBar.foreground", java.awt.Color.class);
        if (value != null) {
            component.setForeground((java.awt.Color) value);
        }
        value = manager.getObject("SProgressBar.preferredSize", SDimension.class);
        if (value != null) {
            component.setPreferredSize((SDimension) value);
        }
        value = manager.getObject("SProgressBar.unfilledColor", java.awt.Color.class);
        if (value != null) {
            component.setUnfilledColor((java.awt.Color) value);
        }
    }

    public void uninstallCG(final SComponent comp) {
    }

//--- code from common area in template.
    private static final SIcon BLIND_ICON = new SResourceIcon("org/wings/icons/blind.gif");


//--- end code from common area in template.


    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SProgressBar component = (SProgressBar) _c;

//--- code from write-template.
        String style = component.getStyle();

        SDimension size = component.getPreferredSize();
        int width = size != null ? size.getIntWidth() : 200;
        int height = size != null ? size.getIntHeight() : 5;

        if (component.isStringPainted()) {
            device.write(__table_border_0);
        }
        if (component.isBorderPainted()) {
            device.write(__table_border_0_1);
            org.wings.plaf.Utils.optAttribute(device, "bgcolor", component.getBorderColor());
            device.write(__);
        }

        device.write(__table_border_0_2);
        device.write(__tr);
        device.write(__td);
        org.wings.plaf.Utils.optAttribute(device, "bgcolor", component.getFilledColor());
        device.write(__);
        device.write(__img);
        org.wings.plaf.Utils.optAttribute(device, "src", BLIND_ICON.getURL());
        device.write(__width);
        org.wings.plaf.Utils.write(device, (int) (width * component.getPercentComplete()));
        device.write(___1);
        device.write(__height);
        org.wings.plaf.Utils.write(device, height);
        device.write(__td_1);
        device.write(__td);
        org.wings.plaf.Utils.optAttribute(device, "bgcolor", component.getUnfilledColor());
        device.write(__);
        device.write(__img);
        org.wings.plaf.Utils.optAttribute(device, "src", BLIND_ICON.getURL());
        device.write(__width);
        org.wings.plaf.Utils.write(device, (int) (width * (1 - component.getPercentComplete())));
        device.write(___1);
        device.write(__height);
        org.wings.plaf.Utils.write(device, height);
        device.write(___2);
        device.write(__td_2);
        device.write(__tr_1);
        device.write(__table);
        if (component.isBorderPainted()) {
            device.write(__td_tr_table);
        }

        if (component.isStringPainted()) {
            device.write(__td_tr_tr_td_al);
            if (style != null) {
                device.write(__span_class);
                org.wings.plaf.Utils.write(device, style);
                device.write(___2);
            }
            org.wings.plaf.Utils.write(device, component.getString());
            if (style != null) {
                device.write(__span);
            }
            device.write(__td_tr_table);
        }
//--- end code from write-template.
    }
}
