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
import org.wings.table.SDefaultTableColumnModel;
import org.wings.table.STableColumn;

import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

        controls = new ScrollPaneControls();
        controls.addControllable(scrollPane);

        SForm p = new SForm(new SBorderLayout());
        p.add(controls, SBorderLayout.NORTH);
        p.add(scrollPane, SBorderLayout.CENTER);
        return p;
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
                    int newMode = mode.getSelectedIndex();
                    switch (newMode) {
                    case 0:
                        scrollPane.setMode(SScrollPane.MODE_SCROLLING);
                        scrollPane.setPreferredSize(new SDimension("100%", SDimension.AUTO));
                        break;
                    case 1:
                        scrollPane.setMode(SScrollPane.MODE_COMPLETE);
                        scrollPane.setPreferredSize(new SDimension("100%", "457"));
                        break;
                    case 2:
                        scrollPane.setMode(SScrollPane.MODE_PAGING);
                        scrollPane.setPreferredSize(new SDimension("100%", SDimension.AUTO));
                        break;
                    }
                }
            });

            final SPageScroller horizontalPageScrollerH = new SPageScroller(SPageScroller.HORIZONTAL);
            horizontalPageScrollerH.setLayoutMode(SPageScroller.HORIZONTAL);
            final SPageScroller horizontalPageScrollerV = new SPageScroller(SPageScroller.VERTICAL);
            horizontalPageScrollerV.setLayoutMode(SPageScroller.HORIZONTAL);
            String[] horizontalScrollBars = {"scrollbar", "pagescroller (H)", "pagescroller (V)", "null"};
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
            String[] verticalScrollBars = {"scrollbar", "pagescroller (H)", "pagescroller (V)", "null"};
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

            final SCheckBox hideSomeColumns = new SCheckBox("Hide some table columns");
            hideSomeColumns.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SDefaultTableColumnModel columnModel = (SDefaultTableColumnModel) table.getColumnModel();
                    for (int i = 0; i < columnModel.getColumnCount(); ++i) {
                        if (i % 3 == 0) {
                            STableColumn column = columnModel.getColumn(i);
                            columnModel.setColumnHidden(column, hideSomeColumns.isSelected());
                        }
                    }
                }
            });

            addControl(new SLabel("Scrollable:"));
            addControl(scrollable);
            addControl(new SLabel(" Mode:"));
            addControl(mode);
            addControl(new SLabel(" Scrollbar (H/V):"));
            addControl(hScrollBar);
            addControl(vScrollBar);
            addControl(new SLabel(" Scrollbar policy (H/V):"));
            addControl(hScrollBarPolicy);
            addControl(vScrollBarPolicy);
            addControl(hideSomeColumns);
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
