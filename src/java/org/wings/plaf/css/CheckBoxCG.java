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
import org.wings.util.SStringBuilder;
import org.wings.io.Device;
import org.wings.resource.ResourceManager;

import java.io.IOException;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

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

        /*
        // text only
        if (icon == null && text != null && (!showAsFormComponent || useIconsInForms && showAsFormComponent)) {
            device.print("<table");
            tableAttributes(device, button);
            device.print("><tr><td>");
            writeText(device, text, button.isWordWrap());
            device.print("</td></tr></table>");
        }
        */
        // icon or input only
        if (text == null) {
            if ((showAsFormComponent && !useIconsInForms) || icon == null)
                writeInput(device, button);
            else {
                device.print("<table");
                tableAttributes(device, button);
                device.print("><tr><td>");
                writeIcon(device, icon, Utils.isMSIE(component));
                device.print("</td></tr></table>");
            }
        }
        else {
            new IconTextCompound() {
                protected void text(Device device) throws IOException {
                    writeText(device, text, button.isWordWrap());
                }

                protected void icon(Device device) throws IOException {
                    if ((showAsFormComponent && !useIconsInForms) || icon == null)
                        writeInput(device, button);
                    else
                        writeIcon(device, icon, Utils.isMSIE(component));
                }

                protected void tableAttributes(Device d) throws IOException {
                    CheckBoxCG.this.tableAttributes(d, button);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition(), false);
        }
    }

    protected void tableAttributes(Device device, SAbstractButton button) throws IOException {
        final boolean showAsFormComponent = button.getShowAsFormComponent();
        // table is clickable
        if (!showAsFormComponent || useIconsInForms) {
            Utils.printClickability(device, button, button.getToggleSelectionParameter(), button.isEnabled(), button.getShowAsFormComponent());

            if (button.isFocusOwner())
                Utils.optAttribute(device, "foc", button.getName());

            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }

        if (button.isSelected())
            device.print(" checked=\"true\"");

        String style = button.getStyle();
        SStringBuilder className = new SStringBuilder(style);
        if (button.getShowAsFormComponent())
            className.append("_form");
        if (!button.isEnabled())
            className.append("_disabled");
        if (button.isSelected())
            className.append("_selected");

        button.setStyle(className.toString());
        writeAllAttributes(device, button);
        button.setStyle(style);
    }

    protected void writeInput(Device device, SAbstractButton button) throws IOException {
    	Object clientProperty = button.getClientProperty("onChangeSubmitListener");
    	// If the application developer attached any ActionListeners or ItemListeners to
        // this SCheckBox or its ButtonGroup, the surrounding form gets submitted as soon
        // as the state of this SCheckBox changed.
    	if (button.getActionListeners().length > 0 || button.getItemListeners().length > 0 ||
        		(button.getGroup() != null && button.getGroup().getActionListeners().length > 0)) {
            if (clientProperty == null) {
            	String event = JavaScriptEvent.ON_CHANGE;
            	String code = "submitForm(" + !button.isCompleteUpdateForced() + ",event);";
            	if (Utils.isMSIE(button)) {
            		// In IE the "onchange"-event gets fired when a control loses the
            		// input focus and its value has been modified since gaining focus.
            		// Even though this is actually the correct behavior, we want the
            		// event to get fired immediately - thats why we use IE's proprietary
            		// "onpropertychange"-event.
            		event = "onpropertychange";
            	}
                JavaScriptListener javaScriptListener = new JavaScriptListener(event, code);
                button.addScriptListener(javaScriptListener);
                button.putClientProperty("onChangeSubmitListener", javaScriptListener);
            }
        } else if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
            button.removeScriptListener((JavaScriptListener) clientProperty);
            button.putClientProperty("onChangeSubmitListener", null);
        }

        device.print("<input type=\"hidden\" name=\"");
        Utils.write(device, Utils.event(button));
        device.print("\" value=\"hidden_reset\"/>");

        device.print("<input type=\"checkbox\" name=\"");
        Utils.write(device, Utils.event(button));
        device.print("\"");
        Utils.writeEvents(device, button, null);
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
