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

import org.wings.SBoxLayout;
import org.wings.SComponent;
import org.wings.SFont;
import org.wings.SLabel;
import org.wings.SMenu;
import org.wings.SMenuItem;
import org.wings.SPanel;
import org.wings.SPopupMenu;
import org.wings.STextField;
import org.wings.SURLIcon;
import org.wings.border.SEmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class PopupExample extends WingSetPane {

    private SLabel selection;

    private final ActionListener menuItemListener = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            selection.setText(((SMenuItem) e.getSource()).getText());
        }
    };

    public SComponent createExample() {

        SPopupMenu menu = new SPopupMenu();
        menu.add(createMenuItem("Cut"));
        menu.add(createMenuItem("Copy"));
        menu.add(createMenuItem("Paste"));

        SMenu subMenu = new SMenu("Help");
        subMenu.add(createMenuItem("About"));
        subMenu.add(createMenuItem("Topics"));
        menu.add(subMenu);
        
        SPopupMenu menu2 = new SPopupMenu();
        menu2.add(createMenuItem("Open"));
        menu2.add(createMenuItem("Save"));
        menu2.add(createMenuItem("Close"));

        SLabel testLabel = new SLabel("This label has a context menu.");
        testLabel.setComponentPopupMenu(menu);
        STextField testLabel2 = new STextField("This textfield has the same context menu.");
        testLabel2.setColumns(testLabel2.getText().length());
        testLabel2.setComponentPopupMenu(menu);
        SLabel testLabel3 = new SLabel("<html>&nbsp;This label has another context menu.", new SURLIcon("../icons/cowSmall.gif"));
        testLabel3.setComponentPopupMenu(menu2);
        SLabel selectionLabel = new SLabel("Selected Menu: ");
        selectionLabel.setBorder(new SEmptyBorder(20,0,0,0));
        selection = new SLabel("none");
        selection.setFont(new SFont(SFont.BOLD));

        SPanel all = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        all.add(testLabel);
        all.add(testLabel2);
        all.add(testLabel3);
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
