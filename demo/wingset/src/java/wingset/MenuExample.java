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
package wingset;

import org.wings.*;
import org.wings.plaf.MenuBarCG;
import org.wings.plaf.MenuCG;
import org.wings.plaf.MenuItemCG;

import javax.swing.*;
import javax.swing.tree.TreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class MenuExample extends WingSetPane {

    private SLabel selectionLabel;
    private SMenuBar menuBar;

    private final ActionListener menuItemListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            selectionLabel.setText(((SMenuItem) e.getSource()).getText());
        }
    };

    SMenuItem createMenuItem(TreeNode node) {
        SMenuItem item = new SMenuItem(node.toString());
        item.setToolTipText(node.toString());
        item.addActionListener(menuItemListener);
        return item;
    }

    SMenu createMenu(TreeNode root) {
        SMenu menu = new SMenu(root.toString());
        menu.addActionListener(menuItemListener);

        for (int i = 0; i < root.getChildCount(); i++) {
            TreeNode node = root.getChildAt(i);
            if (node.isLeaf()) {
                menu.add(createMenuItem(node));
            } else {
                menu.add(createMenu(node));
            }
        }

        return menu;
    }

    SMenuBar createMenuBar(TreeNode root) {
        SMenuBar menuBar = new SMenuBar();

        for (int i = 0; i < root.getChildCount(); i++) {
            TreeNode node = root.getChildAt(i);
            if (node.isLeaf()) {
                menuBar.add(createMenuItem(node));
            } else {
                menuBar.add(createMenu(node));
            }
        }

        return menuBar;
    }

    public SComponent createExample() {
        ComponentControls controls = new ComponentControls();
        SForm panel = new SForm();
        selectionLabel = new SLabel("nothing selected");
        menuBar = createMenuBar(HugeTreeModel.ROOT_NODE);
        controls.addSizable(menuBar);
        panel.add(controls);
        panel.add(menuBar, "MenuBar");
        panel.add(new SLabel("<html><br>Form components are overlayed in css version, disappear in js version. Selected Menu: "), "Intro");
        panel.add(selectionLabel, "SelectionLabel");
        panel.add(new SLabel("<html><hr><br>combobox :"));
        panel.add(new SComboBox(new DefaultComboBoxModel(ListExample.createElements())), "ComboBox");
        panel.add(new SLabel("<html><br>list:"));
        SList list = new SList(ListExample.createListModel());
        list.setVisibleRowCount(3);
        panel.add(list, "List");
        panel.add(new SLabel("<html><br>textfield(stay visible):"));
        panel.add(new STextField("wingS is great"), "TextField");
        panel.add(new SLabel("<html><br>textarea(stay visible):"));
        panel.add(new STextArea("wingS is a great framework for implementing complex web applications"), "TextArea");

        SButtonGroup cgGroup = new SButtonGroup();

        final SRadioButton cssMenu=new SRadioButton("css menu");
        final SRadioButton jsMenu=new SRadioButton("javascript menu");
        cgGroup.add(cssMenu);
        cgGroup.add(jsMenu);
        controls.add(cssMenu);
        controls.add(jsMenu);

        cgGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( jsMenu.isSelected() ) {
                        setJsCG();
                    } else if (cssMenu.isSelected()){
                        //tree.setCG(getSession().getCGManager().getCG("TreeCG"));
                        setCssCG();
                    }
                        
                }
            });
        cssMenu.setSelected(false);
        
        
        return panel;
    }

    /**
     * 
     */
    protected void setJsCG() {
//        menuBar.setAllCGs(
//                (MenuBarCG) getSession().getCGManager().getCG("org.wings.SMenuBarCSS"),
//                (MenuCG) getSession().getCGManager().getCG("org.wings.SMenuCSS"),
//                (MenuItemCG) getSession().getCGManager().getCG("org.wings.SMenuItem"));
        menuBar.setCG((MenuBarCG) getSession().getCGManager().getCG("org.wings.SMenuBarJS"));
        menuBar.setMenuCG((MenuCG) getSession().getCGManager().getCG("org.wings.SMenuJS"));
        menuBar.setMenuItemCG((MenuItemCG) getSession().getCGManager().getCG("org.wings.SMenuItem"));
    }

    /**
     * 
     */
    protected void setCssCG() {
//        menuBar.setAllCGs(
//                (MenuBarCG) getSession().getCGManager().getCG("org.wings.SMenuBarJS"),
//                (MenuCG) getSession().getCGManager().getCG("org.wings.SMenuJS"),
//                (MenuItemCG) getSession().getCGManager().getCG("org.wings.SMenuItem"));
        menuBar.setCG((MenuBarCG) getSession().getCGManager().getCG("org.wings.SMenuBarCSS"));
        menuBar.setMenuCG((MenuCG) getSession().getCGManager().getCG("org.wings.SMenuCSS"));
        menuBar.setMenuItemCG((MenuItemCG) getSession().getCGManager().getCG("org.wings.SMenuItem"));
   }


}


