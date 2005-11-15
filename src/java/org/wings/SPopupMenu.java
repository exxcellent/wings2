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
package org.wings;

import org.wings.plaf.PopupMenuCG;

import java.util.ArrayList;
import java.util.List;

/**
 * A popup or context menu that can be attached to abritritrary components.
 *
 * @author hengels
 * @version $Revision$
 */
public class SPopupMenu
        extends SComponent {
    protected final List menuItems = new ArrayList();
    private double widthScaleFactor = 0.7f;

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
        for (int i = 0; i < menuItems.size(); i++) {
            ((SComponent) menuItems.get(i)).setParentFrame(f);
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
        menuItems.remove(comp);
        comp.setParentFrame(null);
    }

    public void setCG(PopupMenuCG cg) {
        super.setCG(cg);
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
}
