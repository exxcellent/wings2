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

public class ButtonCG
        extends LabelCG
        implements SConstants, org.wings.plaf.ButtonCG {

    private void writeDynamicIcons(final Device device, SAbstractButton abstractButton, SIcon origIcon,
                                   String iconName, boolean renderNameAttribute)
            throws IOException {
        if (abstractButton.isEnabled() &&
                (abstractButton.getGroup() == null || !abstractButton.isSelected())) {
            // render rollover
            SIcon rolloverIcon = abstractButton.getRolloverIcon();
            SIcon pressedIcon = abstractButton.getPressedIcon();

            if (rolloverIcon != null || pressedIcon != null) {
                if (renderNameAttribute) {

                    device.print(" name=\"");
                    Utils.write(device, iconName);

                    device.print("\"");
                } // end of if ()


                if (rolloverIcon != null) {

                    device.print(" onMouseover=\"if(document.images){this.src='");
                    Utils.write(device, rolloverIcon.getURL());

                    device.print("';}\" onmouseout=\"if(document.images){this.src='");
                    Utils.write(device, origIcon.getURL());

                    device.print("';}\"");
                }

                if (pressedIcon != null) {

                    device.print(" onMousedown=\"if(document.images){this.src='");
                    Utils.write(device, pressedIcon.getURL());

                    device.print("';}\" onmouseup=\"if(document.images){this.src='");
                    Utils.write(device, rolloverIcon != null ? rolloverIcon.getURL() : origIcon.getURL());

                    device.print("';}\"");
                }
            }
        }

    }

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        if (button.getShowAsFormComponent()) {
            device.print("<button name=\"");
            Utils.write(device, Utils.event(button));
            device.print("\"");
            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        } else {
            device.print("<a href=\"");
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            addr.write(device);
            device.print("\"");

            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }
        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        Utils.writeEvents(device, button);
        device.print(">");

        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (icon == null && text != null)
            writeText(device, text);
        else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (button.getShowAsFormComponent())
            device.print("</button>");
        else
            device.print("</a>");
    }

    protected SIcon getIcon(SAbstractButton abstractButton) {
        if (abstractButton.isSelected()) {
            return abstractButton.isEnabled()
                    ? abstractButton.getSelectedIcon()
                    : abstractButton.getDisabledSelectedIcon();
        } else {
            return abstractButton.isEnabled()
                    ? abstractButton.getIcon()
                    : abstractButton.getDisabledIcon();
        }
    }
}
