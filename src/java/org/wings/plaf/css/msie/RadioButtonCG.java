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
package org.wings.plaf.css.msie;

import org.wings.SAbstractButton;
import org.wings.SIcon;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;
import org.wings.resource.ResourceManager;

import java.io.IOException;


public final class RadioButtonCG extends org.wings.plaf.css.CheckBoxCG {
    private static final long serialVersionUID = 1L;

    protected void installIcons(final SAbstractButton button) {
        button.setIcon((SIcon) ResourceManager.getObject("SRadioButton.icon", SIcon.class));
        button.setSelectedIcon((SIcon) ResourceManager.getObject("SRadioButton.selectedIcon", SIcon.class));
        button.setRolloverIcon((SIcon) ResourceManager.getObject("SRadioButton.rolloverIcon", SIcon.class));
        button.setRolloverSelectedIcon((SIcon) ResourceManager.getObject("SRadioButton.rolloverSelectedIcon", SIcon.class));
        button.setPressedIcon((SIcon) ResourceManager.getObject("SRadioButton.pressedIcon", SIcon.class));
        button.setDisabledIcon((SIcon) ResourceManager.getObject("SRadioButton.disabledIcon", SIcon.class));
        button.setDisabledSelectedIcon((SIcon) ResourceManager.getObject("SRadioButton.disabledSelectedIcon", SIcon.class));
    }

    protected void writeInput(Device device, SAbstractButton button) throws IOException {
        device.print("<input type=\"hidden\" name=\"");
        Utils.write(device, Utils.event(button));
        device.print("\" value=\"");
        Utils.write(device, button.getDeselectionParameter());
        device.print("\"/>");

        device.print("<input type=\"radio\" name=\"");
        Utils.write(device, Utils.event(button));
        device.print("\" value=\"");
        Utils.write(device, button.getToggleSelectionParameter());
        device.print("\"");

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isFocusOwner())
            Utils.optAttribute(device, "foc", button.getName());

        if (button.isSelected())
            device.print(" checked=\"true\"");

        Utils.writeEvents(device, button, null);
        device.print("/>");
    }
}
