/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.plaf.MenuBarCG;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:andre.lison@general-bytes.com">Andre Lison</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SMenu extends SMenuItem {
    private boolean popupMenuVisible = false;
    protected final List menuItems = new ArrayList();

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
    }

    /**
     * Add a menu item to this menu.
     */
    public void add(SComponent menuItem) {
        menuItems.add(menuItem);
        menuItem.setParentFrame(getParentFrame());
    }

    public void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        for (int i = 0; i < menuItems.size(); i++)
            ((SComponent) menuItems.get(i)).setParentFrame(f);
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
        menuItems.remove(comp);
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
}


