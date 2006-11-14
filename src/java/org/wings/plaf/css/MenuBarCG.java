/*
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
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;

import java.io.IOException;

/**
 * This is the Default XHTML CSS plaf for the SMenuBar Component.
 * @author ole
 */
public class MenuBarCG extends AbstractComponentCG implements
        org.wings.plaf.MenuBarCG, SParentFrameListener {

    private static final long serialVersionUID = 1L;

    /**
     * javascript with the menu magic
     */
    private static final String MENU_JS = (String) ResourceManager.getObject("JScripts.menu", String.class);
    /**
     * handler for clicks outside of menu. these clicks possibly close the menu.
     */
    public static final JavaScriptListener BODY_ONCLICK_SCRIPT = new JavaScriptListener("onclick", "wpm_handleBodyClicks(event)");

    private HeaderUtil headerUtil = new HeaderUtil();

    public MenuBarCG() {
        ClassPathResource resource = new ClassPathResource(MENU_JS, "text/javascript");
        String url = SessionManager.getSession().getExternalizeManager().externalize(resource, ExternalizeManager.GLOBAL);
        headerUtil.addHeader(new Script("text/javascript", new DefaultURLResource(url)));
    }

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

        headerUtil.installHeaders();
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.ComponentCG#uninstallCG(org.wings.SComponent)
     */
    public void uninstallCG(final SComponent comp) {
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.AbstractComponentCG#writeContent(org.wings.io.Device, org.wings.SComponent)
     */
    public void writeInternal(final Device device, final SComponent component) throws IOException {

        final SMenuBar mbar = (SMenuBar) component;
        final int mcount = mbar.getComponentCount();
        writeTablePrefix(device, component);

        printSpacer(device);         /* clear:both to ensuer menubar surrounds all SMenu entries */

        // Left-aligned menues must rendered first in natural order
        for (int i = 0; i < mcount; i++) {
            final SMenu menu = mbar.getMenu(i);
            if (menu != null && menu.isVisible() && menu.getHorizontalAlignment() != SConstants.RIGHT_ALIGN) {
               renderSMenu(device, menu, false);
            }
        }
        // Right-aligned menues must rendered first in revers order due to float:right
        for (int i = mcount-1; i >= 0 ; i--) {
            final SMenu menu = mbar.getMenu(i);
            if (menu != null && menu.isVisible() && menu.getHorizontalAlignment() == SConstants.RIGHT_ALIGN) {
               renderSMenu(device, menu, true);
            }
        }

        printSpacer(device);      /* clear:both to ensuer menubar surrounds all SMenu entries */

        writeTableSuffix(device, component);
    }

    /* Renders the DIV representing a top SMenu item inside the menu bar. */
    protected void renderSMenu(final Device device, final SMenu menu, boolean rightAligned) throws IOException {
        if (menu.isEnabled()) {
            device.print("<div class=\"SMenu\" onMouseDown=\"wpm_menu(event,'");
            device.print(menu.getName());
            device.print("_pop');\" onMouseOver=\"wpm_changeMenu(event,'");
            device.print(menu.getName());
            device.print("_pop');\"");
        } else {
            device.print("<div class=\"SMenu_Disabled\"");
        }
        if (rightAligned)
            device.print(" style=\"float:right\"");
        device.print(">");
        Utils.write(device, menu.getText());
        device.print("</div>");
    }

    /**
     * Prints a spacer if necessary, depending on browser compatibility.
     * Is inserted here for possible overwriting in inheriting plafs for
     * other browsers.
     * @param device the device to print on
     * @throws IOException
     */
    protected void printSpacer(final Device device) throws IOException {
        device.print("<div class=\"spacer\"></div>");
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
    }

    /* (non-Javadoc)
     * @see org.wings.event.SParentFrameListener#parentFrameRemoved(org.wings.event.SParentFrameEvent)
     */
    public void parentFrameRemoved(SParentFrameEvent e) {
        //e.getParentFrame().removeScriptListener(BODY_ONCLICK_SCRIPT);
    }
}
