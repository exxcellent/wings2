/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package wingset;


import org.wings.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Example for STabbedPane.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 */
public class TabbedPaneExample extends WingSetPane {
    private final static SIcon JAVA_CUP_ICON = new SResourceIcon("org/wings/icons/JavaCup.gif");
    private final static SIcon SMALL_COW_ICON = new SURLIcon("../icons/cowSmall.gif");

    protected SComponent createExample() {
        SForm c = new SForm(new SBorderLayout());

        final STabbedPane tabs = new STabbedPane();
        tabs.setBackground(new java.awt.Color(200, 200, 255));
        tabs.setShowAsFormComponent(false);

        SPanel north = new SPanel(new SFlowLayout());
        final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
        showAsFormComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabs.setShowAsFormComponent(showAsFormComponent.isSelected());
            }
        });
        showAsFormComponent.setShowAsFormComponent(false);
        north.add(showAsFormComponent);
        c.add(north, "North");

        SPanel west = new SPanel(new SFlowDownLayout());
        west.add(new SLabel("<html>Tab Placement:&nbsp;"));

        PlacementActionListener placementActionListener = new PlacementActionListener(tabs);

        SRadioButton top = new SRadioButton("Top");
        top.putClientProperty("placement", new Integer(SConstants.TOP));
        top.addActionListener(placementActionListener);
        top.setShowAsFormComponent(false);
        west.add(top);

        SRadioButton left = new SRadioButton("Left");
        left.putClientProperty("placement", new Integer(SConstants.LEFT));
        left.addActionListener(placementActionListener);
        left.setShowAsFormComponent(false);
        west.add(left);

        SRadioButton right = new SRadioButton("Right");
        right.putClientProperty("placement", new Integer(SConstants.RIGHT));
        right.addActionListener(placementActionListener);
        right.setShowAsFormComponent(false);
        west.add(right);

        SRadioButton bottom = new SRadioButton("Bottom");
        bottom.putClientProperty("placement", new Integer(SConstants.BOTTOM));
        bottom.addActionListener(placementActionListener);
        bottom.setShowAsFormComponent(false);
        west.add(bottom);

        SButtonGroup group = new SButtonGroup();
        group.add(top);
        group.add(left);
        group.add(right);
        group.add(bottom);
        group.setSelected(top, true);

        west.add(new SLabel("<html>&nbsp;"));

        group = new SButtonGroup();
        west.add(new SLabel("<html>Color:&nbsp;"));

        final Object[] clrs = {
            "Yellow", Color.yellow,
            "Green", Color.green,
            "Lightblue", new Color(200, 200, 255),
            "Lightgray", Color.lightGray,
            "Orange", new Color(255, 153, 0)};

        for (int i = 0; i < clrs.length; i += 2) {
            SRadioButton btn = new SRadioButton(clrs[i].toString());
            btn.putClientProperty("color", clrs[i+1]);
            btn.addActionListener(new ColorActionListener(tabs));
            btn.setShowAsFormComponent(false);
            group.add(btn);
            if (i == 0) {
                group.setSelected(btn, true);
            }
            west.add(btn);
        }
        west.setHorizontalAlignment(SConstants.CENTER);
        c.add(west, "West");

        final STextArea text = new STextArea();
        text.setPreferredSize(new SDimension(400, 200));
        for (int i = 0; i < 12; ++i) {
            SPanel p = new SPanel(new SBorderLayout());
            p.add(new SLabel("Tab # " + i), "North");
            p.add(text);
            tabs.add("Tab " + i, p);
        }
        tabs.setIconAt(3, JAVA_CUP_ICON);
        tabs.setIconAt(8, SMALL_COW_ICON);
        tabs.setEnabledAt(1, false);
        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                String t = text.getText();
                t += "\n";
                t += "wingS: Changed to tab " + tabs.getSelectedIndex() + "\n";
                text.setText(t);
            }
        });
        c.add(tabs, "Center");

        return c;
    }

    private static class PlacementActionListener implements ActionListener {
        private final STabbedPane tpane;

        public PlacementActionListener(STabbedPane tpane) {
            this.tpane = tpane;
        }

        public void actionPerformed(ActionEvent ae) {
            SComponent source = (SComponent)ae.getSource();
            Integer placement = (Integer)source.getClientProperty("placement");
            tpane.setTabPlacement(placement.intValue());
        }
    }

    private static class ColorActionListener implements ActionListener {
        private final STabbedPane tabs;

        public ColorActionListener(STabbedPane tabs) {
            this.tabs = tabs;
        }

        public void actionPerformed(ActionEvent ae) {
            SComponent source = (SComponent)ae.getSource();
            Color color= (Color)source.getClientProperty("color");
            tabs.setBackground(color);
        }
    }
}
