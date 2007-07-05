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
    private SGridLayout grid;
    private SPanel gridPanel;


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
            SToggleButton button = buttons[i] = new SToggleButton("Text " + (i + 1));
            button.setShowAsFormComponent(true);
            button.setActionCommand(button.getText());
            if (i != 4) {
                button.setIcon(icon);
                button.setDisabledIcon(disabledIcon);
                button.setRolloverIcon(rolloverIcon);
                button.setPressedIcon(pressedIcon);
                button.setSelectedIcon(pressedIcon);
            }
            button.setToolTipText("ToggleButton " + (i+1));
            button.setName("tb" + (i+1));
            button.setVerticalTextPosition(textVPos[(i / 3)% 3]);
            button.setHorizontalTextPosition(textHPos[i % 3]);
            group.add(button);
            controls.addControllable(button);
        }

        grid = new SGridLayout(3);
        gridPanel = new SPanel(grid);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(action);
            gridPanel.add(buttons[i]);
        }

        final SPanel panel = new SPanel(new SGridLayout(2, 1, 0, 20));
        panel.add(gridPanel);
        panel.add(reportLabel);

        return panel;
    }

    class ButtonControls extends ComponentControls {
        public ButtonControls() {
            formComponentCheckBox.setSelected(true);
            formComponentCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    grid.setBorder(formComponentCheckBox.isSelected() ? 0 : 1);
                    gridPanel.reload();
                }
            });

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
