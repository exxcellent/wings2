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
package org.wings.plaf.css.msie;

import org.wings.RequestURL;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.SAbstractButton;
import org.wings.SIcon;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;
import org.wings.plaf.css.AbstractLabelCG;
import org.wings.plaf.css.IconTextCompound;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.session.SessionManager;

import java.io.IOException;

import java.io.IOException;

public class ButtonCG extends AbstractLabelCG implements SParentFrameListener {

    private static final String FORMS_JS = (String) ResourceManager.getObject("JScripts.form", String.class);

    /**
     * a serializable class is supposed to have this ID.
     */
    private static final long serialVersionUID = -1794530181411426283L;

    public void write(final Device device, final SComponent component) throws IOException {
        final SAbstractButton button = (SAbstractButton) component;

        if (button.getShowAsFormComponent()) {
            MSIEUtils.writeSubmitAnchorStart(device, button, button.getToggleSelectionParameter());
            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
            Utils.optAttribute(device, "accesskey", button.getMnemonic());
            if (!button.isEnabled()) {
                device.print(" disabled=\"disabled\"");
            }
        } else {
            RequestURL addr = button.getRequestURL();
            addr.addParameter(button, button.getToggleSelectionParameter());
            writeLinkStart(device, addr);

            Utils.optAttribute(device, "accesskey", button.getMnemonic());
        }
        Utils.printCSSInlineFullSize(device, component.getPreferredSize());

        // use class attribute instead of single attributes for IE compatibility
        final StringBuffer className = new StringBuffer();
        if (!button.isEnabled()) {
            className.append(component.getStyle());
            className.append("_disabled ");
        }
        if (button.isSelected()) {
            className.append(component.getStyle());
            className.append("_selected ");
        }
        Utils.optAttribute(device, "class", className);

        if (component.isFocusOwner())
            Utils.optAttribute(device, "focus", component.getName());

        Utils.writeEvents(device, button);
        device.print(">");

        final String text = button.getText();
        final SIcon icon = org.wings.plaf.css.ButtonCG.getIcon(button);

        if (icon == null && text != null) {
            if (button.getShowAsFormComponent()) {
                MSIEUtils.writeSubmitInputStart(device, button, text);
                device.print(">");
                MSIEUtils.writeSubmitInputEnd(device);
            } else {
                writeText(device, text, false);
            }
        } else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, false);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (button.getShowAsFormComponent())
            MSIEUtils.writeSubmitAnchorEnd(device);
        else
            device.print("</a>");
    }

    protected void writeLinkStart(final Device device, final RequestURL addr) throws IOException {
        MSIEUtils.writeSubmitAnchorStart(device, addr);
        /*device.print("<a ");
        device.print("href=\"").print(addr.toString()).print("\" ");
        //device.print("onclick=\"location.href='").print(addr.toString()).print("';\"");*/
    }

    public void installCG(final SComponent component) {
        super.installCG(component);
        component.addParentFrameListener(this);
    }

    public void parentFrameAdded(final SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        ClasspathResource res = new ClasspathResource(FORMS_JS, "text/javascript");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script("text/javascript", new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(final SParentFrameEvent e) {
    }
}
