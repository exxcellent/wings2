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
import org.wings.border.SLineBorder;

import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;


/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class ScrollPaneExample extends WingSetPane
{
    private ScrollPaneControls controls;
    private STable table;
    private STree tree;
    private SList list;
    private SScrollPane scrollPane;


    protected SComponent createControls() {
        controls = new ScrollPaneControls();
        return controls;
    }

    public SComponent createExample() {
        table = new STable(new TableExample.ROTableModel(15, 42));
        table.setDefaultRenderer(new TableExample.MyCellRenderer());

        tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));

        list = createTestList(42);

        scrollPane = new SScrollPane(table);
        scrollPane.setHorizontalExtent(10);
        scrollPane.setVerticalExtent(20);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(3);
        scrollPane.getVerticalScrollBar().setBlockIncrement(3);
        scrollPane.setVerticalAlignment(SConstants.TOP_ALIGN);
        scrollPane.setPreferredSize(SDimension.FULLAREA);

        controls.addControllable(scrollPane);
        return scrollPane;
    }

    protected void showInPane(SComponent comp) {
        scrollPane.setViewportView(comp);
    }

    class ScrollPaneControls extends ComponentControls {
        public ScrollPaneControls () {
            String[] scrollables = {"table", "tree", "list"};
            final SComboBox scrollable = new SComboBox(scrollables);
            scrollable.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int newScrollable = scrollable.getSelectedIndex();
                    switch (newScrollable) {
                    case 0:
                        showInPane(table);
                        break;
                    case 1:
                        showInPane(tree);
                        break;
                    case 2:
                        showInPane(list);
                        break;
                    }
                }
            });

            String[] scrollpaneModes = {"scrolling", "complete", "paging"};
            final SComboBox mode = new SComboBox(scrollpaneModes);
            mode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollPane.setMode(mode.getSelectedIndex());
                    scrollPane.setPreferredSize(SDimension.FULLAREA);
                    widthTextField.setText("100%");
                    heightTextField.setText("100%");
                }
            });

            Integer[] horizontalExtents = {new Integer(5), new Integer(10), new Integer(15)};
            final SComboBox hScrollpaneExtent = new SComboBox(horizontalExtents);
            hScrollpaneExtent.setSelectedIndex(1);
            hScrollpaneExtent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int extent = ((Integer) hScrollpaneExtent.getSelectedItem()).intValue();
                    scrollPane.setHorizontalExtent(extent);
                }
            });

            Integer[] verticalExtents = {new Integer(10), new Integer(15), new Integer(20)};
            final SComboBox vScrollpaneExtent = new SComboBox(verticalExtents);
            vScrollpaneExtent.setSelectedIndex(2);
            vScrollpaneExtent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int extent = ((Integer) vScrollpaneExtent.getSelectedItem()).intValue();
                    scrollPane.setVerticalExtent(extent);
                }
            });

            final SPageScroller horizontalPageScrollerH = new SPageScroller(SPageScroller.HORIZONTAL);
            horizontalPageScrollerH.setLayoutMode(SPageScroller.HORIZONTAL);
            final SPageScroller horizontalPageScrollerV = new SPageScroller(SPageScroller.VERTICAL);
            horizontalPageScrollerV.setLayoutMode(SPageScroller.HORIZONTAL);
            String[] horizontalScrollBars = {"normal", "page (H)", "page (V)", "null"};
            final SComboBox hScrollBar = new SComboBox(horizontalScrollBars);
            hScrollBar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int newScrollbar = hScrollBar.getSelectedIndex();
                    switch (newScrollbar) {
                    case 0:
                        scrollPane.setHorizontalScrollBar(new SScrollBar(SScrollBar.HORIZONTAL));
                        break;
                    case 1:
                        scrollPane.setHorizontalScrollBar(horizontalPageScrollerH);
                        break;
                    case 2:
                        scrollPane.setHorizontalScrollBar(horizontalPageScrollerV);
                        break;
                    case 3:
                        scrollPane.setHorizontalScrollBar(null);
                        break;
                    }
                }
            });

            final SPageScroller verticalPageScrollerH = new SPageScroller(SPageScroller.HORIZONTAL);
            verticalPageScrollerH.setLayoutMode(SPageScroller.VERTICAL);
            final SPageScroller verticalPageScrollerV = new SPageScroller(SPageScroller.VERTICAL);
            verticalPageScrollerV.setLayoutMode(SPageScroller.VERTICAL);
            String[] verticalScrollBars = {"normal", "page (H)", "page (V)", "null"};
            final SComboBox vScrollBar = new SComboBox(verticalScrollBars);
            vScrollBar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int newScrollbar = vScrollBar.getSelectedIndex();
                    switch (newScrollbar) {
                    case 0:
                        scrollPane.setVerticalScrollBar(new SScrollBar(SScrollBar.VERTICAL));
                        break;
                    case 1:
                        scrollPane.setVerticalScrollBar(verticalPageScrollerH);
                        break;
                    case 2:
                        scrollPane.setVerticalScrollBar(verticalPageScrollerV);
                        break;
                    case 3:
                        scrollPane.setVerticalScrollBar(null);
                        break;
                    }
                }
            });

            String[] horizontalPolicies = {"as needed", "always", "never"};
            final SComboBox hScrollBarPolicy = new SComboBox(horizontalPolicies);
            hScrollBarPolicy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int newPolicy = hScrollBarPolicy.getSelectedIndex();
                    switch (newPolicy) {
                    case 0:
                        scrollPane.setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        break;
                    case 1:
                        scrollPane.setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                        break;
                    case 2:
                        scrollPane.setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        break;
                    }
                }
            });

            String[] verticalPolicies = {"as needed", "always", "never"};
            final SComboBox vScrollBarPolicy = new SComboBox(verticalPolicies);
            vScrollBarPolicy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int newPolicy = vScrollBarPolicy.getSelectedIndex();
                    switch (newPolicy) {
                    case 0:
                        scrollPane.setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        break;
                    case 1:
                        scrollPane.setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        break;
                    case 2:
                        scrollPane.setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_NEVER);
                        break;
                    }
                }
            });

            addControl(new SLabel("scrollable"));
            addControl(scrollable);
            addControl(new SLabel(" mode"));
            addControl(mode);
            addControl(new SLabel(" extent (H/V)"));
            addControl(hScrollpaneExtent);
            addControl(vScrollpaneExtent);
            addControl(new SLabel(" scrollbar (H/V)"));
            addControl(hScrollBar);
            addControl(vScrollBar);
            addControl(new SLabel(" policy (H/V)"));
            addControl(hScrollBarPolicy);
            addControl(vScrollBarPolicy);
        }
    }

    private SList createTestList(int rows) {
        String[] modelData = new String[rows];
        for (int i = 0; i < rows; ++i) {
            modelData[i] = "This is list item number " + (i + 1);
        }

        SList testList = new SList(new SDefaultListModel(modelData));
        testList.setShowAsFormComponent(false);

        return testList;
    }

}
