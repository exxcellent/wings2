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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private final static transient Log log = LogFactory.getLog(AbstractComponentCG.class);
    
    protected AbstractComponentCG() {
    }

    /**
     * Install the appropriate CG for <code>component</code>.
     *
     * @param component the component
     */
    public void installCG(SComponent component) {
        Class clazz = component.getClass();
        while ("org.wings".equals(clazz.getPackage().getName()) == false)
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

    public void write(Device device, SComponent component) throws IOException {
        if (!component.isVisible())
            return;
        org.wings.plaf.PrefixAndSuffixDelegate prefixSuffixDelegate = SessionManager.getSession().getCGManager().getPrefixSuffixDelegate();
        prefixSuffixDelegate.writePrefix(device, component);
        writeContent(device, component);
        prefixSuffixDelegate.writeSuffix(device, component);
    }

    public CSSSelector mapSelector(CSSSelector selector) {
        return selector;
    }

    protected void writeContent(Device device, SComponent component) throws IOException {
    }
}
