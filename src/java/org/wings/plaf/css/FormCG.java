// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Form.plaf'
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


import org.wings.*;
import org.wings.io.Device;

import java.io.IOException;

public class FormCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.FormCG {

//--- byte array converted template snippets.
    private final static byte[] __form_method = "<form method=\"".getBytes();
    private final static byte[] __post = "post".getBytes();
    private final static byte[] __get = "get".getBytes();
    private final static byte[] __ = "\"".getBytes();
    private final static byte[] __name = " name=\"".getBytes();
    private final static byte[] __input_type_ima = "><input type=\"image\" name=\"_capture_enter1\" border=\"0\" ".getBytes();
    private final static byte[] __width_0_height = " width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;;margin:0pxposition:absolute\"/>".getBytes();
    private final static byte[] __input_type_hid = "<input type=\"hidden\" name=\"".getBytes();
    private final static byte[] __value = "\" value=\"".getBytes();
    private final static byte[] ___1 = "\" />".getBytes();
    private final static byte[] __input_type_ima_1 = "<input type=\"image\" name=\"_capture_enter2\" border=\"0\" ".getBytes();
    private final static byte[] __form = "</form>".getBytes();
    private final static byte[] ___2 = "\n".getBytes();

//--- code from common area in template.
    private static final SIcon BLIND_ICON = new SResourceIcon("org/wings/icons/blind.gif");

    /*
    * we render two icons into the page that captures pressing simple 'return'
    * in the page. Why ? Depending on the Browser, the Browser sends the 
    * first or the last submit-button it finds in the page as 'default'-Submit
    * when we simply press 'return' somewhere.
    *
    * However, we don't want to have this arbitrary behaviour in wingS.
    * So we add these two (invisible image-) submit-Buttons, either of it
    * gets triggered on simple 'return'. No real wingS-Button will then be
    * triggered but only the ActionListener added to the SForm. So we have 
    * a way to distinguish between Forms that have been sent as default and
    * pressed buttons.
    *
    * Watchout: the style of these images once had been changed to display:none;
    * to prevent taking some pixel renderspace. However, display:none; made
    * the Internet Explorer not accept this as an input getting the default-focus,
    * so it fell back to the old behaviour. So changed that style to no-padding,
    * no-margin, no-whatever (HZ).
    */

//--- end code from common area in template.


    protected void writeContent(final Device device,
                                final SComponent _c)
            throws IOException {
        final SForm component = (SForm) _c;

//--- code from write-template.

        device.write(__form_method);
        if (component.isPostMethod()) {
            device.write(__post);
        } else {
            device.write(__get);
        }
        device.write(__);
        device.write(__name);
        org.wings.plaf.Utils.write(device, component.getName());
        device.write(__);
        org.wings.plaf.css.Utils.writeEvents(device, component);
        org.wings.plaf.Utils.optAttribute(device, "class", component.getStyle());
        org.wings.plaf.Utils.optAttribute(device, "enctype", component.getEncodingType());
        org.wings.plaf.Utils.optAttribute(device, "action", component.getRequestURL());

        if (component.getPreferredSize() != null)
            device.print(" style=\"width:100%; height: 100%\"");

        device.write(__input_type_ima);
        org.wings.plaf.Utils.optAttribute(device, "src", BLIND_ICON.getURL());
        device.write(__width_0_height);
        device.write(__input_type_hid);
        org.wings.plaf.Utils.write(device, Utils.event(component));
        device.write(__value);
        org.wings.plaf.Utils.write(device, component.getName());
        org.wings.plaf.Utils.write(device, SConstants.UID_DIVIDER);
        device.write(___1);
        Utils.renderContainer(device, component);
        device.write(__input_type_ima_1);
        org.wings.plaf.Utils.optAttribute(device, "src", BLIND_ICON.getURL());
        device.write(__width_0_height);
        device.write(__form);
        device.write(___2);

//--- end code from write-template.
    }
}
