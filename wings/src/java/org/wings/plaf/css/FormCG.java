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


import org.wings.*;
import org.wings.io.Device;
import java.io.IOException;

public class FormCG extends AbstractComponentCG implements org.wings.plaf.FormCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component) throws IOException {
        final SForm container = (SForm) component;
        SLayoutManager layout = container.getLayout();

        device.print("<form method=\"");
        if (container.isPostMethod()) {
            device.print("post");
        } else {
            device.print("get");
        }
        device.print("\"");
        writeAllAttributes(device, container);
        Utils.optAttribute(device, "name", container.getName());
        Utils.optAttribute(device, "enctype", container.getEncodingType());
        Utils.optAttribute(device, "action", container.getRequestURL());
        Utils.writeEvents(device, container, null);

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
        final String defaultButtonName = container.getDefaultButton() != null ? Utils.event(container.getDefaultButton()) : "capture_enter";
        device.print("><input type=\"image\" name=\"").print(defaultButtonName).print("\" border=\"0\" ");
        Utils.optAttribute(device, "src", getBlindIcon().getURL());
        device.print(" width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;margin:0px;position:absolute\"/>");

        // Not sure: Think this was for optionally expiring old GET views?!
        device.print("<input type=\"hidden\" name=\"");
        Utils.write(device, Utils.event(container));
        device.print("\" value=\"");
        Utils.write(device, container.getName());
        device.print(SConstants.UID_DIVIDER);
        device.print("\" />");

        SDimension preferredSize = container.getPreferredSize();
        String height = preferredSize != null ? preferredSize.getHeight() : null;
        boolean clientLayout = isMSIE(container) && height != null && !"auto".equals(height)
            && (layout instanceof SBorderLayout || layout instanceof SGridBagLayout);

        String tableName = container.getName() + "_table";
        device.print("<table id=\"");
        device.print(tableName);
        device.print("\"");

        if (clientLayout) {
            device.print(" style=\"width:100%\"");
            Utils.optAttribute(device, "layoutHeight", height);
            container.getSession().getScriptManager().addScriptListener(new LayoutFillScript(tableName));
        }
        else
            Utils.printCSSInlineFullSize(device, container.getPreferredSize());

        device.print(">");

        // Render the container itself
        Utils.renderContainer(device, container);

        device.print("</table>");

        // Enter capture at end of form
        device.print("<input type=\"image\" name=\"").print(defaultButtonName).print("\" border=\"0\" ");
        Utils.optAttribute(device, "src", getBlindIcon().getURL());
        device.print(" width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;margin:0px;position:absolute\"/>");

        device.print("</form>");
    }
}
