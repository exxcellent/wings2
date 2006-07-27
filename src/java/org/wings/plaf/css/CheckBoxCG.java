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
import org.wings.io.Device;
import org.wings.resource.ResourceManager;

import java.io.IOException;

public class CheckBoxCG extends ButtonCG implements org.wings.plaf.CheckBoxCG {

    private static final long serialVersionUID = 1L;
    protected boolean useIconsInForms = false;

    public boolean isUseIconsInForm() {
        return useIconsInForms;
    }

    public void setUseIconsInForm(boolean useIconsInForm) {
        this.useIconsInForms = useIconsInForm;
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        final SAbstractButton button = (SAbstractButton) component;
        installIcons(button);
    }

    protected void installIcons(final SAbstractButton button) {
        button.setIcon((SIcon) ResourceManager.getObject("SCheckBox.icon", SIcon.class));
        button.setSelectedIcon((SIcon) ResourceManager.getObject("SCheckBox.selectedIcon", SIcon.class));
        button.setRolloverIcon((SIcon) ResourceManager.getObject("SCheckBox.rolloverIcon", SIcon.class));
        button.setRolloverSelectedIcon((SIcon) ResourceManager.getObject("SCheckBox.rolloverSelectedIcon", SIcon.class));
        button.setPressedIcon((SIcon) ResourceManager.getObject("SCheckBox.pressedIcon", SIcon.class));
        button.setDisabledIcon((SIcon) ResourceManager.getObject("SCheckBox.disabledIcon", SIcon.class));
        button.setDisabledSelectedIcon((SIcon) ResourceManager.getObject("SCheckBox.disabledSelectedIcon", SIcon.class));
    }

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        final boolean showAsFormComponent = button.getShowAsFormComponent();
        final String text = button.getText();
        final SIcon icon = getIcon(button);

        writeTablePrefix(device, component);

        if (showAsFormComponent && useIconsInForms) {
            Utils.printButtonStart(device, button, button.getToggleSelectionParameter(), true, button.getShowAsFormComponent());
            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        } else if (showAsFormComponent && !useIconsInForms) {
            device.print("<div");
        } else {
            Utils.printButtonStart(device, button, button.getToggleSelectionParameter(), true, button.getShowAsFormComponent());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }
        Utils.printCSSInlineFullSize(device, component.getPreferredSize());

        if (button.isSelected())
            device.print(" checked=\"true\"");

        device.print(">");

        if (showAsFormComponent && !useIconsInForms) {
            if (text == null)
                writeInput(device, button);
            else {
                new IconTextCompound() {
                    protected void text(Device device) throws IOException {
                        writeText(device, text, button.isWordWrap());
                    }

                    protected void icon(Device device) throws IOException {
                        writeInput(device, button);
                    }

                    protected void tableAttributes(Device d) throws IOException {
                        Utils.printCSSInlineFullSize(d, button.getPreferredSize());
                    }
                }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition(), false);
            }
        }
        else {
            if (icon != null && text == null)
                writeIcon(device, icon, Utils.isMSIE(component));
            else if (text != null && icon == null)
                writeText(device, text, false);
            else if (text != null) {
                new IconTextCompound() {
                    protected void text(Device device) throws IOException {
                        writeText(device, text, false);
                    }

                    protected void icon(Device device) throws IOException {
                        writeIcon(device, icon, Utils.isMSIE(component));
                    }

                    protected void tableAttributes(Device d) throws IOException {
                        Utils.printCSSInlineFullSize(d, button.getPreferredSize());
                    }
                }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition(), false);
            }
        }

        if (showAsFormComponent && useIconsInForms)
            Utils.printButtonEnd(device, button, button.getToggleSelectionParameter(), true);
        else if (showAsFormComponent && !useIconsInForms)
            device.print("</div>");
        else
            Utils.printButtonEnd(device, button, button.getToggleSelectionParameter(), true);

        writeTableSuffix(device, component);
    }

    protected void writeInput(Device device, SAbstractButton button) throws IOException {
        device.print("<input type=\"hidden\" name=\"");
        Utils.write(device, Utils.event(button));
        device.print("\" value=\"hidden_reset\"/>");

        device.print("<input type=\"checkbox\" name=\"");
        Utils.write(device, Utils.event(button));
        device.print("\"");
        Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");
        if (button.isFocusOwner())
            Utils.optAttribute(device, "foc", button.getName());

        device.print("/>");
    }

}
