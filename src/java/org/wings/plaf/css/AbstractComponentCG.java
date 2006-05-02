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

import java.io.IOException;
import java.io.Serializable;

import javax.swing.InputMap;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.io.Device;
import org.wings.plaf.ComponentCG;
import org.wings.session.SessionManager;
import org.wings.style.CSSSelector;

/**
 * Partial CG implementation that is common to all ComponentCGs.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class AbstractComponentCG implements ComponentCG, SConstants, Serializable {
    /**
     * An invisible icon / graphic (spacer graphic)
     */
    private static SIcon BLIND_ICON;

    protected AbstractComponentCG() {
    }

    /**
     * Install the appropriate CG for <code>component</code>.
     *
     * @param component the component
     */
    public void installCG(SComponent component) {
        Class clazz = component.getClass();
        while (clazz.getPackage() == null || "org.wings".equals(clazz.getPackage().getName()) == false)
            clazz = clazz.getSuperclass();
        String style = clazz.getName();
        style = style.substring(style.lastIndexOf('.') + 1);
        component.setStyle(style); // set default style name to component class (ie. SLabel).
    }

    /**
     * Uninstall the CG from <code>component</code>.
     *
     * @param component the component
     */
    public void uninstallCG(SComponent component) {
    }

    public void componentChanged(SComponent component) {
        InputMap inputMap = component.getInputMap();
        if (inputMap != null && inputMap.size() > 0) {
            if (!(inputMap instanceof VersionedInputMap)) {
                inputMap = new VersionedInputMap(inputMap);
                component.setInputMap(inputMap);
            }

            final VersionedInputMap versionedInputMap = (VersionedInputMap) inputMap;
            final Integer inputMapVersion = (Integer) component.getClientProperty("inputMapVersion");
            if (inputMapVersion == null || versionedInputMap.getVersion() != inputMapVersion.intValue()) {
                InputMapScriptListener.install(component);
                component.putClientProperty("inputMapVersion", new Integer(versionedInputMap.getVersion()));
            }
        }
    }

    public boolean wantsPrefixAndSuffix(SComponent component) {
        return true;
    }

    public CSSSelector mapSelector(SComponent addressedComponent, CSSSelector selector) {
        // Default: Do not map/modify the passed CSS selector.
        return selector;
    }

    protected final SIcon getBlindIcon() {
        if(BLIND_ICON == null)
            BLIND_ICON = new SResourceIcon("org/wings/icons/blind.gif");
        return BLIND_ICON;
    }
}
