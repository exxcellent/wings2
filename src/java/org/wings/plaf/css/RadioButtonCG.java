/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;

import java.io.IOException;

public class RadioButtonCG
    extends CheckBoxCG
    implements SConstants, org.wings.plaf.RadioButtonCG
{
    protected void installIcons(final SAbstractButton button) {
        org.wings.plaf.CGManager manager = button.getSession().getCGManager();
        button.setIcon((SIcon)manager.getObject("SRadioButton.icon", SIcon.class));
        button.setSelectedIcon((SIcon)manager.getObject("SRadioButton.selectedIcon", SIcon.class));
        button.setRolloverIcon((SIcon)manager.getObject("SRadioButton.rolloverIcon", SIcon.class));
        button.setRolloverSelectedIcon((SIcon)manager.getObject("SRadioButton.rolloverSelectedIcon", SIcon.class));
        button.setPressedIcon((SIcon)manager.getObject("SRadioButton.pressedIcon", SIcon.class));
        button.setDisabledIcon((SIcon)manager.getObject("SRadioButton.disabledIcon", SIcon.class));
        button.setDisabledSelectedIcon((SIcon)manager.getObject("SRadioButton.disabledSelectedIcon", SIcon.class));
    }

    protected void inputTypeCheckbox(Device device, SAbstractButton button) throws IOException {
        device.write("<input type=\"hidden\" name=\"".getBytes());
        org.wings.plaf.Utils.write( device, org.wings.plaf.css.Utils.event(button));
        device.write("\" value=\"".getBytes());
        org.wings.plaf.Utils.write( device, button.getDeselectionParameter());
        device.write("\"/>".getBytes());

        device.write("<input type=\"radio\" name=\"".getBytes());
        org.wings.plaf.Utils.write( device, Utils.event(button));
        device.write("\" value=\"".getBytes());
        org.wings.plaf.Utils.write( device, button.getToggleSelectionParameter());
        device.write("\"".getBytes());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");

        Utils.writeEvents(device, button);
        device.write("/>".getBytes());
    }
}
