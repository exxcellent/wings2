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

import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        list.setVisibleRowCount(8);
        list.setShowAsFormComponent(false);

        scrollbar = new SPageScroller(Adjustable.VERTICAL);
        scrollbar.setName("scrollbar");

        // its a horizontal scrollbar, but scrolls vertical...
        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
        scrollbar.setDirectPages(10);

        scrollPane = new SScrollPane(list);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.setVerticalScrollBar(null);
        scrollPane.setVerticalExtent(50);

        controls = new PageScrollerControls();
        controls.addSizable(scrollPane);

        SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(scrollPane, SBorderLayout.CENTER);
        return form;
    }

    static void addChildNodes(TreeNode node, ArrayList list, int indent) {
        if (node != null) {
            StringBuffer name = new StringBuffer();
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
            add(new SLabel("Visible Rows: "));
            Object[] visRowsValues = {new Integer(4), new Integer(8), new Integer(12),
                                      new Integer(16), new Integer(20), new Integer(50)};
            final SComboBox visRows = new SComboBox(visRowsValues);
            visRows.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    list.setVisibleRowCount(((Integer) visRows.getSelectedItem()).intValue());
                }
            });
            visRows.setSelectedItem(new Integer(list.getVisibleRowCount()));
            add(visRows);

            add(new SLabel("Direct Pages: "));
            Object[] values = {new Integer(5), new Integer(10), new Integer(15),
                               new Integer(20), new Integer(50)};
            final SComboBox comboBox = new SComboBox(values);
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    scrollbar.setDirectPages(((Integer) comboBox.getSelectedItem()).intValue());
                }
            });
            comboBox.setSelectedItem(new Integer(scrollbar.getDirectPages()));
            add(comboBox);


            add(new SLabel("Layout: "));
            Object[] constraints = {"Top", "Left", "Bottom", "Right"};
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
            add(layout);


            final SCheckBox margin = new SCheckBox("Margin");
            margin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollbar.setMarginVisible(margin.isSelected());
                }
            });
            margin.setSelected(scrollbar.isMarginVisible());
            add(margin);

            final SCheckBox step = new SCheckBox("Step");
            step.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollbar.setStepVisible(step.isSelected());
                }
            });
            step.setSelected(scrollbar.isStepVisible());
            add(step);

            final SCheckBox paging = new SCheckBox("Paged Scrolling");
            paging.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean selected = paging.isSelected();
                    ((SScrollPaneLayout)scrollPane.getLayout()).setPaging(selected);
                }
            });
            paging.setSelected(true);
            add(paging);
        }
    }
}
