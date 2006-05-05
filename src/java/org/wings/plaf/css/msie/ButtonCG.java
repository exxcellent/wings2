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
package org.wings.plaf.css.msie;

import org.wings.SAbstractButton;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.io.Device;
import org.wings.plaf.css.AbstractLabelCG;
import org.wings.plaf.css.IconTextCompound;
import org.wings.plaf.css.Utils;
import org.wings.util.SStringBuilder;

import java.io.IOException;

public final class ButtonCG extends AbstractLabelCG {

    /**
     * a serializable class is supposed to have this ID.
     */
    private static final long serialVersionUID = -1794530181411426283L;

    public void writeInternal(final Device device, final SComponent component) throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        writeTablePrefix(device, component);

        Utils.printButtonStart(device, button, button.getToggleSelectionParameter());
        Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
        Utils.optAttribute(device, "accesskey", button.getMnemonic());
        Utils.printCSSInlineFullSize(device, component.getPreferredSize());

        // use class attribute instead of single attributes for IE compatibility
        final SStringBuilder className = new SStringBuilder();
        if (!button.isEnabled()) {
            className.append(component.getStyle());
            className.append("_disabled ");
        }
        if (button.isSelected()) {
            className.append(component.getStyle());
            className.append("_selected ");
        }
        Utils.optAttribute(device, "class", className);

        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        device.print(">");

        final String text = button.getText();
        final SIcon icon = org.wings.plaf.css.ButtonCG.getIcon(button);

        if (icon == null && text != null) {
            writeText(device, text, false);
        } else if (icon != null && text == null)
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

        Utils.printButtonEnd(device, button, button.getToggleSelectionParameter(), button.isEnabled());

        writeTableSuffix(device, component);
    }
}
