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
package org.wings;

import org.wings.io.Device;
import org.wings.plaf.MenuBarCG;
import org.wings.plaf.MenuCG;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Aggregates various {@link SMenuItem}s under a main header entry.
 *
 * @author <a href="mailto:andre.lison@general-bytes.com">Andre Lison</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SMenu extends SMenuItem {
    private boolean popupMenuVisible = false;
    protected final List menuItems = new ArrayList();
    private double widthScaleFactor = 0.7f;

    public SMenu(String text) {
        super(text);
    }

    public SMenu() {
        super();
    }

    public SMenu(SIcon i) {
        super(i);
    }

    public SMenu(String text, SIcon icon) {
        super(text, icon);
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(SMenuItem menuItem) {
        menuItems.add(menuItem);
        menuItem.setParentMenu(this);
        reload();
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(SComponent menuItem) {
        menuItems.add(menuItem);
        menuItem.setParentFrame(getParentFrame());
        reload();
    }

    public void setParentFrame(SFrame f) {
        if (getParentFrame() == null && f != null) {
            reload();
        }
        if (f != null || (f == null && !getSession().getMenuManager().isMenuLinked(this))) {
            super.setParentFrame(f);
            for (int i = 0; i < menuItems.size(); i++) {
                ((SComponent) menuItems.get(i)).setParentFrame(f);
            }
        }
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(String menuitem) {
        this.add(new SMenuItem(menuitem));
    }

    public SComponent getMenuComponent(int pos) {
        return (SComponent) menuItems.get(pos);
    }

    /**
     * Return the number of items on the menu, including separators.
     */
    public int getMenuComponentCount() {
        return menuItems.size();
    }

    /**
     * Remove all {@link SMenuItem} from this menu.
     */
    public void removeAll() {
        while (menuItems.size() > 0) {
            remove(0);
        }
    }

    /**
     * Removes the menu item at specified index from the menu.
     */
    public void remove(int pos) {
        remove(getMenuComponent(pos));
    }

    /**
     * removes a specific menu item component.
     */
    public void remove(SComponent comp) {
        if (menuItems.remove(comp)) {
            reload();
        }
        comp.setParentFrame(null);
    }

    public void setCG(MenuBarCG cg) {
        super.setCG(cg);
    }

    /**
     * Sets the visibility of the menu's popup.
     * If the menu is not enabled, this method will have no effect.
     *
     * @param b a boolean value -- true to make the menu visible, false to hide it
     */
    public void setPopupMenuVisible(boolean b) {
        if (!isEnabled())
            return;
        popupMenuVisible = b;
    }

    /**
     * Returns true if the menu's popup window is visible.
     *
     * @return true if the menu is visible, else false.
     */
    public boolean isPopupMenuVisible() {
        return popupMenuVisible;
    }

    /**
     * Returns the scale factor for the width of the Menu components.
     * The length of the children texts is multiplied by this factor and set as
     * width (in em) for the children.
     *
     * @return Returns the widthScaleFactor.
     */
    public double getWidthScaleFactor() {
        return widthScaleFactor;
    }
    /**
     * Sets the scale factor for the width of the Menu components.
     * The length of the children texts is multiplied by this factor and set as
     * width (in em) for the children.
     *
     * Default value is 0.8.
     *
     * @param widthScaleFactor The widthScaleFactor to set.
     */
    public void setWidthScaleFactor(double widthScaleFactor) {
        this.widthScaleFactor = widthScaleFactor;
    }

    /**
     * @return Returns the amount of children elements.
     */
    public int getChildrenCount() {
        return menuItems.size();
    }

    /** gets the n'th child of the menu. If the index is too high, returns
     * null.
     * @param index the index of the child to return
     * @return the n'th child.
     */
    public SComponent getChild(int index) {
        if (getChildrenCount() > index) {
            return (SComponent)menuItems.get(index);
        } else {
            return null;
        }
    }

    public void writePopup(Device device) throws IOException {
        ((MenuCG)getCG()).writePopup(device, this);
    }

    public void setAccelerator(KeyStroke keyStroke) {
        throw new UnsupportedOperationException("Only invoke this on SMenuItem.");
    }

    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            Set menuLinks = getSession().getMenuManager().getMenueLinks(this);
            for (Iterator i = menuLinks.iterator(); i.hasNext();) {
                ((SComponent) i.next()).reload();
            }
        }
        super.setEnabled(enabled);
    }

}
