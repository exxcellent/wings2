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
import org.wings.plaf.CGManager;
import org.wings.io.Device;
import org.wings.session.SessionManager;

import java.io.IOException;

public class CheckBoxCG extends ButtonCG implements org.wings.plaf.CheckBoxCG {

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
        CGManager manager = SessionManager.getSession().getCGManager();
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

        /* TODO for the button support in IE hack to be working, this component
         * needs to always or never use buttons when rendered as form component.
         * Therefore best would be to drop button support on this component, since
         * one probably wants to change CheckBox state without submitting.
         * Therefore replace button with table... 
         * At this time it probably never uses buttons, since useIconsInForms is false
         * by default (and probably never set). useIconsInForms should be dropped!
         * Try #setShowAsFormComponent(false) if you want icon checkboxes in your
         * application.
         * (OL)
         */
        
        if (showAsFormComponent && useIconsInForms) {
            writeButtonStart(device, button);
            device.print(" type=\"submit\" name=\"");
            Utils.write(device, Utils.event(button));
            device.print("\"");
            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        } else if (showAsFormComponent && !useIconsInForms) {
            device.print("<span");
        } else {
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            writeLinkStart(device, addr);

            Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        }
        Utils.printCSSInlineFullSize(device, component.getPreferredSize());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        device.print(">");

        if (showAsFormComponent && !useIconsInForms) {
            if (text == null)
                writeInput(device, button);
            else {
                new IconTextCompound() {
                    protected void text(Device device) throws IOException {
                        writeText(device, text);
                    }

                    protected void icon(Device device) throws IOException {
                        writeInput(device, button);
                    }
                }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
            }
        }
        else {
            if (icon != null && text == null)
                writeIcon(device, icon);
            else if (text != null && icon == null)
                writeText(device, text);
            else if (text != null) {
                new IconTextCompound() {
                    protected void text(Device device) throws IOException {
                        writeText(device, text);
                    }

                    protected void icon(Device device) throws IOException {
                        writeIcon(device, icon);
                    }
                }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
            }
        }

        if (showAsFormComponent && useIconsInForms)
            device.print("</button>");
        else if (showAsFormComponent && !useIconsInForms)
            device.print("</span>");
        else
            device.print("</a>");
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

        Utils.writeEvents(device, button);
        device.print("/>");
    }
}
