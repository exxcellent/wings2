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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Example for STabbedPane.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 */
public class TabbedPaneExample extends WingSetPane {
    private final static int INITIAL_TAB_COUNT = 12;
    private final static SIcon JAVA_CUP_ICON = new SResourceIcon("org/wings/icons/JavaCup.gif");
    private final static SIcon SMALL_COW_ICON = new SURLIcon("../icons/cowSmall.gif");
    private TabbedPaneControls controls;
    private STabbedPane tabbedPane;
    private STextArea textArea;

    protected SComponent createExample() {
        controls = new TabbedPaneControls();

        SForm c = new SForm(new SBorderLayout());
        c.add(controls, SBorderLayout.NORTH);

        tabbedPane = new STabbedPane();
        tabbedPane.setBackground(new java.awt.Color(200, 200, 255));
        tabbedPane.setShowAsFormComponent(false);
        controls.addSizable(tabbedPane);

        SPanel west = new SPanel(new SFlowDownLayout());
        west.add(new SLabel("<html>Tab Placement:&nbsp;"));

        PlacementActionListener placementActionListener = new PlacementActionListener(tabbedPane);

        SRadioButton top = new SRadioButton("Top");
        top.putClientProperty("placement", new Integer(SConstants.TOP));
        top.addActionListener(placementActionListener);
        top.setShowAsFormComponent(false);
        west.add(top);

        SRadioButton left = new SRadioButton("Left");
        left.putClientProperty("placement", new Integer(SConstants.LEFT));
        left.addActionListener(placementActionListener);
        left.setShowAsFormComponent(false);
        west.add(left);

        SRadioButton right = new SRadioButton("Right");
        right.putClientProperty("placement", new Integer(SConstants.RIGHT));
        right.addActionListener(placementActionListener);
        right.setShowAsFormComponent(false);
        west.add(right);

        SRadioButton bottom = new SRadioButton("Bottom");
        bottom.putClientProperty("placement", new Integer(SConstants.BOTTOM));
        bottom.addActionListener(placementActionListener);
        bottom.setShowAsFormComponent(false);
        west.add(bottom);

        SButtonGroup group = new SButtonGroup();
        group.add(top);
        group.add(left);
        group.add(right);
        group.add(bottom);
        group.setSelected(top, true);

        west.add(new SLabel("<html>&nbsp;"));

        group = new SButtonGroup();
        west.add(new SLabel("<html>Color:&nbsp;"));

        final Object[] clrs = {
            "Yellow", Color.yellow,
            "Green", Color.green,
            "Lightblue", new Color(200, 200, 255),
            "Lightgray", Color.lightGray,
            "Orange", new Color(255, 153, 0)};

        for (int i = 0; i < clrs.length; i += 2) {
            SRadioButton btn = new SRadioButton(clrs[i].toString());
            btn.putClientProperty("color", clrs[i + 1]);
            btn.addActionListener(new ColorActionListener(tabbedPane));
            btn.setShowAsFormComponent(false);
            group.add(btn);
            if (i == 0) {
                group.setSelected(btn, true);
            }
            west.add(btn);
        }
        west.setHorizontalAlignment(SConstants.CENTER);
        c.add(west, "West");

        textArea = new STextArea();
        textArea.setPreferredSize(new SDimension("100%", "100%"));
        for (int i = 0; i < INITIAL_TAB_COUNT; ++i) {
            SPanel p = new SPanel(new SBorderLayout());
            p.add(new SLabel("Tab # " + i), "North");
            p.add(textArea);
            tabbedPane.add("Tab " + i, p);
        }
        tabbedPane.setIconAt(3, JAVA_CUP_ICON);
        tabbedPane.setIconAt(8, SMALL_COW_ICON);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                String t = textArea.getText();
                t += "\n";
                t += "wingS: Changed to tab " + tabbedPane.getSelectedIndex() + "\n";
                textArea.setText(t);
            }
        });
        c.add(tabbedPane, "Center");

        return c;
    }

    private static class PlacementActionListener implements ActionListener {
        private final STabbedPane tpane;

        public PlacementActionListener(STabbedPane tpane) {
            this.tpane = tpane;
        }

        public void actionPerformed(ActionEvent ae) {
            SComponent source = (SComponent) ae.getSource();
            Integer placement = (Integer) source.getClientProperty("placement");
            tpane.setTabPlacement(placement.intValue());
        }
    }

    private static class ColorActionListener implements ActionListener {
        private final STabbedPane tabs;

        public ColorActionListener(STabbedPane tabs) {
            this.tabs = tabs;
        }

        public void actionPerformed(ActionEvent ae) {
            SComponent source = (SComponent) ae.getSource();
            Color color = (Color) source.getClientProperty("color");
            tabs.setBackground(color);
        }
    }

    class TabbedPaneControls extends ComponentControls {
        private int tabCount = INITIAL_TAB_COUNT;
        public TabbedPaneControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
            showAsFormComponent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabbedPane.setShowAsFormComponent(showAsFormComponent.isSelected());
                }
            });
            add(showAsFormComponent);
            final SButton addTab = new SButton("add a tab");
            addTab.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addTab();
                }
            });
            add(addTab);
            final SCheckBox removeCurrent = new SCheckBox("remove current tab");
            final SButton removeTab = new SButton("remove a tab");
            removeTab.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeTab(removeCurrent.isSelected());
                }
            });
            add(removeTab);
            add(removeCurrent);
        }
        protected void removeTab(boolean removeCurrent) {
            if (removeCurrent) {
                int index = tabbedPane.getSelectedIndex();
                if (index != -1) {
                    tabCount--;
                    tabbedPane.removeTabAt(index);
                }
            } else {
                if (tabCount > 0) {
                    tabCount--;
                    tabbedPane.removeTabAt(tabCount);
                }
            }
        }
        protected void addTab() {
            SPanel p = new SPanel(new SBorderLayout());
            p.add(new SLabel("Tab # " + tabCount), "North");
            p.add(textArea);
            tabbedPane.add("Tab " + tabCount, p);
            tabCount++;
        }
    }
}
