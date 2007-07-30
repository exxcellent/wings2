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
package wingset;

import org.wings.*;
import org.wings.plaf.css.Utils;
import org.wings.border.*;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class MenuExample extends WingSetPane {

    private SLabel selectionLabel;
    private SMenuBar menuBar;
    private int shortcutKey = java.awt.event.KeyEvent.VK_A;

    private ComponentControls controls;


    protected SComponent createControls() {
        controls = new MenuControls();
        return controls;
    }

    public SComponent createExample() {

        menuBar = createMenuBar(HugeTreeModel.ROOT_NODE);

        controls.addControllable(menuBar);

        SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.HORIZONTAL);
        boxLayout.setVgap(20);
        boxLayout.setHgap(20);

        SPanel formPanel = new SPanel();
        formPanel.setLayout(boxLayout);
        formPanel.setPreferredSize(SDimension.FULLWIDTH);
        formPanel.add(new SComboBox(new DefaultComboBoxModel(ListExample.createElements())));

        int tColCount = 1;
        SPanel tListsPanel = new SPanel(new SGridLayout(tColCount));
        for (int i = 0; i < tColCount; i++) {
            SList list = new SList(ListExample.createListModel());
            list.setVisibleRowCount(3);
            tListsPanel.add(list);
        }

        formPanel.add(tListsPanel, "List");
        formPanel.add(new STextField("wingS is great"));
        formPanel.add(new STextArea("wingS is a great framework for implementing complex web applications"));

        SPanel messagePanel = new SPanel(new SGridLayout(1));
        messagePanel.add(new SLabel("Form components are overlayed or hidden (IE bug).\n\nSelected Menu: "));
        selectionLabel = new SLabel("<No menue selected yet>");
        selectionLabel.setFont(new SFont(SFont.BOLD));
        selectionLabel.setForeground(Color.RED);
        messagePanel.add(selectionLabel, "SelectionLabel");
        messagePanel.add(new SLabel("\nTry the menu accelerator keys." +
                "\nAlt-A to Alt-Z call menuitem actions (doesn't work on Konqueror)"));
        messagePanel.setBorder(new SEmptyBorder(50,0,0,0));

        SButton addMenu = new SButton("Add new random menu");
        addMenu.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        addMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nr = new Random().nextInt(100);
                SMenu menu = new SMenu("Menu " + nr);
                for (int i = 0; i < 3; ++i) {
                    SMenuItem item = new SMenuItem(new MenuItemAction("Item " + nr + "-" + (i + 1)));
                    menu.add(item);
                }
                menuBar.add(menu);
            }
        });
        SButton removeMenu = new SButton("Remove last added menu");
        removeMenu.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        removeMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = menuBar.getMenuCount();
                if (count > 0) {
                    menuBar.remove(menuBar.getMenu(count - 1));
                }
            }
        });
        SButton addMenuItem = new SButton("Add new random menu item");
        addMenuItem.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        addMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nr = new Random().nextInt(100);
                SMenuItem menuItem = new SMenuItem(new MenuItemAction("Item " + nr));
                SMenu firstMenu = menuBar.getMenu(0);
                if (firstMenu != null) {
                    firstMenu.add(menuItem);
                }
            }
        });
        SButton removeMenuItem = new SButton("Remove last added menu item");
        removeMenuItem.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        removeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SMenu firstMenu = menuBar.getMenu(0);
                if (firstMenu != null && firstMenu.getChildrenCount() > 0) {
                    firstMenu.remove(firstMenu.getChildrenCount() - 1);
                }
            }
        });
        messagePanel.add(new SSpacer(1, 50));
        messagePanel.add(addMenu);
        messagePanel.add(new SSpacer(1, 10));
        messagePanel.add(removeMenu);
        messagePanel.add(new SSpacer(1, 20));
        messagePanel.add(addMenuItem);
        messagePanel.add(new SSpacer(1, 10));
        messagePanel.add(removeMenuItem);

        SPanel mainPanel = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        mainPanel.add(formPanel);
        mainPanel.add(messagePanel);
        mainPanel.setPreferredSize(SDimension.FULLWIDTH);

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
        SMenuItem item = new SMenuItem(new MenuItemAction(node.toString()));
        /* setToolTipText() cannot be used due to JavaScript performance problems,
         * only occurs when using incremental updates and menu
         */
        //item.setToolTipText(node.toString());
        item.setShowAsFormComponent(true);
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
        menu.setShowAsFormComponent(false);
        menu.addActionListener(new MenuItemAction(null));

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
        SBorder border = new SLineBorder(Color.WHITE, 0);
        border.setThickness(1, SConstants.TOP);
        menuBar.setBorder(border);

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
        helpMenuItem.addActionListener(new MenuItemAction("Help on using WingSet"));
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        SMenu aboutMenu = new SMenu("About");
        aboutMenu.setHorizontalAlignment(RIGHT_ALIGN);
        SMenuItem aboutMenuItem = new SMenuItem("About WingSet");
        aboutMenuItem.addActionListener(new MenuItemAction("About WingSet"));
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

    private class MenuItemAction
        extends AbstractAction
    {
        public MenuItemAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            selectionLabel.setText(((SMenuItem) e.getSource()).getText());
        }
    }
}