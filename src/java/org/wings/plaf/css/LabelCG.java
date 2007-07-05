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

import org.wings.plaf.CGManager;
import org.wings.plaf.Update;
import org.wings.session.SessionManager;
import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SIcon;
import org.wings.io.Device;
import java.io.IOException;

/**
 * CG for SLabel instances.
 *
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public class LabelCG extends AbstractLabelCG implements org.wings.plaf.LabelCG {

    private static final long serialVersionUID = 1L;
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

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SLabel label = (SLabel) component;
        final String text = label.getText();
        final SIcon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();
        final int horizontalTextPosition = label.getHorizontalTextPosition();
        final int verticalTextPosition = label.getVerticalTextPosition();
        final boolean wordWrap = label.isWordWrap();

        if (icon == null && text != null) {
            writeTablePrefix(device, component);
            writeText(device, text, wordWrap);
            writeTableSuffix(device, component);
        }
        else if (icon != null && text == null) {
            writeTablePrefix(device, component);
            writeIcon(device, icon, Utils.isMSIE(component));
            writeTableSuffix(device, component);
        }
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, wordWrap);
                }
                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon, Utils.isMSIE(component));
                }

                protected void tableAttributes(Device d) throws IOException {
                    writeAllAttributes(d, label);
                }
            }.writeCompound(device, component, horizontalTextPosition, verticalTextPosition, true);
        } else {
            //leeres label
            writeTablePrefix(device, component);
            writeTableSuffix(device, component);
        }
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

    public Update getTextUpdate(SLabel label, String text) {
        return text == null ? null : new TextUpdate(label, text);
    }

    public Update getIconUpdate(SLabel label, SIcon icon) {
        return icon == null ? null : new IconUpdate(label, icon);
    }
}
