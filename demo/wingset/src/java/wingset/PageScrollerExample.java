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
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.SList;
import org.wings.SPageScroller;
import org.wings.SScrollPane;
import org.wings.SScrollPaneLayout;
import org.wings.util.SStringBuilder;

import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class PageScrollerExample
        extends WingSetPane {

    SList list;
    SPageScroller scrollbar;
    SScrollPane scrollPane;
    private PageScrollerControls controls;

    public SComponent createExample() {
        list = new SList(listData);
        list.setShowAsFormComponent(false);

        scrollbar = new SPageScroller(Adjustable.VERTICAL);
        scrollbar.setName("scrollbar");
        
        // its a horizontal scrollbar, but scrolls vertical...
        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
        scrollbar.setDirectPages(10);
        scrollbar.setShowAsFormComponent(false);
        
        scrollPane = new SScrollPane(list);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.setVerticalScrollBar(null);
        scrollPane.setVerticalExtent(50);
        scrollPane.setPreferredSize(SDimension.FULLWIDTH);
        
        controls = new PageScrollerControls();
        controls.addControllable(scrollPane);

        SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(scrollPane, SBorderLayout.CENTER);
        return form;
    }

    static void addChildNodes(TreeNode node, ArrayList list, int indent) {
        if (node != null) {
            SStringBuilder name = new SStringBuilder();
            for (int i = 0; i < indent; i++) {
                name.append(".");
            }
            name.append(node.toString());
            list.add(name.toString());
            for (int i = 0; i < node.getChildCount(); i++) {
                addChildNodes(node.getChildAt(i), list, indent + 1);
            }
        }
    }

    static Object[] listData = createData();

    static Object[] createData() {
        TreeNode root = HugeTreeModel.generateTree();

        ArrayList data = new ArrayList();
        addChildNodes(root, data, 0);

        return data.toArray();
    }


    class PageScrollerControls extends ComponentControls {
        public PageScrollerControls() {
            addControl(new SLabel("Visible Rows: "));
            Integer[] visRowsValues = {new Integer(4), new Integer(10), new Integer(12),
                                      new Integer(16), new Integer(20), new Integer(50)};
            final SComboBox visRows = new SComboBox(visRowsValues);
            visRows.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    list.setVisibleRowCount(((Integer) visRows.getSelectedItem()).intValue());
                }
            });
            list.setVisibleRowCount(visRowsValues[2].intValue());
            visRows.setSelectedItem(visRowsValues[2]);
            addControl(visRows);

            addControl(new SLabel("Direct Pages: "));
            Object[] values = {new Integer(5), new Integer(10), new Integer(15),
                               new Integer(20), new Integer(50)};
            final SComboBox comboBox = new SComboBox(values);
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    scrollbar.setDirectPages(((Integer) comboBox.getSelectedItem()).intValue());
                }
            });
            comboBox.setSelectedItem(new Integer(scrollbar.getDirectPages()));
            addControl(comboBox);


            addControl(new SLabel("Layout: "));
            final Object[] constraints = {"Top", "Left", "Bottom", "Right"};
            final SComboBox layout = new SComboBox(constraints);
            layout.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if ("Top".equals(layout.getSelectedItem())) {
                        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
                        scrollPane.setHorizontalScrollBar(scrollbar,
                                SScrollPaneLayout.NORTH);
                    } else if ("Bottom".equals(layout.getSelectedItem())) {
                        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
                        scrollPane.setHorizontalScrollBar(scrollbar,
                                SScrollPaneLayout.SOUTH);
                    } else if ("Left".equals(layout.getSelectedItem())) {
                        scrollbar.setLayoutMode(Adjustable.VERTICAL);
                        scrollPane.setHorizontalScrollBar(scrollbar,
                                SScrollPaneLayout.WEST);
                    } else if ("Right".equals(layout.getSelectedItem())) {
                        scrollbar.setLayoutMode(Adjustable.VERTICAL);
                        scrollPane.setHorizontalScrollBar(scrollbar,
                                SScrollPaneLayout.EAST);
                    }
                }
            });
            layout.setSelectedItem("Bottom");
            addControl(layout);

            /*
            final SCheckBox margin = new SCheckBox("Margin");
            margin.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollbar.setMarginVisible(margin.isSelected());
                }
            });
            margin.setSelected(scrollbar.isMarginVisible());
            add(margin);

            final SCheckBox step = new SCheckBox("Step");
            step.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollbar.setStepVisible(step.isSelected());
                }
            });
            step.setSelected(scrollbar.isStepVisible());
            add(step);
            */
        }
    }
}
