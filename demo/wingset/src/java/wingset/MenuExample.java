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
package wingset;

import org.wings.*;

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

    private final ActionListener menuItemListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            selectionLabel.setText(((SMenuItem) e.getSource()).getText());
        }
    };

    SMenuItem createMenuItem(TreeNode node) {
        SMenuItem item = new SMenuItem(node.toString());
        item.setName(node.toString());
        item.addActionListener(menuItemListener);
        return item;
    }

    SMenu createMenu(TreeNode root) {
        SMenu menu = new SMenu(root.toString());

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
        SForm panel = new SForm();
        selectionLabel = new SLabel("nothing selected");
        panel.add(createMenuBar(HugeTreeModel.ROOT_NODE), "MenuBar");
        panel.add(new SLabel("<html><br>Form components disappear, if needed. Selected Menu: "), "Intro");
        panel.add(selectionLabel, "SelectionLabel");
        panel.add(new SLabel("<html><hr><br>combobox(disappear) :"));
        panel.add(new SComboBox(new DefaultComboBoxModel(ListExample.createElements())), "ComboBox");
        panel.add(new SLabel("<html><br>list(disappear):"));
        SList list = new SList(ListExample.createListModel());
        list.setVisibleRowCount(3);
        panel.add(list, "List");
        panel.add(new SLabel("<html><br>textfield(stay visible):"));
        panel.add(new STextField("wingS is great"), "TextField");
        panel.add(new SLabel("<html><br>textarea(stay visible):"));
        panel.add(new STextArea("wingS is a great framework for implementing complex web applications"), "TextArea");

        return panel;
    }


}


