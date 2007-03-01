package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SMenu;
import org.wings.SMenuItem;
import org.wings.SPopupMenu;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.header.SessionHeaders;
import org.wings.io.Device;
import org.wings.plaf.Update;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class PopupMenuCG extends AbstractComponentCG implements
        org.wings.plaf.PopupMenuCG, SParentFrameListener {

    protected final List headers = new ArrayList();

    public static final JavaScriptListener BODY_ONCLICK_SCRIPT =
        new JavaScriptListener(JavaScriptEvent.ON_CLICK, "wpm_handleBodyClicks(event)");

    public PopupMenuCG() {
        headers.add(Utils.createExternalizedJSHeaderFromProperty(Utils.JS_ETC_MENU));
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addParentFrameListener(this);
    }

    public void uninstallCG(SComponent component) {
        super.uninstallCG(component);
        component.removeParentFrameListener(this);
        if (component.getParentFrame() != null)
            SessionHeaders.getInstance().deregisterHeaders(headers);
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SessionHeaders.getInstance().registerHeaders(headers);
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
        SessionHeaders.getInstance().deregisterHeaders(headers);
    }

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
