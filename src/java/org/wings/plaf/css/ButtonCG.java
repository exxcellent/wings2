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
import org.wings.script.JavaScriptListener;
import org.wings.script.JavaScriptEvent;
import org.wings.io.Device;

import java.io.IOException;

public class ButtonCG extends LabelCG implements org.wings.plaf.ButtonCG {

    /**
     * Use this java script implementation to submit forms on button click
     */
    // TODO: Implement handling of formless submits
    // TODO: Avoid triggering of enter key catchers
    // TODO: Use Utils.loadScript("org/wings/plaf/css/xxx.js")
    public final static String JS_FORM_SUBMIT_SCRIPT = "javascript:this.form.submit();";

    /**
     * Use i.e. {@link SButton#addScriptListener(org.wings.script.ScriptListener)} to add this scripts.
     */
    public final static JavaScriptListener JS_ON_CHANGE_SUBMIT = new JavaScriptListener(JavaScriptEvent.ON_CHANGE, JS_FORM_SUBMIT_SCRIPT);

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

                    device.print(" name=\"");
                    Utils.write(device, iconName);

                    device.print("\"");
                } // end of if ()


                if (rolloverIcon != null) {

                    device.print(" onMouseover=\"if(document.images){this.src='");
                    Utils.write(device, rolloverIcon.getURL());

                    device.print("';}\" onmouseout=\"if(document.images){this.src='");
                    Utils.write(device, origIcon.getURL());

                    device.print("';}\"");
                }

                if (pressedIcon != null) {

                    device.print(" onMousedown=\"if(document.images){this.src='");
                    Utils.write(device, pressedIcon.getURL());

                    device.print("';}\" onmouseup=\"if(document.images){this.src='");
                    Utils.write(device, rolloverIcon != null ? rolloverIcon.getURL() : origIcon.getURL());

                    device.print("';}\"");
                }
            }
        }

    }

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        if (button.getShowAsFormComponent()) {
            writeButtonStart(device, button);
            device.print(" type=\"submit\" name=\"");
            Utils.write(device, Utils.event(button));
            device.print("\"");
            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        } else {
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            writeLinkStart(device, addr);

            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }
        Utils.printCSSInlineFullSize(device, component.getPreferredSize());

        // use class attribute instead of single attributes for IE compatibility
        StringBuffer className = new StringBuffer();
        if (!button.isEnabled()) {
            className.append(component.getStyle());
            className.append("_disabled ");
        }
        if (button.isSelected()) {
            className.append(component.getStyle());
            className.append("_selected ");
        }
        if (className.length() > 0) {
            device.print(" class=\"");
            device.print(className.toString());
            device.print("\"");
        }
        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

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

    /**
     * @param device
     * @throws IOException
     */
    protected void writeButtonStart(final Device device, final SAbstractButton comp) throws IOException {
        device.print("<button");
    }

    /** 
     * Convenience method to keep differences between default and msie
     * implementations small
     * @param device
     * @param addr
     * @throws IOException
     */
    protected void writeLinkStart(final Device device, RequestURL addr) throws IOException {
        device.print("<a href=\"");
        addr.write(device);
        device.print("\"");
    }

    protected SIcon getIcon(SAbstractButton abstractButton) {
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
