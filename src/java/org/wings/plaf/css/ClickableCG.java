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
            org.wings.plaf.Utils.write( device, button.getURL());
            device.print("\"");
            org.wings.plaf.Utils.optAttribute( device, "tabindex", button.getFocusTraversalIndex());
            org.wings.plaf.Utils.optAttribute( device, "title", button.getToolTipText());
        }
        else {
            device.write("<a href=\"".getBytes());
            org.wings.plaf.Utils.write( device, button.getURL());
            device.write("\"".getBytes());
            org.wings.plaf.Utils.optAttribute( device, "title", button.getToolTipText());
        }

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");

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
            }.writeCompound(device, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (button.getShowAsFormComponent())
            device.print("</button>");
        else
            device.print("</a>");
    }

    protected SIcon getIcon(SClickable abstractButton) {
        if ( abstractButton.isSelected() ) {
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
