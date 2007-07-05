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


import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
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
    private final static Object[] TAB_PLACEMENTS = new Object[]{
            new Object[]{"Top", new Integer(SConstants.TOP)},
            new Object[]{"Left", new Integer(SConstants.LEFT)},
            new Object[]{"Right", new Integer(SConstants.RIGHT)},
            new Object[]{"Bottom", new Integer(SConstants.BOTTOM)}
    };
    private TabbedPaneControls controls;
    private STabbedPane tabbedPane;
    private SDocument logText = new DefaultDocument();


    protected SComponent createControls() {
        controls = new TabbedPaneControls();
        return controls;
    }

    protected SComponent createExample() {

        // Create tabbed pane and tabulators
        tabbedPane = new STabbedPane();
        tabbedPane.setPreferredSize(new SDimension("700px", null));
        for (int i = 0; i < INITIAL_TAB_COUNT; ++i) {
            addTab();
        }
        tabbedPane.setShowAsFormComponent(false);
        tabbedPane.setEnabledAt(1, false);          // disable a tab
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                logText.setText(logText.getText() + "Changed to tab: " + tabbedPane.getSelectedIndex() + "\n");
            }
        });
        controls.addControllable(tabbedPane);

        SPanel panel = new SPanel(new SBorderLayout(10, 10));
        panel.add(tabbedPane);
        return panel;
    }


    protected void addTab() {
        int i = tabbedPane.getTabCount();
        SPanel panel = new SPanel(new SBorderLayout(10, 10));
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
            widthTextField.setText("700px");
            final SComboBox placement = new SComboBox(TAB_PLACEMENTS);
            placement.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] objects = (Object[]) placement.getSelectedItem();
                    Integer integer = (Integer) objects[1];
                    tabbedPane.setTabPlacement(integer.intValue());
                }
            });
            placement.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" tab placement"));
            addControl(placement);

            final SComboBox unselectedTabColor = new SComboBox(COLORS);
            unselectedTabColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) unselectedTabColor.getSelectedItem())[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_UNSELECTED_TAB, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            unselectedTabColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" unselected tab"));
            addControl(unselectedTabColor);

            final SComboBox selectedTabColor = new SComboBox(COLORS);
            selectedTabColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) selectedTabColor.getSelectedItem())[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_SELECTED_TAB, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            selectedTabColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" selected tab"));
            addControl(selectedTabColor);

            final SComboBox disabledTabColor = new SComboBox(COLORS);
            disabledTabColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) disabledTabColor.getSelectedItem())[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_DISABLED_TAB, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            disabledTabColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" disabled tab"));
            addControl(disabledTabColor);

            final SComboBox contentColor = new SComboBox(COLORS);
            contentColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) contentColor.getSelectedItem())[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_CONTENT, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            contentColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" content"));
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
