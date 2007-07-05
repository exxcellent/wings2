/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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
import org.wings.plaf.Update;
import org.wings.resource.ResourceManager;

import java.io.IOException;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

public final class RadioButtonCG extends CheckBoxCG implements
        org.wings.plaf.RadioButtonCG {
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

    protected void tableClickability(Device device, SAbstractButton button) throws IOException {
        if (!button.isSelected()) {
            super.tableClickability(device, button);
        }
    }

    protected void writeInput(Device device, SAbstractButton button) throws IOException {
        if (button.getShowAsFormComponent() && !useIconsInForms) {
            Object clientProperty = button.getClientProperty("onChangeSubmitListener");
            // If the application developer attached any ActionListeners, ItemListeners or
            // ChangeListeners to this SRadioButton or its ButtonGroup, the surrounding form
            // gets submitted as soon as the state of this SRadioButton changed.
            if (button.getActionListeners().length > 0 ||
                    button.getItemListeners().length > 0 ||
                    button.getChangeListeners().length > 0 ||
                    (button.getGroup() != null && button.getGroup().getActionListeners().length > 0)) {
                if (clientProperty == null) {
                    String event = JavaScriptEvent.ON_CHANGE;
                	String code = "wingS.request.sendEvent(event, true, " + !button.isReloadForced() + ");";
                    if (Utils.isMSIE(button)) {
                        // In IE the "onchange"-event gets fired when a control loses the
                        // input focus and its value has been modified since gaining focus.
                        // Even though this is actually the correct behavior, we want the
                        // event to get fired immediately - thats why we use a "filtered"
                        // version of IE's proprietary "onpropertychange"-event.
                        event = "onpropertychange";
                        code = "if (event.srcElement.checked) " + code;
                    }
                    JavaScriptListener javaScriptListener = new JavaScriptListener(event, code);
                    button.addScriptListener(javaScriptListener);
                    button.putClientProperty("onChangeSubmitListener", javaScriptListener);
                }
            } else if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
                button.removeScriptListener((JavaScriptListener) clientProperty);
                button.putClientProperty("onChangeSubmitListener", null);
            }
        }

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
        Utils.writeEvents( device, button, null );
        Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");
        if (button.isSelected())
            device.print(" checked=\"true\"");
        if (button.isFocusOwner())
            Utils.optAttribute(device, "foc", button.getName());

        device.print("/>");
    }

    public Update getTextUpdate(SRadioButton radioButton, String text) {
        return text == null ? null : new TextUpdate(radioButton, text);
    }

    public Update getIconUpdate(SRadioButton radioButton, SIcon icon) {
        return icon == null ? null : new IconUpdate(radioButton, icon);
    }

}
