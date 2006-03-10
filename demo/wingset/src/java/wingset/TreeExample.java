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
import org.wings.event.SMouseListener;
import org.wings.event.SMouseEvent;
import org.wings.style.CSSProperty;
import org.wings.tree.SDefaultTreeSelectionModel;
import org.wings.util.PropertyAccessor;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TreeExample
        extends WingSetPane {
    private STree tree;
    private static SIcon ARROW_DOWN = new SResourceIcon("org/wings/icons/ArrowDown.gif");
    private static SIcon ARROW_RIGHT = new SResourceIcon("org/wings/icons/ArrowRight.gif");

    private static SIcon PLUS = new SResourceIcon("org/wings/icons/plus.gif");
    private static SIcon MINUS = new SResourceIcon("org/wings/icons/minus.gif");
    private TreeControls controls;
    private SLabel clicks = new SLabel();
    private boolean consume;

    public SComponent createExample() {
        tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        controls = new TreeControls();
        tree.setName("tree");
        tree.setShowAsFormComponent(false);

        tree.addMouseListener(new SMouseListener() {
            public void mouseClicked(SMouseEvent e) {
                Object object = tree.getPathForRow(tree.getRowForLocation(e.getPoint())).getLastPathComponent();
                TreeNode node = (TreeNode)object;
                if (consume && node.isLeaf())
                    e.consume();
                clicks.setText("clicked " + e.getPoint().getCoordinates());
            }
        });

        controls.addSizable(tree);

        SForm panel = new SForm(new SBorderLayout());
        panel.add(controls, SBorderLayout.NORTH);
        panel.add(tree, SBorderLayout.CENTER);
        panel.add(clicks, SBorderLayout.SOUTH);
        return panel;
    }

    class TreeControls extends ComponentControls {
        private final String[] SELECTION_MODES = new String[]{"single", "contiguous", "discontiguous"};
        private final Integer[] WIDTHS = new Integer[]{new Integer(-12), new Integer(0), new Integer(12), new Integer(24), new Integer(36), new Integer(48)};

        public TreeControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
            showAsFormComponent.setAttribute(CSSProperty.MARGIN_RIGHT, "1.5em");
            showAsFormComponent.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tree.setShowAsFormComponent(showAsFormComponent.isSelected());
                }
            });

            final SCheckBox consume = new SCheckBox("<html>Consume events on leaves&nbsp;&nbsp;&nbsp;");
            consume.setToolTipText("<html>A SMouseListener will intercept the mouse clicks.<br>" +
                    "Consumed events will not be processed by the tree anymore");
            consume.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TreeExample.this.consume = consume.isSelected();
                }
            });

            final SComboBox selectionMode = new SComboBox(SELECTION_MODES);
            //sync selectionMode with tree
            tree.getSelectionModel().setSelectionMode(STree.SINGLE_TREE_SELECTION);
            selectionMode.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (SELECTION_MODES[0].equals(selectionMode.getSelectedItem())) {
                        tree.getSelectionModel().setSelectionMode(STree.SINGLE_TREE_SELECTION);
                    }
                    else if (SELECTION_MODES[1].equals(selectionMode.getSelectedItem())) {
                        tree.getSelectionModel().setSelectionMode(STree.CONTIGUOUS_TREE_SELECTION);
                    }
                    else if (SELECTION_MODES[2].equals(selectionMode.getSelectedItem())) {
                        tree.getSelectionModel().setSelectionMode(STree.DISCONTIGUOUS_TREE_SELECTION);
                    }
                }
            });

            final SComboBox indentationWidth = new SComboBox(WIDTHS);
            // sync indentation width of tree with controller
            indentationWidth.setSelectedIndex(3); // set to 24px indent
            tree.setNodeIndentDepth(WIDTHS[3].intValue());
            // now add the listener
            indentationWidth.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    tree.setNodeIndentDepth(((Integer) indentationWidth.getSelectedItem()).intValue());
                }
            });

            final SRadioButton plusButton = new SRadioButton("plus/minus");
            plusButton.setToolTipText("use [+] and [-] as expansion controls");

            final SRadioButton arrowButton = new SRadioButton("arrows");
            arrowButton.setToolTipText("use right-arrow and down-arrow as expansion controls");

            SButtonGroup group = new SButtonGroup();
            group.add(plusButton);
            group.add(arrowButton);
            plusButton.setSelected(true);

            group.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (plusButton.isSelected()) {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", MINUS);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", PLUS);
                    } else {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", ARROW_DOWN);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", ARROW_RIGHT);
                    }
                }
            });

            add(showAsFormComponent);
            add(consume);
            add(new SLabel(" selection mode "));
            add(selectionMode);
            add(new SLabel(" indentation width "));
            add(indentationWidth);
            add(new SLabel(" folding icons "));
            add(plusButton);
            add(arrowButton);
        }
    }
}
