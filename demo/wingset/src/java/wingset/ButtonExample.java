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

import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SURLIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ButtonExample extends WingSetPane {
    final static int[] textHPos = new int[]{SConstants.LEFT, SConstants.CENTER, SConstants.RIGHT};
    final static int[] textVPos = new int[]{SConstants.TOP, SConstants.CENTER, SConstants.BOTTOM};

    // icons
    private static final SIcon icon = new SURLIcon("../icons/ButtonIcon.gif");
    private static final SIcon disabledIcon = new SURLIcon("../icons/ButtonDisabledIcon.gif");
    private static final SIcon pressedIcon = new SURLIcon("../icons/ButtonPressedIcon.gif");
    private static final SIcon rolloverIcon = new SURLIcon("../icons/ButtonRolloverIcon.gif");

    // pressed label & handler
    private final SLabel reportLabel = new SLabel("No button pressed");
    private final ActionListener action = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            reportLabel.setText("<html>Button <b>'" + e.getActionCommand() + "'</b> pressed");
        }
    };

    // button control itself
    private ButtonControls controls;
    private SButton[] buttons;

    public SComponent createExample() {
        controls = new ButtonControls();
        SContainer p = createButtonExample();

        SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(p, SBorderLayout.CENTER);

        controls.getApplyButton().addActionListener(action);
        final SButton defaultButton = new SButton();
        defaultButton.addActionListener(action);
        defaultButton.setActionCommand("Default Button (Enter key)");
        form.setDefaultButton(defaultButton);

        return form;
    }

    private SContainer createButtonExample() {
        buttons = new SButton[9];

        for (int i = 0; i < buttons.length; i++) {
            final String buttonName = "Text " + (i + 1);
            buttons[i] = new SButton(buttonName);
            buttons[i].setActionCommand(buttons[i].getText());

            buttons[i].setToolTipText("Button " + (i + 1));
            buttons[i].setName("button" + (i + 1));
            buttons[i].setShowAsFormComponent(false);
            buttons[i].setVerticalTextPosition(textVPos[(i / 3) % 3]);
            buttons[i].setHorizontalTextPosition(textHPos[i % 3]);
            buttons[i].setActionCommand(buttonName);
            controls.addControllable(buttons[i]);
        }

        updateIconUsage(true);

        final SGridLayout grid = new SGridLayout(3);
        final SPanel buttonGrid = new SPanel(grid);
        grid.setBorder(1);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(action);
            buttonGrid.add(buttons[i]);
        }

        final SPanel panel = new SPanel(new SGridLayout(1));
        panel.add(buttonGrid);
        panel.add(reportLabel);

        return panel;
    }

    /**
     * Use some icons in buttons or not.
     */
    private void updateIconUsage(boolean useIcons) {
        for (int i = 0; i < buttons.length; i++) {
            final SButton button = buttons[i];
            if (i != 4) {
                button.setIcon(useIcons ? icon : null);
                button.setDisabledIcon(useIcons ? disabledIcon : null);
                button.setRolloverIcon(useIcons ? rolloverIcon : null);
                button.setPressedIcon(useIcons ? pressedIcon : null);
            }
        }
    }

    /**
     * Update {@link SComponent#setEnabled(boolean)}
     */
    private void updateEnabled(boolean allEnabled) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(allEnabled || Math.random() > 0.3);
        }
    }

    /**
     * The additional control toolbar for the button example
     */
    private class ButtonControls extends ComponentControls {
        public ButtonControls() {
            final SCheckBox useImages = new SCheckBox("Use Icons");
            useImages.setSelected(true);
            useImages.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateIconUsage(useImages.isSelected());
                }
            });
            addControl(useImages);

            final SCheckBox disableSomeButtons = new SCheckBox("Disable a few buttons");
            disableSomeButtons.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateEnabled(!disableSomeButtons.isSelected());
                }
            });
            addControl(new SLabel(""));
            addControl(disableSomeButtons);
        }

        public SButton getApplyButton() {
            return super.applyButton;
        }
    }
}
