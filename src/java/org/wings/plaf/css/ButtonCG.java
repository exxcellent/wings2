/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;

import java.io.IOException;

public class ButtonCG
    extends LabelCG
    implements SConstants, org.wings.plaf.ButtonCG {

    private final static byte[] __onMouseover_if = " onMouseover=\"if(document.images){this.src='".getBytes();
    private final static byte[] __onmouseout_if = "';}\" onmouseout=\"if(document.images){this.src='".getBytes();
    private final static byte[] ___4 = "';}\"".getBytes();
    private final static byte[] __onMousedown_if = " onMousedown=\"if(document.images){this.src='".getBytes();
    private final static byte[] __onmouseup_if_d = "';}\" onmouseup=\"if(document.images){this.src='".getBytes();

    private void writeDynamicIcons(final Device device, SAbstractButton abstractButton, SIcon origIcon,
                                   String iconName, boolean renderNameAttribute)
        throws IOException {
        if (abstractButton.isEnabled() &&
            (abstractButton.getGroup() == null || !abstractButton.isSelected())) {
            // render rollover
            SIcon rolloverIcon = abstractButton.getRolloverIcon();
            SIcon pressedIcon = abstractButton.getPressedIcon();

            if (rolloverIcon != null || pressedIcon != null) {
                if (renderNameAttribute) {

                    device.write(" name=\"".getBytes());
                    org.wings.plaf.Utils.write(device, iconName);

                    device.write("\"".getBytes());
                } // end of if ()


                if (rolloverIcon != null) {

                    device.write(__onMouseover_if);
                    org.wings.plaf.Utils.write(device, rolloverIcon.getURL());

                    device.write(__onmouseout_if);
                    org.wings.plaf.Utils.write(device, origIcon.getURL());

                    device.write(___4);
                }

                if (pressedIcon != null) {

                    device.write(__onMousedown_if);
                    org.wings.plaf.Utils.write(device, pressedIcon.getURL());

                    device.write(__onmouseup_if_d);
                    org.wings.plaf.Utils.write(device, rolloverIcon != null ? rolloverIcon.getURL() : origIcon.getURL());

                    device.write(___4);
                }
            }
        }

    }

    public void writeContent(final Device device, final SComponent component)
        throws IOException {
        final SAbstractButton button = (SAbstractButton)component;

        if (button.getShowAsFormComponent()) {
            device.print("<button type=\"submit\" name=\"");
            org.wings.plaf.Utils.write(device, Utils.event(button));
            device.print("\"");
            org.wings.plaf.Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            org.wings.plaf.Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }
        else {
            device.write("<a href=\"".getBytes());
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            addr.write(device);
            device.write("\"".getBytes());

            org.wings.plaf.Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }
        Utils.innerPreferredSize(device, component.getPreferredSize());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");

        Utils.writeEvents(device, button);
        device.print(">");

        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (icon == null && text != null)
            writeText(device, text);
        else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (button.getShowAsFormComponent())
            device.print("</button>");
        else
            device.print("</a>");
    }

    protected SIcon getIcon(SAbstractButton abstractButton) {
        if (abstractButton.isSelected()) {
            return abstractButton.isEnabled()
                ? abstractButton.getSelectedIcon()
                : abstractButton.getDisabledSelectedIcon();
        }
        else {
            return abstractButton.isEnabled()
                ? abstractButton.getIcon()
                : abstractButton.getDisabledIcon();
        }
    }
}
