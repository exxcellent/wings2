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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.*;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;

public class PopupMenuCG extends AbstractComponentCG implements SConstants, org.wings.plaf.MenuBarCG, SParentFrameListener {
    private final transient static Log log = LogFactory.getLog(PopupMenuCG.class);

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

    public static final String UTILS_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.utils",
            String.class);
    private static final String MENU_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.menu",
            String.class);
    private static final JavaScriptListener BODY_ONCLICK_SCRIPT =
        new JavaScriptListener("onclick", "wpm_handleBodyClicks(event)");

    protected void writePopup(final Device device, SPopupMenu menu)
            throws IOException {
        if (menu.isEnabled()) {
            String componentId = menu.getName();
            device.print("<ul");
            writeListAttributes(device, menu);
            device.print(" id=\"");
            device.print(componentId);
            device.print("_pop\">");
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                SComponent menuItem = menu.getMenuComponent(i);
    
                if (menuItem.isVisible()) {
                    device.print("<li");
                    if (menuItem instanceof SMenu) {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenu\"");
                        } else {
                            device.print(" class=\"SMenu_Disabled\"");
                        }
                    } else {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenuItem\"");
                        } else {
    
                            device.print(" class=\"SMenuItem_Disabled\"");
                        }
                    }
                    device.print(">");
                    if (menuItem instanceof SMenuItem) {
                            device.print("<a");
                            if (menuItem.isEnabled()) {
                                device.print(" href=\"");
                                writeAnchorAddress(device, (SMenuItem) menuItem);
                                device.print("\"");
                            }
                            if (menuItem instanceof SMenu) {
                                if (menuItem.isEnabled()) {
                                    device.print(" class=\"x\"");
                                } else {
                                    device.print(" class=\"y\"");
                                }
                            }
                            device.print(">");
                    }
                    menuItem.write(device);
                    if (menuItem instanceof SMenuItem) {
                        device.print("</a>");
                    }
                    if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                        ((SMenu)menuItem).writePopup(device);
                    }
                    device.print("</li>\n");
                }
            }
            device.print("</ul>");
        }
        device.print("\n");
    }

    /** 
     * Convenience method to keep differences between default and msie
     * implementations small
     * @param device
     * @param menu
     * @throws IOException
     */
    protected void writeListAttributes(Device device, SPopupMenu menu) throws IOException {
        // do nothing...
    }

    protected void writeAnchorAddress(Device d, SAbstractButton abstractButton)
            throws IOException {
        RequestURL addr = abstractButton.getRequestURL();
        addr.addParameter(abstractButton,
                abstractButton.getToggleSelectionParameter());
        addr.write(d);
    }

    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        SPopupMenu menu = (SPopupMenu) _c;
        writePopup(device, menu);
    }

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
        parentFrame.addHeader(new Script("JavaScript", mimeType, new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
}
