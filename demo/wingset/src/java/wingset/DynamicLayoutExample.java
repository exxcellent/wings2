/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A quickhack example to show the capabilities of the dynamic wings layout managers.
 *
 * @author bschmid
 */
public class DynamicLayoutExample extends WingSetPane {
    private final SForm panel = new SForm();
    private final SPanel[] demoPanels = {new GridBagDemoPanel(), new GridLayoutDemoPanel(), new BoxLayoutDemoPanel()};
    private final static String[] demoManagerNames = {"SGridBagLayout", "SGridLayout","SBoxLayout"};
    private final SComboBox selectLayoutManager = new SComboBox(demoManagerNames);

    protected SComponent createExample() {
        selectLayoutManager.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                panel.remove(1);
                panel.add(demoPanels[selectLayoutManager.getSelectedIndex()]);
            }
        });

        panel.add(selectLayoutManager);
        panel.add(demoPanels[0]);

        return panel;
    }

    private static void addDummyLabels(final SPanel panel) {
         for (int i = 0; i < 6; i++) {
             final SLabel label = new SLabel("Label " + (i + 1));
             panel.add(label);
             if (i % 3 == 0) {
                 label.setVerticalAlignment(TOP);
             }
             if (i % 3 == 1) {
                 label.setHorizontalAlignment(CENTER);
                 label.setVerticalAlignment(CENTER);
             }
             if (i % 3 == 2) {
                 label.setForeground(Color.RED);
                 label.setHorizontalAlignment(RIGHT);
                 label.setVerticalAlignment(BOTTOM);
             }
         }
     }


    private static class BoxLayoutDemoPanel extends SPanel {
        public BoxLayoutDemoPanel() {
            SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.HORIZONTAL);
            setLayout(boxLayout);
            addDummyLabels(this);
        }
    }

    private static class GridLayoutDemoPanel extends SPanel {
        public GridLayoutDemoPanel() {
            add(new SLabel("Grid Layout panel with 3 colums, border, padding & spacing"));
            SGridLayout layout1 = new SGridLayout(3);
            layout1.setBorder(1);
            layout1.setCellPadding(10);
            layout1.setCellSpacing(20);
            final SPanel panel1 = new SPanel(layout1);
            addDummyLabels(panel1);
            add(panel1);
        }
    }

    private static class GridBagDemoPanel extends SPanel {
        public GridBagDemoPanel() {
            SFlowDownLayout flowLayout = new SFlowDownLayout();
            setLayout(flowLayout);
            setHorizontalAlignment(CENTER);            

            add(new SLabel("Horizontal adding using REMAINDER"));
            SGridBagLayout layout = new SGridBagLayout();
            layout.setBorder(1);
            SPanel p = new SPanel(layout);
            p.setPreferredSize(new SDimension(300, 100));
            p.setBackground(Color.red);
            add(p);

            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.REMAINDER;
            p.add(new SLabel("1"), c);
            c.gridwidth = 1;

            p.add(new SLabel("2"), c);
            c.gridwidth = GridBagConstraints.REMAINDER;
            p.add(new SLabel("3"), c);
            c.gridwidth = 1;

            p.add(new SLabel("4"), c);
            p.add(new SLabel("5"), c);
            c.gridwidth = GridBagConstraints.REMAINDER;
            p.add(new SLabel("6"), c);
            c.gridwidth = 1;

            p.add(new SLabel("7"), c);
            p.add(new SLabel("8"), c);
            p.add(new SLabel("9"), c);
            c.gridwidth = GridBagConstraints.REMAINDER;
            p.add(new SLabel("10"), c);


            add(new SLabel("Vertical adding using pre-defined gridx"));
            layout = new SGridBagLayout();
            layout.setBorder(1);
            p = new SPanel(layout);
            add(p);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridheight = GridBagConstraints.REMAINDER;
            p.add(new SLabel("1"), c);
            c.gridheight = 1;

            c.gridx = 1;
            p.add(new SLabel("2"), c);
            c.gridheight = GridBagConstraints.REMAINDER;
            p.add(new SLabel("3"), c);
            c.gridheight = 1;

            c.gridx = 2;
            p.add(new SLabel("4"), c);
            p.add(new SLabel("5"), c);
            c.gridheight = GridBagConstraints.REMAINDER;
            p.add(new SLabel("6"), c);
            c.gridheight = 1;

            c.gridx = 3;
            p.add(new SLabel("7"), c);
            p.add(new SLabel("8"), c);
            p.add(new SLabel("9"), c);
            c.gridheight = GridBagConstraints.REMAINDER;
            p.add(new SLabel("10"), c);

            add(new SLabel("Random adding with pre-defined gridx+gridy"));
            layout = new SGridBagLayout();
            layout.setBorder(1);
            p = new SPanel(layout);
            add(p);

            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 0;
            p.add(new SLabel("1"), c);
            c.gridx = 3;
            c.gridy = 1;
            p.add(new SLabel("2"), c);
            c.gridx = 2;
            c.gridy = 2;
            p.add(new SLabel("3"), c);
            c.gridx = 1;
            c.gridy = 3;
            p.add(new SLabel("4"), c);
            c.gridx = 0;
            c.gridy = 4;
            p.add(new SLabel("5"), c);


            add(new SLabel("Using weight"));
            layout = new SGridBagLayout();
            layout.setBorder(1);
            p = new SPanel(layout);
            add(p);
            p.setPreferredSize(new SDimension(500, 500));

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0;
            c.weighty = 0;
            p.add(new SLabel("1"), c);
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 1;
            c.weighty = 0;
            p.add(new SLabel("2"), c);
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 2;
            c.weighty = 0;
            p.add(new SLabel("3"), c);
            c.gridx = 3;
            c.gridy = 0;
            c.weightx = 1;
            c.weighty = 0;
            p.add(new SLabel("4"), c);
            c.gridx = 4;
            c.gridy = 0;
            c.weightx = 0;
            c.weighty = 0;
            p.add(new SLabel("5"), c);

            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            c.weighty = 1;
            p.add(new SLabel("6"), c);
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 1;
            c.weighty = 1;
            p.add(new SLabel("7"), c);
            c.gridx = 2;
            c.gridy = 1;
            c.weightx = 2;
            c.weighty = 1;
            p.add(new SLabel("8"), c);
            c.gridx = 3;
            c.gridy = 1;
            c.weightx = 1;
            c.weighty = 1;
            p.add(new SLabel("9"), c);
            c.gridx = 4;
            c.gridy = 1;
            c.weightx = 0;
            c.weighty = 1;
            p.add(new SLabel("10"), c);

            c.gridx = 0;
            c.gridy = 2;
            c.weightx = 0;
            c.weighty = 2;
            p.add(new SLabel("11"), c);
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 1;
            c.weighty = 2;
            p.add(new SLabel("12"), c);
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 2;
            c.weighty = 2;
            p.add(new SLabel("13"), c);
            c.gridx = 3;
            c.gridy = 2;
            c.weightx = 1;
            c.weighty = 2;
            p.add(new SLabel("14"), c);
            c.gridx = 4;
            c.gridy = 2;
            c.weightx = 0;
            c.weighty = 2;
            p.add(new SLabel("15"), c);
            c.gridx = 0;
            c.gridy = 3;
            c.weightx = 0;
            c.weighty = 1;
            p.add(new SLabel("16"), c);
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 1;
            c.weighty = 1;
            p.add(new SLabel("17"), c);
            c.gridx = 2;
            c.gridy = 3;
            c.weightx = 2;
            c.weighty = 1;
            p.add(new SLabel("18"), c);
            c.gridx = 3;
            c.gridy = 3;
            c.weightx = 1;
            c.weighty = 1;
            p.add(new SLabel("19"), c);
            c.gridx = 4;
            c.gridy = 3;
            c.weightx = 0;
            c.weighty = 1;
            p.add(new SLabel("20"), c);
            c.gridx = 0;
            c.gridy = 4;
            c.weightx = 0;
            c.weighty = 0;
            p.add(new SLabel("21"), c);
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 1;
            c.weighty = 0;
            p.add(new SLabel("22"), c);
            c.gridx = 2;
            c.gridy = 4;
            c.weightx = 2;
            c.weighty = 0;
            p.add(new SLabel("23"), c);
            c.gridx = 3;
            c.gridy = 4;
            c.weightx = 1;
            c.weighty = 0;
            p.add(new SLabel("24"), c);
            c.gridx = 4;
            c.gridy = 4;
            c.weightx = 0;
            c.weighty = 0;
            p.add(new SLabel("25"), c);


            add(new SLabel("Adding with gridwidth=RELATIVE"));
            layout = new SGridBagLayout();
            layout.setBorder(1);
            p = new SPanel(layout);
            add(p);
            c = new GridBagConstraints();
            p.add(new SLabel("1"), c);
            p.add(new SLabel("2"), c);
            p.add(new SLabel("3"), c);
            p.add(new SLabel("4"), c);
            c.gridwidth = GridBagConstraints.RELATIVE;
            p.add(new SLabel("5"), c);
            c.gridwidth = 1;
            p.add(new SLabel("end #1"), c);

            p.add(new SLabel("6"), c);
            p.add(new SLabel("7"), c);
            p.add(new SLabel("8"), c);
            c.gridwidth = GridBagConstraints.RELATIVE;
            p.add(new SLabel("9"), c);
            c.gridwidth = 1;
            p.add(new SLabel("end #2"), c);

            p.add(new SLabel("10"), c);
            p.add(new SLabel("11"), c);
            c.gridwidth = GridBagConstraints.RELATIVE;
            p.add(new SLabel("12"), c);
            c.gridwidth = 1;
            p.add(new SLabel("end #3"), c);

            p.add(new SLabel("13"), c);
            c.gridwidth = GridBagConstraints.RELATIVE;
            p.add(new SLabel("14"), c);
            c.gridwidth = 1;
            p.add(new SLabel("end #4"), c);

            c.gridwidth = GridBagConstraints.RELATIVE;
            p.add(new SLabel("15"), c);
            c.gridwidth = 1;
            p.add(new SLabel("end #5"), c);


            add(new SLabel("Vertical adding with gridheight=RELATIVE"));
            layout = new SGridBagLayout();
            layout.setBorder(1);
            p = new SPanel(layout);
            add(p);
            c = new GridBagConstraints();

            c.gridx = 0;
            p.add(new SLabel("1"), c);
            p.add(new SLabel("2"), c);
            p.add(new SLabel("3"), c);
            p.add(new SLabel("4"), c);
            c.gridheight = GridBagConstraints.RELATIVE;
            p.add(new SLabel("5"), c);
            c.gridheight = 1;
            p.add(new SLabel("end #1"), c);

            c.gridx = 1;
            p.add(new SLabel("6"), c);
            p.add(new SLabel("7"), c);
            p.add(new SLabel("8"), c);
            c.gridheight = GridBagConstraints.RELATIVE;
            p.add(new SLabel("9"), c);
            c.gridheight = 1;
            p.add(new SLabel("end #2"), c);

            c.gridx = 2;
            p.add(new SLabel("10"), c);
            p.add(new SLabel("11"), c);
            c.gridheight = GridBagConstraints.RELATIVE;
            p.add(new SLabel("12"), c);
            c.gridheight = 1;
            p.add(new SLabel("end #3"), c);

            c.gridx = 3;
            p.add(new SLabel("13"), c);
            c.gridheight = GridBagConstraints.RELATIVE;
            p.add(new SLabel("14"), c);
            c.gridheight = 1;
            p.add(new SLabel("end #4"), c);

            c.gridx = 4;
            c.gridheight = GridBagConstraints.RELATIVE;
            p.add(new SLabel("15"), c);
            c.gridheight = 1;
            p.add(new SLabel("end #5"), c);
        }

    }


}
