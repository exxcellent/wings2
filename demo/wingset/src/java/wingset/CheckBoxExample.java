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
import org.wings.plaf.css.CheckBoxCG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class CheckBoxExample
        extends WingSetPane
{
    final static int[] textHPos = new int[] {SConstants.LEFT, SConstants.CENTER, SConstants.RIGHT};
    final static int[] textVPos = new int[] {SConstants.TOP, SConstants.CENTER, SConstants.BOTTOM};

    static final SIcon sel = new SResourceIcon("org/wings/icons/green_light_on.png");
    static final SIcon nsel = new SResourceIcon("org/wings/icons/green_light_off.png");
    static final SIcon dissel = new SResourceIcon("org/wings/icons/green_light_on_disabled.png");
    static final SIcon disnsel = new SResourceIcon("org/wings/icons/green_light_off_disabled.png");
    static final SIcon rollsel = new SResourceIcon("org/wings/icons/green_light_on.png");
    static final SIcon rollnsel = new SResourceIcon("org/wings/icons/green_light_on.png");

    static SIcon backup_sel;
    static SIcon backup_nsel;
    static SIcon backup_dissel;
    static SIcon backup_disnsel;
    static SIcon backup_rollsel;
    static SIcon backup_rollnsel;

    private ButtonControls controls;

    private final SLabel reportLabel = new SLabel("No button pressed");
    protected ActionListener action = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            reportLabel.setText("<html>Button <b>'" + e.getActionCommand() + "'</b> pressed");
        }
    };
    private SCheckBox[] buttons;

    protected SComponent createControls() {
        controls = new ButtonControls();
        return controls;
    }

    public SComponent createExample() {
        return createCheckBoxExample();
    }

    SContainer createCheckBoxExample() {
        buttons = new SCheckBox[9];

        for (int i = 0; i < buttons.length; i++) {
            SCheckBox button = buttons[i] = new SCheckBox("Text " + (i + 1));
            if (i == 4)
                button.setText(null);
            button.setShowAsFormComponent(true);
            button.setActionCommand("check" + (i+1));
            button.setToolTipText("CheckBox " + (i+1));
            button.setName("check" + (i+1));
            button.setVerticalTextPosition(textVPos[(i / 3)% 3]);
            button.setHorizontalTextPosition(textHPos[i % 3]);
            controls.addControllable(button);
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

        final SPanel panel = new SPanel(new SGridLayout(2, 1, 0, 20));
        panel.add(buttonGrid);
        panel.add(reportLabel);

        return panel;
    }

    class ButtonControls extends ComponentControls {
        public ButtonControls() {
            formComponentCheckBox.setSelected(true);
            final SCheckBox customIcons = new SCheckBox("custom icons");
            customIcons.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < buttons.length; i++) {
                        SCheckBox button = buttons[i];
                        if (i != 4 && customIcons.isSelected()) {
                            backup_nsel = button.getIcon();
                            backup_sel = button.getSelectedIcon();
                            backup_disnsel = button.getDisabledIcon();
                            backup_dissel = button.getDisabledSelectedIcon();
                            backup_rollnsel = button.getRolloverIcon();
                            backup_rollsel = button.getRolloverSelectedIcon();
                            button.setIcon(nsel);
                            button.setSelectedIcon(sel);
                            button.setDisabledIcon(disnsel);
                            button.setDisabledSelectedIcon(dissel);
                            button.setRolloverIcon(rollnsel);
                            button.setRolloverSelectedIcon(rollsel);
                        }
                        else {
                            button.setIcon(backup_nsel);
                            button.setSelectedIcon(backup_sel);
                            button.setDisabledIcon(backup_disnsel);
                            button.setDisabledSelectedIcon(backup_dissel);
                            button.setRolloverIcon(backup_rollnsel);
                            button.setRolloverSelectedIcon(backup_rollsel);
                        }
                    }
                }
            });
            addControl(customIcons);

            final SCheckBox useImages = new SCheckBox("icons in form");
            final CheckBoxCG cg = (CheckBoxCG) getSession().getCGManager().getCG(SCheckBox.class);
            useImages.setSelected(true);
            cg.setUseIconsInForm(true);
            useImages.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cg.setUseIconsInForm(useImages.isSelected());
                    CheckBoxExample.this.reload();
                }
            });
            addControl(useImages);
        }
    }
}
