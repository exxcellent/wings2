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
import org.wings.util.PropertyAccessor;

import javax.swing.tree.DefaultTreeModel;
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

    public SComponent createExample() {
        controls = new TreeControls();

        tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        tree.setName("tree");
        tree.setShowAsFormComponent(false);
        controls.addSizable(tree);

        SForm panel = new SForm(new SBorderLayout());
        panel.add(controls, SBorderLayout.NORTH);
        panel.add(tree, SBorderLayout.CENTER);
        return panel;
    }

    class TreeControls extends ComponentControls {
        private final String[] SELECTION_MODES = new String[]{"no", "single", "multiple"};
        private final Integer[] WIDTHS = new Integer[]{new Integer(12), new Integer(24), new Integer(36), new Integer(48)};

        public TreeControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("<html>Show as Form Component&nbsp;&nbsp;&nbsp;");
            showAsFormComponent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tree.setShowAsFormComponent(showAsFormComponent.isSelected());
                }
            });

            final SComboBox selectionMode = new SComboBox(SELECTION_MODES);
            selectionMode.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if ("no".equals(selectionMode.getSelectedItem()))
                        tree.getSelectionModel().setSelectionMode(NO_SELECTION);
                    else if ("single".equals(selectionMode.getSelectedItem()))
                        tree.getSelectionModel().setSelectionMode(SINGLE_SELECTION);
                    else if ("multiple".equals(selectionMode.getSelectedItem()))
                        tree.getSelectionModel().setSelectionMode(MULTIPLE_SELECTION);
                }
            });

            final SComboBox indentationWidth = new SComboBox(WIDTHS);
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
                    } else {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", ARROW_DOWN);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", ARROW_RIGHT);
                    }
                }
            });

            add(showAsFormComponent);
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
