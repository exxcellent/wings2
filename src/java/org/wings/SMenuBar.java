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

import org.wings.plaf.MenuBarCG;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * An implementation of a menu bar. You add SMenu objects to the
 * menu bar to construct a menu. When the user selects a SMenu
 * object, its associated {@link org.wings.SMenu} is displayed, allowing the
 * user to select one of the {@link org.wings.SMenuItem}s on it.
 * <p/>
 * Component are rendered in the order of the container. If a component is right
 * aligned, every following components are also right aligned. So you have to
 * sort the components in the order you want and have to take care that te
 * components are sorted by their horizontal alignment
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @see SMenu
 * @see SMenuItem
 */
public class SMenuBar extends SContainer {
    /*
     * Model for the selected subcontrol
     */
    private transient SingleSelectionModel selectionModel;

    private boolean paintBorder = true;
    private Insets margin = null;

    /**
     * Creates a new menu bar.
     */
    public SMenuBar() {
        super();
        setSelectionModel(new DefaultSingleSelectionModel());
    }

    /**
     * Returns the model object that handles single selections.
     *
     * @return the SingleSelectionModel in use
     * @see SingleSelectionModel
     */
    public SingleSelectionModel getSelectionModel() {
        return selectionModel;
    }

    /**
     * Set the model object to handle single selections.
     *
     * @param model the SingleSelectionModel to use
     * description: The selection model, recording which child is selected.
     * @see SingleSelectionModel
     */
    public void setSelectionModel(SingleSelectionModel model) {
        this.selectionModel = model;
    }


    /**
     * Appends the specified menu to the end of the menu bar.
     *
     * @param c the SMenu component to add
     */
    public SMenuItem add(SMenuItem c) {
        super.add(c);
        return c;
    }

    /**
     * Gets the menu at the specified position in the menu bar.
     *
     * @param index an int giving the position in the menu bar, where
     *              0 is the first position
     * @return the SMenu at that position
     */
    public SMenuItem getMenuItem(int index) {
        SComponent c = (SComponent) super.getComponent(index);
        if (c instanceof SMenuItem)
            return (SMenuItem) c;
        return null;
    }

    /**
     * Returns the number of items in the menu bar.
     *
     * @return the number of items in the menu bar
     */
    public int getMenuCount() {
        return getComponentCount();
    }

    /**
     * Returns the index of the specified component.
     *
     * @param c the <code>SComponent</code> to find
     * @return an integer giving the component's position, where 0 is first;
     *         or -1 if it can't be found
     */
    public int getComponentIndex(SComponent c) {
        int ncomponents = this.getComponentCount();
        for (int i = 0; i < ncomponents; i++) {
            SComponent comp = getComponent(i);
            if (comp == c)
                return i;
        }
        return -1;
    }

    /**
     * Sets the currently selected component, producing a
     * a change to the selection model.
     *
     * @param sel the SComponent to select
     */
    public void setSelected(SComponent sel) {
        SingleSelectionModel model = getSelectionModel();
        int index = getComponentIndex(sel);
        model.setSelectedIndex(index);
    }

    /**
     * Returns true if the MenuBar currently has a component selected
     *
     * @return true if a selection has been made, else false
     */
    public boolean isSelected() {
        return selectionModel.isSelected();
    }

    /**
     * Returns true if the Menubar's border should be painted.
     *
     * @return true if the border should be painted, else false
     */
    public boolean isBorderPainted() {
        return paintBorder;
    }

    /**
     * Sets whether the border should be painted.
     *
     * @param b if true and border property is not null, the border is painted.
     * attribute: visualUpdate true
     * description: Whether the border should be painted.
     * @see #isBorderPainted
     */
    public void setBorderPainted(boolean b) {
        paintBorder = b;
    }

    /**
     * Paint the menubar's border if BorderPainted property is true.
     * 
     * @param g the Graphics context to use for painting
     * @see SComponent#paint
     * @see SComponent#setBorder
     */
    /*
    protected void paintBorder(Graphics g) {
        if (isBorderPainted()) {
            super.paintBorder(g);
        }
    }
    */

    /**
     * Sets the margin between the menubar's border and
     * its menus. Setting to null will cause the menubar to
     * use the default margins.
     *
     * @param m an Insets object containing the margin values
     * attribute: visualUpdate true
     * description: The space between the menubar's border and its contents
     * @see Insets
     */
    public void setMargin(Insets m) {
        this.margin = m;
        /*
          if (old == null || !m.equals(old)) {
                revalidate();
                repaint();
          }
        */
    }

    /**
     * Returns the margin between the menubar's border and
     * its menus.
     *
     * @return an Insets object containing the margin values
     * @see Insets
     */
    public Insets getMargin() {
        if (margin == null) {
            return new Insets(0, 0, 0, 0);
        } else {
            return margin;
        }
    }


    /**
     * Implemented to be a MenuElement -- does nothing.
     */
    public void menuSelectionChanged(boolean isIncluded) {
    }

    /**
     * Returns a string representation of this SMenuBar. This method
     * is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not
     * be <code>null</code>.
     *
     * @return a string representation of this SMenuBar.
     */
    public String paramString() {
        String paintBorderString = (paintBorder ?
                "true" : "false");
        String marginString = (margin != null ?
                margin.toString() : "");

        return super.paramString() +
                ",margin=" + marginString +
                ",paintBorder=" + paintBorderString;
    }

    public void setCG(MenuBarCG cg) {
        super.setCG(cg);
    }
    
    /**
     * Close all currently open menus.
     */
    public void closeAllMenus() {
        /*        for ( int i = 0; i < fComponents.size(); i++ ) {
            if ( fComponents.get(i) instanceof SMenu ) {
                ((SMenu) fComponents.get(i)).setActive( false );
            }
            }*/
    }

    public ArrayList getMenus() {
        ArrayList menus = new ArrayList();
        if (isVisible()) {
            SPopupMenu pmenu = getComponentPopupMenu();
            if (pmenu != null) {
                menus.add(pmenu);
            }
            for (int i = 0; i < getComponentCount(); i++) {
                SMenu menu = (SMenu)getComponent(i);
                if (menus.contains(menu)) {
                    remove(menu);
                }
                menus.add(menu);
            }
        }
        return menus;
    }
}


