// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Separator.plaf'
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
import org.wings.SSeparator;
import org.wings.io.Device;

import java.io.IOException;

public class SeparatorCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.SeparatorCG {

//--- byte array converted template snippets.
    private final static byte[] __hr = "<hr".getBytes();
    private final static byte[] __align_right = " align=\"right\"".getBytes();
    private final static byte[] __align_center = " align=\"center\"".getBytes();
    private final static byte[] __align_justify = " align=\"justify\"".getBytes();
    private final static byte[] __noshade_1 = " noshade=\"1\"".getBytes();
    private final static byte[] __ = "/>".getBytes();
    private final static byte[] ___1 = "\n".getBytes();

    public void installCG(final SComponent comp) {
    }

    public void uninstallCG(final SComponent comp) {
    }


    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SSeparator component = (SSeparator) _c;

//--- code from write-template.

        device.write(__hr);
        org.wings.plaf.Utils.optAttribute(device, "class", component.getStyle());
        org.wings.plaf.Utils.optAttribute(device, "width", component.getWidth());
        org.wings.plaf.Utils.optAttribute(device, "size", component.getSize());

        switch (component.getAlignment()) {
            case SSeparator.RIGHT_ALIGN:
                device.write(__align_right);
                break;
            case SSeparator.CENTER_ALIGN:
                device.write(__align_center);
                break;
            case SSeparator.BLOCK_ALIGN:
                device.write(__align_justify);
                break;
        }
        ;
        if (!component.getShade()) {
            device.write(__noshade_1);
        }
        device.write(__);
        device.write(___1);

//--- end code from write-template.
    }
}
