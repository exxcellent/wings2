/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;

import java.io.IOException;

public class ClickableCG
    extends LabelCG
    implements SConstants, org.wings.plaf.ButtonCG {

    public void writeContent(final Device device, final SComponent component)
        throws IOException {
        final SClickable button = (SClickable)component;

        if (button.getShowAsFormComponent()) {
            device.print("<button type=\"submit\" name=\"");
            Utils.write(device, button.getEventTarget().getEncodedLowLevelEventId());
            device.print("\" value=\"");
            Utils.write(device, button.getEvent());
            device.print("\"");
            org.wings.plaf.Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
        }
        else {
            device.write("<a href=\"".getBytes());
            org.wings.plaf.Utils.write(device, button.getURL());
            device.write("\"".getBytes());
        }

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");

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

    protected SIcon getIcon(SClickable abstractButton) {
        if (abstractButton.isSelected()) {
            return abstractButton.isEnabled()
                ? abstractButton.getSelectedIcon()
                : abstractButton.getDisabledSelectedIcon();
        }
        else {
            return abstractButton.isEnabled()
                ? abstractButton.getIcon()
                : abstractButton.getDisabledIcon();
        }
    }
}
