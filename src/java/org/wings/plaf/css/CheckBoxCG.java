/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;

import java.io.IOException;

public class CheckBoxCG
    extends ButtonCG
    implements SConstants, org.wings.plaf.CheckBoxCG {
    private boolean useIconsInForm = false;

    public boolean isUseIconsInForm() {
        return useIconsInForm;
    }

    public void setUseIconsInForm(boolean useIconsInForm) {
        this.useIconsInForm = useIconsInForm;
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        final SAbstractButton button = (SAbstractButton)component;
        installIcons(button);
    }

    protected void installIcons(final SAbstractButton button) {
        org.wings.plaf.CGManager manager = button.getSession().getCGManager();
        button.setIcon((SIcon)manager.getObject("SCheckBox.icon", SIcon.class));
        button.setSelectedIcon((SIcon)manager.getObject("SCheckBox.selectedIcon", SIcon.class));
        button.setRolloverIcon((SIcon)manager.getObject("SCheckBox.rolloverIcon", SIcon.class));
        button.setRolloverSelectedIcon((SIcon)manager.getObject("SCheckBox.rolloverSelectedIcon", SIcon.class));
        button.setPressedIcon((SIcon)manager.getObject("SCheckBox.pressedIcon", SIcon.class));
        button.setDisabledIcon((SIcon)manager.getObject("SCheckBox.disabledIcon", SIcon.class));
        button.setDisabledSelectedIcon((SIcon)manager.getObject("SCheckBox.disabledSelectedIcon", SIcon.class));
    }

    public void writeContent(final Device device, final SComponent component)
        throws IOException {
        final SAbstractButton button = (SAbstractButton)component;

        final boolean showAsFormComponent = button.getShowAsFormComponent();
        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (showAsFormComponent && useIconsInForm) {
            device.print("<button type=\"submit\"");
            org.wings.plaf.Utils.write(device, Utils.event(button));
            org.wings.plaf.Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            org.wings.plaf.Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        }
        else if (showAsFormComponent && !useIconsInForm)
            device.print("<span");
        else {
            device.write("<a href=\"".getBytes());
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            addr.write(device);
            device.write("\"".getBytes());

            org.wings.plaf.Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        }

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");

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
        device.write("<input type=\"hidden\" name=\"".getBytes());
        org.wings.plaf.Utils.write(device, Utils.event(button));
        device.write("\" value=\"hidden_reset\"/>".getBytes());

        device.write("<input type=\"checkbox\" name=\"".getBytes());
        org.wings.plaf.Utils.write(device, Utils.event(button));
        device.write("\"".getBytes());
        org.wings.plaf.Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");

        Utils.writeEvents(device, button);
        device.write("/>".getBytes());
    }
}
