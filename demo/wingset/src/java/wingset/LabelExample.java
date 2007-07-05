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

import org.wings.SBoxLayout;
import org.wings.SButtonGroup;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.SResourceIcon;
import org.wings.SSpacer;
import org.wings.SURLIcon;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class LabelExample extends WingSetPane {
    private static final SIcon WAIT_ICON = new SResourceIcon("org/wings/icons/Wait.gif");
    private static final String directions[] = {"nw", "n", "ne", "w", "e", "sw", "s", "se"};
    private static final SIcon onIcons[];
    private static final SIcon offIcons[];
    private ComponentControls controls;

    static {
        onIcons = new SIcon[directions.length];
        offIcons = new SIcon[directions.length];
        for (int i = 0; i < directions.length; ++i) {
            String d = directions[i];
            onIcons[i] = new SURLIcon("../icons/Label" + d + "On.gif");
            offIcons[i] = new SURLIcon("../icons/Label" + d + "Off.gif");
        }
    }

    protected SComponent createControls() {
        controls = new ComponentControls();
        controls.fontComboBox.setSelectedIndex(1);
        controls.foregroundComboBox.setSelectedIndex(3);
        return controls;
    }

    public SComponent createExample() {
        final SLabel testLabel = new SLabel("A styled SLabel with an icon");
        testLabel.setHorizontalAlignment(LEFT);
        testLabel.setIcon(WAIT_ICON);
        testLabel.setForeground(Color.RED);
        testLabel.setFont(new SFont("serif", SFont.ITALIC | SFont.BOLD, 10));
        controls.addControllable(testLabel);

        SPanel testPanel = new SPanel();
        testPanel.setPreferredSize(new SDimension(250, SDimension.AUTO_INT));
        testPanel.add(testLabel);

        SLabel titleResult = new SLabel("Result");
        titleResult.setHorizontalAlignment(SConstants.CENTER_ALIGN);

        SGridLayout layout = new SGridLayout(3, 3, 10, 10);
        layout.setRenderFirstLineAsHeader(true);
        SPanel panel = new SPanel(layout);
        panel.add(new SLabel("Control the label's text position"));
        panel.add(new SSpacer(50,1));
        panel.add(titleResult);
        panel.add(createRoundRadio(testLabel));
        panel.add(new SLabel());
        panel.add(testPanel);

        return panel;
    }

    private SRadioButton createRadio(SPanel p, String constraint,
                                     SButtonGroup buttonGroup,
                                     String toolTip, int icon) {
        SRadioButton button = new SRadioButton();
        button.setIcon(offIcons[icon]);
        button.setSelectedIcon(onIcons[icon]);
        button.setToolTipText(toolTip);
        p.add(button, constraint);
        buttonGroup.add(button);
        return button;
    }

    SContainer createRoundRadio(final SLabel label) {
        SPanel b = new SPanel(new SBoxLayout(SBoxLayout.Y_AXIS));

        SPanel sp = new SPanel(new SBoxLayout(SBoxLayout.X_AXIS));
        SButtonGroup g = new SButtonGroup();
        final SRadioButton nw = createRadio(sp, "p=nw", g, "North West", 0);
        final SRadioButton n = createRadio(sp, "p=n", g, "North", 1);
        final SRadioButton ne = createRadio(sp, "p=ne", g, "North East", 2);
        b.add(sp);

        SPanel sp1 = new SPanel(new SBoxLayout(SBoxLayout.X_AXIS));

        final SRadioButton w = createRadio(sp1, "p=w", g, "West", 3);
        SLabel sl = new SLabel();
        sl.setIcon(new SURLIcon("../icons/cowSmall.gif"));
        sp1.add(sl);
        final SRadioButton e = createRadio(sp1, "p=e", g, "East", 4);
        b.add(sp1);

        SPanel sp2 = new SPanel(new SBoxLayout(SBoxLayout.X_AXIS));
        final SRadioButton sw = createRadio(sp2, "p=sw", g, "South West", 5);
        final SRadioButton s = createRadio(sp2, "p=s", g, "South", 6);
        final SRadioButton se = createRadio(sp2, "p=se", g, "South East", 7);

        nw.setShowAsFormComponent(false);
        n.setShowAsFormComponent(false);
        ne.setShowAsFormComponent(false);
        w.setShowAsFormComponent(false);
        e.setShowAsFormComponent(false);
        sw.setShowAsFormComponent(false);
        s.setShowAsFormComponent(false);
        se.setShowAsFormComponent(false);

        b.add(sp2);

        g.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Object button = ((SButtonGroup) ev.getSource())
                        .getSelection();
                if (button == n) {
                    label.setVerticalTextPosition(TOP);
                    label.setHorizontalTextPosition(CENTER);
                } else if (button == nw) {
                    label.setVerticalTextPosition(TOP);
                    label.setHorizontalTextPosition(LEFT);
                } else if (button == ne) {
                    label.setVerticalTextPosition(TOP);
                    label.setHorizontalTextPosition(RIGHT);
                } else if (button == w) {
                    label.setVerticalTextPosition(CENTER);
                    label.setHorizontalTextPosition(LEFT);
                } else if (button == e) {
                    label.setVerticalTextPosition(CENTER);
                    label.setHorizontalTextPosition(RIGHT);
                } else if (button == sw) {
                    label.setVerticalTextPosition(BOTTOM);
                    label.setHorizontalTextPosition(LEFT);
                } else if (button == se) {
                    label.setVerticalTextPosition(BOTTOM);
                    label.setHorizontalTextPosition(RIGHT);
                } else if (button == s) {
                    label.setVerticalTextPosition(BOTTOM);
                    label.setHorizontalTextPosition(CENTER);
                }
                /*
                else if (button == cc) {
                    label.setVerticalTextPosition(CENTER);
                    label.setHorizontalTextPosition(CENTER);
                }
                */

            }
        });

        n.setSelected(true);

        return b;
    }
}


