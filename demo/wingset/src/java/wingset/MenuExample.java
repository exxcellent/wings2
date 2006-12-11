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
package wingset;

import org.wings.*;
import org.wings.border.SEmptyBorder;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class MenuExample extends WingSetPane {

    private SLabel selectionLabel;
    private SMenuBar menuBar;
    private int shortcutKey = java.awt.event.KeyEvent.VK_A;

    private final ActionListener menuItemListener = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            selectionLabel.setText(((SMenuItem) e.getSource()).getText());
        }
    };
    private ComponentControls controls;


    protected SComponent createControls() {
        controls = new MenuControls();
        return controls;
    }

    public SComponent createExample() {

        menuBar = createMenuBar(HugeTreeModel.ROOT_NODE);

        controls.addControllable(menuBar);

        SPanel formPanel = new SPanel();
        formPanel.setLayout(new SBoxLayout(SBoxLayout.VERTICAL));
        formPanel.setPreferredSize(SDimension.FULLWIDTH);
        formPanel.add(new SLabel("Combobox:"));
        formPanel.add(new SComboBox(new DefaultComboBoxModel(ListExample.createElements())));

        formPanel.add(new SLabel(" \nList:"));

        int tColCount = 2;
        SPanel tListsPanel = new SPanel(new SGridLayout(tColCount));
        for (int i = 0; i < tColCount; i++) {
            SList list = new SList(ListExample.createListModel());
            list.setVisibleRowCount(3);
            tListsPanel.add(list);
        }

        formPanel.add(tListsPanel, "List");
        formPanel.add(new SLabel(" \ntextfield(stay visible):"));
        formPanel.add(new STextField("wingS is great"));
        formPanel.add(new SLabel(" \ntextarea(stay visible):"));
        formPanel.add(new STextArea("wingS is a great framework for implementing complex web applications"));

        SList list2 = new SList(ListExample.createListModel());
        list2.setVisibleRowCount(7);
        formPanel.add(list2, "List2");

        SPanel messagePanel = new SPanel(new SGridLayout(1));
        messagePanel.add(new SLabel("Form components are overlayed or hidden (IE bug).\n\nSelected Menu: "));
        selectionLabel = new SLabel("<No menue selected yet>");
        selectionLabel.setFont(new SFont(SFont.BOLD));
        selectionLabel.setForeground(Color.RED);
        messagePanel.add(selectionLabel, "SelectionLabel");
        messagePanel.add(new SLabel("\nTry the menu accelerator keys." +
                "\nCtrl-A to Ctrl-Z call menuitem actions (doesn't work on Konqueror)"));
        messagePanel.setBorder(new SEmptyBorder(0,20,0,0));

        SPanel mainPanel = new SPanel(new SBoxLayout(SBoxLayout.HORIZONTAL));
        mainPanel.add(formPanel);
        mainPanel.add(messagePanel);

        SPanel panel = new SPanel(new SBorderLayout());
        panel.add(menuBar, SBorderLayout.NORTH);
        panel.add(mainPanel, SBorderLayout.CENTER);
        panel.setVerticalAlignment(SConstants.TOP_ALIGN);

        return panel;
    }

    protected void setMenuItemsEnabled(boolean enabled) {
        if (menuBar.getComponentCount() > 1) {
            SMenuItem first = (SMenuItem) menuBar.getComponent(0);
            SMenuItem last = (SMenuItem) menuBar.getComponent(menuBar.getComponentCount() - 1);
            recursiveMenuItemSwitch(first, last, enabled);
        } else if (menuBar.getComponentCount() == 1) {
            menuBar.getComponent(0).setEnabled(enabled);
        }
    }

    private void recursiveMenuItemSwitch(SMenuItem first, SMenuItem last, boolean enabled) {
        last.setEnabled(enabled);
        if (first instanceof SMenu) {
            SMenu parent = (SMenu) first;
            if (parent.getChildrenCount() > 1) {
                SMenuItem firstChild = (SMenuItem) parent.getChild(0);
                SMenuItem lastChild = (SMenuItem) parent.getChild(parent.getChildrenCount() - 1);
                recursiveMenuItemSwitch(firstChild, lastChild, enabled);
            } else if (((SMenu) first).getChildrenCount() == 1) {
                parent.getChild(0).setEnabled(enabled);
            }
        }
    }

    SMenuItem createMenuItem(TreeNode node) {
        SMenuItem item = new SMenuItem(node.toString());
        /* setToolTipText() cannot be used due to JavaScript performance problems,
         * only occurs when using incremental updates and menu         
         */
        //item.setToolTipText(node.toString()); 
        item.addActionListener(menuItemListener);
        if (shortcutKey != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(shortcutKey,
                    java.awt.Event.ALT_MASK));
            if (shortcutKey == java.awt.event.KeyEvent.VK_Z) {
                shortcutKey = 0;
            } else {
                shortcutKey++;
            }
        }
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

        // Test right aligned menu
        SMenu helpMenu = new SMenu("Help");
        helpMenu.setHorizontalAlignment(RIGHT_ALIGN);
        SMenuItem helpMenuItem = new SMenuItem("Help on using WingSet");
        helpMenuItem.addActionListener(menuItemListener);
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        SMenu aboutMenu = new SMenu("About");
        aboutMenu.setHorizontalAlignment(RIGHT_ALIGN);
        SMenuItem aboutMenuItem = new SMenuItem("About WingSet");
        aboutMenuItem.addActionListener(menuItemListener);
        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);

        return menuBar;
    }

    class MenuControls extends ComponentControls {
        public MenuControls() {
            widthTextField.setText("100%");
            removeGlobalControl(fontComboBox);
            removeGlobalControl(foregroundComboBox);
            removeGlobalControl(backgroundComboBox);
            removeGlobalControl(formComponentCheckBox);

            final SCheckBox disableSomeMenus = new SCheckBox("Disable some Menus ");
            disableSomeMenus.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setMenuItemsEnabled(!disableSomeMenus.isSelected());
                }
            });
            addControl(disableSomeMenus);
        }
    }
}