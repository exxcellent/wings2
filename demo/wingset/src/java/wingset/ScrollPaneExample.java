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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultTreeModel;

import org.wings.SBorderLayout;
import org.wings.SButtonGroup;
import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.SList;
import org.wings.SRadioButton;
import org.wings.SResourceIcon;
import org.wings.SScrollBar;
import org.wings.SScrollPane;
import org.wings.SScrollPaneLayout;
import org.wings.STable;
import org.wings.STree;

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
    static final SResourceIcon javaCup = new SResourceIcon("org/wings/icons/JavaCup.gif");

    public SComponent createExample() {
        table = new STable(new TableExample.ROTableModel(15, 15));
        table.setShowAsFormComponent(true);
        table.setDefaultRenderer(new TableExample.MyCellRenderer());

        tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        tree.setName("scrolltree");
        tree.setShowAsFormComponent(false);
        
        scrollPane = new SScrollPane(table);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(3);
        scrollPane.getVerticalScrollBar().setBlockIncrement(3);

        ((SScrollBar) scrollPane.getHorizontalScrollBar()).setShowAsFormComponent(false);
        ((SScrollBar) scrollPane.getVerticalScrollBar()).setShowAsFormComponent(false);

        controls = new ScrollPaneControls();
        controls.addSizable(scrollPane);

        SForm p = new SForm(new SBorderLayout());
        p.add(controls, SBorderLayout.NORTH);
        p.add(scrollPane, SBorderLayout.CENTER);
        return p;
    }

    /**
     * @param table
     */
    protected void showInPane(SComponent comp) {
        //scrollPane.removeAll();
        //scrollPane.add(comp);
        scrollPane.setViewportView(comp);
    }
    
    class ScrollPaneControls extends ComponentControls {
        public ScrollPaneControls () {
            final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
            showAsFormComponent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean selected = showAsFormComponent.isSelected();
                    table.setShowAsFormComponent(selected);
                    ((SScrollBar)scrollPane.getHorizontalScrollBar()).setShowAsFormComponent( selected);
                    ((SScrollBar)scrollPane.getVerticalScrollBar()).setShowAsFormComponent(selected);
                }
            });
            add(showAsFormComponent);

            final SCheckBox paging = new SCheckBox("Paged Scrolling");
            paging.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean selected = paging.isSelected();
                    ((SScrollPaneLayout)scrollPane.getLayout()).setPaging(selected);
                }
            });
            paging.setSelected(true);
            add(paging);

            final SRadioButton tableButton = new SRadioButton("table");
            final SRadioButton treeButton = new SRadioButton("tree");
            final SButtonGroup group = new SButtonGroup();
            group.add(tableButton);
            group.add(treeButton);
            tableButton.setSelected(true);

            group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (tableButton.isSelected()) {
                        showInPane(table);
                    } else {
                        showInPane(tree);
                    }
                }
                
            });
            add(new SLabel(" component to scroll "));
            add(tableButton);
            add(treeButton);
        }
    }
}
