/*
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
import org.wings.SToggleButton;
import org.wings.SURLIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class ToggleButtonExample
        extends WingSetPane
{
    final static int[] textHPos = new int[] {SConstants.LEFT, SConstants.CENTER, SConstants.RIGHT};
    final static int[] textVPos = new int[] {SConstants.TOP, SConstants.CENTER, SConstants.BOTTOM};

    static final SIcon icon = new SURLIcon("../icons/ButtonIcon.gif");
    static final SIcon disabledIcon = new SURLIcon("../icons/ButtonDisabledIcon.gif");
    static final SIcon pressedIcon = new SURLIcon("../icons/ButtonPressedIcon.gif");
    static final SIcon rolloverIcon = new SURLIcon("../icons/ButtonRolloverIcon.gif");
    private ButtonControls controls;

    private final SLabel reportLabel = new SLabel("No button pressed");
    protected ActionListener action = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            reportLabel.setText("<html>Button <b>'" + e.getActionCommand() + "'</b> pressed");
        }
    };


    protected SComponent createControls() {
        controls = new ButtonControls();
        return controls;
    }

    public SComponent createExample() {
        return createButtonExample();
    }

    SContainer createButtonExample() {
        SButtonGroup group = new SButtonGroup();
        SToggleButton[] buttons = new SToggleButton[9];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new SToggleButton("Text " + (i + 1));
            buttons[i].setActionCommand(buttons[i].getText());
            if (i != 4) {
                buttons[i].setIcon(icon);
                buttons[i].setDisabledIcon(disabledIcon);
                buttons[i].setRolloverIcon(rolloverIcon);
                buttons[i].setPressedIcon(pressedIcon);
                buttons[i].setSelectedIcon(pressedIcon);
            }
            buttons[i].setToolTipText("ToggleButton " + (i+1));
            buttons[i].setName("tb" + (i+1));
            buttons[i].setShowAsFormComponent(false);
            buttons[i].setVerticalTextPosition(textVPos[(i / 3)% 3]);
            buttons[i].setHorizontalTextPosition(textHPos[i % 3]);
            group.add(buttons[i]);
            controls.addControllable(buttons[i]);
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
            final SCheckBox useImages = new SCheckBox("Use Icons");
            useImages.setSelected(true);
            useImages.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean use = useImages.isSelected();

                    for (Iterator iterator = components.iterator(); iterator.hasNext();) {
                        SAbstractButton component = (SAbstractButton) iterator.next();
                        if (!"tb5".equals(component.getName())) {
                            component.setIcon(use ? icon : null);
                            component.setDisabledIcon(use ? disabledIcon : null);
                            component.setRolloverIcon(use ? rolloverIcon : null);
                            component.setPressedIcon(use ? pressedIcon : null);
                            component.setSelectedIcon(use ? pressedIcon : null);
                        }
                    }
                }
            });
            addControl(useImages);
        }
    }
}
