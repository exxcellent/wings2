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
import org.wings.border.SDefaultBorder;
import org.wings.border.SBorder;
import org.wings.border.SEmptyBorder;
import org.wings.plaf.css.PaddingVoodoo;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;

import java.io.IOException;

public class ButtonCG extends AbstractLabelCG implements org.wings.plaf.ButtonCG {

    /**
     * a serializable class is supposed to have this ID.
     */
    private static final long serialVersionUID = -1794530181411426283L;


    public void installCG(SComponent component) {
        super.installCG(component);
        component.setBorder(SDefaultBorder.DEFAULT);
    }

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        String text = button.getText();
        final SIcon icon = getIcon(button);

        if (icon == null && text == null)
            text = "";
        final String ftext = text;

        if (icon == null) {
            device.print("<table");
            tableAttributes(device, button);
            device.print("><tr><td");
            PaddingVoodoo.doSimpleTablePaddingWorkaround(device, button);
            device.print(">");
            writeText(device, text, false);
            device.print("</td></tr></table>");
        }
        else if (text == null) {
            device.print("<table");
            tableAttributes(device, button);
            device.print("><tr><td");
            PaddingVoodoo.doSimpleTablePaddingWorkaround(device, button);
            device.print(">");
            writeIcon(device, icon, Utils.isMSIE(component));
            device.print("</td></tr></table>");
        }
        else {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, ftext, false);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon, Utils.isMSIE(component));
                }

                protected void tableAttributes(Device d) throws IOException {
                    ButtonCG.this.tableAttributes(d, button);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition(), false);
        }
    }

    protected void tableAttributes(Device device, SAbstractButton button) throws IOException {
        Utils.printClickability(device, button, button.getToggleSelectionParameter(), button.isEnabled(), button.getShowAsFormComponent());

        final String origStyle = button.getStyle();
        updateAssignedCssClass(button);
        writeAllAttributes(device, button);
        button.setStyle(origStyle);  // recover original style.

        if (button.isFocusOwner())
            Utils.optAttribute(device, "foc", button.getName());

        Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
        Utils.optAttribute(device, "accesskey", button.getMnemonic());
    }

    /**
     * For the correct rendering of SButton we need to change the assigned CSS class according to
     * the context (is form element, is selected, etc.). This method updates the
     * CSS class via {@link org.wings.SComponent#setStyle(String)}.
     * @param button Button to update
     */
    protected void updateAssignedCssClass(SAbstractButton button) {
        final SBorder border = button.getBorder();
        final String origStyle = button.getStyle();
        final boolean hasStandardBorder = (border == SDefaultBorder.DEFAULT || border instanceof SEmptyBorder || border instanceof SDefaultBorder);
        // is this a wingS border-styled button? If yes, then we need to do some css logic
        if (origStyle != null && origStyle.contains("SButton") && hasStandardBorder) {
            // create a cleaned copy without any SButton_xxx stuff
            SStringBuilder className = new SStringBuilder(origStyle.replaceAll("SButton[a-z_A-Z]*",""));
            className.append(" SButton");
            if (button.getShowAsFormComponent())
                className.append("_form");
            if (!button.isEnabled())
                className.append("_disabled");
            if (button.isSelected())
                className.append("_selected");
            button.setStyle(className.toString());       // bad! this trigger reloads, etc.
        }
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
