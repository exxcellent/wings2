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

import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFont;
import org.wings.SLabel;
import org.wings.SMenu;
import org.wings.SMenuItem;
import org.wings.SPanel;
import org.wings.SPopupMenu;
import org.wings.SSpacer;
import org.wings.STextField;
import org.wings.SURLIcon;
import org.wings.border.SEmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Holger Engels
 */
public class PopupMenuExample extends WingSetPane {

    private SLabel selection;

    private final ActionListener menuItemListener = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            selection.setText(((SMenuItem) e.getSource()).getText());
        }
    };


    protected SComponent createControls() {
        return null;
    }

    public SComponent createExample() {

        final SPopupMenu menu = new SPopupMenu();
        final SMenuItem cutMenuItem = createMenuItem("Cut");
        cutMenuItem.setShowAsFormComponent(true);
        menu.add(cutMenuItem);
        menu.add(createMenuItem("Copy"));
        menu.add(createMenuItem("Paste"));

        SMenu subMenu = new SMenu("Help");
        subMenu.add(createMenuItem("About"));
        subMenu.add(createMenuItem("Topics"));
        menu.add(subMenu);

        final SPopupMenu menu2 = new SPopupMenu();
        menu2.add(createMenuItem("Open"));
        menu2.add(createMenuItem("Save"));
        menu2.add(createMenuItem("Close"));

        final SLabel testLabel = new SLabel("This label has a context menu.");
        testLabel.setComponentPopupMenu(menu);
        final STextField testLabel2 = new STextField("This textfield has the same context menu.");
        testLabel2.setColumns(testLabel2.getText().length());
        testLabel2.setComponentPopupMenu(menu);
        final SLabel testLabel3 = new SLabel(" This label has another context menu.", new SURLIcon("../icons/cowSmall.gif"));
        testLabel3.setComponentPopupMenu(menu2);
        SLabel selectionLabel = new SLabel("Selected Menu: ");
        selectionLabel.setBorder(new SEmptyBorder(20,0,0,0));
        selection = new SLabel("none");
        selection.setFont(new SFont(SFont.BOLD));

        SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.VERTICAL);
        boxLayout.setVgap(10);

        final SPanel all = new SPanel(boxLayout);

        final String titleMenueEnabled = " first context menu";
        final SButton toggleMenuEnabled = new SButton("Disable" +  titleMenueEnabled);
        toggleMenuEnabled.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        toggleMenuEnabled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (menu.isEnabled()) {
                    menu.setEnabled(false);
                    toggleMenuEnabled.setText("Enable" + titleMenueEnabled);
                } else {
                    menu.setEnabled(true);
                    toggleMenuEnabled.setText("Disable" + titleMenueEnabled);
                }
            }
        });
        final String titleContextMenu = " context menu from first label";
        final SButton toggleContextMenu = new SButton("Remove" + titleContextMenu);
        toggleContextMenu.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        toggleContextMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testLabel.getComponentPopupMenu() != null) {
                    testLabel.setComponentPopupMenu(null);
                    toggleContextMenu.setText("Add" + titleContextMenu);
                } else {
                    testLabel.setComponentPopupMenu(menu);
                    toggleContextMenu.setText("Remove" + titleContextMenu);
                }
            }
        });
        final String titleContextMenu2 = " context menu from textfield";
        final SButton toggleContextMenu2 = new SButton("Remove" +  titleContextMenu2);
        toggleContextMenu2.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        toggleContextMenu2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testLabel2.getComponentPopupMenu() != null) {
                    testLabel2.setComponentPopupMenu(null);
                    toggleContextMenu2.setText("Add" + titleContextMenu2);
                } else {
                    testLabel2.setComponentPopupMenu(menu);
                    toggleContextMenu2.setText("Remove" + titleContextMenu2);
                }
            }
        });
        final String titleContextMenu3 = " context menu second label";
        final SButton toggleContextMenu3 = new SButton("Remove" +  titleContextMenu2);
        toggleContextMenu3.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        toggleContextMenu3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testLabel3.getComponentPopupMenu() != null) {
                    testLabel3.setComponentPopupMenu(null);
                    toggleContextMenu3.setText("Add" + titleContextMenu3);
                } else {
                    testLabel3.setComponentPopupMenu(menu2);
                    toggleContextMenu3.setText("Remove" + titleContextMenu3);
                }
            }
        });

        all.add(testLabel);
        all.add(testLabel2);
        all.add(testLabel3);
        all.add(new SSpacer(1, 10));
        all.add(toggleMenuEnabled);
        all.add(toggleContextMenu);
        all.add(toggleContextMenu2);
        all.add(toggleContextMenu3);
        all.add(selectionLabel);
        all.add(selection);

        return all;
    }

    private SMenuItem createMenuItem(String string) {
        SMenuItem result = new SMenuItem(string);
        result.addActionListener(menuItemListener);
        return result;
    }
}
