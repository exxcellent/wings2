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
import org.wings.SConstants;
import org.wings.io.Device;
import org.wings.plaf.ComponentCG;
import org.wings.session.SessionManager;
import org.wings.style.CSSSelector;

import java.io.IOException;
import java.io.Serializable;

/**
 * Partial CG implementation that is common to all ComponentCGs.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class AbstractComponentCG implements ComponentCG, SConstants, Serializable {

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

    public void componentChanged(SComponent c) {
    }

    public void write(Device device, SComponent component) throws IOException {
        writePrefix(device, component);
        writeContent(device, component);
        writeSuffix(device, component);
    }
    
    
    protected void writePrefix(Device device, SComponent component) throws IOException {
        SessionManager.getSession().getCGManager().getPrefixSuffixDelegate().writePrefix(device, component);
    }
    
    protected void writeSuffix(Device device, SComponent component) throws IOException {
        SessionManager.getSession().getCGManager().getPrefixSuffixDelegate().writeSuffix(device, component);
    }

    public CSSSelector mapSelector(SComponent addressedComponent, CSSSelector selector) {
        // Default: Do not map/modify the passed CSS selector.
        return selector;
    }

    protected void writeContent(Device device, SComponent component) throws IOException {
    }
}
