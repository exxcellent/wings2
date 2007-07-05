/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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
import org.wings.plaf.Update;

import java.io.IOException;

public final class ClickableCG extends AbstractLabelCG implements org.wings.plaf.ClickableCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SClickable button = (SClickable) component;

        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (icon == null && text != null) {
            device.print("<table");
            tableAttributes(device, button);
            device.print("><tr><td>");
            writeText(device, text, false);
            device.print("</td></tr></table>");
        }
        else if (icon != null && text == null) {
            device.print("<table");
            tableAttributes(device, button);
            device.print("><tr><td>");
            writeIcon(device, icon, Utils.isMSIE(component));
            device.print("</td></tr></table>");
        }
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, false);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon, Utils.isMSIE(component));
                }

                protected void tableAttributes(Device d) throws IOException {
                    ClickableCG.this.tableAttributes(d, button);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition(), false);
        }
    }

    protected void tableAttributes(Device device, SClickable button) throws IOException {
        Utils.printClickability(device, button, button.getEvent(), true, button.getShowAsFormComponent());
        writeAllAttributes(device, button);

        if (button.isFocusOwner())
            Utils.optAttribute(device, "foc", button.getName());

        Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
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

    public Update getTextUpdate(SClickable clickable, String text) {
        return text == null ? null : new TextUpdate(clickable, text);
    }

    public Update getIconUpdate(SClickable clickable, SIcon icon) {
        return icon == null ? null : new IconUpdate(clickable, icon);
    }

}
