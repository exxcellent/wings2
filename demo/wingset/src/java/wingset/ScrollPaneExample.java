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
import org.wings.table.STableColumnModel;
import org.wings.table.STableColumn;

import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;


/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ScrollPaneExample
        extends WingSetPane
{
    private ScrollPaneControls controls;
    private STable table;
    private STree tree;
    private SScrollPane scrollPane;

    public SComponent createExample() {
        table = new STable(new TableExample.ROTableModel(15, 15));
        table.setShowAsFormComponent(true);
        table.setDefaultRenderer(new TableExample.MyCellRenderer());

        tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        tree.setName("tree");
        tree.setShowAsFormComponent(false);

        scrollPane = new SScrollPane(table);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(3);
        scrollPane.getVerticalScrollBar().setBlockIncrement(3);

        ((SScrollBar) scrollPane.getHorizontalScrollBar()).setShowAsFormComponent(false);
        ((SScrollBar) scrollPane.getVerticalScrollBar()).setShowAsFormComponent(false);

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
            final SCheckBox paging = new SCheckBox("Paged Scrolling");
            paging.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean selected = paging.isSelected();
                    ((SScrollPaneLayout)scrollPane.getLayout()).setPaging(selected);
                }
            });
            paging.setSelected(true);
            addControl(paging);

            final SRadioButton tableButton = new SRadioButton("table");
            final SRadioButton treeButton = new SRadioButton("tree");
            final SButtonGroup group = new SButtonGroup();
            group.add(tableButton);
            group.add(treeButton);
            tableButton.setSelected(true);

            group.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (tableButton.isSelected()) {
                        showInPane(table);
                    } else {
                        showInPane(tree);
                    }
                }

            });

            final SCheckBox hideSomeColumns = new SCheckBox("Hide some columns");
            hideSomeColumns.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    STableColumnModel columnModel = table.getColumnModel();
                    for (int i=0; i < columnModel.getColumnCount(); i++) {
                        if (i % 3 == 0) {
                            STableColumn column = columnModel.getColumn(i);
                            column.setHidden(hideSomeColumns.isSelected());
                        }
                    }
                }
            });

            addControl(new SLabel(""));
            addControl(tableButton);
            addControl(treeButton);
            addControl(hideSomeColumns);
        }
    }
}
