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


import org.wings.SAbstractButton;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;

import java.io.IOException;

public class ButtonCG extends AbstractLabelCG implements org.wings.plaf.ButtonCG {

    /**
     * a serializable class is supposed to have this ID.
     */
    private static final long serialVersionUID = -1794530181411426283L;

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        // may be ommitted if no width, border and background table
        writeTablePrefix(device, button);

        String cssClass;
        if (button.isEnabled())
            cssClass = button.getShowAsFormComponent() ? "formbutton" : "linkbutton";
        else
            cssClass = button.getShowAsFormComponent() ? "disabled_formbutton" : "disabled_linkbutton";

        Utils.printButtonStart(device, button, button.getToggleSelectionParameter(), button.isEnabled(), button.getShowAsFormComponent(), cssClass);
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
        final SIcon icon = getIcon(button);

        if (icon == null && text != null)
            writeText(device, text, false);
        else if (icon != null && text == null)
            writeIcon(device, icon, Utils.isMSIE(component));
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, false);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon, Utils.isMSIE(component));
                }

                protected void tableAttributes(Device d) throws IOException {
                    Utils.printCSSInlineFullSize(d, button.getPreferredSize());
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition(), false);
        }

        Utils.printButtonEnd(device, button, button.getToggleSelectionParameter(), button.isEnabled());

        writeTableSuffix(device, component);
    }

    /* Retrieve according icon for a button. */
    public static SIcon getIcon(SAbstractButton abstractButton) {
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
