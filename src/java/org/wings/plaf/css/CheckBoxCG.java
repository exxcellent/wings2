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

public class CheckBoxCG        extends ButtonCG        implements SConstants, org.wings.plaf.CheckBoxCG {
    private boolean useIconsInForm = false;

    public boolean isUseIconsInForm() {
        return useIconsInForm;
    }

    public void setUseIconsInForm(boolean useIconsInForm) {
        this.useIconsInForm = useIconsInForm;
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        final SAbstractButton button = (SAbstractButton) component;
        installIcons(button);
    }

    protected void installIcons(final SAbstractButton button) {
        org.wings.plaf.CGManager manager = button.getSession().getCGManager();
        button.setIcon((SIcon) manager.getObject("SCheckBox.icon", SIcon.class));
        button.setSelectedIcon((SIcon) manager.getObject("SCheckBox.selectedIcon", SIcon.class));
        button.setRolloverIcon((SIcon) manager.getObject("SCheckBox.rolloverIcon", SIcon.class));
        button.setRolloverSelectedIcon((SIcon) manager.getObject("SCheckBox.rolloverSelectedIcon", SIcon.class));
        button.setPressedIcon((SIcon) manager.getObject("SCheckBox.pressedIcon", SIcon.class));
        button.setDisabledIcon((SIcon) manager.getObject("SCheckBox.disabledIcon", SIcon.class));
        button.setDisabledSelectedIcon((SIcon) manager.getObject("SCheckBox.disabledSelectedIcon", SIcon.class));
    }

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        final boolean showAsFormComponent = button.getShowAsFormComponent();
        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (showAsFormComponent && useIconsInForm) {
            device.print("<button");
            org.wings.plaf.Utils.write(device, Utils.event(button));
            org.wings.plaf.Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            org.wings.plaf.Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        } else if (showAsFormComponent && !useIconsInForm)
            device.print("<span");
        else {
            device.print("<a href=\"");
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            addr.write(device);
            device.print("\"");

            org.wings.plaf.Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        }
        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");
        if (component.isFocusOwner())
            org.wings.plaf.Utils.optAttribute(device, "focus", component.getName());

        device.print(">");

        if (showAsFormComponent && !useIconsInForm && text == null)
            inputTypeCheckbox(device, button);
        else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (text != null && icon == null)
            writeText(device, text);
        else if (text != null) {
            new IconTextCompound() {
                protected void text(Device device) throws IOException {
                    writeText(device, text);
                }

                protected void icon(Device device) throws IOException {
                    if (showAsFormComponent && !useIconsInForm)
                        inputTypeCheckbox(device, button);
                    else
                        writeIcon(device, icon);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (showAsFormComponent && useIconsInForm)
            device.print("</button>");
        else if (showAsFormComponent && !useIconsInForm)
            device.print("</span>");
        else
            device.print("</a>");
    }

    protected void inputTypeCheckbox(Device device, SAbstractButton button) throws IOException {
        device.print("<input type=\"hidden\" name=\"");
        org.wings.plaf.Utils.write(device, Utils.event(button));
        device.print("\" value=\"hidden_reset\"/>");

        device.print("<input type=\"checkbox\" name=\"");
        org.wings.plaf.Utils.write(device, Utils.event(button));
        device.print("\"");
        org.wings.plaf.Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");

        Utils.writeEvents(device, button);
        device.print("/>");
    }
}
