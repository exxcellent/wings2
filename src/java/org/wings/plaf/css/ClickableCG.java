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


import org.wings.SClickable;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.io.Device;

import java.io.IOException;

public final class ClickableCG extends AbstractLabelCG implements org.wings.plaf.ButtonCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SClickable button = (SClickable) component;

        Utils.printButtonStart(device, button, button.getEvent(), true, button.getShowAsFormComponent());
        writeAllAttributes(device, button);
        // render javascript event handlers
        Utils.writeEvents(device, component, Utils.EXCLUDE_ON_CLICK );
        Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
        device.print(">");

        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (icon == null && text != null)
            writeText(device, text, false);
        else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, false);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        Utils.printButtonEnd(device, button, button.getEvent(), true);
    }

    protected SIcon getIcon(SClickable abstractButton) {
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
