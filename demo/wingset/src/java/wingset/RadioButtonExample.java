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

import org.wings.SAbstractButton;
import org.wings.SBorderLayout;
import org.wings.SButtonGroup;
import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.SURLIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class RadioButtonExample
        extends WingSetPane
{
    final static int[] textHPos = new int[] {SConstants.LEFT, SConstants.CENTER, SConstants.RIGHT};
    final static int[] textVPos = new int[] {SConstants.TOP, SConstants.CENTER, SConstants.BOTTOM};

    static final SIcon sel = new SURLIcon("../icons/RadioButtonSelectedIcon.gif");
    static final SIcon nsel = new SURLIcon("../icons/RadioButtonIcon.gif");
    static final SIcon pressed = new SURLIcon("../icons/RadioButtonPressedIcon.gif");
    static final SIcon dissel = new SURLIcon("../icons/RadioButtonDisabledSelectedIcon.gif");
    static final SIcon disnsel = new SURLIcon("../icons/RadioButtonDisabledIcon.gif");
    static final SIcon rollsel = new SURLIcon("../icons/RadioButtonRolloverSelectedIcon.gif");
    static final SIcon rollnsel = new SURLIcon("../icons/RadioButtonRolloverIcon.gif");
    private ButtonControls controls;

    private final SLabel reportLabel = new SLabel("No button pressed");
    protected ActionListener action = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            reportLabel.setText("<html>Button <b>'" + e.getActionCommand() + "'</b> pressed");
        }
    };


    public SComponent createExample() {
        controls = new ButtonControls();
        SContainer p = createRadioButtonExample();

        SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(p, SBorderLayout.CENTER);
        return form;
    }

    SContainer createRadioButtonExample() {
        SButtonGroup group = new SButtonGroup();
        SRadioButton[] buttons = new SRadioButton[9];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new SRadioButton("Text " + (i + 1));
            buttons[i].setActionCommand(buttons[i].getText());
            if (i != 4) {
                buttons[i].setIcon(nsel);
                buttons[i].setSelectedIcon(sel);
                buttons[i].setDisabledIcon(disnsel);
                buttons[i].setDisabledSelectedIcon(dissel);
                buttons[i].setRolloverIcon(rollnsel);
                buttons[i].setRolloverSelectedIcon(rollsel);
                buttons[i].setPressedIcon(pressed);
            }
            else {
                buttons[i].setIcon(null);
                buttons[i].setSelectedIcon(null);
                buttons[i].setDisabledIcon(null);
                buttons[i].setDisabledSelectedIcon(null);
                buttons[i].setRolloverIcon(null);
                buttons[i].setRolloverSelectedIcon(null);
                buttons[i].setPressedIcon(null);
            }
            buttons[i].setToolTipText("RadioButton " + (i+1));
            buttons[i].setName("button" + (i+1));
            buttons[i].setShowAsFormComponent(false);
            buttons[i].setVerticalTextPosition(textVPos[(i / 3)% 3]);
            buttons[i].setHorizontalTextPosition(textHPos[i % 3]);
            group.add(buttons[i]);
            controls.addSizable(buttons[i]);
        }

        final SGridLayout grid = new SGridLayout(3);
        final SPanel buttonGrid = new SPanel(grid);
        grid.setBorder(1);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(action);
            buttonGrid.add(buttons[i]);
        }

        SPanel panel = new SPanel(new SGridLayout(1));
        panel.add(buttonGrid);
        panel.add(reportLabel);

        return panel;
    }

    class ButtonControls extends ComponentControls {
        public ButtonControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
            showAsFormComponent.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (Iterator iterator = components.iterator(); iterator.hasNext();) {
                        SComponent component = (SComponent) iterator.next();
                        component.setShowAsFormComponent(showAsFormComponent.isSelected());
                    }
                }
            });
            addControl(showAsFormComponent);

            final SCheckBox useImages = new SCheckBox("Use Icons");
            useImages.setSelected(true);
            useImages.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean use = useImages.isSelected();

                    for (Iterator iterator = components.iterator(); iterator.hasNext();) {
                        SAbstractButton component = (SAbstractButton) iterator.next();
                        if (!"button5".equals(component.getName())) {
                            component.setIcon(use ? nsel : null);
                            component.setSelectedIcon(use ? sel : null);
                            component.setDisabledIcon(use ? disnsel : null);
                            component.setDisabledSelectedIcon(use ? dissel : null);
                            component.setRolloverIcon(use ? rollnsel : null);
                            component.setRolloverSelectedIcon(use ? rollsel : null);
                            component.setPressedIcon(use ? pressed : null);
                        }
                    }
                }
            });
            addControl(useImages);
        }
    }
}
