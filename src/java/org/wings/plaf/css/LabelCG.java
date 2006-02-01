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


import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.plaf.CGManager;
import org.wings.io.Device;
import org.wings.session.BrowserType;
import org.wings.session.SessionManager;
import java.io.IOException;

public class LabelCG extends AbstractComponentCG implements
        org.wings.plaf.LabelCG {

    private boolean wordWrapDefault;

    public LabelCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        final String wordWrapDefaultString = (String) manager.getObject("LabelCG.wordWrapDefault", String.class);
        setWordWrapDefault(Boolean.valueOf(wordWrapDefaultString).booleanValue());
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        ((SLabel)component).setWordWrap(wordWrapDefault);
    }

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SLabel label = (SLabel) component;
        final String text = label.getText();
        final SIcon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();
        final int horizontalTextPosition = label.getHorizontalTextPosition();
        final int verticalTextPosition = label.getVerticalTextPosition();
        final boolean wordWrap = label.isWordWrap();
        if (icon == null && text != null) {
            writeText(device, text, wordWrap);
        }
        else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, wordWrap);
                }
                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }
            }.writeCompound(device, component, horizontalTextPosition, verticalTextPosition);
        }
    }

    /** Prefer other writeText method! */
    protected void writeText(Device device, String text) throws IOException {
        writeText(device, text, false);
    }

    protected void writeText(Device device, String text, boolean wordWrap) throws IOException {
        boolean isIE = SessionManager.getSession().getUserAgent().getBrowserType().equals(BrowserType.IE);

        if ((text.length() > 5) && (text.startsWith("<html>"))) {
            Utils.writeRaw(device, text.substring(6));
        } else if (isIE) {
            // IE doesn't handle whitespace:pre correctly, so we do it hardcore by quoting spaces
            // or not (depending on wordwrap)
            Utils.quote(device, text, true, !wordWrap, false);
        } else {
            // Other browser handle the CSS property 'white-space' correctly.
            // The default is set as in swing: don't wrap:
            if (!wordWrap)
                Utils.quote(device,text,false, false, false);
            else {
                // non-default case: overwrite css property, set back to wrap
                device.print("<span style=\"white-space: normal;\">");
                Utils.quote(device, text, true, false, false);
                device.print("</span>");
            }
        }
    }

    protected void writeIcon(Device device, SIcon icon) throws IOException {
        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");
    }

    /**
     * Default word wrap behaviour.
      * @return <code>true</code> if CG should advise all new SLabel
     * instances to allow word wrapping. Default is <code>false</code>
     */
    public boolean isWordWrapDefault() {
        return wordWrapDefault;
    }

    /**
     * Default word wrap behaviour.
     * @param wordWrapDefault <code>true</code> if CG should advise all new
     * SLabel instances to allow word wrapping. Default is <code>false</code>
     */
    public void setWordWrapDefault(boolean wordWrapDefault) {
        this.wordWrapDefault = wordWrapDefault;
    }

}
