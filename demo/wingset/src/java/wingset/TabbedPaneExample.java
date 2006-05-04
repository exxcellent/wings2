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


import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SResourceIcon;
import org.wings.STabbedPane;
import org.wings.STextArea;
import org.wings.SURLIcon;
import org.wings.style.CSSProperty;
import org.wings.style.CSSStyleSheet;
import org.wings.text.DefaultDocument;
import org.wings.text.SDocument;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Example for STabbedPane.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public class TabbedPaneExample extends WingSetPane {
    private final static int INITIAL_TAB_COUNT = 10;
    private final static SIcon JAVA_CUP_ICON = new SResourceIcon("org/wings/icons/JavaCup.gif");
    private final static SIcon SMALL_COW_ICON = new SURLIcon("../icons/cowSmall.gif");
    private final static Object[] TAB_PLACEMENTS = new Object[]{
            new Object[]{"Top", new Integer(SConstants.TOP)},
            new Object[]{"Left", new Integer(SConstants.LEFT)},
            new Object[]{"Right", new Integer(SConstants.RIGHT)},
            new Object[]{"Bottom", new Integer(SConstants.BOTTOM)}
    };
    private TabbedPaneControls controls;
    private STabbedPane tabbedPane;
    private SDocument logText = new DefaultDocument();

    protected SComponent createExample() {
        controls = new TabbedPaneControls();

        // Create tabbed pane and tabulators
        tabbedPane = new STabbedPane();
        for (int i = 0; i < INITIAL_TAB_COUNT; ++i) {
            addTab();
        }
        tabbedPane.setShowAsFormComponent(false);
        tabbedPane.setIconAt(3, JAVA_CUP_ICON);      // decorate with icons
        tabbedPane.setIconAt(8, SMALL_COW_ICON);
        tabbedPane.setEnabledAt(1, false);          // disable a tab
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                logText.setText(logText.getText() + "Changed to tab: " + tabbedPane.getSelectedIndex() + "\n");
            }
        });
        controls.addSizable(tabbedPane);

        final SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(tabbedPane, SBorderLayout.CENTER);
        return form;
    }


    protected void addTab() {
        int i = tabbedPane.getTabCount();
        SPanel panel = new SPanel(new SBorderLayout());
        STextArea textArea = new STextArea(logText, null, 6, 60);
        textArea.setPreferredSize(SDimension.FULLWIDTH);
        panel.add(new SLabel("Tab # " + i), SBorderLayout.NORTH);
        panel.add(textArea, SBorderLayout.CENTER);
        tabbedPane.add("Tab " + i, panel);
    }

    /**
     * Extended component control for this wingset demo.
     */
    private class TabbedPaneControls extends ComponentControls {
        private int tabCount = INITIAL_TAB_COUNT;

        public TabbedPaneControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
            showAsFormComponent.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabbedPane.setShowAsFormComponent(showAsFormComponent.isSelected());
                }
            });
            addControl(showAsFormComponent);

            final SComboBox placement = new SComboBox(TAB_PLACEMENTS);
            placement.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] objects = (Object[]) placement.getSelectedItem();
                    Integer integer = (Integer) objects[1];
                    tabbedPane.setTabPlacement(integer.intValue());
                }
            });
            placement.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel("<html>&nbsp;&nbsp;tab placement"));
            addControl(placement);

            final SComboBox tabColor = new SComboBox(COLORS);
            tabColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] objects = (Object[]) tabColor.getSelectedItem();
                    Color color = (Color) objects[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_UNSELECTED_TAB, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            tabColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel("tab color"));
            addControl(tabColor);

            final SComboBox contentColor = new SComboBox(COLORS);
            contentColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] objects = (Object[]) contentColor.getSelectedItem();
                    Color color = (Color) objects[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_CONTENT, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            contentColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel("content color"));
            addControl(contentColor);

            final SButton addTab = new SButton("add new tab");
            addTab.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addTab();
                }
            });
            addControl(addTab);

            final SButton removeTab = new SButton("remove selected tab");
            removeTab.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeTab();
                }
            });
            addControl(removeTab);
        }

        protected void removeTab() {
            int index = tabbedPane.getSelectedIndex();
            if (index != -1) {
                tabCount--;
                tabbedPane.removeTabAt(index);
            }
        }

    }
}
