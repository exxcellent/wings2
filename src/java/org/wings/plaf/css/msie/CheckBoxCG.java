package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.RequestURL;
import org.wings.SAbstractButton;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.io.Device;
import org.wings.plaf.css.IconTextCompound;
import org.wings.plaf.css.Utils;

public class CheckBoxCG extends org.wings.plaf.css.CheckBoxCG {

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        final boolean showAsFormComponent = button.getShowAsFormComponent();
        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (showAsFormComponent && useIconsInForm) {
            device.print("<button");
            Utils.write(device, Utils.event(button));
            Utils.optAttribute(device, "tabindex", button
                    .getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
            Utils.writeEvents(device, button);
        } else if (showAsFormComponent && !useIconsInForm)
            device.print("<span");
        else {
            device.print("<table onclick=\"javascript:location.href='");
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            addr.write(device);
            device.print("';\"");

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
        if (!showAsFormComponent) {
            device.print("><tr><td");
        }
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
            }.writeCompound(device, component, button
                    .getHorizontalTextPosition(), button
                    .getVerticalTextPosition());
        }

        if (showAsFormComponent && useIconsInForm)
            device.print("</button>");
        else if (showAsFormComponent && !useIconsInForm)
            device.print("</span>");
        else
            device.print("</td></tr></table>");
    }

}
