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
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.externalizer.ExternalizeManager;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;

import java.io.IOException;

/**
 * This is the Default XHTML CSS plaf for the SMenuBar Component.
 * @author ole
 */
public class MenuBarCG extends AbstractComponentCG implements
        org.wings.plaf.MenuBarCG, SParentFrameListener {

    private final transient static Log log = LogFactory.getLog(MenuBarCG.class);

    /**
     * the menu javascript needs this script as a helper (browser detection).
     */
    public static final String UTILS_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.utils",
            String.class);
    /**
     * javascript with the menu magic
     */
    private static final String MENU_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.menu",
            String.class);
    /**
     * handler for clicks outside of menu. these clicks possibly close the menu.
     */
    public static final JavaScriptListener BODY_ONCLICK_SCRIPT =
        new JavaScriptListener("onclick", "wpm_handleBodyClicks(event)");

    /* (non-Javadoc)
     * @see org.wings.plaf.ComponentCG#installCG(org.wings.SComponent)
     */
    public void installCG(final SComponent comp) {
        super.installCG(comp);
        SFrame parentFrame = comp.getParentFrame();
        if (parentFrame != null) {
            addListenersToParentFrame(parentFrame);
        }
        comp.addParentFrameListener(this);
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.ComponentCG#uninstallCG(org.wings.SComponent)
     */
    public void uninstallCG(final SComponent comp) {
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.AbstractComponentCG#writeContent(org.wings.io.Device, org.wings.SComponent)
     */
    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenuBar component = (SMenuBar) _c;

//--- code from write-template.
        SMenuBar mbar = (SMenuBar) component;
        int mcount = mbar.getComponentCount();

        printSpacer(device);
        for (int i = 0; i < mcount; i++) {
            SComponent menu = mbar.getComponent(i);
            if (menu.isVisible()) {
                if (menu.isEnabled()) {
                    device.print("<div class=\"SMenu\" onMouseDown=\"javascript:wpm_menu(event,'");
                    device.print(menu.getName());
                    device.print("_pop');\">");
                } else {
                    device.print("<div class=\"SMenu_Disabled\">");
                }
                Utils.write(device, ((SMenu)menu).getText());
                device.print("</div>");
            }
        }
        printSpacer(device);

//--- end code from write-template.
    }

    /**
     * Prints a spacer if necessary, depending on browser compatibility.
     * Is inserted here for possible overwriting in inheriting plafs for
     * other browsers.
     * @param device the device to print on
     * @throws IOException
     */
    protected void printSpacer(final Device device) throws IOException {
        device.print("<div class=\"spacer\">&nbsp;</div>");
    }

    /* (non-Javadoc)
     * @see org.wings.event.SParentFrameListener#parentFrameAdded(org.wings.event.SParentFrameEvent)
     */
    public void parentFrameAdded(SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        addListenersToParentFrame(parentFrame);
    }

    /**
     * adds the necessary listeners to the parent frame. is called by
     * parent frame listener or from install.
     * @param parentFrame
     */
    private void addListenersToParentFrame(SFrame parentFrame) {
        parentFrame.addScriptListener(BODY_ONCLICK_SCRIPT);
        addExternalizedHeader(parentFrame, UTILS_JS, "text/javascript");
        addExternalizedHeader(parentFrame, MENU_JS, "text/javascript");
    }

    /** 
     * adds the file found at the classPath to the parentFrame header with
     * the specified mimeType
     * @param parentFrame the parent frame of the component
     * @param classPath the classPath to look in for the file
     * @param mimeType the mimetype of the file
     */
    private void addExternalizedHeader(SFrame parentFrame, String classPath, String mimeType) {
        ClasspathResource res = new ClasspathResource(classPath, mimeType);
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script(mimeType, new DefaultURLResource(jScriptUrl)));
    }

    /* (non-Javadoc)
     * @see org.wings.event.SParentFrameListener#parentFrameRemoved(org.wings.event.SParentFrameEvent)
     */
    public void parentFrameRemoved(SParentFrameEvent e) {
        //e.getParentFrame().removeScriptListener(BODY_ONCLICK_SCRIPT);
    }
}
