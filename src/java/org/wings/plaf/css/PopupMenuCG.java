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
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SMenuItem;
import org.wings.SPopupMenu;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.Update;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.io.IOException;

public final class PopupMenuCG extends AbstractComponentCG implements
        org.wings.plaf.PopupMenuCG, SParentFrameListener {
    private static final long serialVersionUID = 1L;

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

    private static final String MENU_JS = (String) ResourceManager.getObject("JS.menu", String.class);
    private static final JavaScriptListener BODY_ONCLICK_SCRIPT =
        new JavaScriptListener(JavaScriptEvent.ON_CLICK, "wpm_handleBodyClicks(event)");

    protected void writePopup(final Device device, SPopupMenu menu)
            throws IOException {
        if (menu.isEnabled()) {
            String componentId = menu.getName();
            device.print("<ul");
            writeListAttributes(device, menu);
            device.print(" id=\"");
            device.print(componentId);
            device.print("_pop\" class=\"");
            device.print(menu.getStyle());
            device.print("\">");
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                SComponent menuItem = menu.getMenuComponent(i);

                if (menuItem.isVisible()) {
                    device.print("\n <li");
                    if (menuItem instanceof SMenu) {
                        if (menuItem.isEnabled()) {
                            device.print(" class=\"SMenu\"");
                            printScriptHandlers(device, menuItem);
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
                        device.print("<a href=\"#\"");
                        if (menuItem instanceof SMenu) {
                            if (menuItem.isEnabled()) {
                                device.print(" class=\"x sub\"");
                            } else {
                                device.print(" class=\"y sub\"");
                            }
                        }
                        Utils.printClickability(
                                device,
                                menuItem,
                                ((SMenuItem) menuItem).getToggleSelectionParameter(),
                                menuItem.isEnabled(),
                                menuItem.getShowAsFormComponent());
                        device.print(">");
                    }
                    menuItem.write(device);
                    if (menuItem instanceof SMenuItem) {
                        device.print("</a>");
                    }
                    if (menuItem.isEnabled() && menuItem instanceof SMenu) {
                        menuItem.putClientProperty("popup", Boolean.TRUE);
                        menuItem.write(device);
                        menuItem.putClientProperty("popup", null);
                    }
                    device.print("</li>");
                }
            }
            device.print("</ul>");
        }
        device.print("\n");
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.PopupMenuCG#writeListAttributes(org.wings.io.Device, org.wings.SPopupMenu)
     */
    protected void writeListAttributes(final Device device, SPopupMenu menu) throws IOException {
        // calculate max length of children texts for sizing of layer
        int maxLength = 0;
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            if (!(menu.getMenuComponent(i) instanceof SMenuItem))
                continue;
            String text = ((SMenuItem)menu.getMenuComponent(i)).getText();
            if (text != null && text.length() > maxLength) {
                maxLength = text.length();
                if (menu.getMenuComponent(i) instanceof SMenu) {
                        maxLength = maxLength + 2; //graphics
                }
            }
        }
        device.print(" style=\"width:");
        String stringLength = String.valueOf(maxLength * menu.getWidthScaleFactor());
        device.print(stringLength.substring(0,stringLength.lastIndexOf('.')+2));
        device.print("em;\"");
    }

    protected void printScriptHandlers(Device device, SComponent menuItem) throws IOException {
        device.print(" onmouseover=\"wpm_openMenu(event, '");
        device.print(((SMenu)menuItem).getName());
        device.print("_pop','");
        device.print(((SMenu)menuItem).getParentMenu().getName());
        device.print("_pop');\"");
    }

    public void writeInternal(final Device device, final SComponent _c)
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
        ClassPathResource res = new ClassPathResource(classPath, mimeType);
        String jScriptUrl = parentFrame.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script(mimeType, new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
    }

    public Update getComponentUpdate(SComponent component) {
        return new ComponentUpdate(component);
    }

    protected class ComponentUpdate extends AbstractComponentCG.ComponentUpdate {

        public ComponentUpdate(SComponent component) {
            super(component);
        }

        public Handler getHandler() {
            UpdateHandler handler = (UpdateHandler) super.getHandler();

            handler.setName("componentMenu");
            handler.setParameter(0, component.getName() + "_pop");

            return handler;
        }

    }

}
