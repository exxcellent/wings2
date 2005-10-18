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
import org.wings.io.Device;
import org.wings.session.SessionManager;

import java.io.IOException;

public class CheckBoxCG extends ButtonCG implements org.wings.plaf.CheckBoxCG {
    private static final SIcon ICON_DISABLEDSELECTED = (SIcon) SessionManager
            .getSession().getCGManager().getObject("SCheckBox.disabledSelectedIcon", SIcon.class);

    private static final SIcon ICON_DISABLED = (SIcon) SessionManager
            .getSession().getCGManager().getObject("SCheckBox.disabledIcon", SIcon.class);

    private static final SIcon ICON_PRESSED = (SIcon) SessionManager
            .getSession().getCGManager().getObject("SCheckBox.pressedIcon", SIcon.class);

    private static final SIcon ICON_ROLLOVERSELECTED = (SIcon) SessionManager
            .getSession().getCGManager().getObject("SCheckBox.rolloverSelectedIcon", SIcon.class);

    private static final SIcon ICON_ROLLOVER = (SIcon) SessionManager
            .getSession().getCGManager().getObject("SCheckBox.rolloverIcon", SIcon.class);

    private static final SIcon ICON_DEFAULT = (SIcon) SessionManager
            .getSession().getCGManager().getObject("SCheckBox.icon", SIcon.class);

    private static final SIcon ICON_SELECTED = (SIcon) SessionManager
                .getSession().getCGManager().getObject("SCheckBox.selectedIcon", SIcon.class);
    
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
        org.wings.plaf.CGManager manager = button.getSession().getCGManager();
        button.setIcon(ICON_DEFAULT);
        button.setSelectedIcon(ICON_SELECTED);
        button.setRolloverIcon(ICON_ROLLOVER);
        button.setRolloverSelectedIcon(ICON_ROLLOVERSELECTED);
        button.setPressedIcon(ICON_PRESSED);
        button.setDisabledIcon(ICON_DISABLED);
        button.setDisabledSelectedIcon(ICON_DISABLEDSELECTED);
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

        if (showAsFormComponent && !useIconsInForms && text == null){
        	if(button.getRolloverIcon() != null){
            	inputRollOverCheckbox(device, icon, button, button.getEncodedLowLevelEventId(), true);
        	}
        	else{
        		inputTypeCheckbox(device, button);
        	}
        }
        else if (icon != null && text == null){
        	if(button.getRolloverIcon() != null){
        		writeRollOverIcon(device, icon, button, "Icon_"+button.getName(), true);
        	}
        	else{
        		writeIcon(device, icon);
        	}
        }
        else if (text != null && icon == null)
            writeText(device, text);
        else if (text != null) {
            new IconTextCompound() {
                protected void text(Device device) throws IOException {
                    writeText(device, text);
                }

                protected void icon(Device device) throws IOException {
                    if (showAsFormComponent && !useIconsInForms){
                    	if(button.getRolloverIcon() != null){
                    		inputRollOverCheckbox(device, icon, button, button.getEncodedLowLevelEventId(), true);
                    	}
                    	else{
                    		inputTypeCheckbox(device, button);
                    	}
                    }
                    else{
                    	if(button.getRolloverIcon() != null){
                    		writeRollOverIcon(device, icon, button, "Icon_"+button.getName(), true);
                    	}
                    	else{
                    		writeIcon(device, icon);
                    	}
                    }
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (showAsFormComponent && useIconsInForms)
            device.print("</button>");
        else if (showAsFormComponent && !useIconsInForms)
            device.print("</span>");
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

    protected void inputTypeCheckbox(Device device, SAbstractButton button) throws IOException {
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
