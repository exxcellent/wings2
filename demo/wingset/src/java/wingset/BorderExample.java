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
import org.wings.border.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class BorderExample
        extends WingSetPane
{
    private static final SIcon WAIT_ICON = new SResourceIcon("org/wings/icons/Wait.gif");

    final SBorder raised = new SBevelBorder(SBevelBorder.RAISED, new Insets(5, 5, 5, 5));
    final SBorder lowered = new SBevelBorder(SBevelBorder.LOWERED, new Insets(5, 5, 5, 5));
    final SBorder line = new SLineBorder(2, new Insets(5, 5, 5, 5));
    final SBorder grooved = new SEtchedBorder(SEtchedBorder.LOWERED, new Insets(5, 5, 5, 5));
    final SBorder ridged = new SEtchedBorder(SEtchedBorder.RAISED, new Insets(5, 5, 5, 5));
    final SBorder titled = new STitledBorder(new SEtchedBorder(SEtchedBorder.LOWERED, new Insets(5, 5, 5, 5)), "This is a title");

    private SLabel borderLabel;
    private BorderControls controls;
    private int thickness = 2;

    public SComponent createExample() {
        controls = new BorderControls();

        borderLabel = new SLabel(WAIT_ICON);
        borderLabel.setBackground(new Color(222, 222, 222));

        SPanel buttons = new SPanel();
        buttons.setLayout(new SGridLayout(1));
        SButtonGroup group = new SButtonGroup();
        final SRadioButton rb = new SRadioButton("Raised");
        group.add(rb);
        buttons.add(rb);
        final SRadioButton lb = new SRadioButton("Lowered");
        group.add(lb);
        buttons.add(lb);
        final SRadioButton lineb = new SRadioButton("Line");
        group.add(lineb);
        buttons.add(lineb);
        final SRadioButton groovedb = new SRadioButton("Grooved");
        group.add(groovedb);
        buttons.add(groovedb);
        final SRadioButton ridgedb = new SRadioButton("Ridged");
        group.add(ridgedb);
        buttons.add(ridgedb);
        final SRadioButton eb = new SRadioButton("Title");
        group.add(eb);
        buttons.add(eb);

        group.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SButtonGroup g = (SButtonGroup) e.getSource();
                Object button = g.getSelection();
                if (button == rb) {
                    borderLabel.setText("Raised Border");
                    borderLabel.setBorder(raised);
                } else if (button == lb) {
                    borderLabel.setText("Lowered Border");
                    borderLabel.setBorder(lowered);
                } else if (button == lineb) {
                    borderLabel.setText("Line Border");
                    borderLabel.setBorder(line);
                } else if (button == groovedb) {
                    borderLabel.setText("Grooved Border");
                    borderLabel.setBorder(grooved);
                } else if (button == ridgedb) {
                    borderLabel.setText("Ridged Border");
                    borderLabel.setBorder(ridged);
                } else if (button == eb) {
                    borderLabel.setText("Titled Border");
                    borderLabel.setBorder(titled);
                }
                borderLabel.getBorder().setThickness(thickness);
            }
        });

        rb.setSelected(true);

        controls.addSizable(borderLabel);

        SForm panel = new SForm(new SBorderLayout());
        panel.add(controls, SBorderLayout.NORTH);
        panel.add(buttons, SBorderLayout.WEST);
        panel.add(borderLabel, SBorderLayout.CENTER);

        return panel;
    }

    class BorderControls extends ComponentControls {

        public BorderControls() {
            final STextField thicknessTextField = new STextField();
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        thickness = Integer.parseInt(thicknessTextField.getText());
                        borderLabel.getBorder().setThickness(thickness);
                    }
                    catch (NumberFormatException e) {}
                }
            });

            add(new SLabel(" thicknessTextField "));
            add(thicknessTextField);
        }
    }
}
