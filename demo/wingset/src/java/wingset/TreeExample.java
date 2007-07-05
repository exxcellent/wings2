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
import org.wings.plaf.css.TreeCG;
import org.wings.border.SLineBorder;
import org.wings.event.SMouseEvent;
import org.wings.event.SMouseListener;
import org.wings.util.PropertyAccessor;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class TreeExample
        extends WingSetPane {
    private STree tree;
    private static SIcon ARROW_DOWN = new SResourceIcon("org/wings/icons/ArrowDown.gif");
    private static SIcon ARROW_RIGHT = new SResourceIcon("org/wings/icons/ArrowRight.gif");

    private static SIcon DOT = new SResourceIcon("org/wings/icons/leaficon.gif");
    private static SIcon PLUS = new SResourceIcon("org/wings/icons/plus.gif");
    private static SIcon MINUS = new SResourceIcon("org/wings/icons/minus.gif");
    private TreeControls controls;
    private SLabel clicks = new SLabel();
    private boolean consume;


    protected SComponent createControls() {
        controls = new TreeControls();
        return controls;
    }

    public SComponent createExample() {
        tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        tree.setName("tree");
        tree.setShowAsFormComponent(false);
        TreeCG treeCG = new TreeCG();
        tree.setCG(treeCG);

        tree.addMouseListener(new SMouseListener() {
            public void mouseClicked(SMouseEvent e) {
                Object object = tree.getPathForRow(tree.getRowForLocation(e.getPoint())).getLastPathComponent();
                TreeNode node = (TreeNode)object;
                if (consume && node.isLeaf())
                    e.consume();
                clicks.setText("clicked " + e.getPoint());
            }
        });
        tree.getSelectionModel().setSelectionMode(STree.SINGLE_TREE_SELECTION);
        tree.setNodeIndentDepth(20);
        tree.setHorizontalAlignment(SConstants.CENTER_ALIGN);

        clicks.setHorizontalAlignment(SConstants.CENTER_ALIGN);

        SScrollPane scrollPane = new SScrollPane(tree);
        scrollPane.setMode(SScrollPane.MODE_COMPLETE);
        scrollPane.setBorder(new SLineBorder(Color.GRAY, 1));
        scrollPane.setPreferredSize(new SDimension("400px", null));
        scrollPane.setHorizontalAlignment(SConstants.CENTER_ALIGN);

        controls.addControllable(scrollPane);

        SPanel panel = new SPanel(new SBorderLayout(10, 10));
        panel.add(scrollPane, SBorderLayout.CENTER);
        panel.add(clicks, SBorderLayout.SOUTH);
        return panel;
    }

    class TreeControls extends ComponentControls {
        private final String[] SELECTION_MODES = new String[]{"single", "contiguous", "discontiguous"};
        private final Integer[] WIDTHS = new Integer[]{ new Integer(15), new Integer(20), new Integer(25), new Integer(30)};

        public TreeControls() {
            widthTextField.setText("400px");
            borderColorComboBox.setSelectedItem(COLORS[1]);
            borderStyleComboBox.setSelectedItem(BORDERS[4]);
            borderThicknessTextField.setText("1");

            final SCheckBox consume = new SCheckBox("Consume events on leaves");
            consume.setToolTipText("<html>A SMouseListener will intercept the mouse clicks.<br>" +
                    "Consumed events will not be processed by the tree anymore");
            consume.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TreeExample.this.consume = consume.isSelected();
                }
            });

            final SComboBox selectionMode = new SComboBox(SELECTION_MODES);
            //sync selectionMode with tree
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
            indentationWidth.setSelectedIndex(1); // set to 20px indent
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

            group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (plusButton.isSelected()) {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", MINUS);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", PLUS);
                        PropertyAccessor.setProperty(tree.getCG(), "leafControlIcon", DOT);
                    } else {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", ARROW_DOWN);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", ARROW_RIGHT);
                        PropertyAccessor.setProperty(tree.getCG(), "leafControlIcon", null);
                    }
                    tree.reload();
                }
            });
            
            final SCheckBox rootVisible = new SCheckBox("show root", true);
            rootVisible.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tree.setRootVisible(rootVisible.isSelected());
                }
            });

            addControl(consume);
            addControl(new SLabel(" selection mode"));
            addControl(selectionMode);
            addControl(new SLabel(" indentation width"));
            addControl(indentationWidth);
            addControl(new SLabel(" folding icons"));
            addControl(plusButton);
            addControl(arrowButton);
            addControl(new SLabel(" "));
            addControl(rootVisible);
        }
    }
}
