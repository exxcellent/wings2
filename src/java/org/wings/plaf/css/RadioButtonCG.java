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
import org.wings.SConstants;
import org.wings.SIcon;
import org.wings.io.Device;

import java.io.IOException;

public class RadioButtonCG
        extends CheckBoxCG
        implements SConstants, org.wings.plaf.RadioButtonCG {
    protected void installIcons(final SAbstractButton button) {
        org.wings.plaf.CGManager manager = button.getSession().getCGManager();
        button.setIcon((SIcon) manager.getObject("SRadioButton.icon", SIcon.class));
        button.setSelectedIcon((SIcon) manager.getObject("SRadioButton.selectedIcon", SIcon.class));
        button.setRolloverIcon((SIcon) manager.getObject("SRadioButton.rolloverIcon", SIcon.class));
        button.setRolloverSelectedIcon((SIcon) manager.getObject("SRadioButton.rolloverSelectedIcon", SIcon.class));
        button.setPressedIcon((SIcon) manager.getObject("SRadioButton.pressedIcon", SIcon.class));
        button.setDisabledIcon((SIcon) manager.getObject("SRadioButton.disabledIcon", SIcon.class));
        button.setDisabledSelectedIcon((SIcon) manager.getObject("SRadioButton.disabledSelectedIcon", SIcon.class));
    }

    protected void inputTypeCheckbox(Device device, SAbstractButton button) throws IOException {
        device.print("<input type=\"hidden\" name=\"");
        org.wings.plaf.Utils.write(device, org.wings.plaf.css.Utils.event(button));
        device.print("\" value=\"");
        org.wings.plaf.Utils.write(device, button.getDeselectionParameter());
        device.print("\"/>");

        device.print("<input type=\"radio\" name=\"");
        org.wings.plaf.Utils.write(device, Utils.event(button));
        device.print("\" value=\"");
        org.wings.plaf.Utils.write(device, button.getToggleSelectionParameter());
        device.print("\"");

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");

        Utils.writeEvents(device, button);
        device.print("/>");
    }
}
