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
import org.wings.style.CSSProperty;
import org.wings.style.CSSStyleSheet;
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
    private final static Object[] COLORS = new Object[] {
        new Object[] { "Translucent", null },
        new Object[] { "Yellow", new Color(255, 255, 200) },
        new Object[] { "Green", new Color(200, 255, 200) },
        new Object[] { "Blue", new Color(200, 200, 255) },
        new Object[] { "Red", new Color(255, 200, 200) },
    };
    private final static Object[] TAB_PLACEMENTS = new Object[] {
        new Object[] { "Top", new Integer(SConstants.TOP) },
        new Object[] { "Left", new Integer(SConstants.LEFT) },
        new Object[] { "Right", new Integer(SConstants.RIGHT) },
        new Object[] { "Bottom", new Integer(SConstants.BOTTOM) }
    };
    private TabbedPaneControls controls;
    private STabbedPane tabbedPane;
    private STextArea textArea;

    protected SComponent createExample() {
        controls = new TabbedPaneControls();

        textArea = new STextArea(6, 60);
        textArea.setPreferredSize(SDimension.FULLWIDTH);

        // Create tabbed pane and tabulators
        tabbedPane = new STabbedPane();
        for (int i = 0; i < INITIAL_TAB_COUNT; ++i) {
            SPanel panel = new SPanel(new SBorderLayout());
            panel.add(new SLabel("Tab # " + i), SBorderLayout.NORTH);
            panel.add(textArea, SBorderLayout.CENTER);
            tabbedPane.add("Tab " + i, panel);
        }
        tabbedPane.setShowAsFormComponent(false);
        tabbedPane.setIconAt(3, JAVA_CUP_ICON);      // decorate with icons
        tabbedPane.setIconAt(8, SMALL_COW_ICON);
        tabbedPane.setEnabledAt(1, false);          // disable a tab
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                textArea.setText(textArea.getText() + "Changed to tab: " + tabbedPane.getSelectedIndex() + "\n");
            }
        });
        controls.addSizable(tabbedPane);

        final SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(tabbedPane, SBorderLayout.CENTER);
        return form;
    }

    /**
     * Extended component control for this wingset demo.
     */
    private class TabbedPaneControls extends ComponentControls {
        private int tabCount = INITIAL_TAB_COUNT;

        public TabbedPaneControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
            showAsFormComponent.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabbedPane.setShowAsFormComponent(showAsFormComponent.isSelected());
                }
            });
            addControl(showAsFormComponent);

            final SComboBox placement = new SComboBox(TAB_PLACEMENTS);
            placement.addActionListener(new wingset.SerializableActionListener() {
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
            tabColor.addActionListener(new wingset.SerializableActionListener() {
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
            contentColor.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] objects = (Object[]) contentColor.getSelectedItem();
                    Color color = (Color) objects[1];
                    tabbedPane.setAttribute(STabbedPane.SELECTOR_CONTENT, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            contentColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel("content color"));
            addControl(contentColor);

            final SButton addTab = new SButton("add a tab");
            addTab.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addTab();
                }
            });
            addControl(addTab);

            final SButton removeTab = new SButton("remove a tab");
            removeTab.addActionListener(new wingset.SerializableActionListener() {
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

        protected void addTab() {
            SPanel p = new SPanel(new SBorderLayout());
            p.add(new SLabel("Tab # " + tabCount), "North");
            p.add(textArea);
            tabbedPane.add("Tab " + tabCount, p);
            tabCount++;
        }

        private class ObjectPairCellRenderer extends SDefaultListCellRenderer {
            public SComponent getListCellRendererComponent(SComponent list, Object value, boolean selected, int row) {
                Object[] objects = (Object[])value;
                value = objects[0];
                return super.getListCellRendererComponent(list, value, selected, row);
            }
        }
    }
}
