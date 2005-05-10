// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/MenuBar.plaf'
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
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;

import java.io.IOException;

public class MenuBarCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.MenuBarCG, SParentFrameListener {

    private final transient static Log log = LogFactory.getLog(MenuBarCG.class);

    private static final String UTILS_JS = "org/wings/plaf/css/Utils.js";
    private static final String MENU_JS = "org/wings/plaf/css/Menu.js";
    public static final JavaScriptListener BODY_ONCLICK_SCRIPT =
        new JavaScriptListener("onClick", "wpm_handleBodyClicks(event)");

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        SFrame parentFrame = comp.getParentFrame();
        if (parentFrame != null) {
            addListenersToParentFrame(parentFrame);
        }
        comp.addParentFrameListener(this);
    }

    public void uninstallCG(final SComponent comp) {
    }

    public void writeContent(final Device device,
                             final SComponent _c)
            throws IOException {
        final SMenuBar component = (SMenuBar) _c;

//--- code from write-template.
        SMenuBar mbar = (SMenuBar) component;
        int mcount = mbar.getComponentCount();

        device.print("<div class=\"spacer\">&nbsp;</div>");
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
        device.print("<div class=\"spacer\">&nbsp;</div>");

//--- end code from write-template.
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
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res);
        parentFrame.addHeader(new Script("JavaScript", mimeType, new DefaultURLResource(jScriptUrl)));
    }

    /* (non-Javadoc)
     * @see org.wings.event.SParentFrameListener#parentFrameRemoved(org.wings.event.SParentFrameEvent)
     */
    public void parentFrameRemoved(SParentFrameEvent e) {
        //e.getParentFrame().removeScriptListener(BODY_ONCLICK_SCRIPT);
    }
}
